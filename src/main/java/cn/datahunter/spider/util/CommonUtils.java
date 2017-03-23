package cn.datahunter.spider.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    public static String getBeforeMonth(Integer month) {
        String currentMonth = dateTime.minusMonths(month).toString("yyyyMM");
        return currentMonth;
    }

    /**
     * 自定义时间格式,返回年月日
     */
    public static String getBeforeMonth(Integer month, String format) {
        String currentMonth = dateTime.minusMonths(month).toString(format);
        return currentMonth;
    }

    /**
     * 判断是否是给定省或直辖市的二级地区
     *
     * @param code         需要判断的区域代码
     * @param topTwoOffset 给定省或直辖市的前2位
     */
    public static boolean isSecondLevelRegison(String code, String topTwoOffset) {
        if (StringUtils.isNotEmpty(code)) {
            String codeTop = code.substring(0, 2);
            if (codeTop.equals(topTwoOffset) && !code.endsWith("0000") && code.endsWith("00")) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getDatesBetweenTwoDate(String beginDate, String endDate) throws ParseException {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin = sdf.parse(beginDate);
        Date dEnd = sdf.parse(endDate);

        List lDate = new ArrayList();
        lDate.add(sdf.format(dBegin));
        cal.setTime(dBegin);
        boolean bContinue = true;
        while (bContinue) {
            //根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            if (dEnd.after(cal.getTime())) {
                lDate.add(sdf.format(cal.getTime()));
            } else {
                break;
            }
        }

        lDate.add(sdf.format(dEnd));//把结束时间加入集合
        return lDate;
    }

    /**
     * 获取接口数据
     */
    public static String getRemoteData(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        InputStream in = url.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte buf[] = new byte[1024];
            int read = 0;
            while ((read = in.read(buf)) > 0) {
                out.write(buf, 0, read);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        byte b[] = out.toByteArray();

        return new String(b, "utf-8");
    }

}
