package cn.datahunter.spider.process;

import cn.datahunter.spider.util.CommonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2017/3/14.
 */
@Component
public class GrossNationalproductProcess implements PageProcessor {
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
        List<String> dataall = new ArrayList<>();
        List<String> dataLst = new ArrayList<>();
        List<String> line = new ArrayList<>();
        String rawText = page.getRawText();
        JSONObject jsonObject = JSON.parseObject(rawText);
        JSONObject      returndata  = jsonObject.getJSONObject("returndata");
        JSONArray dataArrname = returndata.getJSONArray("wdnodes");
        JSONObject nodes=dataArrname.getJSONObject(0);
        JSONArray nodes1 = (JSONArray)nodes.get("nodes");
            for(int i = 0; i < nodes1.size(); i+=2 ){
                line.add(nodes1.getJSONObject(i).get("cname").toString());
            }
        JSONArray dataArr = returndata.getJSONArray("datanodes");
         for (int i = 0; i < dataArr.size(); i+=20) {
            JSONObject data =(JSONObject)dataArr.getJSONObject(i).get("data");
            if (null != data && StringUtils.isNotBlank(data.toString())) {
                String strdata = data.getString("strdata");
                dataLst.add(strdata);
             }
         }
        dataall.add(CommonUtils.removeBrackets(line.toString()));
        dataall.add(CommonUtils.removeBrackets(dataLst.toString()));

        try {
            FileUtils.writeLines(new File("G:/"+ CommonUtils.getCurrentMonth()+".csv"), "UTF-8", dataall);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
