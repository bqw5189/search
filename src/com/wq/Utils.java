package com.wq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by bqw on 14-10-1.
 */
public class Utils {
    public static final Logger logger = LoggerFactory.getLogger(Utils.class);

    /**
     * 打开连接
     *
     * @param url
     */
    public static void openUrl(String url) {
        MusicPlay myMusicPlay = new MusicPlay(MusicPlay.class.getClassLoader().getResource("dog.wav"));

        logger.info("open url:{}", url);

        Runtime rt = Runtime.getRuntime();

//        try {
//            rt.exec("sogouexplorer.exe " + url);
//            rt.exec("sogouexplorer.exe " + url);
//        } catch (IOException e) {
//                //e.printStackTrace();
//        }
        myMusicPlay.stop();
        myMusicPlay.start();//播放一次

        logger.debug("freeMemory ={}, totalMemory={},  maxMemory={}", rt.freeMemory(), rt.totalMemory(), rt.maxMemory());

        rt.gc();
        rt.freeMemory();
    }
}
