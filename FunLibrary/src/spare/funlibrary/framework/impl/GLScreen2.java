package spare.funlibrary.framework.impl;

import spare.funlibrary.framework.Game;
import spare.funlibrary.framework.Screen;

public abstract class GLScreen2 extends Screen {
	protected final GLGraphics glGraphics;
	protected final GLGame2 glGame;
	
	public static float viewportZoom=1;
	public boolean firstTimeCreated=true;
	
	public GLScreen2(Game game) {
		super(game);
		glGame=(GLGame2)game;
		glGraphics=((GLGame2)game).getGLGraphics();
	}
	
	@Override
	public void resume() {
	//Activity或其它Screen调用setScreen()后resume()一次
	//从Home回到Activity的onResume()并不会调用Screen的resume()
	}
	
	@Override
	public void pause() {
	//按Home键->Activity的onPause()让state变成GLGameState.Paused->Activity的onDrawFrame()让screen.pause()
	}
	
	@Override
	public void dispose() {
	//Activity或其它Screen调用setScreen()后之前的screen先被pause()再被dispose()
	}
}
