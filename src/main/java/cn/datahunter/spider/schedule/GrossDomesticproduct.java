package cn.datahunter.spider.schedule;

import cn.datahunter.spider.process.GrossDomesticcproductProcess;
import cn.datahunter.spider.process.GrossNationalproductProcess;
import cn.datahunter.spider.util.CommonUtils;
import cn.datahunter.spider.util.GNPandGDPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

@Component
public class GrossDomesticproduct {

    @Autowired
    private static GrossDomesticcproductProcess Gdp;

    @Scheduled(cron = "0 01 11 * * *", zone = "Asia/Shanghai")
    public void doExecute() {
 }

    public static void main(String[] args) {
        excutEareaGDP();

    }
    //国内生产总值--年度
    @Scheduled(cron = "0 01 11 * * *", zone = "Asia/Shanghai")
    public static void  excuteCountryGDP() {
        GNPandGDPUtil.DATALIST.clear();
        GNPandGDPUtil.NAMELIST.clear();
        String url="";
        for(int i=1;i<4;i++){
            if(i==1) {
                GNPandGDPUtil.timp=10+i;
                url="http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgnd&rowcode=zb&colcode=sj&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A0201%22%7D%5D&k1=1489735010332";
            }else{
                GNPandGDPUtil.timp=20+i;
                url="http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgnd&rowcode=zb&colcode=sj&wds=%5B%5D&dfwds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A02020"+(i-1)+"%22%7D%5D&k1=1489738660016";
            }
        url = url.replace(CommonUtils.getBeforeMonth(0), CommonUtils.getBeforeMonth(0));
       //全国居民GNP-----年度
        Gdp.GDP="CONTRY_GDP_YEAR";
        Spider.create(new GrossDomesticcproductProcess()).addUrl(url)
                .thread(1).run();
    }}
    //各省的地区生产总值--年度
    @Scheduled(cron = "0 01 11 * * *", zone = "Asia/Shanghai")
     public static void  excutEareaGDP() {
        GNPandGDPUtil.DATALIST.clear();
        GNPandGDPUtil.NAMELIST.clear();
        String url= "";
        for(int i=1; i<10;i++) {
            if(i<5) {
                 GNPandGDPUtil.timp=i;
                 url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=fsnd&rowcode=reg&colcode=sj&wds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A02010"+i+"%22%7D%5D&dfwds=%5B%5D&k1=1489996819528";
            }
            else if(i==5){
                GNPandGDPUtil.timp=i;
             url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=fsnd&rowcode=reg&colcode=sj&wds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A02010G%22%7D%5D&dfwds=%5B%5D&k1=1489996819528";
            }
            else{
                GNPandGDPUtil.timp=i;
                url="http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=fsnd&rowcode=reg&colcode=sj&wds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A02020"+(i-5)+"%22%7D%5D&dfwds=%5B%5D&k1=1489997906929";
            }

            url = url.replace(CommonUtils.getBeforeMonth(0), CommonUtils.getBeforeMonth(0));
            //地区居民GNP-----年度
            Gdp.GDP = "AREA_GDP_YEAR";
            Spider.create(new GrossDomesticcproductProcess()).addUrl(url)
                    .thread(1).run();
        }
    }




}