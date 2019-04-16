package net.lzzy.practicesonline.activities.activities.fragments;

import android.content.Context;
import android.view.View;

import net.lzzy.practicesonline.R;

import java.util.Calendar;

/**
 *
 * @author lzzy_gxy
 * @date 2019/4/10
 * Description:
 */
public class SplashFragment extends BaseFragment {

    private int[] imgs=new int[]{R.drawable.splash1,
            R.drawable.splash2,R.drawable.splash3};
    private OnSplashFinishedListener listener;


    @Override
    protected void populate() {
        /** 根据现在的秒数求余来显示那张背景图片 **/
        View wall=find(R.id.fragment_splash_wall);
        int pos= Calendar.getInstance().get(Calendar.SECOND) % 3;
        wall.setBackgroundResource(imgs[pos]);
        /** 点击跳转新界面 **/
        wall.setOnClickListener(v -> listener.cancelCount());
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_splash;
    }

    @Override
    public void search(String kw) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSplashFinishedListener){
            listener= (OnSplashFinishedListener) context;
        }else {
            throw new ClassCastException(context.toString()+"必须实现OnSplashFinishedListener接口");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    public interface OnSplashFinishedListener{
        /**
         *  点击跳过，跳转新界面
         * */
        void cancelCount();
    }
}
