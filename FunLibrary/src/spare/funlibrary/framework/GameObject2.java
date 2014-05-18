package spare.funlibrary.framework;

import spare.funlibrary.framework.math.Rectangle;
import spare.funlibrary.framework.math.Vector2;

public class GameObject2 {
	public final Vector2 position;
	public final Rectangle bounds;
	public final Vector2 shadowPosition;
	public float shadowHeightRatio;
	private float horizon;
	public float width;
	public float height;
	
	public GameObject2(float x, float y, float width, float height){
		this.position=new Vector2(x, y);
		this.bounds=new Rectangle(x-width/2, y-height/2, width, height);
		this.shadowPosition=new Vector2(x, y);
		this.shadowHeightRatio=0.3f;
		this.horizon=this.position.y-height/2;
		this.width=width;
		this.height=height;
	}
	
	public void update(float deltaTime){
		float offsetHorizon=this.position.y-this.horizon;
		this.shadowPosition.set(position.x, this.horizon-offsetHorizon*this.shadowHeightRatio);
	}
	
	public void setHorizon(float horizon){
		this.horizon=horizon;
	}
	
	public float getHorizion(){
		return this.horizon;
	}
}
