package spare.funlibrary.framework;

public interface Sound2 {
	public void play(float volume);
	
	public void play(float volume, float sleepSec);
	
	public void pause();
	
	public void resume();
	
	public void stop();
	
	public void setLoop(int loop);
	
	public void setRate(float rate);
	
	public void setSpb(float spb);
	
	public float getSpb();
	
	public void dispose();
}
