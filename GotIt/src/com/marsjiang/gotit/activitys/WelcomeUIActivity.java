package com.marsjiang.gotit.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.marsjiang.gotit.R;
import com.marsjiang.gotit.R.id;
import com.marsjiang.gotit.R.layout;
import com.marsjiang.gotit.constants.MyConstant;
import com.marsjiang.gotit.service.SharedUtils;

public class WelcomeUIActivity extends Activity {

	private RelativeLayout rl_welcome_bg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome_ui);
		
		init();
	}

	private void init() {
		rl_welcome_bg = (RelativeLayout) findViewById(R.id.hongbao_welcome);
		
		// 旋转动画，0 ~ 360
		RotateAnimation rotateAnimation = new RotateAnimation(
				0, 360, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(1000);
		rotateAnimation.setFillAfter(true);
		
		// 缩放动画，从无到有
		ScaleAnimation scaleAnimation = new ScaleAnimation(
				0, 1, 
				0, 1, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(1000);
		scaleAnimation.setFillAfter(true);
		
		// 渐变动画，从无到有
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);
		alphaAnimation.setFillAfter(true);
		
		// 创建动画集合
		AnimationSet animationSet = new AnimationSet(false);
		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(rotateAnimation);
		animationSet.addAnimation(scaleAnimation);
		
		rl_welcome_bg.startAnimation(animationSet);
		
		// 监听动画
		animationSet.setAnimationListener(new MyAnimationListener());
	}
	
	class MyAnimationListener implements AnimationListener{

		@Override
		public void onAnimationStart(Animation animation) {
			
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			
			
			// 当动画执行后跳转，根据保存的是否应用第一次打开boolean ，判断进入什么界面
			boolean isAppFirstOpen = SharedUtils.getBoolean(getApplicationContext(), MyConstant.IS_APP_FIRST_OPEN, true);
			
			if(isAppFirstOpen){// 应用第一次打开，进入引导界面
				System.out.println("应用第一次打开，进入引导界面");
				startActivity(new Intent(WelcomeUIActivity.this,GuideUI.class));
			}else{// 应用不是第一次打开，进入主界面
				System.out.println("应用不是第一次打开，进入主界面");
				startActivity(new Intent(WelcomeUIActivity.this,MainActivity.class));
			}
			finish();
			
		
			/*startActivity(new Intent(WelcomeUIActivity.this,MainActivity.class));
			finish();*/
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
		
	}
}
