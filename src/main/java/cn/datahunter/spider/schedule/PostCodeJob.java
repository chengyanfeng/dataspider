package cn.datahunter.spider.schedule;

import cn.datahunter.spider.process.PostCodeProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

/**
 * Created by root on 2017/3/18.
 */
@Component
public class PostCodeJob {

    @Autowired
    private PostCodeProcess postCodeProcess;

    public static void main(String[] args) {
        String url = "http://www.ip138.com/post/";
        Spider.create(new PostCodeProcess()).addUrl(url)
                .thread(1).run();
    }

}
