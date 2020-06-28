package com.lovesosa.guli.service.trade.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.common.base.result.ResultCodeEnum;
import com.lovesosa.guli.common.base.util.ExceptionUtils;
import com.lovesosa.guli.common.base.util.HttpClientUtils;
import com.lovesosa.guli.service.base.exception.GuliException;
import com.lovesosa.guli.service.trade.entity.Order;
import com.lovesosa.guli.service.trade.service.IOrderService;
import com.lovesosa.guli.service.trade.service.WeixinPayService;
import com.lovesosa.guli.service.trade.util.WeixinPayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lovesosa
 */
@Slf4j
@Service
public class WeixinPayServiceImpl implements WeixinPayService {

     @Resource
     private IOrderService orderService;
     @Resource
     private WeixinPayProperties weixinPayProperties;

    @Override
    public Map<String, Object> createNative(String orderNo, String remoteAddr) {
        try {
            Order order = orderService.getOrderByOrderNo(orderNo);
            // 调用微信的统一下单API
            HttpClientUtils client = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/unifiedorder");
            // 组装参数
            Map<String, String> params = new HashMap<>();
            params.put("appid",weixinPayProperties.getAppId()); // 公众号id
            params.put("mch_id",weixinPayProperties.getPartner()); // 商户号
            params.put("nonce_str", WXPayUtil.generateNonceStr()); // 随机数
            params.put("body", order.getCourseTitle()); // 商品描述
            params.put("out_trade_no", orderNo); // 订单号
            params.put("total_fee", order.getTotalFee().intValue() + ""); // 订单金额，单位为分
            params.put("spbill_create_ip", remoteAddr); // 终端ip
            params.put("notify_url",weixinPayProperties.getNotifyUrl()); // 通知地址（回调地址）
            params.put("trade_type","NATIVE"); // 交易类型

            // 将参数转换成xml字符串，并且在字符串的最后追加计算的签名
            String xmlParams = WXPayUtil.generateSignedXml(params, weixinPayProperties.getPartnerKey());
            log.info(xmlParams);

            client.setXmlParam(xmlParams);
            client.setHttps(true);
            client.post();
            String resultXml = client.getContent();
            log.info("resultXml:" + resultXml);

            Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);

            //错误处理
            if("FAIL".equals(resultMap.get("return_code")) || "FAIL".equals(resultMap.get("result_code"))){
                log.error("微信支付统一下单错误 - "
                        + "return_code: " + resultMap.get("return_code")
                        + "return_msg: " + resultMap.get("return_msg")
                        + "result_code: " + resultMap.get("result_code")
                        + "err_code: " + resultMap.get("err_code")
                        + "err_code_des: " + resultMap.get("err_code_des"));

                throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
            }

            // 要组装的结果对象
            HashMap<String, Object> map = new HashMap<>();
            map.put("result_code", resultMap.get("result_code"));   // 交易标识
            map.put("code_url", resultMap.get("code_url"));         // 二维码url
            map.put("course_id", order.getCourseId());         // 课程id
            map.put("total_fee", order.getTotalFee());         // 订单金额
            map.put("out_trade_no", order.getOrderNo());         // 交易标识
            return map;

        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }

    }
}
