package spare.funlibrary.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import spare.funlibrary.framework.impl.GLGraphics;
import spare.funlibrary.framework.math.Vector2;

public class Camera2D2 {
	public final Vector2 position;
	public float zoom;
	public float viewportZoom=1;
	public final float frustumWidth;
	public final float frustumHeight;
	final GLGraphics glGraphics;
	public int mode;
	
	public int viewportWidth;
	public int viewportHeight;
	float screenAspRatio;
	float frustumAspRatio;
	public int offsetX;
	public int offsetY;
	//相对屏幕底边的偏移（向上）
	public static final int ALIGN_TOP=0;
	public static final int ALIGN_CENTER=1;
	public static final int ALIGN_BOTTOM=2;
	public static final int STRETCH=3;
	
	public Camera2D2(GLGraphics glGraphics, float frustumWidth, float frustumHeight){
		this.glGraphics=glGraphics;
		this.frustumWidth=frustumWidth;
		this.frustumHeight=frustumHeight;
		screenAspRatio=(float)glGraphics.getHeight()/(float)glGraphics.getWidth();
		frustumAspRatio=(float)frustumHeight/(float)frustumWidth;
		this.position=new Vector2(frustumWidth/2, frustumHeight/2);
		this.zoom=1.0f;
		this.mode=STRETCH;
	}

	public Camera2D2(GLGraphics glGraphics, float frustumWidth, float frustumHeight, int mode){
		this.glGraphics=glGraphics;
		this.frustumWidth=frustumWidth;
		this.frustumHeight=frustumHeight;
		screenAspRatio=(float)glGraphics.getHeight()/(float)glGraphics.getWidth();
		frustumAspRatio=(float)frustumHeight/(float)frustumWidth;
		this.position=new Vector2(frustumWidth/2, frustumHeight/2);
		this.zoom=1.0f;
		this.mode=mode;
	}
	
	public void setViewportAndMatrices(float viewportZoom){
		this.viewportZoom=viewportZoom;
		setViewportAndMatrices();
		this.viewportZoom=1;
	}
	
	public void setViewportAndMatrices(){
		GL10 gl=glGraphics.getGL();
		viewportWidth=(int) (glGraphics.getWidth()*viewportZoom);
		viewportHeight=(int) (glGraphics.getHeight()*viewportZoom);
		if(this.mode==STRETCH){
			gl.glViewport(0, 0, viewportWidth, viewportHeight);
		}else{
			if(frustumAspRatio<screenAspRatio){
				viewportHeight=(int) (viewportWidth*frustumAspRatio);
				switch (this.mode) {
				default:
				case ALIGN_TOP:
					offsetY=glGraphics.getHeight()-viewportHeight;
					break;
				case ALIGN_CENTER:
					offsetY=(glGraphics.getHeight()-viewportHeight)/2;
					break;
				case ALIGN_BOTTOM:
					offsetY=0;
					break;
				}
				gl.glViewport(0, offsetY, viewportWidth, viewportHeight);
			}else{
				viewportWidth=(int) (viewportHeight/frustumAspRatio);
				offsetX=(glGraphics.getWidth()-viewportWidth)/2;
				gl.glViewport(offsetX, 0, viewportWidth, viewportHeight);
			}
		}
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(position.x-frustumWidth*zoom/2, 
					position.x+frustumWidth*zoom/2, 
					position.y-frustumHeight*zoom/2, 
					position.y+frustumHeight*zoom/2, 
					1, -1);
		//易误写成glOrthox
		//仅projection模式下可设置立方体，设置完毕再改模式。
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	public void touchToWorld(Vector2 touch){
		if(this.mode==STRETCH){
			touch.x=(touch.x/(float)glGraphics.getWidth())*frustumWidth*zoom;
			touch.y=(1-touch.y/(float)glGraphics.getHeight())*frustumHeight*zoom;
		}else{
			if(frustumAspRatio<screenAspRatio){
				touch.x=(touch.x/(float)glGraphics.getWidth())*frustumWidth*zoom;
				touch.y=((glGraphics.getHeight()-touch.y)-offsetY)/(float)viewportHeight*frustumHeight*zoom;
			}else{
				touch.x=(touch.x-offsetX)/(float)viewportWidth*frustumWidth*zoom;
				touch.y=(1-touch.y/(float)glGraphics.getHeight())*frustumHeight*zoom;
			}
		}
		touch.add(position).sub(frustumWidth*zoom/2, frustumHeight*zoom/2);
	}
	
	public void touchVelocityToWorld(Vector2 touchVelocity){
		if(this.mode==STRETCH){
			touchVelocity.x=(touchVelocity.x/(float)glGraphics.getWidth())*frustumWidth*zoom;
			touchVelocity.y=-touchVelocity.y/(float)glGraphics.getHeight()*frustumHeight*zoom;
		}else{
			if(frustumAspRatio<screenAspRatio){
				touchVelocity.x=(touchVelocity.x/(float)glGraphics.getWidth())*frustumWidth*zoom;
				touchVelocity.y=-touchVelocity.y/(float)viewportHeight*frustumHeight*zoom;
			}else{
				touchVelocity.x=touchVelocity.x/(float)viewportWidth*frustumWidth*zoom;
				touchVelocity.y=-touchVelocity.y/(float)glGraphics.getHeight()*frustumHeight*zoom;
			}
		}
	}
}
