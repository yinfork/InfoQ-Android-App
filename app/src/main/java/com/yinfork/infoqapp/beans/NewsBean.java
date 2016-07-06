package com.yinfork.infoqapp.beans;

import java.io.Serializable;

/**
 * 新闻实体类
 */
public class NewsBean implements Serializable {
    private String id;
    /**
     * 图片的链接
     */
    private String imgLink;
    /**
     * 内容
     */
    private String content;
    /**
     * 标题
     */
    private String title;
    /**
     * 链接
     */
    private String link;
    /**
     * 发布日期
     */
    private String ptime;
    /**
     * 修改时间
     */
    private String modify;
    /**
     * 阅读时间
     */
    private String view_time;
    /**
     * 评论
     */
    private String num_recom;

    /**
     * 类型
     */
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // 作者
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getView_time() {
        return view_time;
    }

    public void setView_time(String view_time) {
        this.view_time = view_time;
    }

    public String getNum_recom() {
        return num_recom;
    }

    public void setNum_recom(String num_recom) {
        this.num_recom = num_recom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return ptime;
    }

    public void setDate(String date) {
        this.ptime = date;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getModify() {
        return modify;
    }

    public void setModify(String modify) {
        this.modify = modify;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof NewsBean) {
            String title = ((NewsBean) object).getTitle();
            String link = ((NewsBean) object).getLink();

            if (null != title && title.equals(getTitle())
                    && null != link && link.equals(getLink())) {
                return true;
            }
        }

        return false;
    }
}
