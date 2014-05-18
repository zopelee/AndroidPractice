package spare.funlibrary.framework.impl;

import java.util.ArrayList;
import java.util.List;

import spare.funlibrary.framework.Pool;
import spare.funlibrary.framework.Input2.GestureEvent;
import spare.funlibrary.framework.Input2.TouchEvent;
import spare.funlibrary.framework.Pool.PoolObjectFactory;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class SingleTouchHandler2 implements TouchHandler2, OnGestureListener, OnDoubleTapListener {
	boolean isTouched;
	int touchX;
	int touchY;
	Pool<TouchEvent> touchEventPool;
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	int gestureX1;
	int gestureY1;
	int gestureX2;
	int gestureY2;
	Pool<GestureEvent> gestureEventPool;
	List<GestureEvent> gestureEvents=new ArrayList<GestureEvent>();
	List<GestureEvent> gestureEventsBuffer=new ArrayList<GestureEvent>();
	float scaleX;
	float scaleY;
	private GestureDetectorCompat gestureDetectorCompat;
	
	public SingleTouchHandler2(View view, float scaleX, float scaleY) {
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		PoolObjectFactory<GestureEvent> gestureFactory=new PoolObjectFactory<GestureEvent>() {
			@Override
			public GestureEvent createObject() {
				return new GestureEvent();
			}
		};
		gestureEventPool=new Pool<GestureEvent>(gestureFactory, 100);
		view.setOnTouchListener(this);
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		gestureDetectorCompat=new GestureDetectorCompat(view.getContext(), this);
		gestureDetectorCompat.setOnDoubleTapListener(this);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		gestureDetectorCompat.onTouchEvent(event);
		synchronized(this) {
			TouchEvent touchEvent = touchEventPool.newObject();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchEvent.type = TouchEvent.TOUCH_DOWN;
				isTouched=true;
				break;
			case MotionEvent.ACTION_MOVE:
				touchEvent.type = TouchEvent.TOUCH_DRAGGED;
				isTouched=true;
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				touchEvent.type = TouchEvent.TOUCH_UP;
				isTouched=false;
				break;
			}
			touchEvent.x=touchX=(int)(event.getX()*scaleX);
			touchEvent.y=touchY=(int)(event.getY()*scaleY);
			touchEventsBuffer.add(touchEvent);
			return true;
		}
	}

	@Override
	public boolean isTouchDown(int pointer) {
		synchronized(this) {
			if(pointer==0)
				return isTouched;
			else
				return false;
		}
	}

	@Override
	public int getTouchX(int pointer) {
		synchronized(this) {
			return touchX;
		}
	}

	@Override
	public int getTouchY(int pointer) {
		synchronized(this) {
			return touchY;
		}
	}

	@Override
	public List<TouchEvent> getTouchEvents() {
		synchronized(this){
			int len=touchEvents.size();
			for(int i=0;i<len;i++)
				touchEventPool.free(touchEvents.get(i));
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}
	
	@Override
	public List<GestureEvent> getGestureEvents() {
		synchronized (this) {
			int len=gestureEvents.size();
			for(int i=0; i<len; i++){
				gestureEventPool.free(gestureEvents.get(i));
			}
			gestureEvents.clear();
			gestureEvents.addAll(gestureEventsBuffer);
			gestureEventsBuffer.clear();
			return gestureEvents;
		}
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		Log.d("aaa", "double");
		synchronized(this) {
			GestureEvent gestureEvent = gestureEventPool.newObject();
			gestureEvent.type=GestureEvent.DOUBLE_TAP;
			gestureEvent.x1=gestureEvent.x2=gestureX1=gestureX2=(int)(e.getX()*scaleX);
			gestureEvent.y1=gestureEvent.y2=gestureY1=gestureY2=(int)(e.getY()*scaleY);
			gestureEvent.velocityX=gestureEvent.velocityY=0;
			gestureEventsBuffer.add(gestureEvent);
			return true;
		}
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		Log.d("aaa", "doubleEvent");
		synchronized(this) {
			GestureEvent gestureEvent = gestureEventPool.newObject();
			gestureEvent.type=GestureEvent.DOUBLE_EVENT;
			gestureEvent.x1=gestureEvent.x2=gestureX1=gestureX2=(int)(e.getX()*scaleX);
			gestureEvent.y1=gestureEvent.y2=gestureY1=gestureY2=(int)(e.getY()*scaleY);
			gestureEvent.velocityX=gestureEvent.velocityY=0;
			gestureEventsBuffer.add(gestureEvent);
			return true;
		}
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		Log.d("aaa", "doubleConfirmed");
		synchronized(this) {
			GestureEvent gestureEvent = gestureEventPool.newObject();
			gestureEvent.type=GestureEvent.SINGLE_CONFIRMED;
			gestureEvent.x1=gestureEvent.x2=gestureX1=gestureX2=(int)(e.getX()*scaleX);
			gestureEvent.y1=gestureEvent.y2=gestureY1=gestureY2=(int)(e.getY()*scaleY);
			gestureEvent.velocityX=gestureEvent.velocityY=0;
			gestureEventsBuffer.add(gestureEvent);
			return true;
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		Log.d("aaa", "down");
		synchronized(this) {
			GestureEvent gestureEvent = gestureEventPool.newObject();
			gestureEvent.type=GestureEvent.DOWN;
			gestureEvent.x1=gestureEvent.x2=gestureX1=gestureX2=(int)(e.getX()*scaleX);
			gestureEvent.y1=gestureEvent.y2=gestureY1=gestureY2=(int)(e.getY()*scaleY);
			gestureEvent.velocityX=gestureEvent.velocityY=0;
			gestureEventsBuffer.add(gestureEvent);
			return true;
		}
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.d("aaa", "fling");
		synchronized(this) {
			GestureEvent gestureEvent = gestureEventPool.newObject();
			gestureEvent.type=GestureEvent.FLING;
			gestureEvent.x1=gestureX1=(int)(e1.getX()*scaleX);
			gestureEvent.y1=gestureY1=(int)(e1.getY()*scaleY);
			gestureEvent.x2=gestureX2=(int)(e2.getX()*scaleX);
			gestureEvent.y2=gestureY2=(int)(e2.getY()*scaleY);
			gestureEvent.velocityX=velocityX;
			gestureEvent.velocityY=velocityY;
			gestureEventsBuffer.add(gestureEvent);
			return true;
		}
	}

	@Override
	public void onLongPress(MotionEvent e) {
		Log.d("aaa", "long");
		synchronized(this) {
			GestureEvent gestureEvent = gestureEventPool.newObject();
			gestureEvent.type=GestureEvent.LONG_PRESS;
			gestureEvent.x1=gestureEvent.x2=gestureX1=gestureX2=(int)(e.getX()*scaleX);
			gestureEvent.y1=gestureEvent.y2=gestureY1=gestureY2=(int)(e.getY()*scaleY);
			gestureEvent.velocityX=gestureEvent.velocityY=0;
			gestureEventsBuffer.add(gestureEvent);
		}
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		Log.d("aaa", "scroll");
		synchronized(this) {
			GestureEvent gestureEvent = gestureEventPool.newObject();
			gestureEvent.type=GestureEvent.SCROLL;
			gestureEvent.x1=gestureX1=(int)(e1.getX()*scaleX);
			gestureEvent.y1=gestureY1=(int)(e1.getY()*scaleY);
			gestureEvent.x2=gestureX2=(int)(e2.getX()*scaleX);
			gestureEvent.y2=gestureY2=(int)(e2.getY()*scaleY);
			gestureEvent.velocityX=gestureEvent.velocityY=0;
			gestureEventsBuffer.add(gestureEvent);
			return true;
		}
	}

	@Override
	public void onShowPress(MotionEvent e) {
		Log.d("aaa", "showPress");
		synchronized(this) {
			GestureEvent gestureEvent = gestureEventPool.newObject();
			gestureEvent.type=GestureEvent.SHOW_PRESS;
			gestureEvent.x1=gestureEvent.x2=gestureX1=gestureX2=(int)(e.getX()*scaleX);
			gestureEvent.y1=gestureEvent.y2=gestureY1=gestureY2=(int)(e.getY()*scaleY);
			gestureEvent.velocityX=gestureEvent.velocityY=0;
			gestureEventsBuffer.add(gestureEvent);
		}
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Log.d("aaa", "singleUp");
		synchronized(this) {
			GestureEvent gestureEvent = gestureEventPool.newObject();
			gestureEvent.type=GestureEvent.SINGLE_UP;
			gestureEvent.x1=gestureEvent.x2=gestureX1=gestureX2=(int)(e.getX()*scaleX);
			gestureEvent.y1=gestureEvent.y2=gestureY1=gestureY2=(int)(e.getY()*scaleY);
			gestureEvent.velocityX=gestureEvent.velocityY=0;
			gestureEventsBuffer.add(gestureEvent);
			return true;
		}
	}
}
