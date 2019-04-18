package net.lzzy.practicesonline.activities.activities.models;

import net.lzzy.sqllib.Ignored;
import net.lzzy.sqllib.Sqlitable;

import java.util.UUID;

/**
 * Created by lzzy_gxy on 2019/4/16.
 * Description:
 */
public class Favorite extends BaseEntity implements Sqlitable {
    private UUID questionId;
   @Ignored
   public static final String COL_QUESTION_ID="questionid";
   public UUID getQuestionId(){
       return questionId;
   }
   public void setQuestionId(UUID questionId){
       this.questionId=questionId;
   }

    @Override
    public boolean needUpdate() {
        return false;
    }
}
