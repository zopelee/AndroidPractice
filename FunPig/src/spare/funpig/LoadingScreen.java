package spare.funpig;

import java.util.List;

import spare.funlibrary.framework.Game;
import spare.funlibrary.framework.Input2.TouchEvent;
import spare.funlibrary.framework.gl.Camera2D2;
import spare.funlibrary.framework.gl.SpriteBatcher2;
import spare.funlibrary.framework.impl.GLScreen2;
import spare.funlibrary.framework.math.Rectangle;
import spare.funlibrary.framework.math.Vector2;

public class LoadingScreen extends GLScreen2 {
	
	int state;
	Camera2D2 guiCam;
	Vector2 touchPoint;
	SpriteBatcher2 batcher;
	
	Rectangle buttonBounds;
	
	public LoadingScreen(Game game) {
		super(game);
		guiCam=new Camera2D2(glGraphics, 320, 480);
		touchPoint=new Vector2();
		batcher=new SpriteBatcher2(glGraphics, 1000);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		int len = touchEvents.size();
		for(int i=0; i<len; i++){
			TouchEvent event = touchEvents.get(i);
			if (event.type==TouchEvent.TOUCH_DOWN) {
			}
		}
		game.setScreen(new GameScreen(game));
	}
	
	@Override
	public void present(float deltaTime) {
	}
}