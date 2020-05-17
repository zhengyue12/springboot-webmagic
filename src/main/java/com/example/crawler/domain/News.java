package com.example.crawler.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <h3>webmagic-scheduled</h3>
 * <p></p>
 *
 * @author : zhengyue
 * @date : 2020-05-14 07:35
 **/
@Data
public class News implements Serializable {
    /**
     * url地址
     */
    private String url;

    /**
     * 题目
     */
    private String title;

    /**
     * 来源
     */
    private String source;

    /**
     * 作者
     */
    private String author;

    /**
     * 发布日期
     */
    private String releaseDate;

    /**
     * 浏览量
     */
    private String pageViews;

    /**
     * 内容
     */
    private String content;
}
