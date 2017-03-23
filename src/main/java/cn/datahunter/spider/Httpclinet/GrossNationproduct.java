package cn.datahunter.spider.Httpclinet;

import cn.datahunter.spider.util.CommonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.omg.CORBA.Object;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
public class GrossNationproduct {
    /**
     * @param args
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static void main(String[] args) throws IOException, IOException
    {
        List<String> output=new ArrayList<>();
        String url1 = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgjd&rowcode=zb&colcode=sj&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A0501%22%7D%5D&k1=1489633246976";
        String url2 = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgjd&rowcode=zb&colcode=sj&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A0502%22%7D%5D&k1=1489635097812";
        String url3 = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgjd&rowcode=zb&colcode=sj&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A0503%22%7D%5D&k1=1489635625650";
        List<String> listname1=new ArrayList<>();
        List<String> listdata1=new ArrayList<>();
        Map<String, List<Object>> stringObjectMap =residentsOfbalanceOfpayments(url1, listname1, listdata1);
        List<Object> listname2 =stringObjectMap.get("name");
        List<Object> listdata2 =stringObjectMap.get("data");
        Map<String, List<Object>> urbanResidentsMap =urbanResidents(url2, listname2, listdata2);
        List<Object> listname3 =urbanResidentsMap.get("name");
        List<Object> listdata3 =urbanResidentsMap.get("data");
        Map<String, List<Object>> uralesidentsMap =uralesidents(url3, listname3, listdata3);
        List<Object> listnamelast =uralesidentsMap.get("name");
        List<Object> listdatalast =uralesidentsMap.get("data");
        output.add(CommonUtils.removeBrackets(listnamelast.toString()));
        output.add(CommonUtils.removeBrackets(listdatalast.toString()));
        FileUtils.writeLines(new File("G:/"+"全国居民季度收支表"+ CommonUtils.getBeforeMonth(0)+".csv"), "UTF-8", output);
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


        //全国居民收支情况
    public static Map<String,List<Object>> residentsOfbalanceOfpayments (String url, List listname, List listdata) throws IOException {
        HashMap<String,List<Object>> map= new HashMap<>();
        // 创建HttpClient实例
        HttpClient httpclient = new DefaultHttpClient();
        // 创建Get方法实例
        //String url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgjd&rowcode=zb&colcode=sj&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A0501%22%7D%5D&k1=1489570740759";
        HttpGet httpgets = new HttpGet(url);
        HttpResponse    response = httpclient.execute(httpgets);
       HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instreams = entity.getContent();
            String str = convertStreamToString(instreams);
            JSONObject parse = (JSONObject) JSON.parse(str);
            JSONObject returndata = parse.getJSONObject("returndata");
          //获取数据
            JSONArray datanodes = returndata.getJSONArray("datanodes");
            for ( int i = 0; i <datanodes.size() ; i++ ) {
                if(i==0||i==48) {
                    JSONObject data = (JSONObject) datanodes.getJSONObject(i).get("data");
                    String strdata = (String) data.get("strdata");
                    listdata.add(strdata);
                }
            }
          //获取名字
            JSONObject nodes = returndata.getJSONArray("wdnodes").getJSONObject(0);
            JSONArray dataname = nodes.getJSONArray("nodes");
            for ( int i = 0; i <dataname.size() ; i++ ) {
                if(i==0||i==8) {
                    String cname = dataname.getJSONObject(i).getString("cname");
                    listname.add(cname);
                }
            }
           map.put("name",listname);
            map.put("data",listdata);
        }

        return map;
    }

    //居民收支情况
    public static Map<String,List<Object>> urbanResidents (String url, List listname, List listdata) throws IOException {
        HashMap<String,List<Object>> map= new HashMap<>();
        // 创建HttpClient实例
        HttpClient httpclient = new DefaultHttpClient();
        // 创建Get方法实例
        //String url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgjd&rowcode=zb&colcode=sj&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A0501%22%7D%5D&k1=1489570740759";
        HttpGet httpgets = new HttpGet(url);
        HttpResponse    response = httpclient.execute(httpgets);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instreams = entity.getContent();
            String str = convertStreamToString(instreams);
            JSONObject parse = (JSONObject) JSON.parse(str);
            JSONObject returndata = parse.getJSONObject("returndata");
            //获取数据
            JSONArray datanodes = returndata.getJSONArray("datanodes");
            for ( int i = 0; i <datanodes.size() ; i++ ) {
                if(i==0||i==36) {
                    JSONObject data = (JSONObject) datanodes.getJSONObject(i).get("data");
                    String strdata = (String) data.get("strdata");
                    listdata.add(strdata);
                }
            }
            //获取名字
            JSONObject nodes = returndata.getJSONArray("wdnodes").getJSONObject(0);
            JSONArray dataname = nodes.getJSONArray("nodes");
            for ( int i = 0; i <dataname.size() ; i++ ) {
                if(i==0||i==6) {
                    String cname = dataname.getJSONObject(i).getString("cname");
                    listname.add(cname);
                }
            }
            map.put("name",listname);
            map.put("data",listdata);
        }

        return map;
    }
    //农村居民收支
    public static Map<String,List<Object>> uralesidents (String url, List listname, List listdata) throws IOException {
        HashMap<String,List<Object>> map= new HashMap<>();
        // 创建HttpClient实例
        HttpClient httpclient = new DefaultHttpClient();
        // 创建Get方法实例
        //String url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgjd&rowcode=zb&colcode=sj&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A0501%22%7D%5D&k1=1489570740759";
        HttpGet httpgets = new HttpGet(url);
        HttpResponse    response = httpclient.execute(httpgets);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instreams = entity.getContent();
            String str = convertStreamToString(instreams);
            JSONObject parse = (JSONObject) JSON.parse(str);
            JSONObject returndata = parse.getJSONObject("returndata");
            //获取数据
            JSONArray datanodes = returndata.getJSONArray("datanodes");
            for ( int i = 0; i <datanodes.size() ; i++ ) {
                if(i==0||i==36) {
                    JSONObject data = (JSONObject) datanodes.getJSONObject(i).get("data");
                    String strdata = (String) data.get("strdata");
                    listdata.add(strdata);
                }
            }
            //获取名字
            JSONObject nodes = returndata.getJSONArray("wdnodes").getJSONObject(0);
            JSONArray dataname = nodes.getJSONArray("nodes");
            for ( int i = 0; i <dataname.size() ; i++ ) {
                if(i==0||i==6) {
                    String cname = dataname.getJSONObject(i).getString("cname");
                    listname.add(cname);
                }
            }
            map.put("name",listname);
            map.put("data",listdata);
        }

        return map;
    }
}