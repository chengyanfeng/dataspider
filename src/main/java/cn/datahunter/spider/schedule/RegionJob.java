package cn.datahunter.spider.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.datahunter.spider.process.RegionProcess;
import us.codecraft.webmagic.Spider;

/**
 * Created by root on 2017/3/16.
 */
@Component
public class RegionJob {

    @Autowired
    private RegionProcess regionProcess;

    /**
     * 人口数据-省份
     */
    @Scheduled(cron = "0 5 0 23 3 *", zone = "Asia/Shanghai")
    public void execute() {

        String url = "http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/";
        Spider.create(regionProcess).addUrl(url)
                .thread(1).run();
    }
}
