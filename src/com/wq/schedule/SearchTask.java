package com.wq.schedule;


import com.wq.SuperParser;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * 任务扫描 根据 任务类型 调用相应处理脚本
 * 逆变器数据采集
 * Created by baiqw on 14-11-12.
 */
@Component
// 默认将类中的所有public函数纳入事务管理.
//@Transactional(readOnly = true)
public class SearchTask {
    private static Logger logger = LoggerFactory.getLogger(SearchTask.class);
    protected static String[] KEYS;

    protected static String[] SOURCES = null;


    static {
        SOURCES = System.getProperty("com.wq.source").split(",");

        try {
            KEYS = new String(System.getProperty("com.wq.keys").getBytes("ISO-8859-1"),"utf8").split(",");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void execute(String parserName) throws Exception {

        Class parserClass = Class.forName(parserName);

        SuperParser parser = (SuperParser) parserClass.getConstructor().newInstance();

        if (ArrayUtils.indexOf(SOURCES, parser.getID()) < 0) {
            return;
        }

        for (String key : KEYS) {
            parser.setKey(key);

            logger.info("{} task begin ...", parser.getID());

            parser.run();

            logger.info("{} task end .", parser.getID());
        }
    }


}
