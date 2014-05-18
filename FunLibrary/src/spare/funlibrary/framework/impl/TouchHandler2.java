package spare.funlibrary.framework.impl;

import java.util.List;

import spare.funlibrary.framework.Input2.GestureEvent;
import spare.funlibrary.framework.Input2.TouchEvent;
import android.view.View.OnTouchListener;

public interface TouchHandler2 extends OnTouchListener{
	public boolean isTouchDown(int pointer);
	
	public int getTouchX(int pointer);
	
	public int getTouchY(int pointer);
	
	public List<TouchEvent> getTouchEvents();
	
	public List<GestureEvent> getGestureEvents();
}
