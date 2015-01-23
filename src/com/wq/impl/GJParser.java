package com.wq.impl;

import com.wq.SuperParser;
import com.wq.entity.HtmlEntity;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bqw on 14-10-1.
 */
public class GJParser extends SuperParser {
    /**
     * 解析页面
     *
     * @param url 搜索条件
     */
    public List<HtmlEntity> parseHtml(String url) throws Exception {
        List<HtmlEntity> result = new ArrayList<HtmlEntity>();

        log("parseHtml:" + url);

        Document doc = request(url);

        Elements itemInfos = doc.select(".j-every");

        for (Element webElement : itemInfos) {
            Elements itemTitle = webElement.select(".f-introd");
            Elements itemTitleA = webElement.select("a");
            Elements itemPubTime = webElement.select(".j-time");


            HtmlEntity htmlEntity = new HtmlEntity();

            htmlEntity.setTitle(itemTitle.text());
            htmlEntity.setLink("http://nj.ganji.com" + itemTitleA.attr("href"));
            htmlEntity.setPubTime(itemPubTime.text());
            htmlEntity.setOpen(isOpen(itemPubTime.text()));

            try{
                doc = request(htmlEntity.getLink());
                Elements price = doc.select(".det-infor .f22");
                htmlEntity.setPrice(Float.parseFloat(price.text()));
            }catch (SocketTimeoutException e){

            }catch (HttpStatusException e){

            }
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

            try {
                int time = Integer.parseInt(itemPubTime.substring(0, end).trim());

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
        return "gj";
    }
}
