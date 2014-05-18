package spare.funlibrary.framework.impl;

import spare.funlibrary.framework.Game;
import spare.funlibrary.framework.Screen;

public abstract class GLScreen extends Screen {
	protected final GLGraphics glGraphics;
	protected final GLGame2 glGame;
	
	public GLScreen(Game game) {
		super(game);
		glGame=(GLGame2)game;
		glGraphics=((GLGame2)game).getGLGraphics();
	}
}
