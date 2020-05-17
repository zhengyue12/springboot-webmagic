package com.example.crawler.processer;


import com.example.crawler.domain.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * <h3>webmagic-scheduled</h3>
 * <p></p>
 *
 * @author : 你的名字
 * @date : 2020-05-14 11:03
 **/
@Component
public class RedisPipeline implements Pipeline {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void process(ResultItems resultItems, Task task) {
        News newInfo = resultItems.get("newInfo");
//        System.out.println("数据："+newInfo);
        if (newInfo != null) {
            redisTemplate.opsForSet().add("news", newInfo);
            System.out.println("保存成功");
        }

    }
}
