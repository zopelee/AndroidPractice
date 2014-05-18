package spare.funlibrary.framework.impl;

import java.io.IOException;

import spare.funlibrary.framework.Audio;
import spare.funlibrary.framework.Music2;
import spare.funlibrary.framework.Sound2;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public class AndroidAudio implements Audio{
	AssetManager assets;
	SoundPool soundPool;
	
	public AndroidAudio(Activity activity) {
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets=activity.getAssets();
		this.soundPool=new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
	}
	
	@Override
	public Music2 newMusic(String fileName) {
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(fileName);
			return new AndroidMusic2(assetDescriptor);
		}catch (IOException e){
			throw new RuntimeException("Music Loading Error: "+fileName);
		}
	}

	@Override
	public Sound2 newSound(String fileName) {
		try {
			AssetFileDescriptor assetDescriptor=assets.openFd(fileName);
			int soundId=soundPool.load(assetDescriptor, 0);
			return new AndroidSound2(soundPool,soundId);
		}catch (IOException e) {
			throw new RuntimeException("No Sound Loaded: "+fileName);
		}
	}
}
