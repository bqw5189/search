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
public class WBParser extends SuperParser {


    /**
     * 解析页面
     *
     * @param url 搜索条件
     */
    public List<HtmlEntity> parseHtml(String url) throws Exception {
        List<HtmlEntity> result = new ArrayList<HtmlEntity>();

        log("parseHtml:" + url);

        Document doc = request(url);

        Elements itemInfos = doc.select(".tbimg").get(1).select("tr");

        for (Element webElement : itemInfos) {
            if (webElement.select("td").size()<2){
                continue;
            }
            Element itemTitle = webElement.select("td").get(1);
            Elements itemTitleA = itemTitle.select("a");
            Elements itemPubTime = webElement.select(".fl");
            Elements price = webElement.select(".pri");

            HtmlEntity htmlEntity = new HtmlEntity();

            htmlEntity.setTitle(itemTitle.text());
            htmlEntity.setLink(itemTitleA.attr("href"));
            htmlEntity.setPubTime(itemPubTime.text());
            htmlEntity.setOpen(isOpen(itemPubTime.text()));
            htmlEntity.setPrice(Float.parseFloat(price.text()));

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

            try {
                int time = Integer.parseInt(itemPubTime.substring(itemPubTime.lastIndexOf(" "), end).trim());

                if (time < 30) {
                    result = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public String getID() {
        return "wb";
    }
}
