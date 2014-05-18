package spare.funlibrary.framework;

import spare.funlibrary.framework.math.Rectangle;
import spare.funlibrary.framework.math.Vector2;

public class Callout{
	public boolean firstTimeCreated;
	public float alphaChnl;
	
	public final Vector2 position;
	public final Rectangle bounds;
	public final Vector2 arrowPosition;
	public final Vector2 arrowPointPosition;
	public final Vector2 arrowOffset;
	public final Vector2 arrowPointOffset;
	public float width;
	public float height;
	public float cornerRadius;
	public float arrowWidth;
	public float arrowDist;
	public float arrowAngle;
	public float r,g,b;
	public int state;
	public float stateTime;
	
	public static final int STATE_FADE_IN=0;
	public static final int STATE_FADE_OUT=1;
	public static final int STATE_SHOWN=2;
	public static final int STATE_HIDDEN=3;
	
	public float fadeSpeed=1;
	
	public Callout(float x, float y, float width, float height, float cornerRadius, 
			float arrowPointX, float arrowPointY, float arrowWidth, float r, float g, float b){
		this.position=new Vector2(x, y);
		this.bounds=new Rectangle(x-width/2, y-height/2, width, height);
		this.width=width;
		this.height=height;
		this.cornerRadius=cornerRadius;
		float arrowX=(x+arrowPointX)/2;
		float arrowY=(y+arrowPointY)/2;
		this.arrowPosition=new Vector2(arrowX, arrowY);
		this.arrowPointPosition=new Vector2(arrowPointX, arrowPointY);
		this.arrowOffset=new Vector2(arrowPosition).sub(position);
		this.arrowPointOffset=new Vector2(arrowPointPosition).sub(position);
		this.arrowWidth=arrowWidth;
		this.arrowDist=(float)Math.sqrt((arrowPointX-x)*(arrowPointX-x)+(arrowPointY-y)*(arrowPointY-y));
		this.arrowAngle=(float)Math.atan2(arrowY-y, arrowX-x)*Vector2.TO_DEGREES;
		//看清param！先y后x！
		this.arrowAngle-=90;
		//竖着的三角形变平躺
		this.r=r;
		this.g=g;
		this.b=b;
		firstTimeCreated=true;
		alphaChnl=0;
	}
	
	public void update(float deltaTime){
		stateTime+=deltaTime;
		arrowPosition.set(position.x+arrowOffset.x, position.y+arrowOffset.y);
		arrowPointPosition.set(position.x+arrowPointOffset.x, position.y+arrowPointOffset.y);
		switch (state) {
		case STATE_SHOWN:
			alphaChnl=1;
			break;
		case STATE_HIDDEN:
			alphaChnl=0;
			break;
		case STATE_FADE_IN:
			if(alphaChnl<1){
				alphaChnl+=deltaTime*fadeSpeed;
			}else{
				alphaChnl=1;
				state=STATE_SHOWN;
				stateTime=0;
			}
			break;
		case STATE_FADE_OUT:
			if(alphaChnl>0){
				alphaChnl-=deltaTime*fadeSpeed;
			}else{
				alphaChnl=0;
				state=STATE_HIDDEN;
				stateTime=0;
			}
			break;
		default:
			break;
		}
	}
	
	public void fadeIn(float fadeSpeed){
		this.fadeSpeed=fadeSpeed;
		state=STATE_FADE_IN;
		stateTime=0;
	}
	
	public void fadeOut(float fadeSpeed){
		this.fadeSpeed=fadeSpeed;
		state=STATE_FADE_OUT;
		stateTime=0;
	}
}
