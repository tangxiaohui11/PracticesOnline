package net.lzzy.practicesonline.activities.activities.models;

import net.lzzy.practicesonline.activities.activities.constants.ApiConstants;
import net.lzzy.practicesonline.activities.activities.network.QuestionService;
import net.lzzy.sqllib.Ignored;
import net.lzzy.sqllib.Jsonable;
import net.lzzy.sqllib.Sqlitable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lzzy_gxy on 2019/4/16.
 * Description:
 */
public class Question extends BaseEntity implements Sqlitable, Jsonable {
    private String content;
    @Ignored
    private QuestionType type;
    private int dbType;
    private String analysis;
    private UUID practiceId;
    @Ignored
    private List<Option> options;


    public Question(){
        options=new ArrayList<>();

    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public int getDbType() {
        return dbType;
    }

    public void setDbType(int dbType) {
        this.dbType = dbType;
        type=QuestionType.getInstance(dbType);
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public UUID getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(UUID practiceId) {
        this.practiceId = practiceId;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options.clear();
        this.options = options;
    }

    @Override
    public boolean needUpdate() {
        return false;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        return null;
    }

    @Override
    public void fromJson(JSONObject json) throws JSONException {
        analysis=json.getString(ApiConstants.JSON_QUESTION_ANALYSIS);
        content=json.getString(ApiConstants.JSON_QUESTION_CONTENT);
        setDbType(json.getInt(ApiConstants.JSON_QUESTION_TYPE));
        String strOptions=json.getString(ApiConstants.JSON_QUESTION_OPTIONS);
        String strAnswer=json.getString(ApiConstants.JSON_QUESTION_ANSWERS);
        try {
            List<Option>options= QuestionService.getOptionFromJson(strOptions,strAnswer);
            for (Option option:options){
                option.setQuestionId(id);
            }
            setOptions(options);
        }catch (IllegalAccessException|InstantiationException e){
            e.printStackTrace();
        }


    }
}
