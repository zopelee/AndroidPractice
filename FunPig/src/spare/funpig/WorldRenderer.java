package spare.funpig;

import javax.microedition.khronos.opengles.GL10;

import spare.funlibrary.framework.gl.Animation2;
import spare.funlibrary.framework.gl.Camera2D2;
import spare.funlibrary.framework.gl.ShapeBatcher;
import spare.funlibrary.framework.gl.SpriteBatcher2;
import spare.funlibrary.framework.gl.TextureRegion2;
import spare.funlibrary.framework.impl.GLGraphics;



public class WorldRenderer {
	static final float FRUSTUM_WIDTH=6;
	static final float FRUSTUM_HEIGHT=8;
	GLGraphics glGraphics;
	World world;
	Camera2D2 cam;
	SpriteBatcher2 batcher;
	ShapeBatcher shapeBatcher;
	
	final float aspectRatio;
	public float virtualRatio;
	
	public float alphaChnl=1;
	public boolean firstTimeRender=true;
	
	public WorldRenderer(GLGraphics glGraphics, SpriteBatcher2 batcher, ShapeBatcher shapBatcher, World world){
		this.glGraphics=glGraphics;
		this.world=world;
		this.aspectRatio=(float)glGraphics.getHeight()/glGraphics.getWidth();
		this.virtualRatio=(float)Pig.WIDTH/Assets.pigBody01AnimCrawl.width;
		this.cam=new Camera2D2(glGraphics, 
				FRUSTUM_WIDTH, 
				FRUSTUM_HEIGHT, 
				Camera2D2.ALIGN_CENTER);
		this.cam.zoom=1;
		this.batcher=batcher;
		this.shapeBatcher=shapBatcher;
	}
	
	public WorldRenderer(GLGraphics glGraphics, SpriteBatcher2 batcher, World world){
		this(glGraphics, batcher, new ShapeBatcher(glGraphics, 100, 30), world);
	}
	
	public WorldRenderer(GLGraphics glGraphics, World world){
		this(glGraphics, new SpriteBatcher2(glGraphics, 100), new ShapeBatcher(glGraphics, 100, 30), world);
	}

	public void render(){
		cam.position.x=FRUSTUM_WIDTH/2;
		cam.position.y=FRUSTUM_HEIGHT/2;
		cam.setViewportAndMatrices();
		renderBackground();
		renderObjects();
	}

	private void renderBackground() {
		batcher.beginBatch(Assets.bkg);
		batcher.drawSprite(FRUSTUM_WIDTH/2, FRUSTUM_HEIGHT/2, FRUSTUM_WIDTH, FRUSTUM_HEIGHT, Assets.bkgRegion);
		batcher.endBatch();
	}

	private void renderObjects() {
		GL10 gl=glGraphics.getGL();
		gl.glEnable(GL10.GL_BLEND);
		//gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		//premultiplied
		
		renderPig();
		renderFinger();
		
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		renderCallout();
		gl.glDisable(GL10.GL_BLEND);
	}

	private void renderPig(){
		switch (world.pig.state) {
		case Pig.STATE_MISS:
		case Pig.STATE_CATCH:
			renderPigHead();
			renderBird();
			renderPigBody();
			break;
		default:
			renderPigLeg();
			renderPigBody();
			renderPigHead();
			renderPigArm();
			renderBird();
			break;
		}
	}
	
	private void renderBird() {
		batcher.beginBatch(Assets.bird);
		batcher.drawSprite(world.bird.position.x, world.bird.position.y, 
				Assets.birdRegion, virtualRatio, 1, world.bird.zoomY, world.bird.aChnl);
		batcher.endBatch();
	}
	
