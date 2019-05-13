package net.lzzy.practicesonline.activities.activities.fragments;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.tv.TvContract;
import android.os.Build;
import android.os.Bundle;
import android.util.AndroidException;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.activities.models.FavoriteFactory;
import net.lzzy.practicesonline.activities.activities.models.Option;
import net.lzzy.practicesonline.activities.activities.models.Question;
import net.lzzy.practicesonline.activities.activities.models.QuestionFactory;
import net.lzzy.practicesonline.activities.activities.models.QuestionType;
import net.lzzy.practicesonline.activities.activities.models.UserCookies;


import java.util.List;

/**
 *
 * @author lzzy_gxy
 * @date 2019/4/30
 * Description:
 */
public class QuestionFragment extends BaseFragment {
    public static final String ARG_QUESTION = "argQuestion";
    public static final String ARG_POS = "argPos";
    public static final String ARG_IS_COMMITTED = "argIsCommitted";
    private Question question;
    private int pos;
    private boolean isCommitted;
    private TextView tvType;
    private boolean isMulti=false;
    private RadioGroup rgOptions;
    private ImageButton imgFavorite;
    private TextView tvContent;

    /** 静态工厂传参数据 **/
    public static QuestionFragment newInstance(String questionId,int pos,boolean isCommitted){
        QuestionFragment fragment=new QuestionFragment();
        Bundle args=new Bundle();
        args.putString(ARG_QUESTION,questionId);
        args.putInt(ARG_POS,pos);
        args.putBoolean(ARG_IS_COMMITTED,isCommitted);
        fragment.setArguments(args);
        return fragment;
    }

    /** 读取保存在缓存的数据 **/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            pos=getArguments().getInt(ARG_POS);
            isCommitted=getArguments().getBoolean(ARG_IS_COMMITTED);
            question= QuestionFactory.getInstance().getById(getArguments().getString(ARG_QUESTION));

        }
    }


    @Override
    protected void populate() {
        initViews();
        displayQuestion();
        generateOptions();

        int starId= FavoriteFactory.getInstance().isQuestionStarred(question.getId().toString())?
                android.R.drawable.star_on : android.R.drawable.star_off;
        imgFavorite.setImageResource(starId);

    }

    private void generateOptions() {
        List<Option> options =question.getOptions();
        for (Option option:options){
            CompoundButton btn=isMulti ? new CheckBox(getContext()):new RadioButton(getContext());
            String content=option.getLabel()+"."+option.getContent();
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                btn.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
            }
            btn.setText(content);
            btn.setEnabled(!isCommitted);
            //添加点击监听，选中了就要记录选项到文件sharedPreferences，取消选中则从文件中把该选项去掉
          btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  UserCookies.getInstance().changoptionState(option,isChecked,isMulti);
              }
          });


            rgOptions.addView(btn);
            //勾选，到文件中找是否在该选项的id，存在则勾选
            boolean shouldCheck=UserCookies.getInstance().isOptionSelceted(option);
            if (isMulti){
                btn.setChecked(shouldCheck);
            }else {
                rgOptions.check(btn.getId());
            }
            if (isCommitted && option.isAnswer()){
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    btn.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        }

    }
    private void displayQuestion() {
        isMulti = question.getType() == QuestionType.MULTI_CHOICE;
        int label=pos+1;
        String qType=label+"."+question.getType().toString();
        tvType.setText(qType);
        tvContent.setText(question.getContent());
        int starId= FavoriteFactory.getInstance().isQuestionStarred(question.getId().toString())?
                android.R.drawable.star_on : android.R.drawable.star_off;
        imgFavorite.setImageResource(starId);
        imgFavorite.setOnClickListener(v -> switchStar());



    }

    private void switchStar() {
        FavoriteFactory factory=FavoriteFactory.getInstance();
        if (factory.isQuestionStarred(question.getId().toString())){
            FavoriteFactory.getInstance().cancelStarQuestion(question.getId());
            imgFavorite.setImageResource(android.R.drawable.star_off);
        }else {
            FavoriteFactory.getInstance().starQuestion(question.getId());
            imgFavorite.setImageResource(android.R.drawable.star_on);
        }
    }

    private void initViews() {
        tvType = find(R.id.fragment_question_tv_type);
        imgFavorite = find(R.id.fragment_question_img_favorite);
        tvContent = find(R.id.fragment_question_tv_content);
        rgOptions=find(R.id.fragment_question_option_container);
        if (isCommitted){
            rgOptions.setOnClickListener(v ->
                    new AlertDialog.Builder(getContext())
                            .setMessage(question.getAnalysis())
            .show());

        }
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_question;
    }

    @Override
    public void search(String kw) {

    }


}
