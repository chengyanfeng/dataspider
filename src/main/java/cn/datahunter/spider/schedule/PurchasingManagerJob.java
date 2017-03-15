package cn.datahunter.spider.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.datahunter.spider.process.PurchasingManagerProcess;
import cn.datahunter.spider.util.Constants;
import us.codecraft.webmagic.Spider;

/**
 * Created by root on 2017/3/15.
 */
@Component
public class PurchasingManagerJob {

    @Autowired
    private PurchasingManagerProcess purchasingManagerProcess;


    /**
     * 居民消费价格指数-中国全国月度CPI/中国各个省份月度CPI
     */
    @Scheduled(cron = "0 59 16 * * *", zone = "Asia/Shanghai")
    public void execute1() {

        String url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgyd&rowcode=zb&colcode=sj&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A1901%22%7D%5D&k1=1489561838913";
        purchasingManagerProcess.PURCHASINGMANAGER_CATALOG = Constants.CATALOG_INDUSTRY;
        Spider.create(purchasingManagerProcess).addUrl(url)
                .thread(1).run();
    }

    /**
     * 中国主要城市
     */
    @Scheduled(cron = "0 59 16 * * *", zone = "Asia/Shanghai")
    public void execut2() {

        String url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgyd&rowcode=zb&colcode=sj&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A1902%22%7D%5D&k1=1489567681693";
        purchasingManagerProcess.PURCHASINGMANAGER_CATALOG = Constants.CATALOG_NONINDUSTRY;
        Spider.create(purchasingManagerProcess).addUrl(url)
                .thread(1).run();
    }


}
