package net.lzzy.practicesonline.activities.activities.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import net.lzzy.practicesonline.activities.activities.utils.AppUtils;

/**
 *
 * @author lzzy_gxy
 * @date 2019/4/11
 * Description:
 */
@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity {
    public BaseActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutRes());
        /** 调用添加方法**/
        AppUtils.addActivity(this);
        //region  托管Fragment
        FragmentManager manager=getSupportFragmentManager();
        Fragment fragment=manager.findFragmentById(getContainerId());
        if (fragment==null){
            fragment=createFragment();
            manager.beginTransaction().add(getContainerId(),fragment).commit();
        }
        //endregion
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /** 调用移除方法**/
        AppUtils.removeActivity(this);
    }


    /** 在运行时赋值 **/
    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.setRunning(getLocalClassName());
    }

    /** 在停止的时候清空 **/
    @Override
    protected void onStop() {
        super.onStop();
        AppUtils.setStopped(getLocalClassName());

    }

    /**
     * Activity的布局文件id
     * @return 布局资源id
     */
    protected abstract int getLayoutRes();

    /** Fragment容器id
     *
     * @return 容器id
     */
    protected abstract int getContainerId();

    /**
     * 生成托管Fragment对象
     * @return fragment
     */
    protected abstract Fragment createFragment();
}
