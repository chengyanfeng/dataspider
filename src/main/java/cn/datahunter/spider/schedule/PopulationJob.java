package cn.datahunter.spider.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.datahunter.spider.process.PopulationProcess;
import cn.datahunter.spider.util.Constants;
import us.codecraft.webmagic.Spider;

/**
 * Created by root on 2017/3/15.
 */
@Component
public class PopulationJob {

    @Autowired
    static PopulationProcess populationProcess;


    /**
     * 人口数据-全国
     */
    @Scheduled(cron = "0 59 16 * * *", zone = "Asia/Shanghai")
    public void execute1() {

        String url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgnd&rowcode=zb&colcode=sj&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A0301%22%7D%5D&k1=1489628034871";
        populationProcess.CATALOG_POPULATION = Constants.CATALOG_POPULATION_ALL;
        Spider.create(populationProcess).addUrl(url)
                .thread(1).run();
    }

    /**
     * 人口数据-主要城市
     */
    @Scheduled(cron = "0 59 16 * * *", zone = "Asia/Shanghai")
    public void execute2() {

        String url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=csnd&rowcode=reg&colcode=zb&wds=%5B%7B%22wdcode%22%3A%22sj%22%2C%22valuecode%22%3A%222015%22%7D%5D&dfwds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A02%22%7D%5D&k1=1489577110517";
        populationProcess.CATALOG_POPULATION = Constants.CATALOG_POPULATION_MAINCITY;
        Spider.create(populationProcess).addUrl(url)
                .thread(1).run();
    }

    /**
     * 人口数据-省份
     */
    @Scheduled(cron = "0 59 16 * * *", zone = "Asia/Shanghai")
    public void execute3() {

        String url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=fsnd&rowcode=reg&colcode=sj&wds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A030101%22%7D%5D&dfwds=%5B%7B%22wdcode%22%3A%22sj%22%2C%22valuecode%22%3A%222015%22%7D%5D&k1=1489631705626";
        populationProcess.CATALOG_POPULATION = Constants.CATALOG_POPULATION_PROVINCE;
        Spider.create(populationProcess).addUrl(url)
                .thread(1).run();
    }

}
