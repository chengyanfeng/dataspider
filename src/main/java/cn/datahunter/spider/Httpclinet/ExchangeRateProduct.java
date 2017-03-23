package cn.datahunter.spider.Httpclinet;

import cn.datahunter.spider.util.CommonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/20.
 */
public class ExchangeRateProduct {
//汇率接口
    public static void main(String args[]) throws Exception {
        List dataList=new ArrayList<>();
        URL u=new URL("http://api.k780.com:88/?app=finance.rate&scur=USD&tcur=CNY&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4");
        InputStream in=u.openStream();
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        try {
            byte buf[]=new byte[1024];
            int read = 0;
            while ((read = in.read(buf)) > 0) {
                out.write(buf, 0, read);
            }
        }  finally {
            if (in != null) {
                in.close();
            }
        }
        byte b[]=out.toByteArray( );
        System.out.println(new String(b,"utf-8"));
        JSONObject jsonObject = JSON.parseObject(new String(b,"utf-8"));
        JSONObject  returnData  = jsonObject.getJSONObject("result");

        String rate = returnData.getString("rate");
        String scur = returnData.getString("scur");
        String date = returnData.getString("update");
        String tcur = returnData.getString("tcur");
        String retenm = returnData.getString("retenm");
        String status = returnData.getString("status");
        dataList.add("rate,scur,date,tcur,retenm,status");
        dataList.add(rate+","+scur+","+date+","+tcur+","+retenm+","+status);


        FileUtils.writeLines(new File("G:/"+"人民币汇率"+ CommonUtils.getBeforeMonth(0)+".csv"), "UTF-8",dataList);
    }
}
