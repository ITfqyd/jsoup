package com.cxyxs.jsoup.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URL;

/**
 * Description：测试
 * Author: 程序猿学社
 * Date:  2020/5/10 18:07
 * Modified By:
 */
public class JsoupUtils {
    public static void main(String[] args) throws  Exception {
        getList("https://blog.csdn.net/qq_16855077/category_9722768.html");
    }

    /**
     * 通过csdn的系列文章网址输出对应的内容
     * @param url
     * @throws Exception
     */
    public static void getList(String url)throws  Exception{
        //网址
        Document document = Jsoup.connect(url).get();

        // 获取标题
        //String title = document.title();
        //获取头部信息
        //Element head = document.head();
        //取到菜单列表ul
        Elements elements = document.getElementsByClass("column_article_list");

        //取出每个li
        Elements elementsByTag = elements.select("li");
        for (Element el : elementsByTag) {
            //取出每个li元素的href
            String href = el.getElementsByTag("a").attr("href");
            //先取出h2标签
            String titleDiv = el.getElementsByTag("h2").text();
            String[] titles = titleDiv.split(" ");
            //![描述](网址)
            System.out.println("!["+titles[1]+"]("+href+")");
        }
    }
}
