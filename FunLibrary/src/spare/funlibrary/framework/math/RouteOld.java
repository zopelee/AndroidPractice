package spare.funlibrary.framework.math;



public abstract class RouteOld {
	public final float startPoint;
	public final float endPoint;
	private boolean isStarted;
	private boolean isEnded;
	private float phaseTime;
	private float timePiece;

	
	public RouteOld(float startPoint, float endPoint) {
		this.startPoint=startPoint;
		this.endPoint=endPoint;
		isStarted=false;
		isEnded=false;
	}
	
	public void update(float stateTime, float deltaTime){
		phaseTime=stateTime-startPoint;
		if(stateTime>startPoint && stateTime<endPoint){
			if(isStarted==false){
				routing(phaseTime, phaseTime);
				isStarted=true;
			}else{
				routing(phaseTime, deltaTime);
			}
		}
		if(stateTime>endPoint){
			if(isEnded==false){
				timePiece=deltaTime-(stateTime-endPoint);
				routing(phaseTime, timePiece);
				isEnded=true;
			}else{
				return;
			}
		}
	}
	
	public abstract void routing(float phaseTime, float timePiece);
}
