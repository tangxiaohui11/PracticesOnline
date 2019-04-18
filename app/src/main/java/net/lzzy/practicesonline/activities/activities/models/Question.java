package net.lzzy.practicesonline.activities.activities.models;

import net.lzzy.sqllib.Ignored;
import net.lzzy.sqllib.Sqlitable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lzzy_gxy on 2019/4/16.
 * Description:
 */
public class Question extends BaseEntity implements Sqlitable {
    private String content;
    @Ignored
    private QuestionType type;
    private int dbType;
    private String analysis;
    private UUID practiceld;
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
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public UUID getPracticeld() {
        return practiceld;
    }

    public void setPracticeld(UUID practiceld) {
        this.practiceld = practiceld;
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
}
