package com.lovesosa.guli.service.statistics.controller.admin;


import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.statistics.service.IDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author lovesosa
 * @since 2020-06-07
 */
@Api(tags = "统计分析管理")
@RestController
@RequestMapping("/admin/statistics/daily")
public class DailyController {

    @Autowired
    private IDailyService dailyService;

    @ApiOperation("生成统计记录")
    @PostMapping("/create/{day}")
    public R createdStatisticsByDay(@PathVariable("day") String day) {
        dailyService.createStatisticsByDay(day);
        return R.ok().message("统计数据生成成功!");
    }

    @ApiOperation("展示统计数据")
    @GetMapping("/show-chart/{begin}/{end}")
    public R showChart(@PathVariable("begin") String begin, @PathVariable("end") String end) {

        Map<String, Map<String, Object>> map = dailyService.getChartData(begin, end);

        return R.ok().data("chartData",map);
    }
}

