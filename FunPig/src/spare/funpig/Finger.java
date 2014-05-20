package spare.funpig;

import spare.funlibrary.framework.GameObject2;
import spare.funlibrary.framework.math.Router;
import spare.funlibrary.framework.math.Router.Route;
import spare.funlibrary.framework.math.Vector2;

public class Finger extends GameObject2 {
	public static final int WIDTH=3;
	public static final int HEIGHT=4;
	
	public static final int STATE_READY=0;
	public static final int STATE_TOUCH=1;
	public static final int STATE_FLIP=2;
	
	public int state;
	public float stateTime;
	public Vector2 positionCopy;
	public Router routerTouch;
	public Router routerFlip;
	public float zoom=1;
	
	public Finger(float x, float y, float width, float height) {
		super(x, y, width, height);
		state=STATE_READY;
		positionCopy=new Vector2();
		
		routerTouch=new Router();
		routerTouch.routes.add(new Route(0.5f) {
			@Override
			public void routing(float phaseTime, float timePiece) {
				float speed=2f/duration;
				position.set(positionCopy).add(0, speed*phaseTime);
			}
		});
		routerTouch.routes.add(new Route(1f) {
			@Override
			public void routing(float phaseTime, float timePiece) {
				zoom=1-0.3f*(float)Math.sin(Math.PI*(phaseTime/duration));
			}
		});
		routerTouch.routes.add(new Route(1f) {
			@Override
			public void routing(float phaseTime, float timePiece) {
				zoom=1-0.3f*(float)Math.sin(Math.PI*(phaseTime/duration));
			}
		});
		routerTouch.routes.add(new Route(0.5f) {
			@Override
			public void routing(float phaseTime, float timePiece) {
				float speed=2f/duration;
				position.set(positionCopy).add(0, -speed*phaseTime);
			}
		});
		routerTouch.sequence();
		
		routerFlip=new Router();
		routerFlip.routes.add(new Route(0.5f) {
			@Override
			public void routing(float phaseTime, float timePiece) {
				float speed=2f/duration;
				position.set(positionCopy).add(0, speed*phaseTime);
			}
		});
		routerFlip.routes.add(new Route(0.5f) {
			@Override
			public void routing(float phaseTime, float timePiece) {
				float speed=0.3f/duration;
				zoom=1-speed*phaseTime;
			}
		});
		routerFlip.routes.add(new Route(0.2f) {
			@Override
			public void routing(float phaseTime, float timePiece) {
				float speed=2f/duration;
				position.set(positionCopy).add(0, 2+speed*phaseTime);
			}
		});
		routerFlip.routes.add(new Route(0.2f) {
			@Override
			public void routing(float phaseTime, float timePiece) {
				float speed=0.3f/duration;
				zoom=0.7f+speed*phaseTime;
			}
		});
		routerFlip.routes.add(new Route(1f) {
			@Override
			public void routing(float phaseTime, float timePiece) {
				float speed=4f/duration;
				position.set(positionCopy).add(0, 2+2-speed*phaseTime);
			}
		});
		routerFlip.sequence();
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		stateTime+=deltaTime;
		
		switch (state) {
		case STATE_READY:
			positionCopy.set(2, -1);
			position.set(positionCopy);
			break;
		case STATE_TOUCH:
			routerTouch.update(stateTime, deltaTime);
			if(stateTime>routerTouch.getDuration()){
				state=STATE_READY;
				stateTime=0;
			}
			break;
		case STATE_FLIP:
			routerFlip.update(stateTime, deltaTime);
			if(stateTime>routerFlip.getDuration()){
				state=STATE_READY;
				stateTime=0;
			}
			break;
		default:
			break;
		}
	}

}
