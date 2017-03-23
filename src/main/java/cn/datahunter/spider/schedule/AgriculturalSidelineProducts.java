package cn.datahunter.spider.schedule;

import cn.datahunter.spider.process.AgriculturalSidelineProductsProcess;
import cn.datahunter.spider.process.GrossDomesticcproductProcess;
import cn.datahunter.spider.util.CommonUtils;
import cn.datahunter.spider.util.GNPandGDPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import us.codecraft.webmagic.Spider;

/**
 * Created by Administrator on 2017/3/21.
 */
public class AgriculturalSidelineProducts {


    @Autowired
    private static GrossDomesticcproductProcess Gdp;

    @Scheduled(cron = "0 01 11 * * *", zone = "Asia/Shanghai")
    public void doExecute() {
    }

    public static void main(String[] args) {
        agriculturalProducts();

    }

    //各地的农产品价格--年度
    @Scheduled(cron = "0 01 11 * * *", zone = "Asia/Shanghai")
    public static void agriculturalProducts() {
        GNPandGDPUtil.DATALIST.clear();
        GNPandGDPUtil.NAMELIST.clear();
        String url = "";
       char ch='A';
        for (int i = 1; i < 36; i++) {
            if (i <10) {
                GNPandGDPUtil.timp =i;
               url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=fsjd&rowcode=reg&colcode=sj&wds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A05010"+i+"%22%7D%5D&dfwds=%5B%5D&k1=1490065438001";
            } else {
                GNPandGDPUtil.timp =i;
                int T=ch+i-10;
                url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=fsjd&rowcode=reg&colcode=sj&wds=%5B%7B%22wdcode%22%3A%22zb%22%2C%22valuecode%22%3A%22A05010"+(char)(T)+"%22%7D%5D&dfwds=%5B%5D&k1=1490065438001";
            }
            AgriculturalSidelineProductsProcess.ARG="AREA_AGRICULTURAL";
            url = url.replace(CommonUtils.getBeforeMonth(0), CommonUtils.getBeforeMonth(0));
            //全国居民GNP-----年度
            Spider.create(new AgriculturalSidelineProductsProcess()).addUrl(url)
                    .thread(1).run();
        }
    }



}
