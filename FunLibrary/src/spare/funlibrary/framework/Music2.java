package spare.funlibrary.framework;

public interface Music2 {
    public void play();

    public void stop();

    public void pause();
    
    public void playFromStart();

    public void setLooping(boolean looping);

    public void setVolume(float volume);

    public boolean isPlaying();

    public boolean isPaused();
    
    public boolean isStopped();

    public boolean isLooping();
    
    public boolean isPrepared();

    public void dispose();
    
    public void setSpb(float spb);
    
    public void setBpm(float bpm);
    
    public float getSpb();
    
    public float getBpm();
    
    public float getPosition();
    
    public float getDuration();
    
    public void seekTo(float position);

}
