package com.lovesosa.guli.service.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.lovesosa.guli.common.base.result.ResultCodeEnum;
import com.lovesosa.guli.service.base.dto.CourseDto;
import com.lovesosa.guli.service.base.dto.MemberDto;
import com.lovesosa.guli.service.base.exception.GuliException;
import com.lovesosa.guli.service.trade.entity.Order;
import com.lovesosa.guli.service.trade.entity.PayLog;
import com.lovesosa.guli.service.trade.feign.EduCourseService;
import com.lovesosa.guli.service.trade.feign.UcenterMemberService;
import com.lovesosa.guli.service.trade.mapper.OrderMapper;
import com.lovesosa.guli.service.trade.mapper.PayLogMapper;
import com.lovesosa.guli.service.trade.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lovesosa.guli.service.trade.util.OrderNoUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author lovesosa
 * @since 2020-06-04
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Resource
    private EduCourseService eduCourseService;
    @Resource
    private UcenterMemberService ucenterMemberService;
    @Resource
    private PayLogMapper payLogMapper;

    @Override
    public String saveOrder(String courseId, String memberId) {

        // 查询当前用户是否已有当前课程订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("member_id", memberId);
        Order orderExist = baseMapper.selectOne(queryWrapper);
        if (orderExist != null) { // 如果订单已存在，则直接返回订单id
            return orderExist.getId();
        }

        // 查询课程信息
        CourseDto courseDtoById = eduCourseService.getCourseDtoById(courseId);
        if (courseDtoById == null) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        // 查询会员信息
        MemberDto memberDtoByMemberId = ucenterMemberService.getMemberDtoByMemberId(memberId);
        if (memberDtoByMemberId == null) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNo(OrderNoUtils.getOrderNo());
        order.setCourseId(courseDtoById.getId());
        order.setCourseTitle(courseDtoById.getTitle());
        order.setCourseCover(courseDtoById.getCover());
        order.setTeacherName(courseDtoById.getTeacherName());
        order.setTotalFee(courseDtoById.getPrice().multiply(new BigDecimal(100)));

        order.setMemberId(memberDtoByMemberId.getId());
        order.setMobile(memberDtoByMemberId.getMobile());
        order.setNickname(memberDtoByMemberId.getNickname());

        order.setStatus(0); // 未支付
        order.setPayType(1); // 微信支付

        baseMapper.insert(order);

        return order.getId();
    }

    @Override
    public Order getByOrderId(String orderId, String id) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId);
        queryWrapper.eq("member_id",id);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean isBuyByCourseId(String courseId, String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("member_id", memberId);
        queryWrapper.eq("status", 1);
        Integer count = baseMapper.selectCount(queryWrapper);

        return count.intValue() > 0;
    }

    @Override
    public List<Order> selectByMemberId(String memberId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.eq("member_id", memberId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean removeById(String orderId, String id) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId).eq("member_id",id);
        return this.remove(queryWrapper);
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        return baseMapper.selectOne(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOrderStatus(Map<String, String> notifyMap) {
        String orderNo = notifyMap.get("out_trade_no");
        Order order = this.getOrderByOrderNo(orderNo);
        order.setStatus(1); // 更新已支付
        baseMapper.updateById(order);

        // 记录支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(orderNo);
        payLog.setPayTime(new Date());
        payLog.setPayType(1); // 1微信支付
        payLog.setTotalFee(order.getTotalFee().longValue());
        payLog.setTradeState(notifyMap.get("result_code"));
        payLog.setTransactionId(notifyMap.get("transaction_id"));
        payLog.setAttr(new Gson().toJson(notifyMap));
        payLogMapper.insert(payLog);

        // 更新课程销量
        eduCourseService.updateBuyCountById(order.getCourseId());

    }

    /**
     * 查询支付结果
     * @param orderNo
     * @return true已支付  false未支付
     */
    @Override
    public boolean queryPayStatus(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        Order order = baseMapper.selectOne(queryWrapper);
        return order.getStatus() == 1;
    }
}
