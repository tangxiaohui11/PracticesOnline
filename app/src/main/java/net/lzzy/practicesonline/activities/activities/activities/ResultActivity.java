package net.lzzy.practicesonline.activities.activities.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.activities.fragments.ChartFragment;
import net.lzzy.practicesonline.activities.activities.fragments.GridFragment;
import net.lzzy.practicesonline.activities.activities.models.QuestionResult;

import java.util.List;
/**
 * Created by lzzy_gxy on 2019/5/13.
 * Description:
 */
public class ResultActivity extends BaseActivity implements GridFragment.OnGridSkipListener,ChartFragment.OnChartSkipListener
{
    private List<QuestionResult> results;
    public static final String POSITION = "position";
    public static final int RESULT_CODE = 1;
    private String practiceId;
    public static final String PRACTICES_ID = "practicesId";
    public static final int RESULT_CODE_PRACTICE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        practiceId = getIntent().getStringExtra(QuestionActivity.EXTRA_PRACTICE_ID);
    }



    @Override
    protected int getLayoutRes() {
        return R.layout.activity_result;
    }

    @Override
    protected int getContainerId() {
        return R.id.activity_result_container;
    }
    @Override
    protected Fragment createFragment() {
        results = getIntent().getParcelableArrayListExtra(QuestionActivity.EXTRA_RESULT);
        return GridFragment.newInstance(results);
    }
    @Override
    public void onGridSkip(int position) {
        Intent intent=new Intent(this,QuestionActivity.class);
        intent.putExtra(POSITION,position);
        setResult(RESULT_CODE,intent);
        finish();
    }
    
    @Override
    public void gotoChart() {
        getManager().beginTransaction().replace(R.id.activity_result_container,ChartFragment.newInstance(results)).commit();
    }


    @Override
    public void gotoGrid() {
        getManager().beginTransaction().replace(R.id.activity_result_container,GridFragment.newInstance(results)).commit();
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("You want to go back there?")
                .setNeutralButton("返回题目",(dialog, which) -> {

                    finish();
                })
                .setNegativeButton("章节列表",(dialog, which) -> {
                    startActivity(new Intent(this,PracticesActivity.class));
                    finish();
                })
                .setPositiveButton("查看收藏", (dialog, which) -> {
                    Intent intent=new Intent(this,QuestionActivity.class);
                    intent.putExtra(PRACTICES_ID,practiceId);
                    setResult(RESULT_CODE_PRACTICE,intent);
                    finish();
                })
                .show();
    }


}
