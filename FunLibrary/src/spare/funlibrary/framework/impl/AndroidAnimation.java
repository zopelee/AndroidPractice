package spare.funlibrary.framework.impl;

import spare.funlibrary.framework.Game;
import spare.funlibrary.framework.Graphics;
import spare.funlibrary.framework.Pixmap;

public class AndroidAnimation {
	public Pixmap film;
	public boolean isBackPlay;
	public boolean isLoop;
	public int filmWidth;
	public int filmHeight;
	public int frameWidth;
	public int frameCount;
	public float fpsTime;
	
	public Graphics g;
	public boolean foreLoop=true;
	public float passTime=0;
	public int frameIndex=0;
	
	public AndroidAnimation (Game game, Pixmap film, 
			boolean isBackPlay, boolean isLoop,
			int frameWidth, float fpsTime) {
		this.g=game.getGraphics();
		this.film=film;
		this.isBackPlay=isBackPlay;
		this.isLoop=isLoop;
		this.frameWidth = frameWidth;
		
		this.filmWidth = film.getWidth();
		this.filmHeight = film.getHeight();
		
		this.frameCount=filmWidth/frameWidth;
		this.fpsTime=fpsTime;
	}
	
	public void update(float deltaTime) {
		passTime+=deltaTime;
		while(passTime>fpsTime){
			passTime-=fpsTime;
			
			if (foreLoop==true)
				frameIndex=frameIndex+1;
			else
				frameIndex=frameIndex-1;
			
			if (frameIndex == (frameCount-1) && foreLoop==true) {
				foreLoop=false;
			}
			if (frameIndex == 0 && foreLoop==false) {
				foreLoop=true;
			}
		}
	}
	
	public void present(int x, int y) {
		g.drawPixmap(film, x, y, frameIndex*frameWidth, 0, 
				frameWidth, filmHeight);
	}
}
