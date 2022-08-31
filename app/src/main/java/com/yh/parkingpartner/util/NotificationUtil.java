package com.yh.parkingpartner.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.yh.parkingpartner.R;
import com.yh.parkingpartner.config.Config;
import com.yh.parkingpartner.model.Data;
import com.yh.parkingpartner.ui.MainActivity;

import java.text.ParseException;
import java.util.Date;

public class NotificationUtil extends BroadcastReceiver {

    private String channelId = "com.yh.parkingpartner";
    private String channelName = "파킹파트너";

    String accessToken;
    String name;
    String email;
    String img_profile;
    // 주차완료정보 관련
    Data data=new Data();

    int diffMin;
    int unitDiv;
    int unitMod;

    @Override
    public void onReceive(Context context, Intent intent) {

        //todo : 주차완료 정보 확인하여 입차시간 기준 설정한 분 경과 단위로 알림을 출력한다.
        //Util.NOTIFICATION_USE_PRK_AT_INTERVAL = 입차시간 기준 경과 단위
        readSharedPreferences(context);
        if(data.getPrk_id()==0){
            Log.i("로그", "NotificationUtil.onReceive 주차완료 정보 없음");
            return;
        }

        try {
            Date startPrkAt=Util.getStringToDateTime(data.getStart_prk_at());
            diffMin = (int) ((System.currentTimeMillis() - startPrkAt.getTime()) / 60000);         //분 차이
            Log.i("로그", "NotificationUtil.onReceive 주차 경과 시간(분) : "+diffMin);
            unitDiv = diffMin / Util.NOTIFICATION_USE_PRK_AT;
            if(unitDiv > 0){
                unitMod=diffMin % Util.NOTIFICATION_USE_PRK_AT;
                if(unitMod > 0){
                    return;
                }
            }else{
                return;
            }
        } catch (ParseException e) {
            Log.i("로그", "NotificationUtil.onReceive 입차시간 확인 에러");
            e.printStackTrace();
            return;
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //오레오 대응
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), channelId);
        Intent notificationIntent = new Intent(context, MainActivity.class);  // 알림 클릭 시 이동할 액티비티 지정
        notificationIntent.putExtra("notification", true);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // FLAG_UPDATE_CURRENT : 이미 생성된 PendingIntent가 존재하면 해당 Intent의 Extra Data만 변경한다.
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext()
                , Util.NOTIFICATION_REQUEST_CODE
                , notificationIntent
                , PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle("주차경과알림") //제목
                .setContentText("주차 후 "+Util.myDecFormatter.format(diffMin)+"분이 경과되었습니다.") //내용
                .setDefaults(Notification.DEFAULT_ALL) //알림 설정(사운드, 진동)
                .setAutoCancel(true) //터치 시 자동으로 삭제할 지 여부
                .setPriority(NotificationCompat.PRIORITY_HIGH) // 알림의 중요도
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round))
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("주차장명 : "+data.getPrk_plce_nm())
                        .addLine("현재시간 : "+Util.getNowDateTime())
                        .addLine("입차시간 : "+data.getStart_prk_at())
                        .addLine("주차 후 "+Util.myDecFormatter.format(diffMin)+"분이 경과되었습니다.")
                        .addLine("확인하시려면 탭하세요."))
                .setContentIntent(pendingIntent);

        notificationManager.notify(Util.NOTIFICATION_ID, builder.build());

        Log.i("로그", "NotificationUtil.onReceive");
    }

    void readSharedPreferences(Context context){
        //SharedPreferences 를 이용해서, 앱 내의 저장소에 영구저장된 데이터를 읽어오는 방법
        SharedPreferences sp = context.getSharedPreferences(Config.SP_NAME, context.MODE_PRIVATE);
        accessToken = sp.getString(Config.SP_KEY_ACCESS_TOKEN, "");
        name = sp.getString(Config.SP_KEY_NAME, "");
        email = sp.getString(Config.SP_KEY_EMAIL, "");
        img_profile = sp.getString(Config.SP_KEY_IMG_PROFILE, "");

        // 주차완료정보 관련
        // prk_id-주차ID
        data.setPrk_id(sp.getInt(Config.SP_KEY_PRK_ID, 0));
        //prk_center_id-주차장ID
        data.setPrk_center_id(sp.getString(Config.SP_KEY_PRK_CENTER_ID, ""));
        Log.i("로그", "getPrk_center_id : "+data.getPrk_center_id());
        //prk_plce_nm-주차장명
        data.setPrk_plce_nm(sp.getString(Config.SP_KEY_PRK_PLCE_NM, ""));
        Log.i("로그", "getPrk_plce_nm : "+data.getPrk_plce_nm());
        //prk_plce_adres-주차장주소
        data.setPrk_plce_adres(sp.getString(Config.SP_KEY_PRK_PLCE_ADRES, ""));
        // start_prk_at-입차시간
        data.setStart_prk_at(sp.getString(Config.SP_KEY_START_PRK_AT, ""));
        // Img_prk-주차사진URL
        data.setImg_prk(sp.getString(Config.SP_KEY_IMG_PAK, ""));
        // prk_area-주차구역
        data.setPrk_area(sp.getString(Config.SP_KEY_PRK_AREA, ""));
        // parking_chrge_bs_time-기본시간
        data.setParking_chrge_bs_time(sp.getInt(Config.SP_KEY_PARKING_CHRGE_BS_TIME, 0));
        // parking_chrge_bs_chrg-기본요금
        data.setParking_chrge_bs_chrg(sp.getInt(Config.SP_KEY_PARKING_CHRGE_BS_CHRG, 0));
        // parking_chrge_adit_unit_time-추가단위시간
        data.setParking_chrge_adit_unit_time(sp.getInt(Config.SP_KEY_PARKING_CHRGE_ADIT_UNIT_TIME, 0));
        // parking_chrge_adit_unit_chrge-추가단위요금
        data.setParking_chrge_adit_unit_chrge(sp.getInt(Config.SP_KEY_PARKING_CHRGE_ADIT_UNIT_CHRGE, 0));
        // parking_chrge_one_day_chrge-1일요금
        data.setParking_chrge_one_day_chrge(sp.getInt(Config.SP_KEY_PARKING_CHRGE_ONE_DAY_CHRGE, 0));
    }
}
