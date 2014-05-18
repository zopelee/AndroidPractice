package spare.funpig;

import spare.funlibrary.framework.GameObject2;
import spare.funlibrary.framework.math.Router;
import spare.funlibrary.framework.math.Vector2;
import spare.funlibrary.framework.math.Router.Route;


public class Pig extends GameObject2{
	public static final int STATE_CRAWL=0;
	public static final int STATE_HELLO=1;
	public static final int STATE_LOOK_UP=7;
	public static final int STATE_EAT=2;
	public static final int STATE_JUMP=3;
	public static final int STATE_FALL=4;
	public static final int STATE_CATCH=5;
	public static final int STATE_MISS=6;
	
	public static final float WIDTH=2.5f;
	public static final float HEIGHT=WIDTH*(3f/5f);
	public static final float JUMP_HEIGHT=4;
	
	public final Vector2 positionCopy=new Vector2();
	public final Vector2 headPosition=new Vector2();
	public final Vector2 armPosition=new Vector2();
	public final Vector2 legPosition=new Vector2();
	
	public float speed=0.5f;
	public int direction=-1;
	
	public Router routerJump;
	public Router routerFall;
	
	public int state=STATE_CRAWL;
	public float stateTime=0;
	
	public Pig(float x, float y) {
		super(x,y,WIDTH,HEIGHT);
		positionCopy.set(position);
		routerJump=new Router();
		routerJump.routes.add(new Route(0.5f) {
			@Override
			public void routing(float phaseTime, float timePiece) {
				float accel=2*JUMP_HEIGHT/(duration*duration);
				position.set(positionCopy.x, (float)(positionCopy.y+
						Math.sqrt(2*accel*JUMP_HEIGHT)*phaseTime-0.5*accel*phaseTime*phaseTime));
			}
		});
		routerJump.sequence();
		routerFall=new Router();
		routerFall.routes.add(new Route(0.5f) {
			@Override
			public void routing(float phaseTime, float timePiece) {
				float accel=2*JUMP_HEIGHT/(duration*duration);
				position.set(positionCopy.x, (float)(positionCopy.y+
						JUMP_HEIGHT-0.5*accel*phaseTime*phaseTime));
			}
			@Override
			public void correcting() {
				super.correcting();
				position.set(positionCopy.x, positionCopy.y);
			}
		});
		routerFall.sequence();
	}
	
	public void update(float deltaTime){
		super.update(deltaTime);
		stateTime+=deltaTime;
		bounds.lowerLeft.set(position).sub(bounds.width/2, bounds.height/2);
		
		updateBody(deltaTime);
		updateHead(deltaTime);
		updateArm(deltaTime);
		updateLeg(deltaTime);
	}
	
	private void updateBody(float deltaTime){
		switch (state) {
		case STATE_CRAWL:
			//位移在world里
			break;
		case STATE_LOOK_UP:
			if(stateTime>Assets.pigBody02AnimLookUp.getDuration()){
				state=STATE_CRAWL;
				stateTime=0;
			}
		case STATE_JUMP:
			routerJump.update(stateTime, deltaTime);
			bounds.width=0.5f;
			bounds.height=0.8f;
			if(stateTime>routerJump.getDuration()){
				state=STATE_MISS;
				stateTime=0;
			}
			break;
		case STATE_MISS:
			if(stateTime>Assets.pigBody03AnimMiss.getDuration()){
				state=STATE_FALL;
				stateTime=0;
			}
		case STATE_CATCH:
			if(stateTime>Assets.pigBody03AnimCatch.getDuration()){
				state=STATE_FALL;
				stateTime=0;
			}
			break;
		case STATE_FALL:
			routerFall.update(stateTime, deltaTime);
			if(stateTime>routerFall.getDuration()){
				bounds.width=Pig.WIDTH;
				bounds.height=Pig.HEIGHT;
				state=STATE_CRAWL;
				stateTime=0;
			}
			break;
		case STATE_HELLO:
			position.set(positionCopy).add(direction*0.25f, 0.25f);
			if(stateTime>Assets.pigBody04AnimHello.getDuration()){
				position.set(positionCopy);
				state=STATE_CRAWL;
				stateTime=0;
			}
			break;
		default:
			break;
		}
	}
	
	private void updateHead(float deltaTime){
		switch (state) {
		case STATE_CRAWL:
			headPosition.set(position).add(direction*1.5f/2, 1f/2);
			break;
		case STATE_MISS:
		case STATE_CATCH:
			headPosition.set(position).add(direction*1f/2, 1.5f/2);
			break;
		default:
			break;
		}
	}
	
	private void updateLeg(float deltaTime){
		switch (state) {
		case STATE_CRAWL:
			legPosition.set(position);
			break;
		case STATE_EAT:
		case STATE_HELLO:
			legPosition.set(positionCopy);
			break;
		default:
			break;
		}
	}
	
	private void updateArm(float deltaTime){
		switch (state) {
		case STATE_HELLO:
			armPosition.set(position);
			break;
		default:
			break;
		}
	}
	
	public void lookUp(){
		state=STATE_LOOK_UP;
		stateTime=0;
	}
	
	public void sayHello(){
		state=STATE_HELLO;
		stateTime=0;
		Assets.laugh.play(0.5f);
	}
	
	public void jump(){
		state=STATE_JUMP;
		stateTime=0;
		Assets.laugh.stop();
	}
	
	public void catching(){
		position.set(positionCopy).add(0, JUMP_HEIGHT);
		state=STATE_CATCH;
		stateTime=0;
	}
	
	public void resetStateTimes(){
		stateTime=0;
	}
}