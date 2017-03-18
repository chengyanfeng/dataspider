package cn.datahunter.spider.schedule;

import org.springframework.beans.factory.annotation.Autowired;

import cn.datahunter.spider.process.RegionProcess;
import us.codecraft.webmagic.Spider;

/**
 * Created by root on 2017/3/16.
 */
public class RegionJob {

    @Autowired
    private RegionProcess regionProcess;

    public static void main(String[] args) {
        String url = "http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201703/t20170310_1471429.html";
        RegionProcess regisionProcess = new RegionProcess();
        Spider.create(regisionProcess).addUrl(url)
                .thread(1).run();
    }
}
