package com.wq;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by bqw on 14-2-20.
 */
public class TestHtml {
    /**
     * 搜索关键字
     */

    private static List<String> SEARCH_KEYS = new ArrayList<String>();
    /**
     * 已经打开连接
     */
    private static Set<String> OPEND_URL = new TreeSet<String>();

    private static Map<String, RunThread> THREAD_RUN_STATUS = new HashMap<String, RunThread>();

    private static SynchronousQueue<Runnable> QUEUE = new SynchronousQueue<Runnable>();

    private static long LOOP_TIMES = 500l;

    static {
        SEARCH_KEYS.add("TR100");
        SEARCH_KEYS.add("TR150");
        SEARCH_KEYS.add("TR200");
        SEARCH_KEYS.add("TR350");
    }

    public static void main(String[] args) {

        ThreadPoolExecutor executorService = new ThreadPoolExecutor(0, 2 * SEARCH_KEYS.size(),
                2L, TimeUnit.SECONDS,
                QUEUE);
        executorService.setKeepAliveTime(2, TimeUnit.SECONDS);


        for (String search : SEARCH_KEYS) {
            RunThread thread = new RunThread("http://s.2.taobao.com/list/list.htm?st_edtime=1&q=" + search + "&ist=0");
            THREAD_RUN_STATUS.put(search, thread);
            thread.setName(search);
        }


        while (true) {
            for (Map.Entry<String, RunThread> runThreadEntry : THREAD_RUN_STATUS.entrySet()) {
                try {
                    Thread.sleep(LOOP_TIMES);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (QUEUE.size() > SEARCH_KEYS.size()) {
                    QUEUE.clear();
                }

                try {
                    executorService.execute(runThreadEntry.getValue());
                } catch (Exception e) {
                    executorService.shutdownNow();
                    QUEUE.clear();
                    executorService.setKeepAliveTime(2, TimeUnit.SECONDS);
                    executorService = new ThreadPoolExecutor(0, 2 * SEARCH_KEYS.size(),
                            5L, TimeUnit.SECONDS,
                            QUEUE);

                }
                System.out.print(" || " + runThreadEntry.getKey() + " run time is " + runThreadEntry.getValue().getRunTime() + " last run time is :" + runThreadEntry.getValue().getLastRunDate().toLocaleString());
            }

            System.out.println(" curent time is:" + new Date().toLocaleString() + "  getActiveCount:" + ((ThreadPoolExecutor) executorService).getActiveCount());
        }


    }

    public static class RunThread extends Thread {
        private HtmlUnitDriver driver = new HtmlUnitDriver();
        private Date lastRunDate = new Date();
        private int runTime = 0;

        private String url;

        public Date getLastRunDate() {
            return lastRunDate;
        }

        public void setLastRunDate(Date lastRunDate) {
            this.lastRunDate = lastRunDate;
        }

        public RunThread(String url) {
            this.url = url;
        }

        public int getRunTime() {
            return runTime;
        }

        public void setRunTime(int runTime) {
            this.runTime = runTime;
        }

        @Override
        public void run() {
            try {
                parseHtml(url);
            } catch (Exception e) {
                System.out.print(" == " + this.getName() + " parse html error! == ");

                driver.quit();
                driver = null;
                driver = new HtmlUnitDriver();
            }
            lastRunDate = new Date();
            runTime++;
        }

        /**
         * 解析页面
         *
         * @param url 搜索条件
         */
        private void parseHtml(String url) {
            driver.get(url);

            List<WebElement> itemInfos = driver.findElements(By.className("item-info"));

            for (WebElement webElement : itemInfos) {
                WebElement itemTitle = webElement.findElement(By.className("item-title"));
                WebElement itemTitleA = itemTitle.findElement(By.tagName("a"));
                WebElement itemPubTime = webElement.findElement(By.className("item-pub-time"));

                final String href = itemTitleA.getAttribute("href");

                System.out.println("itemTitle = " + itemTitle.getText() + " itemPubTime is: " + itemPubTime.getText());

                if (isOpen(itemPubTime.getText()) && !OPEND_URL.contains(href)) {

                    System.out.println("open itemTitle = " + itemTitle.getText() + " itemTitleA = " + itemTitleA.getAttribute("href"));

                    openUrl(href);

                    OPEND_URL.add(href);
                } else {
                    break;
                }
            }
        }
    }


    /**
     * 判断是否打开该链接
     *
     * @param itemPubTime 发布时间
     * @return
     */
    private static boolean isOpen(String itemPubTime) {
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

    /**
     * 打开连接
     *
     * @param url
     */
    private static void openUrl(String url) {
        MusicPlay myMusicPlay = new MusicPlay(MusicPlay.class.getClassLoader().getResource("dog.wav"));

        Runtime rt = Runtime.getRuntime();

        try {
            rt.exec("sogouexplorer.exe " + url);
            rt.exec("sogouexplorer.exe " + url);
        } catch (IOException e) {
//                e.printStackTrace();
        }
        myMusicPlay.stop();
        myMusicPlay.start();//播放一次

        System.out.println("freeMemory =" + rt.freeMemory() + " totalMemory=" + rt.totalMemory() + " maxMemory=" + rt.maxMemory());

        rt.gc();
        rt.freeMemory();


    }
}
