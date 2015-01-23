package com.wq.entity;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by bqw on 15/1/22.
 */
public class HtmlEntity {
    /**
     * 标题
     */
    private String title;
    /**
     * 关键字
     */
    private String key;
    /**
     * 发布时间
     */
    private String pubTime;
    /**
     * 链接
     */
    private String link;
    /**
     * 价格
     */
    private float price;

    /**
     * 是否浏览
     */
    private boolean isOpen;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isOpen() {
        float priceMax = Float.parseFloat(StringUtils.defaultString(System.getProperty("com.wq."+this.getKey()+".price.max"), "100000"));
        float priceMin = Float.parseFloat(StringUtils.defaultString(System.getProperty("com.wq."+this.getKey()+".price.min"),"0"));

        return isOpen && this.getPrice()<=priceMax && this.getPrice() >= priceMin;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "HtmlEntity{" +
                "title='" + title + '\'' +
                ", key='" + key + '\'' +
                ", pubTime='" + pubTime + '\'' +
                ", link='" + link + '\'' +
                ", price=" + price +
                ", isOpen=" + isOpen +
                '}';
    }
}
