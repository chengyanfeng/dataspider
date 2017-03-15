package cn.datahunter.spider.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.datahunter.spider.util.CommonUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by root on 2017/3/14.
 */
@Component
public class ResidentConsumptionProcess implements PageProcessor {

    //主要城市-maincity  省份-province
    public static String catalog = StringUtils.EMPTY;

    private Site site =
            Site.me()
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
        JSONArray dataArr = jsonObject.getJSONArray("exceltable");

        List<String> dataLst = new ArrayList<>();
        List<String> line = new ArrayList<>();

        for (int i = 0; i < dataArr.size(); i++) {

            Object data = dataArr.getJSONObject(i).get("data");
            if (null != data && StringUtils.isNotBlank(data.toString())) {

                line.add(data.toString());
                if (line.size() == 16) {

                    dataLst.add(CommonUtils.removeBrackets(line.toString()));
                    line = new ArrayList<>();
                }

            }
        }
        try {
            FileUtils.writeLines(new File("E:/" + catalog + "/" + CommonUtils.getCurrentMonth() + ".csv"), "UTF-8", dataLst);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
