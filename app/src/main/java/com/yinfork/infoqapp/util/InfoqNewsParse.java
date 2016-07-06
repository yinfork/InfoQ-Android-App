package com.yinfork.infoqapp.util;

import android.util.Log;

import com.yinfork.infoqapp.beans.NewsBean;
import com.yinfork.infoqapp.beans.NewsDetailBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * InfoQ文档解析类
 */
public class InfoqNewsParse {
    private static InfoqNewsParse mInstance;
    private static final String BASE_URL = "http://www.infoq.com";

    public static InfoqNewsParse getmInstance() {
        if (mInstance == null) {
            synchronized (InfoqNewsParse.class) {
                if (mInstance == null) {
                    mInstance = new InfoqNewsParse();
                }
            }
        }
        return mInstance;
    }

    public List<NewsBean> getNewsList(String htmlUrl) {
        List<NewsBean> newsList = new ArrayList<NewsBean>();
        NewsBean news = null;
        //解析成文档
        Document document = Jsoup.parse(htmlUrl);
        //通过类名解析元素
        Elements elements = document.getElementsByClass("news_type_block");
        for (int i = 0; i < elements.size(); i++) {
            news = new NewsBean();
            Element element = elements.get(i);
            //h2
            Element h2 = element.getElementsByTag("h2").get(0);
            //h2_a
            Element h2_a = h2.child(0);
            //h1_a_title
            String title = h2_a.text();
            //h1_a_href
            String href = h2_a.attr("href");
            news.setTitle(title);
            news.setLink(BASE_URL + href);

            String text = null;
            try {
                //span
                Element span = element.getElementsByTag("span").get(0);
                text = span.text();

                // 去掉"日"后面的字符
                int lastIndex = text.lastIndexOf("日");
                if (lastIndex != -1) {

                    text = text.substring(0, lastIndex + 1);
                    text = text.replace("    ", " "); // 将三个空格替换成一个
                    text = text.replace("   ", " "); // 将三个空格替换成一个
                    text = text.replace("  ", " ");  // 将两个空格替换成一个
                    text = text.replace("，", " ");  //  将","替换成空格

                    String[] strArray = text.split(" ");
                    List<String> list = Arrays.asList(strArray);

                    String time = "";
                    String author = "";
                    int time_index = list.indexOf("发布于 ");
                    if (time_index != -1) {
                        time = list.get(time_index + 1);
                    }

                    int author_index = list.indexOf("作者");
                    if (author_index != -1) {
                        author = list.get(author_index + 1);
                        news.setAuthor(author);
                    }

                    try {
                        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                        Date date = format.parse(time);
                        format = new SimpleDateFormat("yyyy-MM-dd");
                        time = format.format(date);
                        news.setPtime(time);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                Log.d("yinjianhua", "Error: title: " + title + " text: " + text, e);
            }
            //p
            Element p = element.getElementsByTag("p").get(0);
            // content
            String content = p.text();
            news.setContent(content);
            newsList.add(news);
        }
        return newsList;
    }

    public List<NewsBean> getVideosList(String htmlUrl) {
        List<NewsBean> newsList = new ArrayList<NewsBean>();
        NewsBean news = null;

        try {
            //解析成文档
            Document document = Jsoup.parse(htmlUrl);
            //通过类名解析元素
            Elements elements = document.getElementsByClass("news_type_video");
            for (int i = 0; i < elements.size(); i++) {
                news = new NewsBean();
                Element element = elements.get(i);
                //h1
                Element h1 = element.getElementsByTag("h2").get(0);
                //h1_a
                Element h1_a = h1.child(0);
                //h1_a_title
                String title = h1_a.text();
                //h1_a_href
                String href = h1_a.attr("href");
                news.setTitle(title);
                news.setLink(BASE_URL + href);

                try {
                    Element icon = element.getElementsByTag("img").get(0);
                    news.setImgLink(icon.attr("src"));
                } catch (Exception e) {

                }

                Element author = element.getElementsByClass("author").get(0);
                news.setAuthor(author.text());

                //p
                Element p = element.getElementsByTag("p").get(0);
                // content
                String content = p.text();
                news.setContent(content);
                newsList.add(news);
            }
        } catch (Exception e) {

        }
        return newsList;
    }

    public NewsDetailBean getNewsDetial(String html) {
        html = html.replace("page page_news_item", "page_page_news_item");

        NewsDetailBean news = new NewsDetailBean();
        Document doc = Jsoup.parse(html);
        // 获得文章中的第一个detail
        Element detail = doc.select("div.page_page_news_item").first();
        //获取title
        Element title = detail.select("h1").first();
        news.setTitle(title.text());
        //infor
        Element infor = detail.select("p.heading_author").first();
        news.setInfor(infor.text());
        Elements elements = detail.select(".news_item_content p");
        StringBuffer buffer = new StringBuffer();
        for (Element element : elements) {
            element.select("img").attr("width", "100%").attr("style", "");
            buffer.append("<p>");
            buffer.append(element.html());
            buffer.append("</p>");
        }
        news.setTexts(buffer.toString());
        return news;
    }
}
