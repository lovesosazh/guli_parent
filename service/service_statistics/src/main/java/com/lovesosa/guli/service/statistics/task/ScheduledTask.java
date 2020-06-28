package com.lovesosa.guli.service.statistics.task;

import com.lovesosa.guli.service.statistics.service.IDailyService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author lovesosa
 */
@Slf4j
@Component
public class ScheduledTask {

    @Autowired
    private IDailyService dailyService;

    @Scheduled(cron = "0 0 1 * * ? ")
    public void genStatisticsData() {
        String day = new DateTime().minusDays(1).toString("yyyy-MM-dd");
        dailyService.createStatisticsByDay(day);
    }
}
