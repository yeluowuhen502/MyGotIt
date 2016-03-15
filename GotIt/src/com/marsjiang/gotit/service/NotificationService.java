package com.marsjiang.gotit.service;

import java.util.MissingFormatArgumentException;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.marsjiang.gotit.R;
import com.marsjiang.gotit.activitys.MainActivity;

public class NotificationService extends Service {
	
	private int mID = 10001;
	@Override
	public IBinder onBind(Intent intent) {
	
		return null;
	}

	@Override
	public void onCreate() {
		System.out.println("通知服务开启");
		Notification notification = new Notification();

		notification.icon = R.drawable.ic_launcher;
		notification.contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
		
		Intent intent = new Intent(NotificationService.this, MainActivity.class);
    	PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, 10, intent, 0);
    	notification.setLatestEventInfo(NotificationService.this, "正在持续为您抢红包", "正在持续为您抢红包", pendingIntent);
		startForeground(mID , notification);
		}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		//super.onDestroy();
		stopForeground(true);
	}
}
