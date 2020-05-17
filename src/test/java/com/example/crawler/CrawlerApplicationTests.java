package com.example.crawler;

import com.example.crawler.domain.News;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class CrawlerApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void contextLoads() {

        Set<News> news = new HashSet<>();
        News news1 = new News();
        news1.setAuthor("1");
        news1.setContent("2");
        news1.setPageViews("3L");
        Date date = new Date();
        news1.setReleaseDate("date");
        news1.setSource("4");
        news1.setUrl("5");
        news1.setTitle("6");

        News news2 = new News();
        news2.setAuthor("2");
        news2.setContent("2");
        news2.setPageViews("3L");
        Date date2 = new Date();
        news2.setReleaseDate("date");
        news2.setSource("4");
        news2.setUrl("5");
        news2.setTitle("6");
//        news.add(news1);
//        news.add(news2);

        redisTemplate.opsForSet().add("news", news1);
        redisTemplate.opsForSet().add("news", news2);
        redisTemplate.expire("news", 60, TimeUnit.SECONDS);
    }

    @Test
    public void test() {
        Set news = redisTemplate.opsForSet().members("news");
        System.out.println(news);
    }

    @Test
    public void stringTest() {
        String a = new String("abcd");
        String replace = a.replace("a", "c");
        System.out.println(replace);
    }
}
