package net.lzzy.practicesonline.activities.activities.models;

import net.lzzy.practicesonline.activities.activities.activities.SplashActivity;
import net.lzzy.practicesonline.activities.activities.constants.DbCounstants;
import net.lzzy.practicesonline.activities.activities.utils.AppUtils;
import net.lzzy.sqllib.SqlRepository;

import java.nio.channels.NonWritableChannelException;
import java.util.List;
import java.util.UUID;

/**
 * Created by lzzy_gxy on 2019/4/17.
 * Description:
 */
public class FavoriteFactory {
    private static final FavoriteFactory OUR_INSTANCE=new FavoriteFactory();
    private SqlRepository<Favorite> repoaitory;

    public static FavoriteFactory getInstance(){
        return OUR_INSTANCE;
    }
    private FavoriteFactory(){
        repoaitory= new SqlRepository<>(AppUtils.getContext(),Favorite.class, DbCounstants.packager);


    }
    private Favorite getByQuestion(String questionId){
        try {
            List<Favorite>favorites=repoaitory
                    .getByKeyword(questionId,new String[]{Favorite.COL_QUESTION_ID},true);
            if (favorites.size()>0){
                return favorites.get(0);
            }

        }catch (IllegalAccessException|InstantiationException e){
            e.printStackTrace();
        }
        return null;
    }
    String gerDeleteString(String questionId){
        Favorite favorite=getByQuestion(questionId);
        return favorite==null?null:repoaitory.getDeleteString(favorite);
    }
    public boolean isQuestionStarred(String questionId){
        try {
            List<Favorite>favorites=repoaitory.getByKeyword(questionId,
                    new String[]{Favorite.COL_QUESTION_ID},true);
            return favorites.size()>0;
        }catch (IllegalAccessException|InstantiationException e){
            e.printStackTrace();
            return false;
        }
    }
    public void starQuestion(UUID questionId){
        Favorite favorite=getByQuestion(questionId.toString());
        if (favorite==null){
            favorite=new Favorite();
            favorite.setQuestionId(questionId);
            repoaitory.insert(favorite);
        }
    }
    public void cancelStarQuestion(UUID questionId){
        Favorite favorite=getByQuestion(questionId.toString());
        if (favorite!=null){
            repoaitory.delete(favorite);
        }
    }
}
