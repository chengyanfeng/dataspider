package cn.datahunter.spider.schedule;

import cn.datahunter.spider.process.GrossNationalproductProcess;
import cn.datahunter.spider.util.CommonUtils;
import cn.datahunter.spider.util.GNPandGDPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

@Component
public class GrossNationalproduct {

    @Autowired
    private static  GrossNationalproductProcess Gnp;

    @Scheduled(cron = "0 01 11 * * *", zone = "Asia/Shanghai")
    public void doExecute() {
 }

    public static void main(String[] args) {
        //excuteCountryGNP();
      //excutEareaGnp();
        //excutEareaGnpMonth();
       //excutEareaGnpMonth();
        excutEareaGnp();


    }
    //全国居民年度支出
    @Scheduled(cron = "0 01 11 * * *", zone = "Asia/Shanghai")
    public static void  excuteCountryGNP() {

        GNPandGDPUtil.DATALIST.clear();
        GNPandGDPUtil.NAMELIST.clear();

        String url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgjd&rowcode=zb&colcode=sj&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A0501%22%7D%5D&k1=1489570740759";
        url = url.replace(CommonUtils.getBeforeMonth(0), CommonUtils.getBeforeMonth(0));
       //全国居民GNP-----年度
        Gnp.GNP="CONTRY_GNP_YEAR";
        Spider.create(new GrossNationalproductProcess()).addUrl(url)
                .thread(1).run();
    }
    @Scheduled(cron = "0 01 11 * * *", zone = "Asia/Shanghai")
    //各省的居民年度支出
    public static void  excutEareaGnp() {
        GNPandGDPUtil.DATALIST.clear();
        GNPandGDPUtil.NAMELIST.clear();
        String url= "";
        for(int i=1; i<12;i+=2) {
            if(i>10) {
                 GNPandGDPUtil.timp=i;
                 url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=fsnd&rowcode=reg&colcode=sj&wds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A0A00"+i+"%22%7D%5D&dfwds=%5B%5D&k1=1489715545221";
            }
            else{
                GNPandGDPUtil.timp=i;
             url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=fsnd&rowcode=reg&colcode=sj&wds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A0A000"+i+"%22%7D%5D&dfwds=%5B%5D&k1=1489715545221";
            }

            url = url.replace(CommonUtils.getBeforeMonth(0), CommonUtils.getBeforeMonth(0));
            //地区居民GNP-----年度
            Gnp.GNP = "AREA_GNP_YEAR";
            Spider.create(new GrossNationalproductProcess()).addUrl(url)
                    .thread(1).run();
        }
    }

    //各省的居民季度支出
    @Scheduled(cron = "0 01 11 * * *", zone = "Asia/Shanghai")
   public static void  excutEareaGnpMonth() {
        GNPandGDPUtil.DATALIST.clear();
        GNPandGDPUtil.NAMELIST.clear();
    String url= "";
        for(int i=1; i<12;i+=2) {
        if(i>10) {
            GNPandGDPUtil.timp=i;
            url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=fsjd&rowcode=reg&colcode=sj&wds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A0300"+i+"%22%7D%5D&dfwds=%5B%5D&k1=1489715545221";
        }
        else{
            GNPandGDPUtil.timp=i;
            url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=fsjd&rowcode=reg&colcode=sj&wds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A03000"+i+"%22%7D%5D&dfwds=%5B%5D&k1=1489715545221";
        }

        url = url.replace(CommonUtils.getBeforeMonth(0), CommonUtils.getBeforeMonth(0));
        //地区居民GNP-----季度
        Gnp.GNP = "AREA_GNP_MONTH";
        Spider.create(new GrossNationalproductProcess()).addUrl(url)
                .thread(1).run();
    }
}


}