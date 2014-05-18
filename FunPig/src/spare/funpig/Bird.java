package spare.funpig;

import spare.funlibrary.framework.GameObject2;
import spare.funlibrary.framework.math.Vector2;

public class Bird extends GameObject2{
	public static final int WIDTH=1;
	public static final int HEIGHT=1;
	
	public static final int STATE_READY=0;
	public static final int STATE_FLY=1;
	public static final int STATE_CATCHED=2;
	
	public int state;
	public float stateTime;
	public float speed=7;
	public float zoomY=1;
	public float aChnl=1;
	public Vector2 positionCopy;
	
	public Bird(float x, float y, float width, float height) {
		super(x, y, width, height);
		position.set(-2, 4);
		positionCopy=new Vector2(position);
		state=STATE_READY;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		stateTime+=deltaTime;
		bounds.lowerLeft.set(position).sub(bounds.width/2, bounds.height/2);
		
		switch (state) {
		case STATE_READY:
			position.set(-2, 1+4);
			if(stateTime>3){
				fly();
			}
			break;
		case STATE_FLY:
			position.add(speed*deltaTime, 0);
			positionCopy.set(position);
			if(stateTime>2){
				state=STATE_READY;
				stateTime=0;
			}
			break;
		case STATE_CATCHED:
			position.set(positionCopy.x, positionCopy.y+0.7f+0.1f*(float)Math.sin(50*stateTime));
			zoomY=0.7f;
			if(stateTime>1){
				state=STATE_FLY;
				zoomY=1;
				aChnl=1;
				stateTime=0;
			}
			break;
		default:
			break;
		}
	}
	
	public void fly(){
		state=STATE_FLY;
		stateTime=0;
	}
	
	public void catched(){
		state=STATE_CATCHED;
		stateTime=0;
	}
}
