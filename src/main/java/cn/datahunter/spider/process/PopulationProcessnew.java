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

/**
 * Created by root on 2017/3/14.
 */
@Component
public class PopulationProcessnew implements PageProcessor {

    private Site site =
            Site.me()
                    .setCharset("utf-8")
                    .setSleepTime(3000)
                    .setRetryTimes(3)
                    .setCycleRetryTimes(3)
                    .setTimeOut(60000)
                    .addCookie("JSESSIONID","230C63250897C58564351909DCDE7B52")
                    .addCookie("u","6")
                    .addHeader("Accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .addHeader(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
    public static String ARG = StringUtils.EMPTY;
    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        //备用list
        List<String> dataOut = new ArrayList<>();
        List<String> dataLst = new ArrayList<>();
        //List<String> nameLst = new ArrayList<>();
        //解析page
        String rawText = page.getRawText();
        JSONObject jsonObject = JSON.parseObject(rawText);
        JSONObject      returnData  = jsonObject.getJSONObject("returndata");
        //人口
        Map<String, List> AgriculturalMap = getPopulation(dataLst, returnData);
        GNPandGDPUtil.NAMELIST.add(GNPandGDPUtil.ColumnName);
        List<String> datalist = AgriculturalMap.get("data");
        if(GNPandGDPUtil.timp==1){
            for(int i=0;i<datalist.size();i=i+4) {
                GNPandGDPUtil.DATALIST.add(datalist.get(0+i) + "," +
                        datalist.get(1+i) + "," +
                        datalist.get(2+i)
                        + "," +
                        datalist.get(3+i));

            }
        }else{
            for(int i=1;i<GNPandGDPUtil.DATALIST.size()+1;i++)
            {
                String s =GNPandGDPUtil.DATALIST.get(i-1)+","+datalist.get(i*4-1);

                GNPandGDPUtil.DATALIST.add(i-1,s);
                GNPandGDPUtil.DATALIST.remove(i);



            }
        }
        try {
            if(GNPandGDPUtil.timp==4){
                dataOut.add(CommonUtils.removeBrackets(GNPandGDPUtil.NAMELIST.toString()));
                dataOut.addAll(GNPandGDPUtil.DATALIST);
                FileUtils.writeLines(new File("F:\\test\\test\\" + "人口" + CommonUtils.getBeforeMonth(0) + ".csv"), "UTF-8", dataOut);
                FileUtils.writeLines(new File("F:\\test\\test\\" +  "人口" + "最新.csv"), "UTF-8", GNPandGDPUtil.DATALIST);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //获取各个省的人口
    public Map<String,List> getPopulation(List<String> dataLst,JSONObject returnData){
        Map<String,List> map=new HashMap<>();
        JSONArray dataArrname = returnData.getJSONArray("wdnodes");
        GNPandGDPUtil.ColumnName=dataArrname.getJSONObject(0).getJSONArray("nodes").getJSONObject(0).getString("cname");
        JSONArray nodes=dataArrname.getJSONObject(1).getJSONArray("nodes");
        JSONArray dataArr = returnData.getJSONArray("datanodes");
        //获取众多个年份
        JSONArray year = dataArrname.getJSONObject(2).getJSONArray("nodes");

        for(int b=0;b<year.size();b++) {
            for (int i = 0; i < nodes.size(); i++) {
                dataLst.add(nodes.getJSONObject(i).get("cname").toString());
                dataLst.add(year.getJSONObject(b).getString("name").substring(0,4));
                dataLst.add(year.getJSONObject(b).getString("name"));

                JSONObject data = (JSONObject) dataArr.getJSONObject(i*year.size()+b).
                        getJSONObject("data");
                if (null != data && StringUtils.isNotBlank(data.toString())) {
                    String strdata = data.getString("strdata");
                    dataLst.add(strdata);
                    System.out.print(GNPandGDPUtil.timp);
                if(GNPandGDPUtil.timp==3){
                    System.out.print("data");
                }
                }
            }


        }
        map.put("data",dataLst);
        return map;
    }
}
