package spare.funlibrary.framework.impl;

import android.media.SoundPool;
import spare.funlibrary.framework.Sound2;

public class AndroidSound2 implements Sound2{
	int soundId;
	int streamId;
	SoundPool soundPool;
	float spb;
	PlayThread sleepThread;
	
	class PlayThread extends Thread {
		private float sec=0;
		float leftVolume=1;
		float rightVolume=1;
		int priority=0;
		int loop=0;
		float rate=1;
		
		@Override
		public void run() {
			try {
				sleep((long) (sec*1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			streamId=soundPool.play(soundId, leftVolume, rightVolume, priority, loop, rate);
		}
		
		public void setSec(float sec){
			this.sec=sec;
		}
		
		public void setLeftVolume(float leftVolume){
			this.leftVolume=leftVolume;
		}
		
		public void setRightVolume(float rightVolume){
			this.rightVolume=rightVolume;
		}
		
		public void setStreamPriority(int priority){
			this.priority=priority;
		}
		
		public void setLoop(int loop){
			this.loop=loop;
		}
		
		public void setRate(int rate){
			this.rate=rate;
		}
	}

	public AndroidSound2(SoundPool soundPool, int soundId) {
		this.soundId=soundId;
		this.soundPool=soundPool;
	}
	
	@Override
	public void play(float volume) {
		streamId=soundPool.play(soundId, volume, volume, 0, 0, 1);
		//书上无视play()的返回值
	}
	
	@Override
	public void play(float volume, float sleepSec) {
		sleepThread=new PlayThread();
		//thread无法被restart，故每次都需new
		sleepThread.setSec(sleepSec);
		sleepThread.setLeftVolume(volume);
		sleepThread.setRightVolume(volume);
		sleepThread.setStreamPriority(0);
		sleepThread.setLoop(0);
		sleepThread.setRate(1);
		sleepThread.start();
	}

	@Override
	public void dispose() {
		soundPool.unload(soundId);
	}

	@Override
	public void pause() {
		soundPool.pause(soundId);
	}

	@Override
	public void resume() {
		soundPool.resume(soundId);
	}

	@Override
	public void stop() {
		soundPool.stop(streamId);
		//书上是soundId，错了
	}

	@Override
	public void setLoop(int loop) {
		soundPool.setLoop(soundId, loop);
	}

	@Override
	public void setRate(float rate) {
		soundPool.setRate(soundId, rate);
	}

	@Override
	public void setSpb(float spb) {
		this.spb=spb;
	}

	@Override
	public float getSpb() {
		return this.spb;
	}
}
