package com.yinfork.infoqapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.helper.VolleyImageHelper;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.yinfork.infoqapp.beans.NewsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinjianhua on 16/5/5.
 */
public class NewsAdapter extends BaseRefreshAdapter<NewsBean> {
    private static final int ITEM_NORMAL = 100;
    private static final int ITEM_FOOT = 101;

    private LayoutInflater inflater;
    private final ImageLoader mImageLoader;

    public interface DataBindHolder {
        int getTag();
    }

    public NewsAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mImageLoader = new ImageLoader(MyApplication.getmRequestQueue(), new VolleyImageHelper());
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case ITEM_NORMAL:
                return getNormalView(position, convertView, parent);
            case ITEM_FOOT:
                return getFootView(position, convertView, parent);
            default:
                return null;
        }
    }

    private View getNormalView(int position, View convertView, ViewGroup parent) {
        int tag = getHolderTag(convertView);
        ItemViewHolder viewHolder;

        if (null == inflater) return null;

        if (null == convertView || tag != ITEM_NORMAL) {
            convertView = inflater.inflate(R.layout.item_news, parent, false);
            viewHolder = new ItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ItemViewHolder) convertView.getTag();
        }

        if (null != viewHolder && null != mDatas && mDatas.size() > position) {
            viewHolder.tv_title.setText(mDatas.get(position).getTitle());
            viewHolder.tv_content.setText(mDatas.get(position).getContent());
            viewHolder.tv_num_recom.setText(mDatas.get(position).getNum_recom());

            if (mDatas.get(position).getImgLink() != null) {
                viewHolder.nv_head.setVisibility(View.VISIBLE);
                viewHolder.nv_head.setBackgroundColor(Color.GRAY);
                viewHolder.nv_head.setImageUrl(mDatas.get(position).getImgLink(), mImageLoader);
            }else{
                viewHolder.nv_head.setVisibility(View.GONE);
                viewHolder.nv_head.setBackgroundColor(Color.GRAY);
            }
        }

        return convertView;
    }

    private View getFootView(int position, View convertView, ViewGroup parent) {
        int tag = getHolderTag(convertView);
        FootHolder viewHolder;

        if (null == inflater) return null;

        if (null == convertView || tag != ITEM_FOOT) {
            convertView = inflater.inflate(R.layout.item_news_foot, parent, false);
            viewHolder = new FootHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FootHolder) convertView.getTag();
        }

        return convertView;
    }

    private int getHolderTag(View convertView) {
        int tag = -1;

        if (null != convertView) {
            Object holder = convertView.getTag();

            if (holder instanceof DataBindHolder) {
                tag = ((DataBindHolder) holder).getTag();
            }
        }

        return tag;
    }


    @Override
    public int getItemViewType(int position) {
//        if (position < getCount() - 1) {
//            return ITEM_NORMAL;
//        } else {
//            return ITEM_FOOT;
//        }

        return ITEM_NORMAL;
    }


    public List<NewsBean> getCopyDatas(){

        if(mDatas == null || mDatas.size() == 0){
            return null;
        }

        return new ArrayList<>(mDatas);

    }

    public List<NewsBean> getDatas(){
        return mDatas;
    }


    class ItemViewHolder implements DataBindHolder {
        TextView tv_title;
        TextView tv_content;
        TextView tv_num_recom;
        NetworkImageView nv_head;

        public ItemViewHolder(View itemView) {
            tv_title = (TextView) itemView.findViewById(R.id.item_news_title);
            tv_content = (TextView) itemView.findViewById(R.id.item_news_content);
            tv_num_recom = (TextView) itemView.findViewById(R.id.item_news_num_recom);
            nv_head = (NetworkImageView) itemView.findViewById(R.id.item_news_head);
        }

        @Override
        public int getTag() {
            return ITEM_NORMAL;
        }
    }

    class FootHolder implements DataBindHolder {
        LinearLayout foot;
        ProgressWheel progressBar;
        TextView message;

        public FootHolder(View itemView) {
            foot = (LinearLayout) itemView.findViewById(R.id.item_news_foot);
            progressBar = (ProgressWheel) itemView.findViewById(R.id.item_news_progressbar);
            message = (TextView) itemView.findViewById(R.id.item_news_message);
        }

        @Override
        public int getTag() {
            return ITEM_FOOT;
        }
    }
}
