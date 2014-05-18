package spare.funlibrary.framework.math;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class SlotSwitcher {
	private int activeSlotIndex;
	private float activeSlotDuration;
	private float totalDuration;
	private float phaseTime;
	private boolean isLoop;
	private boolean isSlotChanged=false;
	private List<Float> timeSlots;
	private List<Float> sumTimeSlots;
	private float sumTimeSlotPassed=0;
	private int size;
	private int lastLoopCount;
	private int lastIndex;
	public static final int NONLOOP=0;
	public static final int LOOP=1;
	
	public SlotSwitcher(List<Float> timeSlots, int loopMode) {
		setTimeSlots(timeSlots, loopMode);
	}
	
	public void setTimeSlots(List<Float> timeSlots){
		this.timeSlots=timeSlots;
		size=timeSlots.size();
		sumTimeSlots=new ArrayList<Float>();
		float sum=0;
		for(int i=0; i<size; i++){
			sum+=timeSlots.get(i);
			//不是get(0)！
			sumTimeSlots.add(sum);
		}
		totalDuration=sum;
		activeSlotIndex=0;
		activeSlotDuration=this.timeSlots.get(0);
		sumTimeSlotPassed=0;
		phaseTime=0;
	}
	
	public void setTimeSlots(List<Float> timeSlots, int loopMode){
		if(loopMode==NONLOOP){
			isLoop=false;
		}else{
			isLoop=true;
		}
		this.setTimeSlots(timeSlots);
	}
	
	private void updating(){
		if(phaseTime<0){
			phaseTime=0;
		}
		if(activeSlotIndex>timeSlots.size()-1){
			if(isLoop==false){
				return;
			}else{
				activeSlotIndex=0;
			}
		}
		activeSlotDuration=timeSlots.get(activeSlotIndex);
		isSlotChanged=false;
		if(phaseTime>activeSlotDuration){
			isSlotChanged=true;
			sumTimeSlotPassed+=activeSlotDuration;
			phaseTime -=activeSlotDuration;
			//不对，万一phaseTime已经超大了呢
			activeSlotIndex++;
			if(activeSlotIndex>timeSlots.size()-1){
				Log.d("aaa", "slot index top");
				activeSlotIndex=0;
				activeSlotDuration=timeSlots.get(0);
			}else{
				activeSlotDuration=timeSlots.get(activeSlotIndex);
			}
		}
	}
	
	public void update(float stateTime){
		//stateTime可正可负数
		isSlotChanged=false;
		float netStateTime=stateTime;
		int loopCount=0;
		while(netStateTime<0){
			netStateTime+=totalDuration;
			loopCount--;
		}
		while(netStateTime>totalDuration){
			netStateTime-=totalDuration;
			loopCount++;
		}
		if(activeSlotIndex<=0 || activeSlotIndex>size-1){
			activeSlotIndex=0;
			activeSlotDuration=timeSlots.get(0);
			sumTimeSlotPassed=0;
			if(loopCount==lastLoopCount+1 && lastIndex==size-1){
				isSlotChanged=true;
			}
		}
		while(netStateTime<sumTimeSlotPassed || netStateTime>sumTimeSlotPassed+activeSlotDuration){
			activeSlotIndex++;
			if(activeSlotIndex>size-1){
				activeSlotIndex=0;
			}
			activeSlotDuration=timeSlots.get(activeSlotIndex);
			if(activeSlotIndex>0){
				sumTimeSlotPassed=sumTimeSlots.get(activeSlotIndex-1);
			}else{
				sumTimeSlotPassed=0;
			}
			if(loopCount==lastLoopCount && activeSlotIndex==lastIndex+1){
				isSlotChanged=true;
			}
		}
		lastLoopCount=loopCount;
		lastIndex=activeSlotIndex;
		sumTimeSlotPassed+=loopCount*totalDuration;
		phaseTime=stateTime-sumTimeSlotPassed;
	}
	
	public void update2(float deltaTime){
		float stateTime=sumTimeSlotPassed+phaseTime+deltaTime;
		update(stateTime);
	}
	
	public int getSize(){
		return this.size;
	}
	
	public float getTotalDuration(){
		return this.totalDuration;
	}
	
	public int getActiveSlotIndex(){
		return activeSlotIndex;
	}
	
	public float getActiveSlotDuration(){
		return activeSlotDuration;
	}
	
	public float getPhaseTime(){
		return phaseTime;
	}
	
	public boolean isSlotChanged(){
		return this.isSlotChanged;
	}
	
	public boolean isWitinIndices(int newSlotIndex){
		if(newSlotIndex<0 || newSlotIndex>size-1){
			return false;
		}else{
			return true;
		}
	}
	
	public void setIndex(int newSlotIndex){
		//newSlotIndex可正可负
		while(newSlotIndex<0){
			newSlotIndex+=size;
		}
		while(newSlotIndex>size-1){
			newSlotIndex-=size;
		}
		activeSlotIndex=newSlotIndex;
		activeSlotDuration=timeSlots.get(newSlotIndex);
		phaseTime=0;
		if(newSlotIndex>0){
			sumTimeSlotPassed=sumTimeSlots.get(newSlotIndex-1);
		}else{
			sumTimeSlotPassed=0;
		}
	}
	
	public boolean isWithinSlots(float stateTime){
		if(stateTime<0 || stateTime>totalDuration){
			return false;
		}else{
			return true;
		}
	}
}
