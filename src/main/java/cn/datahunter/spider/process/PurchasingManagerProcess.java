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
 * Created by root on 2017/3/15.
 */
@Component
public class PurchasingManagerProcess implements PageProcessor {

    //制造业采购经理指数-industry  省份-nonindustry
    public static String PURCHASINGMANAGER_CATALOG = StringUtils.EMPTY;

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

        int columnNum = 13;

        String rawText = page.getRawText();
        JSONObject returndata = (JSONObject) JSON.parseObject(rawText).get("returndata");

        JSONArray datanodesJSONArr = returndata.getJSONArray("datanodes");

        List<String> resultData = new ArrayList<>();

        /*取列名* */
        JSONObject wdnodesJSONObject = returndata.getJSONArray("wdnodes").getJSONObject(0);
        JSONArray wdnodes = wdnodesJSONObject.getJSONArray("nodes");
        List<String> wdLst = new ArrayList<>();
        for (int i = 0; i < wdnodes.size(); i++) {
            JSONObject jsonObject = (JSONObject) wdnodes.get(i);
            String name = jsonObject.getString("name");
            wdLst.add(name + "(%)");
        }
        resultData.add(CommonUtils.removeBrackets(wdLst.toString()));

        /*取数据*/
        List<String> dataLst = new ArrayList<>();
        for (int i = 1; i < datanodesJSONArr.size() + 1; i++) {
            //一共13列，余数是1就是第一列
            if (i % columnNum == 1) {
                JSONObject data = (JSONObject) datanodesJSONArr.get(i - 1);
                JSONObject dataObj = (JSONObject) data.get("data");
                dataLst.add(dataObj.getString("data"));
            }
        }
        resultData.add(CommonUtils.removeBrackets(dataLst.toString()));

        try {
            FileUtils.writeLines(new File("E:/" + PURCHASINGMANAGER_CATALOG + "/" + CommonUtils.getCurrentMonth() + ".csv"), "UTF-8", resultData);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
