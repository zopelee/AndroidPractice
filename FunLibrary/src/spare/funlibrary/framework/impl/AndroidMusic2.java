package spare.funlibrary.framework.impl;

import spare.funlibrary.framework.Music2;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class AndroidMusic2 implements Music2, OnCompletionListener{
	MediaPlayer mediaPlayer;
	boolean isPrepared=false;
	boolean isLoop=false;
	float volume=1;
	float spb=-1;
	float bpm=-1;
	float duration;
	AssetFileDescriptor assetDescriptor;
	
	public AndroidMusic2(AssetFileDescriptor assetDescriptor){
		this.assetDescriptor=assetDescriptor;
		mediaPlayer=new MediaPlayer();
		try {
			mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
					assetDescriptor.getStartOffset(),
					assetDescriptor.getLength());
			mediaPlayer.prepare();
			isPrepared=true;
			mediaPlayer.setOnCompletionListener(this);
			duration=(float)mediaPlayer.getDuration()/1000;
			Log.d("aaa", "prepared (in construction)");
		}catch(Exception e){
			throw new RuntimeException("Music Loading Error!");
		}
	}
	
	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		Log.d("aaa", "on completion");
		synchronized(this){
			isPrepared=false;
		}
	}

	@Override
	public void play() {
		if (mediaPlayer.isPlaying()){
			//pause()会让isPlaying()变false
			//即使pause()也触发onCompletion使得isPrepared变false?
			Log.d("aaa", "play (already playing)");
			return;
		}
		
		try {
			synchronized(this){
				if (isPrepared==false){
					mediaPlayer.stop();
					mediaPlayer.reset();
					mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
							assetDescriptor.getStartOffset(),
							assetDescriptor.getLength());
					mediaPlayer.prepare();
					isPrepared=true;
					mediaPlayer.setOnCompletionListener(this);
					duration=(float)mediaPlayer.getDuration()/1000;
					
					//reset()后，原设为loop的音乐变得不loop了
					mediaPlayer.setVolume(volume, volume);
					mediaPlayer.setLooping(isLoop);
					
					Log.d("aaa", "play (with prepare)");
				}else{
					Log.d("aaa", "play (from paused)");
				}
				mediaPlayer.start();
			}
		}catch(IllegalStateException e) {
			e.printStackTrace();
		}catch(Exception e){
			throw new RuntimeException("Music Loading Error!");
		}
	}

	@Override
	public void stop() {
		Log.d("aaa", "stoping()");
		mediaPlayer.stop();
		synchronized(this){
			isPrepared=false;
		}
		Log.d("aaa", "stopped()");
	}

	@Override
	public void pause() {
		if (mediaPlayer.isPlaying()){
			Log.d("aaa", "pausing (from playing)");
			mediaPlayer.pause();
			Log.d("aaa", "paused (from playing)");
		}
	}
	
	@Override
	public void playFromStart() {
		synchronized (this) {
			if(!isStopped()){
				stop();
			}
			do {
			} while (isStopped()==false);
			play();
		}
	}

	@Override
	public void setLooping(boolean looping) {
		mediaPlayer.setLooping(looping);
		this.isLoop=looping;
	}

	@Override
	public void setVolume(float volume) {
		mediaPlayer.setVolume(volume,volume);
		this.volume=volume;
	}

	@Override
	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	@Override
	public boolean isStopped() {
		return !isPrepared;
	}

	@Override
	public boolean isLooping() {
		return mediaPlayer.isLooping();
	}

	@Override
	public void dispose() {
		if (mediaPlayer.isPlaying()){
			mediaPlayer.stop();
		}
		mediaPlayer.release();
	}

	@Override
	public float getSpb() {
		return this.spb;
	}
	
	@Override
	public float getBpm() {
		return this.bpm;
	}

	@Override
	public void setSpb(float spb) {
		this.spb=spb;
		this.bpm=(1/spb)*60;
	}

	@Override
	public void setBpm(float bpm) {
		this.bpm=bpm;
		this.spb=60/bpm;
	}
	@Override
	public float getPosition() {
		return (float)mediaPlayer.getCurrentPosition()/1000;
	}
	
	@Override
	public void seekTo(float position) {
		mediaPlayer.seekTo((int)(position*1000));
	}

	@Override
	public boolean isPrepared() {
		return this.isPrepared;
	}

	@Override
	public float getDuration() {
		return duration;
	}

	@Override
	public boolean isPaused() {
		if(isStopped()==false && isPlaying()==false){
			return true;
		}else{
			return false;
		}
	}
}
