package cn.datahunter.spider.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.datahunter.spider.process.ResidentConsumptionProcess;
import cn.datahunter.spider.util.CommonUtils;
import cn.datahunter.spider.util.Constants;
import us.codecraft.webmagic.Spider;

@Component
public class ResidentConsumptionJob {

    @Autowired
    private ResidentConsumptionProcess residentConsumptionProcess;

    /**
     * 居民消费价格指数-中国全国月度CPI/中国各个省份月度CPI
     */
    @Scheduled(cron = "0 13 19 * * *", zone = "Asia/Shanghai")
    public void execute1() {

        String monthPart = CommonUtils.getCurrentMonth();
        String url = "http://data.stats.gov.cn/tablequery.htm?m=QueryData&code=AA0108&wds=%5B%7B%22wdcode%22%3A%22sj%22%2C%22valuecode%22%3A%22" + monthPart + "%22%7D%5D";
        residentConsumptionProcess.RESIDENTCONSUMPTING_CATALOG = Constants.CATALOG_PRONVINCE;
        Spider.create(residentConsumptionProcess).addUrl(url)
                .thread(1).run();
    }

    /**
     * 中国主要城市
     */
    @Scheduled(cron = "0 13 19 * * *", zone = "Asia/Shanghai")
    public void execut2() {

        String monthPart = CommonUtils.getCurrentMonth();
        String url = "http://data.stats.gov.cn/tablequery.htm?m=QueryData&code=AA010A&wds=%5B%7B%22wdcode%22%3A%22sj%22%2C%22valuecode%22%3A%22" + monthPart + "%22%7D%5D";
        residentConsumptionProcess.RESIDENTCONSUMPTING_CATALOG = Constants.CATALOG_MAINCITY;
        Spider.create(residentConsumptionProcess).addUrl(url)
                .thread(1).run();
    }
}