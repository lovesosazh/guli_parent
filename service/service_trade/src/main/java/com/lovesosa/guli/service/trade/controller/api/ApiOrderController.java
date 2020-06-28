package com.lovesosa.guli.service.trade.controller.api;


import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.common.base.result.ResultCodeEnum;
import com.lovesosa.guli.common.base.util.JwtInfo;
import com.lovesosa.guli.common.base.util.JwtUtils;
import com.lovesosa.guli.service.trade.entity.Order;
import com.lovesosa.guli.service.trade.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author lovesosa
 * @since 2020-06-04
 */
@RestController
@RequestMapping("/api/trade/order")
@Api(tags = "网站订单管理")
@Slf4j
public class ApiOrderController {

    @Autowired
    private IOrderService orderService;



    @ApiOperation("新增订单")
    @PostMapping("/auth/save/{courseId}")
    public R save(@PathVariable("courseId") String courseId, HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);

        String orderId = orderService.saveOrder(courseId,jwtInfo.getId());

        return R.ok().data("orderId", orderId);
    }

    @ApiOperation("获取订单")
    @GetMapping("/auth/get/{orderId}")
    public R get(@PathVariable("orderId") String orderId, HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        Order order = orderService.getByOrderId(orderId, jwtInfo.getId());
        return R.ok().data("item", order);
    }

    @ApiOperation( "判断课程是否购买")
    @GetMapping("/auth/is-buy/{courseId}")
    public R isBuyByCourseId(@PathVariable("courseId") String courseId, HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        boolean isBuy = orderService.isBuyByCourseId(courseId, jwtInfo.getId());
        return R.ok().data("isBuy", isBuy);
    }

    @ApiOperation(value = "获取当前用户订单列表")
    @GetMapping("/auth/list")
    public R list(HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        List<Order> orderList = orderService.selectByMemberId(jwtInfo.getId());
        return R.ok().data("items",orderList);
    }

    @ApiOperation(value = "删除订单")
    @DeleteMapping("/auth/remove/{orderId}")
    public R remove(@PathVariable String orderId, HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        boolean result = orderService.removeById(orderId, jwtInfo.getId());
        if (result) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败，数据不存在");
        }
    }

    @GetMapping("/query-pay-status/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {
        boolean result = orderService.queryPayStatus(orderNo);
        if (result) {//支付成功
            return R.ok().message("支付成功");
        }
        return R.setResult(ResultCodeEnum.PAY_RUN);//支付中
    }
}

