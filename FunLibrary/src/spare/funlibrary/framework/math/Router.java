package spare.funlibrary.framework.math;

import java.util.ArrayList;



public class Router {
	
	public abstract static class Route{
		public float startPoint;
		public float endPoint;
		public float duration;
		private boolean isStarted;
		private boolean isEnded;
		private float phaseTime;
		private float timePiece;
		
		public Route(float duration) {
			setRoute(duration);
		}
		
		public Route(float startPoint, float duration) {
			setRoute(startPoint, duration);
		}
		
		public void setRoute(float duration){
			this.startPoint=-1;
			this.endPoint=-1;
			this.duration=duration;
			isStarted=false;
			isEnded=false;
		}
		
		public void setRoute(float startPoint, float duration){
			setRoute(duration);
			this.startPoint=startPoint;
			this.endPoint=startPoint+duration;
		}
		
		public void update(float stateTime, float deltaTime){
			if(stateTime>startPoint && stateTime<endPoint){
				phaseTime=stateTime-startPoint;
				if(isStarted==false){
					routing(phaseTime, phaseTime);
					isStarted=true;
				}else{
					routing(phaseTime, deltaTime);
				}
			}
			if(stateTime>endPoint){
				if(isEnded==false){
//					phaseTime=stateTime-startPoint;
					timePiece=deltaTime-(stateTime-endPoint);
					routing(duration, timePiece);
					correcting();
					isEnded=true;
				}else{
					return;
				}
			}
		}
		
		public abstract void routing(float phaseTime, float timePiece);
		public void correcting(){
		}
	}
	
	public ArrayList<Route> routes;
	public int activeRouteNo;
	public float activePhaseTime;
	public float totalDuration;
	public boolean isActive=false;
	
	public Router(){
		routes=new ArrayList<Router.Route>();
		activeRouteNo=-1;
		activePhaseTime=-1;
		totalDuration=0;
	}
	
	public void sequence(){
		if(routes.get(0).startPoint==-1){
			routes.get(0).startPoint=0;
			routes.get(0).endPoint=routes.get(0).startPoint+routes.get(0).duration;
		}
		
		totalDuration=routes.get(0).duration;
		for(int i=1; i<routes.size(); i++){
			if(routes.get(i).startPoint==-1){
				routes.get(i).startPoint=routes.get(i-1).endPoint;
				routes.get(i).endPoint=routes.get(i).startPoint+routes.get(i).duration;
				totalDuration+=routes.get(i).duration;
			}
		}
		for(int i=1; i<routes.size(); i++){
			if(routes.get(i).startPoint+routes.get(i).duration>totalDuration){
				totalDuration=routes.get(i).startPoint+routes.get(i).duration;
				//假定从0开始算totalDuration
			}
		}
	}
	
	public float getDuration(){
		return this.totalDuration;
	}
	
	public void reset(){
		isActive=false;
		activePhaseTime=-1;
		activeRouteNo=-1;
		for(int i=0; i<routes.size(); i++){
			routes.get(i).isStarted=false;
			routes.get(i).isEnded=false;
		}
	}
	
	public void update(float stateTime, float deltaTime){
		if(stateTime>totalDuration){
			if(isActive==true){
				routes.get(routes.size()-1).correcting();
				//还没到最后一个route的correcting()就被return了，所以此处补偿
				reset();
			}
			return;
		}
		
		isActive=true;
		int size=routes.size();
		for(int i=0; i<size; i++){
			routes.get(i).update(stateTime, deltaTime);
			
			if(size==1){
				activeRouteNo=0;
				activePhaseTime=routes.get(i).phaseTime;
			}else{
				if(i<size-1){
					if(routes.get(i).isStarted==true && routes.get(i+1).isStarted==false){
						activeRouteNo=i;
						activePhaseTime=routes.get(i).phaseTime;
					}
				}else{
					if(routes.get(size-1).isStarted==true){
						activeRouteNo=size-1;
						activePhaseTime=routes.get(size-1).phaseTime;
					}
				}
			}
		}
	}
	
	public void scaleDurations(float scale){
		int size=routes.size();
		for(int i=0; i<size; i++){
			float startPoint=routes.get(i).startPoint;
			float duration=routes.get(i).duration;
			routes.get(i).setRoute(startPoint*scale, duration*scale);
		}
		sequence();
	}
}
