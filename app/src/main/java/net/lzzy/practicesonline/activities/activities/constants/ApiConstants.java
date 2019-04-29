package net.lzzy.practicesonline.activities.activities.constants;

import net.lzzy.practicesonline.activities.activities.utils.AppUtils;

import java.net.URL;

/**
 * Created by lzzy_gxy on 2019/4/15.
 * Description:
 */
public class ApiConstants {
    private static final String Ip= AppUtils.loadServerSetting(AppUtils.getContext()).first;
    private static final String PORT=AppUtils.loadServerSetting(AppUtils.getContext()).second;
    private static final String PROTOCOL="http://";

    public static final String URL_APT=PROTOCOL.concat(Ip).concat(":").concat(PORT);

    public static final String ACTION_PRACTICES="/api/practices";
    public static final String URL_PRACTICES= URL_APT.concat(ACTION_PRACTICES);

    public static final String ACTION_QUESTIONS="/api/pquestions?practiceid=";
    public static final String URL_QUESTIONS=URL_APT.concat(ACTION_QUESTIONS);


    public static final String JSON_PRACTICE_API_ID="Id";
    public static final String JSON_PRACTICE_NAME="Name";
    public static final String JSON_PRACTICE_OUTLINES="OutLines";
    public static final String JSON_PRACTICE_QUESTION_COUNT="QuestionCount";

    public static final String JSON_QUESTION_ANALYSIS="Analysis";
    public static final String JSON_QUESTION_CONTENT="Content";
    public static final String JSON_QUESTION_TYPE="QuestionType";
    public static final String JSON_QUESTION_OPTIONS="Options";
    public static final String JSON_QUESTION_ANSWERS="Answers";

    public static final String JSON_OPTION_CONTENT="Content";
    public static final String JSON_OPTION_LABEL="Label";
    public static final String JSON_OPTION_API_ID="Id";
    public static final String JSON_ANSWER_OPTION_ID="OptionId";
}
