package cn.datahunter.spider.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.datahunter.spider.util.CommonUtils;
import cn.datahunter.spider.util.Constants;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by root on 2017/3/15.
 */
public class PopulationProcess implements PageProcessor {

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

    public static String CATALOG_POPULATION = StringUtils.EMPTY;

    List<String> resultData = new ArrayList<>();
    List<String> wdLst = new ArrayList<>();
    List<String> dataLst = new ArrayList<>();

    JSONArray datanodesJSONArr = new JSONArray();

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {

        String rawText = page.getRawText();
        JSONObject returndata = (JSONObject) JSON.parseObject(rawText).get("returndata");

        /*取列名* */
        int arrIndex = StringUtils.equals(CATALOG_POPULATION, Constants.CATALOG_POPULATION_ALL) ? 0 : 1;
        JSONObject wdnodesJSONObject = returndata.getJSONArray("wdnodes").getJSONObject(arrIndex);
        JSONArray wdnodes = wdnodesJSONObject.getJSONArray("nodes");
        for (int i = 0; i < wdnodes.size(); i++) {
            JSONObject jsonObject = (JSONObject) wdnodes.get(i);
            String name = jsonObject.getString("name");
            wdLst.add(name);
        }

        /*取数据*/
        datanodesJSONArr = returndata.getJSONArray("datanodes");

        if (StringUtils.equals(CATALOG_POPULATION, Constants.CATALOG_POPULATION_MAINCITY)) {

            int columnNum = 36;

            for (int i = 0; i < columnNum; i++) {

                setDataLst(i);
            }
        } else if (StringUtils.equals(CATALOG_POPULATION, Constants.CATALOG_POPULATION_PROVINCE)) {

            for (int i = 0; i < datanodesJSONArr.size(); i++) {

                setDataLst(i);
            }
        } else {

            int columnNum = 10;
            for (int i = 0; i < datanodesJSONArr.size(); i++) {
                if (i % columnNum == 0) {

                    setDataLst(i);
                }
            }
        }

        String year = StringUtils.EMPTY;

        //非全国
        if (!StringUtils.equals(CATALOG_POPULATION, Constants.CATALOG_POPULATION_ALL) && (wdLst.size() == dataLst.size())) {

            year = datanodesJSONArr.getJSONObject(0).getJSONArray("wds").getJSONObject(2).getString("valuecode");

            //把行转列
            for (int i = 0; i < wdLst.size(); i++) {

                StringBuilder line = new StringBuilder();
                if (i == 0) {

                    line.append("地区").append(",").append("人口数(万)").append(",").append("年份");
                    resultData.add(line.toString());
                    line = new StringBuilder();
                }

                line.append(wdLst.get(i)).append(",").append(dataLst.get(i)).append(",").append(year);
                resultData.add(line.toString());
            }
        } else {

            year = datanodesJSONArr.getJSONObject(0).getJSONArray("wds").getJSONObject(1).getString("valuecode");
            resultData.add(CommonUtils.removeBrackets(wdLst.toString()) + "," + "年份");
            resultData.add(CommonUtils.removeBrackets(dataLst.toString()) + "," + year);
        }

        //写入csv中
        try {
            FileUtils.writeLines(new File("/" + CATALOG_POPULATION + "/" + year + ".csv"), "UTF-8", resultData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据数组赋值
     */
    private void setDataLst(int index) {

        JSONObject data = (JSONObject) datanodesJSONArr.get(index);
        JSONObject dataObj = (JSONObject) data.get("data");
        dataLst.add(dataObj.getString("data"));
    }

}
