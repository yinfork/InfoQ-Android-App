package com.yinfork.infoqapp.util;

import android.app.Activity;
import android.support.design.widget.TabLayout;

import com.yinfork.infoqapp.R;

public class ToolbarUtil {

    public static TabLayout getTab(Activity activity){

        TabLayout tabLayoutl = null;

        if(null != activity){
            tabLayoutl = (TabLayout) activity.findViewById(R.id.main_tablayout);
        }

        return tabLayoutl;
    }
}
