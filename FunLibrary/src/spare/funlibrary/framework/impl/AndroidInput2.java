package spare.funlibrary.framework.impl;

import java.util.List;

import spare.funlibrary.framework.Input2;
import android.content.Context;
import android.view.View;

public class AndroidInput2 implements Input2{
	TouchHandler2 touchHandler;
	
	public AndroidInput2(Context context, View view, float scaleX, float scaleY) {
//		if(Integer.parseInt(VERSION.SDK)<5)
//			touchHandler=new SingleTouchHandler(view, scaleX,scaleY);
//		else
//			touchHandler=new SingleTouchHandler(view, scaleX,scaleY);
		
		touchHandler=new SingleTouchHandler2(view, scaleX,scaleY);
	}
	
	@Override
	public boolean isKeyPressed(int keyCode) {
		return false;
	}

	@Override
	public boolean isTouchDown(int pointer) {
		return touchHandler.isTouchDown(pointer);
	}

	@Override
	public int getTouchX(int pointer) {
		return touchHandler.getTouchX(pointer);
	}

	@Override
	public int getTouchY(int pointer) {
		return touchHandler.getTouchY(pointer);
	}

	@Override
	public float getAccelX() {
		return 0;
	}

	@Override
	public float getAccelY() {
		return 0;
	}

	@Override
	public float getAccelZ() {
		return 0;
	}

	@Override
	public List<KeyEvent> getKeyEvents() {
		return null;
	}

	@Override
	public List<TouchEvent> getTouchEvents() {
		return touchHandler.getTouchEvents();
	}

	@Override
	public List<GestureEvent> getGestureEvents() {
		return touchHandler.getGestureEvents();
	}
}
