package cn.datahunter.spider.schedule;

import cn.datahunter.spider.process.GrossNationalproductProcess;
import cn.datahunter.spider.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

@Component
public class GrossNationalproduct {

    @Autowired
    private GrossNationalproductProcess residentConsumptionProcess;

    @Scheduled(cron = "0 01 11 * * *", zone = "Asia/Shanghai")
    public void doExecute() {



    }

    public static void main(String[] args) {
        String url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgjd&rowcode=zb&colcode=sj&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A0501%22%7D%5D&k1=1489570740759";
       // String url ="http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgnd&rowcode=zb&colcode=sj&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A0A00%22%7D%5D&";
        url = url.replace(CommonUtils.getLastMonth(), CommonUtils.getCurrentMonth());

        Spider.create(new GrossNationalproductProcess()).addUrl(url)
                .thread(1).run();
    }
}