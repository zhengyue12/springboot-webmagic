package com.example.crawler.processer;

import com.example.crawler.domain.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * <h3>webmagic-scheduled</h3>
 * <p></p>
 *
 * @author : Culaccino
 * @date : 2020-05-14 07:40
 **/
@Component
public class NewsProcesser implements PageProcessor {

    private static final String URL = "https://www.jhc.cn/";

    @Autowired
    private RedisPipeline redisPipeline;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void process(Page page) {
        try {
            if (!page.getUrl().toString().contains("page.htm")) {
                page.addTargetRequests(page.getHtml().xpath("//li[@class='news-item']").links().all());
            } else {
                News newInfo = new News();
                String url = page.getUrl().toString();
                newInfo.setUrl(url);

                String title = page.getHtml().xpath("//h1[@class='arti-title']/text()").toString();
                newInfo.setTitle(title);

                String[] source = page.getHtml().xpath("//p[@class='arti-metas']/span[1]/text()").toString().split("：");
                newInfo.setSource(source[1]);

                String[] auther = page.getHtml().xpath("//p[@class='arti-metas']/span[2]/text()").toString().split("：");
                if (auther.length > 2) {
                    newInfo.setAuthor(auther[1]);
                } else {
                    newInfo.setAuthor("");
                }


                String[] date = page.getHtml().xpath("//p[@class='arti-metas']/span[3]/text()").toString().split("：");
                newInfo.setReleaseDate(date[1]);

                String view = page.getHtml().xpath("//p[@class='arti-metas']/span[4]/span/text()").toString();
                newInfo.setPageViews(view);

                String content = page.getHtml().xpath("//div[@class='wp_articlecontent']").toString();
                //剔除带class属性div
                String replace = content.replace("<div class=\"wp_articlecontent\">", "");
                String replace2 = replace.replace("</div>", "");
                newInfo.setContent(replace2);

                System.out.println("爬取：" + url);
                page.putField("newInfo", newInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Site site = Site.me()
            .setCharset("utf8")
            //超时时间
            .setTimeOut(10 * 1000)
            //重试间隔时间
            .setRetrySleepTime(3000)
            //重试次数
            .setRetryTimes(3);

    @Override
    public Site getSite() {
        return site;
    }

//    @Scheduled(initialDelay = 1000, fixedDelay = 100 * 1000)
    @Scheduled(cron = "0 53 14 * * ?")
    public void runCrawler() {
        if (redisTemplate.hasKey("news")) {
            redisTemplate.delete("news");
        }
        Spider.create(new NewsProcesser())
                .addUrl(URL)
                .thread(5)
                .addPipeline(redisPipeline)
                .run();
    }

//    public static void main(String[] args) {
//        new NewsProcesser().runCrawler();
//    }
}
