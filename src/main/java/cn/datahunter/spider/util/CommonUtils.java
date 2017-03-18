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

    /**
     * 判断是否是给定省或直辖市的二级地区
     * @param code  需要判断的区域代码
     * @param topTwoOffset  给定省或直辖市的前2位
     * @return
     */
    public static boolean isSecondLevelRegison(String code,String topTwoOffset) {
        if (StringUtils.isNotEmpty(code)) {
            String codeTop = code.substring(0, 2);
            if (codeTop.equals(topTwoOffset) && !code.endsWith("0000") && code.endsWith("00")) {
                return true;
            }
        }
        return false;
    }

}
