package spare.funlibrary.framework.impl;

import java.io.IOException;

import spare.funlibrary.framework.Music;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class AndroidMusic implements Music, OnCompletionListener{
	MediaPlayer mediaPlayer;
	boolean isPrepared=false;
	
	public AndroidMusic(AssetFileDescriptor assetDescriptor){
		mediaPlayer=new MediaPlayer();
		try {
			mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
					assetDescriptor.getStartOffset(),
					assetDescriptor.getLength());
			mediaPlayer.prepare();
			isPrepared=true;
			mediaPlayer.setOnCompletionListener(this);
		}catch(Exception e){
			throw new RuntimeException("Music Loading Error!");
		}
	}
	
	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		synchronized(this){
			isPrepared=false;
		}
	}

	@Override
	public void play() {
		if (mediaPlayer.isPlaying())
			return;
		
		try {
			synchronized(this){
				if (!isPrepared)
					mediaPlayer.prepare();
				mediaPlayer.start();
			}
		}catch(IllegalStateException e) {
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		mediaPlayer.stop();
		synchronized(this){
			isPrepared=false;
		}
	}

	@Override
	public void pause() {
		if (mediaPlayer.isPlaying())
			mediaPlayer.pause();
	}

	@Override
	public void setLooping(boolean looping) {
		mediaPlayer.setLooping(looping);
	}

	@Override
	public void setVolume(float volume) {
		mediaPlayer.setVolume(volume,volume);
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
		if (mediaPlayer.isPlaying())
			mediaPlayer.stop();
		mediaPlayer.release();
	}
}
