package cn.datahunter.spider.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.datahunter.spider.util.CommonUtils;
import cn.datahunter.spider.util.Constants;

/**
 * Created by root on 2017/3/20.
 */
@Component
public class WeatherJob {

    /**
     * 当天主要城市/区县 天气
     */
    @Scheduled(cron = "0 50 17 * * *", zone = "Asia/Shanghai")
    public void execute1() throws ParseException, IOException {

        getTodayWeather(false);
        getTodayWeather(true);
    }


    /**
     * 历史区县天气
     */
//    @Scheduled(cron = "0 5 0 23 3 *", zone = "Asia/Shanghai")
    public void execute3() throws ParseException, IOException {
        List<String> datesBetweenTwoDate = CommonUtils.getDatesBetweenTwoDate("2015-07-07", "2017-03-22");
        getHistoryWeather(true, datesBetweenTwoDate);
    }

    /**
     * 历史主要城市天气
     */
//    @Scheduled(cron = "0 5 0 23 3 *", zone = "Asia/Shanghai")
    public void execute4() throws ParseException, IOException {
        List<String> datesBetweenTwoDate = CommonUtils.getDatesBetweenTwoDate("2015-07-07", "2017-03-22");
        getHistoryWeather(true, datesBetweenTwoDate);
    }

    private void getHistoryWeather(boolean countryFlag, List<String> dateList) throws IOException {

        Map<String, String> areaMap = countryFlag ? getAreaMap() : getMainSimpleMap();

        String columnName = countryFlag ? "区县,城市,省份,日期,天气,最低气温,最高气温,风向,风速" :
                "城市,省份,日期,天气,最低气温,最高气温,风向,风速,空气质量级别";

        List<String> resultData = new ArrayList<>();
        resultData.add(columnName);
        List<Integer> tempSortLst = new ArrayList<>();

        Iterator<Map.Entry<String, String>> iterator = areaMap.entrySet().iterator();

        while (iterator.hasNext()) {
            String weaid = iterator.next().getKey();
            String cityAndProvince = areaMap.get(weaid);

            for (String date : dateList) {
                //从未来天气中获得当天的
                String urlStr = "" +
                        "http://api.k780.com:88/?app=weather.history&weaid=" + weaid + "&date=" + date + "&appkey=23789&sign=abe1ba69c5f65c3fd1d95c535a5f7ed4&format=json";
                String remoteData = CommonUtils.getRemoteData(urlStr);

                JSONObject jsonObj = JSON.parseObject(remoteData);
                JSONArray jsonArray = jsonObj.getJSONArray("result");

                for (Object obj : jsonArray) {

                    JSONObject entity = (JSONObject) obj;
                    tempSortLst.add(entity.getInteger("temp"));
                    Collections.sort(tempSortLst);
                }

                StringBuilder fullInformation = new StringBuilder();

                JSONObject entity = jsonArray.getJSONObject(0);

                String citynm = entity.getString("citynm");
                fullInformation.append(citynm).append(",").append(cityAndProvince).append(",");

                fullInformation.append(date).append(",");

                String weather = entity.getString("weather");
                fullInformation.append(weather).append(",");

                Integer temp_low = tempSortLst.get(0);
                fullInformation.append(temp_low).append(",");
                Integer temp_high = tempSortLst.get(tempSortLst.size() - 1);
                fullInformation.append(temp_high).append(",");
                tempSortLst.clear();

                String wind = entity.getString("wind");
                fullInformation.append(wind).append(",");

                String winp = entity.getString("winp");
                fullInformation.append(winp);

                if (!countryFlag) {
                    String aqi = entity.getString("aqi");
                    fullInformation.append(",").append(aqi);
                }

                resultData.add(fullInformation.toString());
            }
            try {
                Thread.sleep(7500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        try {
            String catalog = countryFlag ? Constants.CATALOG_WEATHER_HISTORY_COUNTRY : Constants.CATALOG_WEATHER_HISTORY_MAINCITY;
            FileUtils.writeLines(new File("/" + catalog + ".csv"), "UTF-8", resultData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 当天区县/主要城市天气
     * countryFlag true表示区县，false是主要城市
     */
    private void getTodayWeather(boolean countryFlag) throws IOException {

        //区县或主要城市
        Map<String, String> area3Map = countryFlag ? getAreaMap() : getMainSimpleMap();

        String columnName = countryFlag ? "区县,城市,省份,日期,天气,最低气温,最高气温,风向,风速" :
                "城市,省份,日期,天气,最低气温,最高气温,风向,风速,空气质量级别";

        List<String> resultData = new ArrayList<>();
        resultData.add(columnName);

        Iterator<Map.Entry<String, String>> iterator = area3Map.entrySet().iterator();
        while (iterator.hasNext()) {

            String weaid = iterator.next().getKey();
            String cityAndProvince = area3Map.get(weaid);

            //从未来天气中获得当天的
            String urlStr = "http://api.k780.com:88/?app=weather.future&weaid=" + weaid + "&&appkey=23789&sign=abe1ba69c5f65c3fd1d95c535a5f7ed4&format=json";
            String remoteData = CommonUtils.getRemoteData(urlStr);

            JSONObject jsonObj = JSON.parseObject(remoteData);
            jsonObj = (JSONObject) jsonObj.getJSONArray("result").get(0);

            StringBuilder fullInformation = new StringBuilder();

            String citynm = jsonObj.getString("citynm");
            fullInformation.append(citynm).append(",").append(cityAndProvince).append(",");

            String day = jsonObj.getString("days");
            fullInformation.append(day).append(",");

            String weather = jsonObj.getString("weather");
            fullInformation.append(weather).append(",");

            String temp_low = jsonObj.getString("temp_low");
            fullInformation.append(temp_low).append(",");

            String temp_high = jsonObj.getString("temp_high");
            fullInformation.append(temp_high).append(",");

            String wind = jsonObj.getString("wind");
            fullInformation.append(wind).append(",");

            String winp = jsonObj.getString("winp");
            fullInformation.append(winp);

            if (!countryFlag) {
                String url = "http://api.k780.com:88/?app=weather.pm25&weaid=" + weaid + "&appkey=23789&sign=abe1ba69c5f65c3fd1d95c535a5f7ed4&format=json";
                remoteData = CommonUtils.getRemoteData(url);
                jsonObj = JSON.parseObject(remoteData).getJSONObject("result");
                String aqi_levnm = jsonObj.getString("aqi_levnm");

                fullInformation.append(",").append(aqi_levnm);
            }

            resultData.add(fullInformation.toString());

            try {
                Thread.sleep(7500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String catalog = countryFlag ? Constants.CATALOG_WEATHER_COUNTRY : Constants.CATALOG_WEATHER_MAINCITY;

        String time = CommonUtils.getBeforeMonth(0, "yyyy-MM-dd");

        try {
            FileUtils.writeLines(new File("/" + catalog + "/" + time + ".csv"), "UTF-8", resultData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取主要城市数据
     */
    private Map<String, String> getMainSimpleMap() {

        Map<String, String> mainCityMap = new HashMap<>();
        mainCityMap.put("beijing", "北京");
        mainCityMap.put("tianjin", "天津");
        mainCityMap.put("shijiazhuang", "河北");
        mainCityMap.put("taiyuan", "山西");
        mainCityMap.put("huhehaote", "内蒙古");
        mainCityMap.put("shenyang", "辽宁");
        mainCityMap.put("dalian", "辽宁");
        mainCityMap.put("changchun", "吉林");
        mainCityMap.put("haerbin", "黑龙江");
        mainCityMap.put("shanghai", "上海");
        mainCityMap.put("nanjing", "江苏");
        mainCityMap.put("hangzhou", "浙江");
        mainCityMap.put("ningbo", "浙江");
        mainCityMap.put("hefei", "安徽");
        mainCityMap.put("fuzhou", "福建");
        mainCityMap.put("xiamen", "福建");
        mainCityMap.put("nanchang", "江西");
        mainCityMap.put("jinan", "山东");
        mainCityMap.put("qingdao", "山东");
        mainCityMap.put("zhengzhou", "河南");
        mainCityMap.put("wuhan", "湖北");
        mainCityMap.put("changsha", "湖南");
        mainCityMap.put("guangzhou", "广东");
        mainCityMap.put("shenzhen", "广东");
        mainCityMap.put("nanning", "广西");
        mainCityMap.put("haikou", "海南");
        mainCityMap.put("chongqing", "重庆");
        mainCityMap.put("chengdu", "四川");
        mainCityMap.put("guiyang", "贵州");
        mainCityMap.put("kunming", "云南");
        mainCityMap.put("lasa", "西藏");
        mainCityMap.put("xian", "陕西");
        mainCityMap.put("lanzhou", "甘肃");
        mainCityMap.put("xining", "青海");
        mainCityMap.put("yinchuan", "宁夏");
        mainCityMap.put("wulumuqi", "新疆");

        return mainCityMap;
    }

    /**
     * 获取区县cityid等数据
     */
    private Map<String, String> getAreaMap() throws IOException {

        String jsonString =
                CommonUtils.getRemoteData("http://api.k780.com:88/?app=weather.city&cou=1&appkey=23789&sign=abe1ba69c5f65c3fd1d95c535a5f7ed4&format=json");

        JSONObject allCity = (JSONObject) JSONObject.parseObject(jsonString).get("result");
        Collection<Object> allCityCollect = allCity.values();

        Map<String, String> area3 = new HashMap<String, String>();

        for (Object city : allCityCollect) {
            JSONObject cityJSONObj = (JSONObject) city;
            boolean cityWeather = cityJSONObj.containsValue("城区");
            if (!cityWeather) {
                area3.put(cityJSONObj.getString("cityid"), cityJSONObj.getString("area_2") + "," + cityJSONObj.getString("area_1"));
            }
        }
        return area3;
    }

}
