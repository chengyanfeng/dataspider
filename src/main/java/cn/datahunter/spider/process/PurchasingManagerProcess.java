package cn.datahunter.spider.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by root on 2017/3/15.
 */
public class PurchasingManagerProcess implements PageProcessor {

    private Site site = Site.me()
            .setCharset("utf-8")
            .setSleepTime(3000)
            .setRetryTimes(3)
            .setCycleRetryTimes(3)
            .setTimeOut(60000)
            .addHeader("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            .addHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        String rawText = page.getRawText();
        JSONObject jsonObject = JSON.parseObject(rawText);
        System.out.println("------------");
    }

}
