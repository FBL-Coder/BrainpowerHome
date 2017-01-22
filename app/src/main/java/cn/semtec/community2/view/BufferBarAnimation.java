package cn.semtec.community2.view;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class BufferBarAnimation {
	static Animation animation;

	public BufferBarAnimation() {
	}

	public static Animation getAnimation() {
		if (animation == null) {
			animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			animation.setInterpolator(new LinearInterpolator());
			// 设置动画执行时间
			animation.setDuration(2000);
			animation.setRepeatCount(Animation.INFINITE);
		}
		return animation;
	}
}
