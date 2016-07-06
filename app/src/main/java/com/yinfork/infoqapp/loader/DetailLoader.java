package com.yinfork.infoqapp.loader;

import android.os.Environment;

import com.android.volley.VolleyError;
import com.yinfork.infoqapp.beans.NewsDetailBean;
import com.yinfork.infoqapp.util.FileUtil;
import com.yinfork.infoqapp.util.InfoqNewsParse;
import com.yinfork.infoqapp.util.NetWorkUtil;
import com.yinfork.infoqapp.view.HtmlFrame;
import com.yinfork.infoqapp.volley.VolleyClient;
import com.yinfork.infoqapp.volley.VolleyResponse;

import java.io.File;

/**
 * Created by yinjianhua on 16/6/29.
 */
public class DetailLoader {
    public static final String TAG = "DetailLoader";


    public void load(final String url, final String id, final LoaderListener listener) {
        if (url == null) {
            return;
        }

        if (NetWorkUtil.isConnected() && !isHasCatch(id)) {
            VolleyClient.RequestGet(url, TAG, new VolleyResponse() {
                @Override
                public void onSuccess(String result) {
                    NewsDetailBean newsBean = InfoqNewsParse.getmInstance().getNewsDetial(result);
                    if (newsBean == null) {
                        return;
                    }
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(formatHtml(HtmlFrame.FRAME, newsBean.getTitle(),
                            newsBean.getInfor(), newsBean.getTexts()));

                    if (null != listener) {
                        listener.onSuccess(stringBuffer.toString());
                    }

                    saveData(id, newsBean.getTexts());
                }

                @Override
                public void onError(VolleyError error) {
                    if (null != listener) {
                        listener.onError();
                    }
                }
            });
        }else{
            String content = getLocalData(id);

            if(null != listener) {
                if (null != content && content.length() > 0) {
                    listener.onSuccess(content);
                }else{
                    listener.onError();
                }
            }

        }
    }

    /**
     * 格式化html
     *
     * @param frame 框架
     * @param title 标题
     * @param infor 作者时间
     * @param texts 内容
     * @return
     */
    private String formatHtml(String frame, String title, String infor, String texts) {
        return String.format(frame, title, infor, texts);
    }

    private boolean isHasCatch(String id){
        String fileName =  Environment.getExternalStorageDirectory()
                .getAbsolutePath()+ File.separator + FileUtil.PATH+
                File.separator + id;


        return FileUtil.isExistsFile(fileName);
    }

    private String getLocalData(String id){
        String fileName =  Environment.getExternalStorageDirectory()
                .getAbsolutePath()+ File.separator + FileUtil.PATH+
                File.separator + id;


        return FileUtil.readSDFile(fileName);
    }

    private void saveData(String id, String content) {
        if (null == id || id.length() <= 0) return;

        FileUtil.writeSDFile(id, content);
    }

    public interface LoaderListener {
        public void onSuccess(String data);

        public void onError();
    }

}
