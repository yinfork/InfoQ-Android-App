package com.yinfork.infoqapp.database;

import android.content.ContentValues;

public class NewsDbBean {
    public static final String TABLE = "news";

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String LINK = "link";
    public static final String CONTENT = "content";
    public static final String PTIME = "ptime";
    public static final String MODIFY_TIME = "modify";
    public static final String AUTHOR = "author";
    public static final String TYPE = "type";

    public static final String CREATE_TABLE = ""
            + "CREATE TABLE " + TABLE + "("
            + ID + " STRING NOT NULL PRIMARY KEY,"
            + LINK + " STRING NOT NULL,"
            + TITLE + " STRING NOT NULL,"
            + CONTENT + " TEXT,"
            + PTIME + " STRING,"
            + MODIFY_TIME + " STRING,"
            + AUTHOR + " STRING,"
            + TYPE + " STRING"
            + ")";


    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(String id) {
            values.put(ID, id);
            return this;
        }

        public Builder title(String title) {
            values.put(TITLE, title);
            return this;
        }

        public Builder link(String link) {
            values.put(LINK, link);
            return this;
        }

        public Builder content(String summary) {
            values.put(CONTENT, summary);
            return this;
        }

        public Builder ptime(String ptime) {
            values.put(PTIME, ptime);
            return this;
        }

        public Builder modify(String modify) {
            values.put(MODIFY_TIME, modify);
            return this;
        }

        public Builder author(String author) {
            values.put(AUTHOR, author);
            return this;
        }

        public Builder type(String type) {
            values.put(TYPE, type);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
