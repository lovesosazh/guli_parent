package com.lovesosa.guli.service.trade.controller.api;

import com.github.wxpay.sdk.WXPayUtil;
import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.trade.entity.Order;
import com.lovesosa.guli.service.trade.service.IOrderService;
import com.lovesosa.guli.service.trade.service.WeixinPayService;
import com.lovesosa.guli.service.trade.util.StreamUtils;
import com.lovesosa.guli.service.trade.util.WeixinPayProperties;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lovesosa
 */
@RestController
@RequestMapping("/api/trade/weixin-pay")
@Api(tags = "网站微信支付")
@Slf4j
public class ApiWeixinPayController {

    @Autowired
    private WeixinPayService weixinPayService;
    @Autowired
    private WeixinPayProperties weixinPayProperties;

    @Autowired
    private IOrderService orderService;

    /**
     * 统一下单接口的调用
     * @param orderNo
     * @param request
     * @return
     */
    @GetMapping("/create-native/{orderNo}")
    public R createNative(@PathVariable("orderNo") String orderNo, HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        Map<String, Object> map = weixinPayService.createNative(orderNo, remoteAddr);
        return R.ok().data(map);
    }

    @PostMapping("/callback/notify")
    public String wxNotify(HttpServletRequest request, HttpServletResponse response)throws Exception {
        log.info("\n callback\\notify 被调用");
        ServletInputStream inputStream = request.getInputStream();
        String notifyXml = StreamUtils.inputStream2String(inputStream, "utf-8");
        log.info("\n notifyXml = " + notifyXml);

        // 校验签名
        if (WXPayUtil.isSignatureValid(notifyXml, weixinPayProperties.getPartnerKey())) {
            // 解析返回结果
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyXml);

            // 验证支付结果是否成功
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                // 金额校验
                String totalFee = notifyMap.get("total_fee"); // 支付结果返回的订单金额
                String orderNo = notifyMap.get("out_trade_no"); // 订单号
                Order order = orderService.getOrderByOrderNo(orderNo); // 查询本地订单

                // 校验返回订单的金额与商户测的金额是否一致
                if (order != null && order.getTotalFee().intValue() == Integer.parseInt(totalFee)) {

                    // 接口调用幂等性：无论接口被调用多少次，最后所影响的结果都是一致的
                    if (order.getStatus() == 0) {
                        // 更新订单状态
                        orderService.updateOrderStatus(notifyMap);
                    }

                    // 支付成功,给微信响应
                    // 创建响应对象
                    Map<String, String> returnMap = new HashMap<>();
                    returnMap.put("return_code", "SUCCESS");
                    returnMap.put("return_msg", "OK");
                    String returnXml = WXPayUtil.mapToXml(returnMap);
                    response.setContentType("text/xml");
                    log.info("支付成功，通知已处理");
                    return returnXml;
                }
            }

        }

        // 微信接收到校验失败的效果后，会反复的调用回调函数
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("return_code", "FAIL");
        returnMap.put("return_msg", "");
        String returnXml = WXPayUtil.mapToXml(returnMap);
        response.setContentType("text/xml");
        log.info("校验失败");
        return returnXml;


    }
}
