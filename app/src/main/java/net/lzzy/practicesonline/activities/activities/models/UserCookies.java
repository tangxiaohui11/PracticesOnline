package net.lzzy.practicesonline.activities.activities.models;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import net.lzzy.practicesonline.activities.activities.utils.AppUtils;
import net.lzzy.practicesonline.activities.activities.utils.DateTimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lzzy_gxy on 2019/4/24.
 * Description:
 */
public class UserCookies {
    private static final String KEY_TIME="time";
    private SharedPreferences spTime;
    private static final UserCookies INSTANCE=new UserCookies();

    private UserCookies(){
        spTime= AppUtils.getContext()
                .getSharedPreferences("refresh_time", Context.MODE_PRIVATE);

    }
    public static UserCookies getInstance(){
        return INSTANCE;
    }
    public void updateLastRefreshTime(){
        String time= DateTimeUtils.DATE_TIME_FORMAT.format(new Date());
        spTime.edit().putString(KEY_TIME,time).apply();
    }
    public String getLastRefreshTime(){
        return spTime.getString(KEY_TIME,"");

    }


}
