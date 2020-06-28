package com.lovesosa.guli.service.trade.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author lovesosa
 */
public class OrderNoUtils {

    /**
     * 生成订单号
     * @return
     */
    public static String getOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        return newDate + result;
    }
}
