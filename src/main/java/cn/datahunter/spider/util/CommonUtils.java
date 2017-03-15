package cn.datahunter.spider.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

/**
 * Created by root on 2017/3/14.
 */
public class CommonUtils {

    static DateTime dateTime = new DateTime();

    public static String removeBrackets(String str) {
        str = StringUtils.removeStart(str, "[");
        str = StringUtils.removeEnd(str, "]");
        return str;
    }

    /**
     * 站点只能获得上个月的统计值，这里的current代表上个月
     * ep:201702
     */
    public static String getCurrentMonth() {
        String currentMonth = dateTime.minusMonths(1).toString("yyyyMM");
        return currentMonth;
    }

    /**
     * currentMonth再减一个月
     * ep:201701
     */
    public static String getLastMonth() {
        String lastMonth = dateTime.minusMonths(2).toString("yyyyMM");
        return lastMonth;
    }
}
