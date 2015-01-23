package com.wq.impl;

import com.wq.SuperParser;
import com.wq.entity.HtmlEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bqw on 14-10-1.
 */
public class TBParser extends SuperParser {

    /**
     * 解析页面
     *
     * @param url 搜索条件
     */
    public List<HtmlEntity> parseHtml(String url) throws Exception {
        List<HtmlEntity> result = new ArrayList<HtmlEntity>();

        Document doc = request(url);

        Elements itemInfos = doc.select(".item-info");

        for (Element webElement : itemInfos) {
            HtmlEntity htmlEntity = new HtmlEntity();

            Elements itemTitle = webElement.select(".item-title");
            Elements itemTitleA = itemTitle.select("a");
            Elements itemPubTime = webElement.select(".item-pub-time");
            Elements price = webElement.select(".price em");

            htmlEntity.setTitle(itemTitle.text());
            htmlEntity.setLink(itemTitleA.attr("href"));
            htmlEntity.setPubTime(itemPubTime.text());
            htmlEntity.setOpen(isOpen(itemPubTime.text()));
            htmlEntity.setPrice(Float.parseFloat(price.text()));

//            log(htmlEntity.toString());

            result.add(htmlEntity);
        }
        return result;
    }

    /**
     * 判断是否打开该链接
     *
     * @param itemPubTime 发布时间
     * @return
     */
    public boolean isOpen(String itemPubTime) {
        boolean result = false;

        int end = itemPubTime.indexOf("分钟");

        if (itemPubTime.indexOf("分钟") > -1) {

            int time = Integer.parseInt(itemPubTime.substring(0, end));

            if (time < 30) {
                result = true;
            }
        }

        return result;
    }


    @Override
    public String getID() {
        return "tb";
    }
}
