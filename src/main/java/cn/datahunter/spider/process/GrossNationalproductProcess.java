package cn.datahunter.spider.process;

import cn.datahunter.spider.util.CommonUtils;
import cn.datahunter.spider.util.GNPandGDPUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.datahunter.spider.util.GNPandGDPUtil.*;

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
    public static String GNP = StringUtils.EMPTY;
    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        List<String> dataOut = new ArrayList<>();
        List<String> dataLst = new ArrayList<>();
        List<String> nameLst = new ArrayList<>();
        String rawText = page.getRawText();
        JSONObject jsonObject = JSON.parseObject(rawText);
        JSONObject      returnData  = jsonObject.getJSONObject("returndata");
       if(GNP.equals("CONTRY_GNP_YEAR")) {
           Map<String, List> contryGnpMap = contryGnp(nameLst, dataLst, returnData);
           List name = contryGnpMap.get("name");
           List data = contryGnpMap.get("data");
           nameLst=name;
           dataLst=data;

       }
        if(GNP.equals("AREA_GNP_YEAR")||GNP.equals("AREA_GNP_MONTH")) {
            Map<String, List> contryGnpMap = areaGnp(nameLst, dataLst, returnData);
            List name = contryGnpMap.get("name");
            List data = contryGnpMap.get("data");
            if(GNPandGDPUtil.timp==1) {
                nameLst = name;
                GNPandGDPUtil.DATALIST.add("分类,"+CommonUtils.removeBrackets(nameLst.toString()));
            }
            dataLst=data;
            GNPandGDPUtil.DATALIST.add(GNPandGDPUtil.getGNPColumnName(GNPandGDPUtil.timp)+","+CommonUtils.removeBrackets(
                    dataLst.toString()));
         }

        if(!(GNP.equals(AREA_GNP_YEAR)||GNP.equals(AREA_GNP_MONTH))){
             dataOut.add(CommonUtils.removeBrackets(nameLst.toString()));
             dataOut.add(CommonUtils.removeBrackets(dataLst.toString()));
        }


        try {
            if(GNP.equals("AREA_GNP_YEAR")||GNP.equals("AREA_GNP_MONTH")){
                if(GNPandGDPUtil.timp==11) {
                    FileUtils.writeLines(new File("G:/" + GNPandGDPUtil.getGNPName(GNP) + CommonUtils.getBeforeMonth(0) + ".csv"), "UTF-8", GNPandGDPUtil.DATALIST);
                }
            }else{
            FileUtils.writeLines(new File("G:/"+ GNPandGDPUtil.getGNPName(GNP) +CommonUtils.getBeforeMonth(0)+".csv"), "UTF-8", dataOut);}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //全国居民收支年度数据
    public Map<String,List> contryGnp(List<String> nameLst,List<String> dataLst,JSONObject returnData){
        Map<String,List> map=new HashMap<>();
        JSONArray dataArrname = returnData.getJSONArray("wdnodes");
        JSONObject nodes=dataArrname.getJSONObject(0);
        JSONArray nodes1 = (JSONArray)nodes.get("nodes");
        for(int i = 0; i < nodes1.size(); i+=2 ){
            nameLst.add(nodes1.getJSONObject(i).get("cname").toString());
        }
        JSONArray dataArr = returnData.getJSONArray("datanodes");
        for (int i = 0; i < dataArr.size(); i+=20) {
            JSONObject data =(JSONObject)dataArr.getJSONObject(i).get("data");
            if (null != data && StringUtils.isNotBlank(data.toString())) {
                String strdata = data.getString("strdata");
                dataLst.add(strdata);
            }
        }
        map.put("name",nameLst);
        map.put("data",dataLst);
        return map;
    }

    //地区居民收支年度数据---季度
    public Map<String,List> areaGnp(List<String> nameLst,List<String> dataLst,JSONObject returnData){
        Map<String,List> map=new HashMap<>();
        JSONArray dataArrname = returnData.getJSONArray("wdnodes");
        JSONArray nodes=dataArrname.getJSONObject(1).getJSONArray("nodes");
       for(int i = 0; i < nodes.size(); i++){
            nameLst.add(nodes.getJSONObject(i).get("cname").toString());
        }
        JSONArray dataArr = returnData.getJSONArray("datanodes");
        for (int i = 0; i < dataArr.size(); i+=GNP.equals("AREA_GNP_YEAR") ? 10:6) {
            JSONObject data =(JSONObject)dataArr.getJSONObject(i).
                    getJSONObject("data");
            if (null != data && StringUtils.isNotBlank(data.toString())) {
                String strdata = data.getString("strdata");
                dataLst.add(strdata);
            }
        }
        map.put("name",nameLst);
        map.put("data",dataLst);
        return map;
    }
}
