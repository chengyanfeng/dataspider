package cn.datahunter.spider.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */
public class GNPandGDPUtil {
    //全国居民年度数据---GNP
    public static final  String CONTRY_GNP_YEAR="全国居民年度数据";
    //省份居民年度数据---GNP
    public static final  String AREA_GNP_YEAR="地区居民年度数据";
    // 省份居民季度数据---GNP
    public static final  String AREA_GNP_MONTH="地区居民季度数据";
    //全国GDP每年的数据---GDP
    public static final String CONTRY_GDP_YEAR="全国GDP每年的数据";
    //省份GDP每年的数据---GDP
    public static final String AREA_GDP_YEAR="省份GDP每年的数据";
    //各个地级市GDP每年的数据--GDP
    public static final String AREA_GDP_CITY="各个地级市GDP每年的数据";
    //各个地级市GDP每年的数据--GDP
    public static final String AREA_AGRICULTURAL="各地区农副产品价格指数";

    //接收数据的list
    public static List<String> DATALIST=new ArrayList<>() ;
    //接收名字的list
    public static List<String> NAMELIST=new ArrayList<>() ;

    //循环次数
    public static  int timp;
    //
    public static  String ColumnName;
    //返回名称
    public static  String getGNPName(String name){
          if(name.equals("CONTRY_GNP_YEAR")){
              return CONTRY_GNP_YEAR;
          }else if(name.equals("AREA_GNP_YEAR")){
              return AREA_GNP_YEAR;
          }else if(name.equals("AREA_GNP_MONTH")){
            return AREA_GNP_MONTH;
        }else if(name.equals("CONTRY_GDP_YEAR")){
              return CONTRY_GDP_YEAR;
          }
          else if(name.equals("AREA_GDP_YEAR")){
              return AREA_GDP_YEAR;
          }
          else if(name.equals("AREA_GDP_CITY")){
              return AREA_GDP_CITY;
          }
          else if(name.equals("AREA_AGRICULTURAL")){
              return AREA_AGRICULTURAL;
          }


        else {
              return "未知数据";
          }
      }
    //返回列名
    public static String getGNPColumnName(int i){
        switch (i){
            case 1: return "居民人均可支配收入" ;
            case 3:return "城镇居民人均可支配收入";
            case 5:return "农村居民人均可支配收入";
            case 7: return "居民人均消费支入" ;
            case 9:return "城镇居民人均消费支入";

            case 11:return "农村居民人均消费支入";


        }
        return null;
    }
    public static String getGDPColumnName(int i){
        switch (i){
            case 1: return "地区生产总值" ;
            case 2:return "第一产业增加值(亿元)";
            case 3:return "第二产业增加值(亿元)";
            case 4: return "第三产业增加值(亿元)" ;
            case 5:return "人均地区生产总值(元/人)";
            case 6:return "地区生产总值指数";
            case 7:return "第一产业增加值指数";
            case 8:return "第二产业增加值指数";
            case 9:return "第三产业增加值指数";



        }
        return null;
    }


}