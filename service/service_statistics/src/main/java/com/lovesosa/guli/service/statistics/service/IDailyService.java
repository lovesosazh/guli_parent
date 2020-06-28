package com.lovesosa.guli.service.statistics.service;

import com.lovesosa.guli.service.statistics.entity.Daily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author lovesosa
 * @since 2020-06-07
 */
public interface IDailyService extends IService<Daily> {

    void createStatisticsByDay(String day);

    Map<String, Map<String, Object>> getChartData(String begin, String end);
}
