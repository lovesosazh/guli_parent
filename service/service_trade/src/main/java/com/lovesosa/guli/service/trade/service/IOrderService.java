package com.lovesosa.guli.service.trade.service;

import com.lovesosa.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author lovesosa
 * @since 2020-06-04
 */
public interface IOrderService extends IService<Order> {

    String saveOrder(String courseId, String memberId);

    Order getByOrderId(String orderId, String id);

    boolean isBuyByCourseId(String courseId, String memberId);

    List<Order> selectByMemberId(String memberId);

    boolean removeById(String orderId, String id);

    Order getOrderByOrderNo(String orderNo);

    void updateOrderStatus(Map<String, String> notifyMap);

    boolean queryPayStatus(String orderNo);
}
