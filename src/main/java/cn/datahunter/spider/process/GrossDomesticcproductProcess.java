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

import static cn.datahunter.spider.util.GNPandGDPUtil.AREA_GNP_MONTH;
import static cn.datahunter.spider.util.GNPandGDPUtil.AREA_GNP_YEAR;
import static cn.datahunter.spider.util.GNPandGDPUtil.getGNPName;

/**
 * Created by root on 2017/3/14.
 */
@Component
public class GrossDomesticcproductProcess implements PageProcessor {

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
    public static String GDP = StringUtils.EMPTY;
    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        //备用list
        List<String> dataOut = new ArrayList<>();
        List<String> dataLst = new ArrayList<>();
        List<String> nameLst = new ArrayList<>();
        //解析page
        String rawText = page.getRawText();
        JSONObject jsonObject = JSON.parseObject(rawText);
        JSONObject      returnData  = jsonObject.getJSONObject("returndata");
       //国民经济数据--年度
        if(GDP.equals("CONTRY_GDP_YEAR")) {
           Map<String, List> contryGDpMap = contryGdp(nameLst, dataLst, returnData);
            GNPandGDPUtil.NAMELIST.addAll(contryGDpMap.get("name"));
           GNPandGDPUtil.DATALIST.addAll(contryGDpMap.get("data"));

       }
        if(GDP.equals("AREA_GDP_YEAR")) {
            Map<String, List> areaGdpMap = areaGdp(nameLst, dataLst, returnData);
           if(GNPandGDPUtil.timp==1) {
               nameLst = areaGdpMap.get("name");
               GNPandGDPUtil.DATALIST.add("分类," + CommonUtils.removeBrackets(nameLst.toString()));
           }
        int i=   GNPandGDPUtil.timp;
            GNPandGDPUtil.DATALIST.add(GNPandGDPUtil.getGDPColumnName(GNPandGDPUtil.timp)+","+CommonUtils.removeBrackets(
                    areaGdpMap.get("data").toString()));

         }

 try {
     if(GDP.equals("AREA_GDP_YEAR")&&GNPandGDPUtil.timp==9){
         FileUtils.writeLines(new File("G:/" + GNPandGDPUtil.getGNPName(GDP) + CommonUtils.getBeforeMonth(0) + ".csv"), "UTF-8", GNPandGDPUtil.DATALIST);
        }

            if(GDP.equals("CONTRY_GDP_YEAR")&&GNPandGDPUtil.timp==23){
                dataOut.add(CommonUtils.removeBrackets(GNPandGDPUtil.NAMELIST.toString()));
                dataOut.add(CommonUtils.removeBrackets(GNPandGDPUtil.DATALIST.toString()));
                FileUtils.writeLines(new File("G:/" + GNPandGDPUtil.getGNPName(GDP) + CommonUtils.getBeforeMonth(0) + ".csv"), "UTF-8", dataOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //国民GDP数据--年度
    public Map<String,List> contryGdp(List<String> nameLst,List<String> dataLst,JSONObject returnData){
        Map<String,List> map=new HashMap<>();
        JSONArray dataArrname = returnData.getJSONArray("wdnodes");
        JSONObject nodes=dataArrname.getJSONObject(0);
        JSONArray nodes1 = (JSONArray)nodes.get("nodes");
        for(int i = 0; i < nodes1.size(); i++ ){
            nameLst.add(nodes1.getJSONObject(i).get("cname").toString());
        }
        JSONArray dataArr = returnData.getJSONArray("datanodes");
        for (int i = 0; i < dataArr.size(); i+=10) {
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

    //地区GDP数据--年度
    public Map<String,List> areaGdp(List<String> nameLst,List<String> dataLst,JSONObject returnData){
        Map<String,List> map=new HashMap<>();
        JSONArray dataArrname = returnData.getJSONArray("wdnodes");
        JSONArray nodes=dataArrname.getJSONObject(1).getJSONArray("nodes");
       for(int i = 0; i < nodes.size(); i++){
            nameLst.add(nodes.getJSONObject(i).get("cname").toString());
        }
        JSONArray dataArr = returnData.getJSONArray("datanodes");
        for (int i = 0; i < dataArr.size(); i+=10) {
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
