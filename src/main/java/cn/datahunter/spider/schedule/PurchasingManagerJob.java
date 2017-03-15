package cn.datahunter.spider.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.datahunter.spider.process.PurchasingManagerProcess;
import us.codecraft.webmagic.Spider;

/**
 * Created by root on 2017/3/15.
 */
@Component
public class PurchasingManagerJob {

    @Autowired
    private  PurchasingManagerProcess purchasingManagerProcess;

    public static void main(String[] args) {
        Spider.create(new PurchasingManagerProcess()).addUrl("http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgyd&rowcode=zb&colcode=sj&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22sj%22%2C%22valuecode%22%3A%22LAST13%22%7D%5D&k1=1489542283462")
                .thread(1).run();
    }


}
