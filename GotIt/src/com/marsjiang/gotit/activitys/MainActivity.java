package com.marsjiang.gotit.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.marsjiang.gotit.R;
import com.marsjiang.gotit.R.id;
import com.marsjiang.gotit.R.layout;
import com.marsjiang.gotit.constants.MyConstant;
import com.marsjiang.gotit.service.NotificationService;
import com.marsjiang.gotit.service.SharedUtils;
import com.marsjiang.gotit.util.ToastUtil;

public class MainActivity extends Activity {
	Button button;
	Button button_cancel;
	
	RadioButton radiobutton_00;
	RadioButton radiobutton_01;
	
	RadioGroup rg;
	Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		radiobutton_00 = (RadioButton) findViewById(R.id.radiobtn_00);
		radiobutton_01 = (RadioButton) findViewById(R.id.radiobtn_01);
		
		String flag = getIntent().getStringExtra(MyConstant.BOOTUP);

		System.out.println("flag" + flag);
		boolean flagIsForce = SharedUtils.getBoolean(getApplicationContext(), MyConstant.IS_FORCED,false);
		if(!flagIsForce){
			radiobutton_00.setChecked(true);
		}else{
			radiobutton_01.setChecked(true);
		}
		
		Intent service = new Intent(MainActivity.this,
				NotificationService.class);
		startService(service);

		if (!TextUtils.isEmpty(flag)) {
			if (flag.equals(MyConstant.START)) {
				ToastUtil.showText(MainActivity.this, "正在持续为您抢微信红包！");

				this.finish();
			}
		} else {
			initView();
		}

	}

	private void initView() {
		button = (Button) findViewById(R.id.button);
		button_cancel = (Button) findViewById(R.id.button_cancel);

		rg = (RadioGroup) findViewById(R.id.rg);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				System.out.println(checkedId);
				switch (checkedId) {
				case R.id.radiobtn_00:
					System.out.println("普通模式");
					ToastUtil.showText(getApplicationContext(), "已经切换到普通模式啦！");
					SharedUtils.putBoolean(getApplicationContext(),
							MyConstant.IS_FORCED, false);
					break;
				case R.id.radiobtn_01:
					ToastUtil.showText(getApplicationContext(), "已经切换到强力模式啦！");
					SharedUtils.putBoolean(getApplicationContext(),
							MyConstant.IS_FORCED, true);
					break;

				default:
					break;
				}
			}
		});
		// 取消按钮
		button_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(intent);
				ToastUtil.showText(getApplicationContext(), "请点击我就要抢!并且关闭");
				SharedUtils.putBoolean(getApplicationContext(),
						MyConstant.IS_GET, false);
				Intent service = new Intent(MainActivity.this,
						NotificationService.class);
				stopService(service);

			}
		});

		// 开启按钮
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SharedUtils.putBoolean(getApplicationContext(),
						MyConstant.IS_GET, true);
				Intent service = new Intent(MainActivity.this,
						NotificationService.class);
				startService(service);
				if (!isAccessibilitySettingsOn(getApplicationContext())) {

					startActivity(intent);
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							ToastUtil.showText(getApplicationContext(),
									"服务已经开启！不需要重复点击啦！");
						}
					});

				}

			}
		});
	}

	// 判断服务是否运行
	private boolean isAccessibilitySettingsOn(Context mContext) {
		int accessibilityEnabled = 0;
		final String service = "com.marsjiang.gotit";
		boolean accessibilityFound = false;
		try {
			accessibilityEnabled = Settings.Secure.getInt(mContext
					.getApplicationContext().getContentResolver(),
					android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
			// Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
			System.out.println("服务打开了吗");
		} catch (SettingNotFoundException e) {
			/*
			 * Log.e(TAG,
			 * "Error finding setting, default accessibility to not found: " +
			 * e.getMessage());
			 */
			System.out.println("没有找到这个设置");
		}
		TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

		if (accessibilityEnabled == 1) {
			System.out.println("服务已经打开");
			accessibilityFound = true;
		} else {
			// Log.v(TAG, "***ACCESSIBILIY IS DISABLED***");
			System.out.println("服务没有打开");
		}

		return accessibilityFound;
	}

}
