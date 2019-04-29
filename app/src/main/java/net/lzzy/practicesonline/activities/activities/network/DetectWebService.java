package net.lzzy.practicesonline.activities.activities.network;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.AndroidException;

import androidx.annotation.Nullable;

import net.lzzy.practicesonline.activities.activities.activities.PracticesActivity;
import net.lzzy.practicesonline.activities.activities.models.Practice;
import net.lzzy.practicesonline.activities.activities.utils.AppUtils;

import java.util.List;

/**
 * Created by lzzy_gxy on 2019/4/28.
 * Description:
 */
public class DetectWebService extends Service {
    public static final String EXTRA_REFRESH = "extraRefresh";
    private int locaCount;
    private NotificationManager manager;
    public   static final int NOTIFICATION_DETECT_ID=1;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        locaCount=intent.getIntExtra(PracticesActivity.EXTRA_LOCAL_COUNT,0);
        return new DetectWebBinder();
    }
    public class DetectWebBinder extends Binder{

         static final int FLAG_SERVER_EXCEPTION=0;
         static  final int FLAG_DATA_CHANGED=1;
         static  final int FLAG_DATA_SAME=2;


        public void detect(){
            AppUtils.getExecutor().execute(()-> {


               int flag=comparData();
               if (flag==FLAG_SERVER_EXCEPTION){
                   notifyUser("服务器无法连接",android.R.drawable.ic_menu_compass,false);
               }else if (flag==FLAG_DATA_CHANGED) {
                   notifyUser("远程服务器更新",android.R.drawable.ic_popup_sync,true);
               }else {
                   if (manager!=null){
                       manager.cancel(NOTIFICATION_DETECT_ID);
                   }
               }
            });
        }

        private void notifyUser(String info, int icon, boolean refresh) {
            Intent intent=new Intent(DetectWebService.this,PracticesActivity.class);
            intent.putExtra(EXTRA_REFRESH,refresh);
            PendingIntent pendingIntent=PendingIntent.getActivity(DetectWebService.this,
                    0,intent,PendingIntent.FLAG_ONE_SHOT);

            manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notification= new Notification.Builder(DetectWebService.this,"0")
                            .setContentTitle("检测远程服务器")
                            .setContentText(info)
                            .setSmallIcon(icon)
                            .setContentIntent(pendingIntent)
                            .setWhen(System.currentTimeMillis())
                            .build();

            }else {
                notification=new Notification.Builder(DetectWebService.this)
                        .setContentTitle("检测远程服务器")
                        .setContentText(info)
                        .setSmallIcon(icon)
                        .setContentIntent(pendingIntent)
                        .setWhen(System.currentTimeMillis())
                        .build();
            }
            if (manager!=null){
                manager.notify(NOTIFICATION_DETECT_ID,notification);
            }
        }

        private int comparData(){
            try {
                List<Practice>remote=
                        PracticeService.getPractices(PracticeService.getPracticeFromSever());
                if (remote.size()!=locaCount){
                    return FLAG_DATA_CHANGED;

                }else {
                    return FLAG_DATA_SAME;
                }
            }catch (Exception e){
                return FLAG_SERVER_EXCEPTION;
            }
        }

    }


}
