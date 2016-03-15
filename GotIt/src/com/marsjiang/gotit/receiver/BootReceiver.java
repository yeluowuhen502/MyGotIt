package com.marsjiang.gotit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.marsjiang.gotit.activitys.MainActivity;
import com.marsjiang.gotit.constants.MyConstant;
import com.marsjiang.gotit.service.SharedUtils;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean isget = SharedUtils.getBoolean(context, MyConstant.IS_GET, false);
		if(isget){
			System.out.println("您已经开机了！");
			Intent it = new Intent(context, MainActivity.class);
			
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.putExtra(MyConstant.BOOTUP, MyConstant.START);
			
			context.startActivity(it);
			
		}else{
			
		}
		
	}

}
