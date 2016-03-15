package com.marsjiang.gotit.service;

import java.util.ArrayList;
import java.util.List;

import com.marsjiang.gotit.constants.MyConstant;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class GotService extends AccessibilityService {

	private static final CharSequence ENVELOPE_TEXT_KEY = "[微信红包]";
	private AccessibilityNodeInfo mRootNodeInfo = null;
	private ArrayList<String> list = new ArrayList<String>();

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		boolean isForce = SharedUtils.getBoolean(getApplicationContext(),
				MyConstant.IS_FORCED, false);
		int eventType = event.getEventType();
	//System.out.println(event.getSource());
		// 通知栏事件
		if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
			System.out.println("通知事件");
			List<CharSequence> texts = event.getText();
			if (!texts.isEmpty()) {
				for (CharSequence t : texts) {
					String text = String.valueOf(t);
					if (text.contains(ENVELOPE_TEXT_KEY)) {
						// 将微信的通知栏消息打开
						System.out.println("微信消息");
						Notification notification = (Notification) event
								.getParcelableData();

						PendingIntent pendingIntent = notification.contentIntent;
						try {
							pendingIntent.send();
							SharedUtils.putBoolean(getApplicationContext(),
									MyConstant.IS_GO_TO_QIANG, true);
						} catch (PendingIntent.CanceledException e) {
							e.printStackTrace();
						}
						break;
					}
				}
			}
		}

		boolean flag = SharedUtils.getBoolean(getApplicationContext(),
				MyConstant.IS_GO_TO_QIANG, false);
		// 如果当前是有窗口事件的，则进入修改
		// System.out.println("flag"+flag);
		if (flag || isForce) {
			mRootNodeInfo = event.getSource();
			
			// System.out.println("变化了");
			if (mRootNodeInfo == null) {
				return;
			}

			if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
				// System.out.println("窗口变化");
				// System.out.println(mRootNodeInfo);
				/*
				 * String temp = mRootNodeInfo.toString(); String regex =
				 * "[A-Za-z]"; String temp1 = temp.replaceAll(regex, "");
				 * System.out.println(temp1); list.add(temp1); boolean flag =
				 * true; for(String string:list){ if(string.contains("您领取了")){
				 * System.out.println("找到了"); flag = false; } }
				 */
				// System.out.println(mRootNodeInfo);
				// System.out.println(flag);
				List<AccessibilityNodeInfo> hongbaoList = mRootNodeInfo
						.findAccessibilityNodeInfosByText("领取红包");
			
				if (hongbaoList.size() > 0) {
					System.out.println("找到红包窗口");
					AccessibilityNodeInfo curNodeInfo = hongbaoList
							.get(hongbaoList.size() - 1);
					SharedUtils.putBoolean(getApplicationContext(),MyConstant.IS_GO_TO_QIANG, false);
					// System.out.println();
					//System.out.println(curNodeInfo.getParent());
					curNodeInfo.getParent().performAction(
							AccessibilityNodeInfo.ACTION_CLICK);
					
				}

			}

			if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
			//	System.out.println(event.getWindowId());
				mRootNodeInfo = event.getSource();
				
				
				List<AccessibilityNodeInfo> clickWindowList = mRootNodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/b43");
				System.out.println("数目"+clickWindowList.size());

				if (clickWindowList.size() > 0) {
					System.out.println("打开红包");
					AccessibilityNodeInfo curNodeInfo2 = clickWindowList
							.get(clickWindowList.size() - 1);
					curNodeInfo2.performAction(AccessibilityNodeInfo.ACTION_CLICK);
					
					
					new Thread() {
						public void run() {
							SystemClock.sleep(1000);
							// 退出到桌面
							Intent intent = new Intent(Intent.ACTION_MAIN);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.addCategory(Intent.CATEGORY_HOME);
							startActivity(intent);
						};
					}.start();

				}

			}

			/*if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
				List<AccessibilityNodeInfo> clickWindowList = mRootNodeInfo
						.findAccessibilityNodeInfosByText("已领取");

				if (clickWindowList.size() > 0) {
					// System.out.println("打开红包") ;
					System.out.println("lingqule");
					AccessibilityNodeInfo curNodeInfo2 = clickWindowList
							.get(clickWindowList.size() - 1);
					// curNodeInfo2.performAction(AccessibilityNodeInfo.ACTION_CLICK);
					curNodeInfo2
							.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
					new Thread() {
						public void run() {
							SystemClock.sleep(200);
						};
					}.start();
					// 退出到桌面
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.addCategory(Intent.CATEGORY_HOME);
					startActivity(intent);
				}

			}*/
		}

	}

	@Override
	public void onInterrupt() {

	}

}
