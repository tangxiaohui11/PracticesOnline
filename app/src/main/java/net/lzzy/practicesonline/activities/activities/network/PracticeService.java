package net.lzzy.practicesonline.activities.activities.network;

import net.lzzy.practicesonline.activities.activities.constants.ApiConstants;
import net.lzzy.practicesonline.activities.activities.models.Practice;
import net.lzzy.practicesonline.activities.activities.models.PracticeResult;
import net.lzzy.practicesonline.activities.activities.utils.AppUtils;
import net.lzzy.sqllib.JsonConverter;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/4/22.
 * Description:
 */
public class PracticeService {
    public static String getPracticeFromSever()throws IOException{
        return ApiService.okGet(ApiConstants.URL_PRACTICES);
    }
    public static List<Practice> getPractices(String json) throws IllegalAccessException, JSONException, InstantiationException {
        JsonConverter<Practice> converter = new JsonConverter<>(Practice.class);
        return converter.getArray(json);
    }
    public static int posResult(PracticeResult result) throws JSONException,IOException{
        return ApiService.okPost(ApiConstants.URL_RESULT,result.toJson());
    }
}
