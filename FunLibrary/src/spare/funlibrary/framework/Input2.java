package spare.funlibrary.framework;

import java.util.List;

public interface Input2 {
	public static class KeyEvent {
		public static final int KEY_DOWN = 0;
		public static final int KEY_UP = 1;
		
		public int type;
		public int keyCode;
		public int keyChar;
		
		public String toString() {
			StringBuilder builder = new StringBuilder();
			if (type==KEY_DOWN)
				builder.append("key down, ");
			else
				builder.append("key up, ");
			builder.append(keyCode);
			builder.append(", ");
			builder.append(keyChar);
			return builder.toString();
		}
	}

	public static class TouchEvent {
		public static final int TOUCH_DOWN = 0;
		public static final int TOUCH_UP = 1;
		public static final int TOUCH_DRAGGED = 2;
		
		public int type;
		public int x, y;
		public int pointer;
		
		public String toString() {
			StringBuilder builder = new StringBuilder();
			if (type==TOUCH_DOWN)
				builder.append("touch down, ");
			else if (type==TOUCH_DRAGGED)
				builder.append("touch dragged, ");
			else
				builder.append("touch up, ");
			
			builder.append(pointer);
			builder.append(", ");
			builder.append(x);
			builder.append(", ");
			builder.append(y);
			return builder.toString();
		}
	}
	
	public static class GestureEvent {
		public static final int DOUBLE_TAP= 0;
		public static final int DOUBLE_EVENT= 1;
		public static final int SINGLE_CONFIRMED= 2;
		public static final int DOWN=3;
		public static final int FLING=4;
		public static final int LONG_PRESS=5;
		public static final int SCROLL=6;
		public static final int SHOW_PRESS=7;
		public static final int SINGLE_UP=8;
		
		public int type;
		public int x1, y1, x2, y2;
		public float velocityX, velocityY;
		public int pointer;
	}
	
	public boolean isKeyPressed(int keyCode);
	
	public boolean isTouchDown(int pointer);
	
	public int getTouchX(int pointer);

	public int getTouchY(int pointer);
	
	public float getAccelX();
	
	public float getAccelY();
	
	public float getAccelZ();
	
	public List<KeyEvent> getKeyEvents();
	
	public List<TouchEvent> getTouchEvents();
	
	public List<GestureEvent> getGestureEvents();
}