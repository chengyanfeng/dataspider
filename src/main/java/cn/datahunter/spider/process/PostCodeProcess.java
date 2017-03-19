package cn.datahunter.spider.process;

import cn.datahunter.spider.util.CommonUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2017/3/18.
 */
@Component
public class PostCodeProcess implements PageProcessor {

    private String urlIndex = "http://www.ip138.com/post/";

    private int catchSize = 0;

    private int count = 0;

    private List<String> resultData = new ArrayList<>();

    private Site site = Site.me()
            .setCharset("GBK")
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

        if (StringUtils.equals(page.getUrl().toString(), urlIndex)) {

            List<String> proviceLinksLst = page.getHtml().xpath("//table[@class='t4']/tbody/tr/td/a").links().all();

            catchSize = proviceLinksLst.size();

            page.addTargetRequests(proviceLinksLst);
        } else {

            count++;

            Html detailHtml = page.getHtml();
            String firstLevel = detailHtml.xpath("//table[@class='t3']/tbody/tr/td/a[3]/text()").get();

            String secondLevel = detailHtml.xpath("//table[@class='t12']/tbody/tr[2]/td/a/b/text()").get();
            secondLevel = StringUtils.isEmpty(secondLevel) ? "市辖区" : secondLevel;

            List<Selectable> nodes = detailHtml.xpath("//table[@class='t12']/tbody/tr[@bgcolor='#ffffff']").nodes();
            for (Selectable node : nodes) {

                String city = node.xpath("//tr/td/a/b/text()").get();
                if (StringUtils.isNotEmpty(city))
                    secondLevel = city;

                String postName1 = node.xpath("//tr/td[1]/text()").get();
                String postCode1 = node.xpath("//tr/td[2]/a/text()").get();
                if (StringUtils.isNotEmpty(postName1) && StringUtils.isNotEmpty(postCode1)) {
                    StringBuilder str1 = new StringBuilder();
                    str1.append(firstLevel).append(",").append(secondLevel).append(",")
                            .append(postName1).append(",").append(postCode1);
                    resultData.add(str1.toString());
                }

                String postName2 = node.xpath("//tr//td[4]/text()").get();
                String postCode2 = node.xpath("//tr//td[5]/a/text()").get();
                if (StringUtils.isNotEmpty(postName2) && StringUtils.isNotEmpty(postCode2)) {
                    StringBuilder str2 = new StringBuilder();
                    str2.append(firstLevel).append(",").append(secondLevel).append(",")
                            .append(postName2).append(",").append(postCode2);
                    resultData.add(str2.toString());
                }
            }


            if (count == catchSize) {
                try {
                    FileUtils.writeLines(new File("E:/postCode/" + CommonUtils.getCurrentMonth() + ".csv"), "UTF-8", resultData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
