package spare.funpig;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import spare.funlibrary.framework.Game;
import spare.funlibrary.framework.Input2.GestureEvent;
import spare.funlibrary.framework.Input2.TouchEvent;
import spare.funlibrary.framework.gl.Camera2D2;
import spare.funlibrary.framework.gl.ShapeBatcher;
import spare.funlibrary.framework.gl.SpriteBatcher2;
import spare.funlibrary.framework.impl.GLScreen2;
import spare.funlibrary.framework.math.OverlapTester;
import spare.funlibrary.framework.math.Rectangle;
import spare.funlibrary.framework.math.Vector2;
import spare.funpig.World.WorldListener;

public class GameScreen extends GLScreen2 {
	static final int GAME_RUNNING=1;
	static final int GAME_PAUSED=2;
	
	int state;
	Camera2D2 guiCam;
	Vector2 touchPoint;
	SpriteBatcher2 batcher;
	ShapeBatcher shapeBatcher;
	World world;
	WorldListener worldListener;
	WorldRenderer renderer;
	
	Rectangle pauseBtnBound;
	Rectangle resumeBtnBound;
	Rectangle exitBtnBound;
	
	List<TouchEvent> touchEvents;
	TouchEvent event;
	List<GestureEvent> gestureEvents;
	GestureEvent gestureEvent;
	
	public GameScreen(Game game) {
		super(game);
		state=GAME_RUNNING;
		guiCam=new Camera2D2(glGraphics, 480, 640, Camera2D2.ALIGN_CENTER);
		touchPoint=new Vector2();
		batcher=new SpriteBatcher2(glGraphics, 1000);
		shapeBatcher=new ShapeBatcher(glGraphics, 100, 30);
		worldListener=new WorldListener() {
			public void hit() {
			}
		};
		
		pauseBtnBound=new Rectangle(320, 480, 160, 160);
		resumeBtnBound=new Rectangle(320, 480, 160, 160);
		exitBtnBound=new Rectangle(80, 200, 320, 120);
		
		Assets.lastBgm=Assets.activeBgm;
		Assets.activeBgm=Assets.bgm01;
		//先设置好bgm再创建world
		world = new World(worldListener);
		renderer=new WorldRenderer(glGraphics, batcher, shapeBatcher, world);
		viewportZoom=0;
	}

	@Override
	public void update(float deltaTime) {
		touchEvents = game.getInput().getTouchEvents();
		gestureEvents=game.getInput().getGestureEvents();
		game.getInput().getKeyEvents();
		
		switch(state){
		case GAME_RUNNING:
			updateRunning(deltaTime);
			break;
		case GAME_PAUSED:
			updatePaused();
			break;
		}
	}
	
	private void updateRunning(float deltaTime) {
		for(int i=0; i<gestureEvents.size(); i++){
			gestureEvent=gestureEvents.get(i);
			switch (gestureEvent.type) {
			case GestureEvent.DOWN:
				touchPoint.set(gestureEvent.x1, gestureEvent.y1);
				guiCam.touchToWorld(touchPoint);
				if(OverlapTester.pointInRectangle(pauseBtnBound, touchPoint)==true){
					state=GAME_PAUSED;
					if(Assets.activeBgm.isPlaying()==true){
						Assets.activeBgm.pause();
					}
					return;
				}else if(world.pig.state==Pig.STATE_CRAWL){
					world.pig.sayHello();
				}
				break;
			case GestureEvent.FLING:
				if(world.pig.state==Pig.STATE_CRAWL || world.pig.state==Pig.STATE_HELLO){
					world.pig.jump();
				}
				break;
			default:
				break;
			}
		}
		world.update(deltaTime);
	}
	
	private void updatePaused(){
		for(int i=0; i<touchEvents.size(); i++){
			event = touchEvents.get(i);
			if (event.type==TouchEvent.TOUCH_DOWN) {
				touchPoint.set(event.x, event.y);
				guiCam.touchToWorld(touchPoint);
				if(OverlapTester.pointInRectangle(resumeBtnBound, touchPoint)==true){
					state=GAME_RUNNING;
					Assets.activeBgm.play();
					return;
				}else if(OverlapTester.pointInRectangle(exitBtnBound, touchPoint)==true){
					Assets.activeBgm.stop();
					game.setScreen(new LoadingScreen(game));
				}
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl=glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		renderer.render();
		guiCam.setViewportAndMatrices();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		switch(state){
		case GAME_RUNNING:
			presentRunning();
			break;
		case GAME_PAUSED:
			presentPaused();
			break;
		}
		gl.glDisable(GL10.GL_BLEND);
	}
	
	private void presentRunning() {
		batcher.beginBatch(Assets.ui01);
		batcher.drawSprite(400, 560, 160, 160, Assets.ui01BtnPauseRegion);
		batcher.endBatch();
	}
	
	private void presentPaused(){
		batcher.beginBatch(Assets.ui01);
		batcher.drawSprite(400, 560, 160, 160, Assets.ui01BtnResumeRegion);
		batcher.endBatch();
	}

	@Override
	public void resume() {
		super.resume();
		if(state == GAME_RUNNING){
			Assets.activeBgm.play();
		}
	}
	
	@Override
	public void pause() {
		super.pause();
		Assets.activeBgm.pause();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		Assets.activeBgm.stop();
	}
}