	private void renderPigBody() {
		TextureRegion2 keyFrame=null;
		switch (world.pig.state) {
		case Pig.STATE_CRAWL:
			batcher.beginBatch(Assets.pigBody01);
			keyFrame=Assets.pigBody01AnimCrawl.getKeyFrame(world.pig.stateTime, Animation2.LOOPING);
			break;
		case Pig.STATE_LOOK_UP:
			batcher.beginBatch(Assets.pigBody02);
			keyFrame=Assets.pigBody02AnimLookUp.getKeyFrame(world.pig.stateTime, Animation2.NONLOOPING);
			break;
		case Pig.STATE_EAT:
			batcher.beginBatch(Assets.pigBody02);
			keyFrame=Assets.pigBody02AnimEat.getKeyFrame(world.pig.stateTime, Animation2.NONLOOPING);
			break;
		case Pig.STATE_JUMP:
			batcher.beginBatch(Assets.pigBody03);
			keyFrame=Assets.pigBody03AnimJump.getKeyFrame(world.pig.stateTime, Animation2.NONLOOPING);
			break;
		case Pig.STATE_FALL:
			batcher.beginBatch(Assets.pigBody03);
			keyFrame=Assets.pigBody03AnimFall.getKeyFrame(world.pig.stateTime, Animation2.NONLOOPING);
			break;
		case Pig.STATE_CATCH:
			batcher.beginBatch(Assets.pigBody03);
			keyFrame=Assets.pigBody03AnimCatch.getKeyFrame(world.pig.stateTime, Animation2.NONLOOPING);
			break;
		case Pig.STATE_MISS:
			batcher.beginBatch(Assets.pigBody03);
			keyFrame=Assets.pigBody03AnimMiss.getKeyFrame(world.pig.stateTime, Animation2.NONLOOPING);
			break;
		case Pig.STATE_HELLO:
			batcher.beginBatch(Assets.pigBody04);
			keyFrame=Assets.pigBody04AnimHello.getKeyFrame(world.pig.stateTime, Animation2.NONLOOPING);
			break;
		default:
			keyFrame=null;
			break;
		}
		if(keyFrame!=null){
			batcher.drawSprite(world.pig.position.x, world.pig.position.y, 
					keyFrame, virtualRatio,
					-world.pig.direction, 1);
			batcher.endBatch();
		}
	}
	
	private void renderPigHead(){
		TextureRegion2 keyFrame=null;
		switch (world.pig.state) {
		case Pig.STATE_CRAWL:
			batcher.beginBatch(Assets.pigHead01);
			keyFrame=Assets.pigHead01Region;
			break;
		case Pig.STATE_MISS:
		case Pig.STATE_CATCH:
			batcher.beginBatch(Assets.pigHead02);
			keyFrame=Assets.pigHead02AnimCatch.getKeyFrame(world.pig.stateTime, Animation2.NONLOOPING);
			break;
		default:
			break;
		}
		if(keyFrame!=null){
			batcher.drawSprite(world.pig.headPosition.x, world.pig.headPosition.y, 
					keyFrame, virtualRatio,
					-world.pig.direction, 1);
			batcher.endBatch();
		}
	}
	
	private void renderPigLeg(){
		batcher.beginBatch(Assets.pigLeg01);
		TextureRegion2 keyFrame=null;
		switch (world.pig.state) {
		case Pig.STATE_CRAWL:
			keyFrame=Assets.pigLeg01AnimCrawl.getKeyFrame(world.pig.stateTime, Animation2.LOOPING);
			break;
		case Pig.STATE_HELLO:
		case Pig.STATE_LOOK_UP:
		case Pig.STATE_EAT:
			keyFrame=Assets.pigLeg01AnimCrawl.getKeyFrame(0, Animation2.NONLOOPING);
			break;
		default:
			break;
		}
		if(keyFrame!=null){
			batcher.drawSprite(world.pig.legPosition.x, world.pig.legPosition.y, keyFrame, virtualRatio,
					-world.pig.direction, 1);
			batcher.endBatch();
		}
	}
	
	private void renderPigArm(){
		batcher.beginBatch(Assets.pigArm01);
		TextureRegion2 keyFrame=null;
		switch (world.pig.state) {
		case Pig.STATE_HELLO:
			keyFrame=Assets.pigArm01AnimWave.getKeyFrame(world.pig.stateTime, Animation2.NONLOOPING);
			break;
		default:
			break;
		}
		if(keyFrame!=null){
			batcher.drawSprite(world.pig.armPosition.x, world.pig.armPosition.y, keyFrame, virtualRatio,
					-world.pig.direction, 1);
			batcher.endBatch();
		}
	}
	
	private void renderCallout(){
		if(world.tipIndex !=-1){
			GL10 gl = glGraphics.getGL();
			gl.glDisable(GL10.GL_TEXTURE_2D);
			shapeBatcher.beginBatch();
			shapeBatcher.drawCallout(world.callout);
			shapeBatcher.endBatch();
			gl.glEnable(GL10.GL_TEXTURE_2D);
			batcher.beginBatch(Assets.tipTexts.get(world.tipIndex));
			batcher.drawSprite(world.callout.position.x, world.callout.position.y,
					0.8f, 0, Assets.tipTextRegions.get(world.tipIndex), world.callout.alphaChnl);
			batcher.endBatch();
		}
	}
	
	private void renderFinger() {
		batcher.beginBatch(Assets.ui02);
		batcher.drawSprite(world.finger.position.x, world.finger.position.y, 
				Assets.ui02FingerRegion, virtualRatio, 
				world.finger.zoom, world.finger.zoom);
		batcher.endBatch();
	}
}
