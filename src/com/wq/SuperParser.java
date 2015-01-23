package com.wq;

import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.wq.entity.HtmlEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by bqw on 14-10-1.
 */
public abstract class SuperParser implements IParser {
    protected static Set<String> OPEND_URL = new TreeSet<String>();
    public static int TIME_OUT = 2000;

    private String key;

    protected String sourceUrl;

    protected String[] sources;


    protected SuperParser() {
        this.sourceUrl = System.getProperty("com.wq.source." + getID() + ".url");
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public abstract String getID();

    public Document request(String url) throws IOException {
        return Jsoup.connect(url).timeout(TIME_OUT).get();
    }

    @Override
    public void run() {
        try {
            List<HtmlEntity> htmlEntityList = parseHtml(sourceUrl.replaceAll("\\$\\{key}", key));

            for (HtmlEntity entity : htmlEntityList) {
                entity.setKey(key);
//                log(entity.toString());

                if (entity.isOpen() && !OPEND_URL.contains(entity.getLink())) {

                    log("open " + entity);

                    Utils.openUrl(entity.getLink());

                    OPEND_URL.add(entity.getLink());
                } else {
                    break;
                }
            }
        } catch (SocketTimeoutException e) {

        } catch (HttpStatusException e) {

        } catch (Exception e) {
//                e.printStackTrace();
            log(e.getMessage());
        }

    }

    public void log(String log) {
        System.out.println(this.getClass().getName() + ":" + new Date().toLocaleString() + ":" + log);
    }

}
