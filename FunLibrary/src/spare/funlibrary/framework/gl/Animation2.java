package spare.funlibrary.framework.gl;




public class Animation2 {
	public static final int LOOPING=0;
	public static final int NONLOOPING=1;
	public static final int BOUNCE=1;
	public static final int NONBOUNCE=0;
	public static final int REVERSE=1;
	public static final int NONREVERSE=0;
	
	public final TextureRegion2[] keyFrames;
	public float frameDuration;
	public float totalDuration;
	public float bouncedDuration;
	public boolean isBounce;
	public boolean isReverse;
	
	public final float width;
	public final float height;
	public final float aspRatio;
	
	public Animation2(float frameDuration, TextureRegion2 ... keyFrames){
		this.frameDuration=frameDuration;
		this.keyFrames=keyFrames;
		this.isBounce=false;
		this.isReverse=false;
		this.totalDuration=frameDuration*keyFrames.length;
		this.bouncedDuration=totalDuration*2-frameDuration*2;
		
		width=keyFrames[0].width;
		height=keyFrames[0].height;
		aspRatio=keyFrames[0].aspRatio;
	}
	
	public float getDuration(){
		return this.totalDuration;
	}
	
	public void setDuration(float totalDuration){
		this.totalDuration=totalDuration;
		this.frameDuration=totalDuration/keyFrames.length;
		this.bouncedDuration=totalDuration*2-frameDuration*2;
	}
	
	public void setFrameDuration(float frameDuration){
		this.frameDuration=frameDuration;
		this.totalDuration=frameDuration*keyFrames.length;
		this.bouncedDuration=totalDuration*2-frameDuration*2;
	}
	
	public TextureRegion2 getKeyFrame(float stateTime, int loopMode, int bounceMode){
		isBounce=bounceMode==BOUNCE? true:false;
		return getKeyFrame(stateTime, loopMode);
	}
	
	public TextureRegion2 getKeyFrame(float stateTime, int loopMode, int bounceMode, int reverseMode){
		isBounce=bounceMode==BOUNCE? true:false;
		isReverse=reverseMode==REVERSE? true:false;
		return getKeyFrame(stateTime, loopMode);
	}
	
	public TextureRegion2 getKeyFrame(float stateTime, int loopMode){
		int frameNumber=(int)(stateTime/frameDuration);
		int frameNumberBounce=keyFrames.length-1-frameNumber;
		int odev;
		
		if(loopMode==NONLOOPING){
			if(isBounce==true && frameNumberBounce<0){
				frameNumber=Math.max(0, keyFrames.length-1+frameNumberBounce);
			}else{
				frameNumber=Math.min(keyFrames.length-1, frameNumber);
			}
		}else{
			if(isBounce==false){
				frameNumber=frameNumber%keyFrames.length;
			}else{
				odev=frameNumber/(keyFrames.length-1)+1;
				if(odev%2==1){
					frameNumber=frameNumber%(keyFrames.length-1);
				}else{
					frameNumber=keyFrames.length-1+frameNumberBounce%(keyFrames.length-1);
				}
			}
		}
		
		isBounce=false;
		if(isReverse==true){
			isReverse=false;
			return keyFrames[keyFrames.length-1-frameNumber];
		}else{
			isReverse=false;
			return keyFrames[frameNumber];
		}
	}
}
