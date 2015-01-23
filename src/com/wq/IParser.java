package com.wq;

import com.wq.entity.HtmlEntity;

import java.util.List;

/**
 * Created by bqw on 14-10-1.
 */
public interface IParser extends Runnable {
    public boolean isOpen(String itemPubTime);

    public List<HtmlEntity> parseHtml(String url) throws Exception;

    public void log(String log);
}
