package spare.funpig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import spare.funlibrary.framework.Callout;
import spare.funlibrary.framework.math.OverlapTester;
import spare.funlibrary.framework.math.SlotSwitcher;
import spare.funlibrary.framework.math.Vector2;

public class World {
	public interface WorldListener{
		public void hit();
	}
	
	public static final float WIDTH=6;
	public static final float HEIGHT=8;
	public static final float PIG_Y=1;
	
	public static final int STATE_RUNNING=0;
	public static final Vector2 gravity=new Vector2(0, -9.8f);
	
	public final WorldListener listener;
	public final Pig pig;
	public final Bird bird;
	public final Callout callout;
	public final Finger finger;
	public int tipIndex;
	public int lastTipIndex;
	
	public int state;
	public int score=0;
	public int lastScore=0;
	
	public float bgmStateTime=0;
	public float runningStateTime=0;
	public SlotSwitcher beatPieceSlotSwitcher;
	public List<Integer> scoreCriteria=new ArrayList<Integer>();
	public List<Integer> hitScores=new ArrayList<Integer>();
	public Vector2 lastPigPosition=new Vector2();
	public boolean firstTimeTouch=true;
	public boolean firstTimeFlip=true;
	
	public World(WorldListener listener) {
		state=STATE_RUNNING;
		pig=new Pig(3,PIG_Y);
		bird=new Bird(-2, 1+4, Bird.WIDTH, Bird.HEIGHT);
		callout=new Callout(3, 3, 1f, 0.5f, 0.2f, 2.8f, 2.5f, 0.5f, 0.9f, 0.9f, 0.9f);
		finger=new Finger(2, -1, Finger.WIDTH, Finger.HEIGHT);
		tipIndex=-1;
		scoreCriteria=Arrays.asList(-2,-1,0,1,2);
		beatPieceSlotSwitcher=new SlotSwitcher(Arrays.asList(
				0.05f*Assets.activeBgm.getSpb(),
				0.05f*Assets.activeBgm.getSpb(),
				0.3f*Assets.activeBgm.getSpb(),
				0.05f*Assets.activeBgm.getSpb(),
				0.05f*Assets.activeBgm.getSpb()
				), SlotSwitcher.NONLOOP);
		this.listener=listener;
	}
	
	public void update(float deltaTime) {
		bgmStateTime=Assets.activeBgm.getPosition()-0.2f;
		runningStateTime+=deltaTime;
		updatePig(deltaTime);
		updateBird(deltaTime);
		updateCallout(deltaTime);
		updateFinger(deltaTime);
		checkCatch();
	}
	
	private void updatePig(float deltaTime){
		if(pig.state==Pig.STATE_CRAWL){
			lastPigPosition.set(pig.position);
			if(pig.position.x<0.7*Pig.WIDTH){
				pig.direction=1;
			}else if(pig.position.x>WIDTH-0.7*Pig.WIDTH){
				pig.direction=-1;
			}
			pig.positionCopy.add(pig.direction*pig.speed*deltaTime, 0);
			pig.position.set(pig.positionCopy);
		}
		pig.update(deltaTime);
	}
	
	private void updateBird(float deltaTime) {
		bird.update(deltaTime);
	}
	
	private void updateCallout(float deltaTime){
		callout.update(deltaTime);
		switch (pig.state) {
		case Pig.STATE_HELLO:
			callout.position.set(pig.positionCopy.x, pig.positionCopy.y+1);
			tipIndex=0;
			callout.fadeIn(3);
			break;
		case Pig.STATE_CATCH:
			callout.position.set(pig.position.x, pig.position.y+2);
			tipIndex=1;
			callout.fadeIn(3);
			break;
		default:
			callout.fadeOut(3);
			break;
		}
	}
	
	private void updateFinger(float deltaTime) {
		if(firstTimeTouch==true && bgmStateTime>3){
			finger.state=Finger.STATE_TOUCH;
			finger.stateTime=0;
			firstTimeTouch=false;
		}
		if(firstTimeFlip==true && bgmStateTime>9){
			finger.state=Finger.STATE_FLIP;
			finger.stateTime=0;
			firstTimeFlip=false;
		}
		finger.update(deltaTime);
	}
	
	private void checkCatch() {
		if(pig.state==Pig.STATE_JUMP && bird.state==Bird.STATE_FLY){
			if(OverlapTester.overlapRectangles(pig.bounds, bird.bounds)){
				lastScore=score;
				score++;
				pig.catching();
				bird.catched();
				bird.positionCopy.x=pig.position.x+pig.direction*0.2f;
			}
		}
	}
}

