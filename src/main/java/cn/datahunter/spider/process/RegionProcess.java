package cn.datahunter.spider.process;


import cn.datahunter.spider.util.CommonUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2017/3/16.
 */
@Component
public class RegionProcess implements PageProcessor {

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

        if (StringUtils.equals(page.getUrl().toString(), "http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/")) {

            String url = page.getHtml().xpath("//ul[@class='center_list_contlist']/li/a/@href").get();
            page.addTargetRequest(url);
        } else {

            List<Selectable> pnodes = page.getHtml().xpath("//div[@class='TRS_PreAppend']/p[@class='MsoNormal']").nodes();

            List<String> resultData = new ArrayList<>();
            resultData.add("省级行政区划代码,省级行政区划名称,市级行政区划代码,市级行政区划名称,区县行政区划代码,区县行政区划名称");

            String firstLevel = StringUtils.EMPTY;
            String secondLevel = StringUtils.EMPTY;
            String thirdyLevel = StringUtils.EMPTY;

            String topTwoOffset = StringUtils.EMPTY;

            for (Selectable pnode : pnodes) {

                String regionCode = pnode.xpath("//b[1]/span[@lang='EN-US']/text()").get();
                regionCode = StringUtils.isEmpty(regionCode) ? pnode.xpath("//span[@lang='EN-US']/text()").get() : regionCode;

                String regionName = pnode.xpath("//b[2]/span/text()").get();
                regionName = StringUtils.isEmpty(regionName) ? pnode.xpath("//span[3]/text()").get() : regionName;
                regionName = org.springframework.util.StringUtils.trimAllWhitespace(regionName);

                //后4位是0，表示省或直辖市
                if (StringUtils.isNotEmpty(regionCode) && regionCode.endsWith("0000")) {

                    firstLevel = regionCode + "," + regionName;

                    topTwoOffset = regionCode.substring(0, 2);
                } else if (CommonUtils.isSecondLevelRegison(regionCode, topTwoOffset)) {

                    //市或市辖区
                    secondLevel = regionCode + "," + regionName;
                } else {

                    thirdyLevel = firstLevel + "," + secondLevel + "," + regionCode + "," + regionName;

                    resultData.add(thirdyLevel);
                }

                try {
                    FileUtils.writeLines(new File("/regioncode/" + CommonUtils.getBeforeMonth(0) + ".csv"), "UTF-8", resultData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
