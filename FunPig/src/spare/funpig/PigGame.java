package spare.funpig;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import spare.funlibrary.framework.Screen;
import spare.funlibrary.framework.impl.GLGame2;


public class PigGame extends GLGame2 {
	boolean firstTimeCreated=true;

	@Override
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);
		if(firstTimeCreated){
			Assets.load(this);
			firstTimeCreated=false;
		}else{
			Assets.reload();
		}
	}
}
