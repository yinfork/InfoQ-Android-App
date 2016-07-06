package com.yinfork.infoqapp.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.yinfork.infoqapp.beans.NewsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库管理类
 */
public class DatabaseManager {
    private static final String TAG = "DatabaseManager";

    public void insertNews(NewsBean news) {
        SQLiteDatabase db = DbOpenHelper.getInstance().getWritableDatabase();
        long groceryListId = db.insert(NewsDbBean.TABLE, null, new NewsDbBean.Builder()
                .id(news.getId())
                .title(news.getTitle())
                .link(news.getLink())
                .content(news.getContent())
                .ptime(news.getPtime())
                .modify(news.getModify())
                .author(news.getAuthor())
                .type(news.getType())
                .build());
    }

    public List<NewsBean> queryNews(String newsType, int pageNum, int queryConut) {
        String sql =String.format( "select * from news where type=\"%s\" limit %d,%d",newsType,pageNum,queryConut);
        SQLiteDatabase db = DbOpenHelper.getInstance().getReadableDatabase();

        Cursor cursor = null;

        List<NewsBean> list = new ArrayList<>();

        try {
            cursor = db.rawQuery(sql, null);

            if (cursor != null && cursor.moveToFirst()) {

                int idIndex = cursor.getColumnIndex(NewsDbBean.ID);
                int titleIndex = cursor.getColumnIndex(NewsDbBean.TITLE);
                int linkIndex = cursor.getColumnIndex(NewsDbBean.LINK);
                int authorIndex = cursor.getColumnIndex(NewsDbBean.AUTHOR);
                int ptimeIndex = cursor.getColumnIndex(NewsDbBean.PTIME);
                int typeIndex = cursor.getColumnIndex(NewsDbBean.TYPE);
                int contentIndex = cursor.getColumnIndex(NewsDbBean.CONTENT);

                while (!cursor.isAfterLast()) {
                    String id = cursor.getString(idIndex);
                    String title = cursor.getString(titleIndex);
                    String link = cursor.getString(linkIndex);
                    String author = cursor.getString(authorIndex);
                    String ptime = cursor.getString(ptimeIndex);
                    String type = cursor.getString(typeIndex);
                    String content = cursor.getString(contentIndex);

                    if(!TextUtils.isEmpty(id) && !TextUtils.isEmpty(title) && !TextUtils.isEmpty(link)){
                        NewsBean bean = new NewsBean();
                        bean.setId(id);
                        bean.setTitle(title);
                        bean.setLink(link);
                        bean.setAuthor(author);
                        bean.setPtime(ptime);
                        bean.setType(type);
                        bean.setContent(content);

                        list.add(bean);
                    }
                    cursor.moveToNext();
                }
            }

        } catch (Exception e) {
            Log.d(TAG,"",e);
        } finally {
            if (null != cursor){
                cursor.close();
            }
        }

        return list;
    }


}
