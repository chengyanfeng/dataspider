package cn.datahunter.spider.process;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.datahunter.spider.util.CommonUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 * Created by root on 2017/3/16.
 */
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

        List<Selectable> pnodes = page.getHtml().xpath("//div[@class='TRS_PreAppend']/p[@class='MsoNormal']").nodes();

        List<String> resultData = new ArrayList<>();
        resultData.add("行政区划代码,行政区划名称");

        Map<String, Map<String,List<String>>> hierarchyMap = new HashMap<>();
        Map<String, List<String>> internalMap = new HashMap<>();
        List<String> dataLst = new LinkedList<>();

        String hierarchyKey = StringUtils.EMPTY;
        String internalKey = StringUtils.EMPTY;

        for (Selectable pnode : pnodes) {

            String regionCode = pnode.xpath("//b[1]/span[@lang='EN-US']/text()").get();
            regionCode = StringUtils.isEmpty(regionCode) ? pnode.xpath("//span[@lang='EN-US']/text()").get() : regionCode;

            String regionName = pnode.xpath("//b[2]/span/text()").get();
            regionName = StringUtils.isEmpty(regionName) ? pnode.xpath("//span[3]/text()").get() : regionName;
            regionName = org.springframework.util.StringUtils.trimAllWhitespace(regionName);

            //后4位是0，表示省或直辖市
            if (StringUtils.isNotEmpty(regionCode) && regionCode.endsWith("0000")) {
                hierarchyMap.clear();

                dataLst.add(0, regionCode);
                dataLst.add(1, regionName);

                internalKey = regionCode.substring(0, 3);
                internalMap.put(internalKey, dataLst);

                hierarchyKey = regionCode.substring(0, 2);
                hierarchyMap.put(hierarchyKey, internalMap);
            }

            if (CommonUtils.isSecondLevelRegison(regionCode, hierarchyKey)) {
                dataLst.add(2, regionCode);
                dataLst.add(3, regionName);

                internalMap = hierarchyMap.get(hierarchyKey);

                internalMap.clear();
                internalKey = regionCode.substring(0, 4);
                internalMap.put(internalKey,dataLst);
            }




            if (StringUtils.isEmpty(regionCode)) {
                //市级以下


                if (StringUtils.isNotEmpty(regionName) && !regionName.equals("市辖区")) {

                    StringBuilder str = new StringBuilder();
                    str.append(regionCode).append(",").append(regionName);
                    resultData.add(str.toString());
                }

            }else{
                //市级
                StringBuilder str = new StringBuilder();
                str.append(regionCode).append(",").append(regionName);
                resultData.add(str.toString());
            }
        }

        try {
            FileUtils.writeLines(new File("E:/regioncode/" + CommonUtils.getCurrentMonth() + ".csv"), "UTF-8", resultData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
