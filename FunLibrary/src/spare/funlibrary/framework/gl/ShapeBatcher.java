package spare.funlibrary.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import spare.funlibrary.framework.Callout;
import spare.funlibrary.framework.impl.GLGraphics;
import spare.funlibrary.framework.math.Vector2;
import android.util.FloatMath;

public class ShapeBatcher {
	final float[] verticesBufTriangle;
	final float[] verticesBufRectangle;
	final float[] verticesBufCircle;
	final Vertices verticesTriangle;
	final Vertices verticesRectangle;
	final Vertices verticesCircle;
	int bufferIndexTriangle;
	int bufferIndexRectangle;
	int bufferIndexCircle;
	int numTriangles;
	int numRectangles;
	int numCircles;
	float centralAngle;
	int maxShapes;
	int trianglesPerCircle;
	
	public static final int TRIANGLE=0;
	public static final int RECTANGLE=1;
	public static final int CIRCLE=2;
	
	public ShapeBatcher(GLGraphics glGraphics, int maxShapes, int trianglesPerCircle){
		this.maxShapes=maxShapes;
		this.trianglesPerCircle=trianglesPerCircle;
		this.centralAngle=360f/trianglesPerCircle;
		this.verticesBufTriangle=new float[maxShapes*3*(2+4)];
		this.verticesBufRectangle=new float[maxShapes*4*(2+4)];
		this.verticesBufCircle=new float[(maxShapes*(trianglesPerCircle*1+1))*(2+4)];
		//莫maxShapes*trianglesPerCircle*1+1
		this.verticesTriangle=new Vertices(glGraphics, maxShapes*3, 0, true, false);
		this.verticesRectangle=new Vertices(glGraphics, maxShapes*4, maxShapes*6, true, false);
		this.verticesCircle=new Vertices(glGraphics, maxShapes*(trianglesPerCircle*1+1), maxShapes*trianglesPerCircle*3, true, false);
		
		this.bufferIndexTriangle=0;
		this.bufferIndexRectangle=0;
		this.bufferIndexCircle=0;
		this.numTriangles=0;
		this.numRectangles=0;
		this.numCircles=0;
		
		short[] indicesRectangle=new short[maxShapes*6];
		for(int i=0, j=0; i<indicesRectangle.length; i+=6, j+=4){
			indicesRectangle[i+0]=(short)(j+0);
			indicesRectangle[i+1]=(short)(j+1);
			indicesRectangle[i+2]=(short)(j+2);
			indicesRectangle[i+3]=(short)(j+2);
			indicesRectangle[i+4]=(short)(j+3);
			indicesRectangle[i+5]=(short)(j+0);
		}
		short[] indicesCircle=new short[maxShapes*trianglesPerCircle*3];
		for(short i=0; i<maxShapes; i++){
		//per圆
			for(short j=0; j<trianglesPerCircle; j++){
			//per三角形
				indicesCircle[i*trianglesPerCircle*3+(j*3)]=(short)(i*trianglesPerCircle+i);
				for(int k=1; k<3; k++){
					indicesCircle[i*trianglesPerCircle*3+(j*3)+k]=(short)(i*trianglesPerCircle+(j+1)+(k-1)+i);
					//per三角形的另外两点
				}
			}
			indicesCircle[(i+1)*trianglesPerCircle*3-1]=indicesCircle[i*trianglesPerCircle*3+1];
			//重写per圆的最后一点
		}
		
		verticesRectangle.setIndices(indicesRectangle, 0, indicesRectangle.length);
		verticesCircle.setIndices(indicesCircle, 0, indicesCircle.length);
	}
	
	public void beginBatch(){
		this.bufferIndexTriangle=0;
		this.bufferIndexRectangle=0;
		this.bufferIndexCircle=0;
		this.numTriangles=0;
		this.numRectangles=0;
		this.numCircles=0;
	}
	
	public void endBatch(){
		if(numTriangles>0){
			verticesTriangle.setVertices(verticesBufTriangle, 0, bufferIndexTriangle);
			verticesTriangle.bind();
			verticesTriangle.draw(GL10.GL_TRIANGLES, 0, numTriangles*3);
			verticesTriangle.unbind();
		}
		if(numRectangles>0){
			verticesRectangle.setVertices(verticesBufRectangle, 0, bufferIndexRectangle);
			verticesRectangle.bind();
			verticesRectangle.draw(GL10.GL_TRIANGLES, 0, numRectangles*6);
			verticesRectangle.unbind();
		}
		if(numCircles>0){
			verticesCircle.setVertices(verticesBufCircle, 0, bufferIndexCircle);
			verticesCircle.bind();
			verticesCircle.draw(GL10.GL_TRIANGLES, 0, numCircles*trianglesPerCircle*3);
			verticesCircle.unbind();
		}
	}
	
	public void drawCircle(float x, float y, float radius, float r, float g, float b, float a){
		float width=2*radius*FloatMath.sin(centralAngle/2*Vector2.TO_RADIANS);
		float height=radius*FloatMath.cos(centralAngle/2*Vector2.TO_RADIANS);
		float halfWidth=width/2;
		float halfHeight=height/2;
		
		verticesBufCircle[bufferIndexCircle++]=x;
		verticesBufCircle[bufferIndexCircle++]=y;
		verticesBufCircle[bufferIndexCircle++]=r;
		verticesBufCircle[bufferIndexCircle++]=g;
		verticesBufCircle[bufferIndexCircle++]=b;
		verticesBufCircle[bufferIndexCircle++]=a;
		for(float angle=0; angle<360; angle+=centralAngle){
			float rad=angle*Vector2.TO_RADIANS;
			float cos=FloatMath.cos(rad);
			float sin=FloatMath.sin(rad);
			
			float x1=-halfWidth*cos-(-halfHeight)*sin;
			float y1=-halfWidth*sin+(-halfHeight)*cos;
			
			x1+=x+halfHeight*sin;
			y1+=y-halfHeight*cos;
			
			verticesBufCircle[bufferIndexCircle++]=x1;
			verticesBufCircle[bufferIndexCircle++]=y1;
			verticesBufCircle[bufferIndexCircle++]=r;
			verticesBufCircle[bufferIndexCircle++]=g;
			verticesBufCircle[bufferIndexCircle++]=b;
			verticesBufCircle[bufferIndexCircle++]=a;
		}
		
		numCircles++;
	}
	
	public void drawTriangle(float x, float y, float width, float height, float r, float g, float b, float a){
		float halfWidth=width/2;
		float halfHeight=height/2;
		float x1=x-halfWidth;
		float y1=y-halfHeight;
		float x2=x+halfWidth;
		float y2=y+halfHeight;
		
		verticesBufTriangle[bufferIndexTriangle++]=x1;
		verticesBufTriangle[bufferIndexTriangle++]=y1;
		verticesBufTriangle[bufferIndexTriangle++]=r;
		verticesBufTriangle[bufferIndexTriangle++]=g;
		verticesBufTriangle[bufferIndexTriangle++]=b;
		verticesBufTriangle[bufferIndexTriangle++]=a;
		
		verticesBufTriangle[bufferIndexTriangle++]=x2;
		verticesBufTriangle[bufferIndexTriangle++]=y1;
		verticesBufTriangle[bufferIndexTriangle++]=r;
		verticesBufTriangle[bufferIndexTriangle++]=g;
		verticesBufTriangle[bufferIndexTriangle++]=b;
		verticesBufTriangle[bufferIndexTriangle++]=a;
		
		verticesBufTriangle[bufferIndexTriangle++]=x;
		verticesBufTriangle[bufferIndexTriangle++]=y2;
		verticesBufTriangle[bufferIndexTriangle++]=r;
		verticesBufTriangle[bufferIndexTriangle++]=g;
		verticesBufTriangle[bufferIndexTriangle++]=b;
		verticesBufTriangle[bufferIndexTriangle++]=a;
		
		numTriangles++;
	}
	
	public void drawTriangle(float x, float y, float width, float height, float angle, float r, float g, float b, float a){
		float halfWidth=width/2;
		float halfHeight=height/2;
		
		float rad=angle*Vector2.TO_RADIANS;
		float cos=FloatMath.cos(rad);
		float sin=FloatMath.sin(rad);
		
		float x1=-halfWidth*cos-(-halfHeight)*sin;
		float y1=-halfWidth*sin+(-halfHeight)*cos;
		float x2=halfWidth*cos-(-halfHeight)*sin;
		float y2=halfWidth*sin+(-halfHeight)*cos;
//		float x3=halfWidth*cos-halfHeight*sin;
//		float y3=halfWidth*sin+halfHeight*cos;
//		float x4=-halfWidth*cos-halfHeight*sin;
//		float y4=-halfWidth*sin+halfHeight*cos;
		float x5=-halfHeight*sin;
		float y5=halfHeight*cos;
		

		x1+=x;
		y1+=y;
		x2+=x;
		y2+=y;
		x5+=x;
		y5+=y;
		
		verticesBufTriangle[bufferIndexTriangle++]=x1;
		verticesBufTriangle[bufferIndexTriangle++]=y1;
		verticesBufTriangle[bufferIndexTriangle++]=r;
		verticesBufTriangle[bufferIndexTriangle++]=g;
		verticesBufTriangle[bufferIndexTriangle++]=b;
		verticesBufTriangle[bufferIndexTriangle++]=a;
		
		verticesBufTriangle[bufferIndexTriangle++]=x2;
		verticesBufTriangle[bufferIndexTriangle++]=y2;
		verticesBufTriangle[bufferIndexTriangle++]=r;
		verticesBufTriangle[bufferIndexTriangle++]=g;
		verticesBufTriangle[bufferIndexTriangle++]=b;
		verticesBufTriangle[bufferIndexTriangle++]=a;
		
		verticesBufTriangle[bufferIndexTriangle++]=x5;
		verticesBufTriangle[bufferIndexTriangle++]=y5;
		verticesBufTriangle[bufferIndexTriangle++]=r;
		verticesBufTriangle[bufferIndexTriangle++]=g;
		verticesBufTriangle[bufferIndexTriangle++]=b;
		verticesBufTriangle[bufferIndexTriangle++]=a;
		
		numTriangles++;
	}
	
	public void drawRectangle(float x, float y, float width, float height, float r, float g, float b, float a){
		float halfWidth=width/2;
		float halfHeight=height/2;
		float x1=x-halfWidth;
		float y1=y-halfHeight;
		float x2=x+halfWidth;
		float y2=y+halfHeight;
		
		verticesBufRectangle[bufferIndexRectangle++]=x1;
		verticesBufRectangle[bufferIndexRectangle++]=y1;
		verticesBufRectangle[bufferIndexRectangle++]=r;
		verticesBufRectangle[bufferIndexRectangle++]=g;
		verticesBufRectangle[bufferIndexRectangle++]=b;
		verticesBufRectangle[bufferIndexRectangle++]=a;

		verticesBufRectangle[bufferIndexRectangle++]=x2;
		verticesBufRectangle[bufferIndexRectangle++]=y1;
		verticesBufRectangle[bufferIndexRectangle++]=r;
		verticesBufRectangle[bufferIndexRectangle++]=g;
		verticesBufRectangle[bufferIndexRectangle++]=b;
		verticesBufRectangle[bufferIndexRectangle++]=a;
		
		verticesBufRectangle[bufferIndexRectangle++]=x2;
		verticesBufRectangle[bufferIndexRectangle++]=y2;
		verticesBufRectangle[bufferIndexRectangle++]=r;
		verticesBufRectangle[bufferIndexRectangle++]=g;
		verticesBufRectangle[bufferIndexRectangle++]=b;
		verticesBufRectangle[bufferIndexRectangle++]=a;
		
		verticesBufRectangle[bufferIndexRectangle++]=x1;
		verticesBufRectangle[bufferIndexRectangle++]=y2;
		verticesBufRectangle[bufferIndexRectangle++]=r;
		verticesBufRectangle[bufferIndexRectangle++]=g;
		verticesBufRectangle[bufferIndexRectangle++]=b;
		verticesBufRectangle[bufferIndexRectangle++]=a;
		
		numRectangles++;
	}
	
	public void drawRectangle(float x, float y, float width, float height, float angle, float r, float g, float b, float a){		
		float halfWidth=width/2;
		float halfHeight=height/2;
		
		float rad=angle*Vector2.TO_RADIANS;
		float cos=FloatMath.cos(rad);
		float sin=FloatMath.sin(rad);
		
		float x1=-halfWidth*cos-(-halfHeight)*sin;
		float y1=-halfWidth*sin+(-halfHeight)*cos;
		float x2=halfWidth*cos-(-halfHeight)*sin;
		float y2=halfWidth*sin+(-halfHeight)*cos;
		float x3=halfWidth*cos-halfHeight*sin;
		float y3=halfWidth*sin+halfHeight*cos;
		float x4=-halfWidth*cos-halfHeight*sin;
		float y4=-halfWidth*sin+halfHeight*cos;

		x1+=x;
		y1+=y;
		x2+=x;
		y2+=y;
		x3+=x;
		y3+=y;
		x4+=x;
		y4+=y;
		
		verticesBufRectangle[bufferIndexRectangle++]=x1;
		verticesBufRectangle[bufferIndexRectangle++]=y1;
		verticesBufRectangle[bufferIndexRectangle++]=r;
		verticesBufRectangle[bufferIndexRectangle++]=g;
		verticesBufRectangle[bufferIndexRectangle++]=b;
		verticesBufRectangle[bufferIndexRectangle++]=a;

		verticesBufRectangle[bufferIndexRectangle++]=x2;
		verticesBufRectangle[bufferIndexRectangle++]=y2;
		verticesBufRectangle[bufferIndexRectangle++]=r;
		verticesBufRectangle[bufferIndexRectangle++]=g;
		verticesBufRectangle[bufferIndexRectangle++]=b;
		verticesBufRectangle[bufferIndexRectangle++]=a;

		verticesBufRectangle[bufferIndexRectangle++]=x3;
		verticesBufRectangle[bufferIndexRectangle++]=y3;
		verticesBufRectangle[bufferIndexRectangle++]=r;
		verticesBufRectangle[bufferIndexRectangle++]=g;
		verticesBufRectangle[bufferIndexRectangle++]=b;
		verticesBufRectangle[bufferIndexRectangle++]=a;

		verticesBufRectangle[bufferIndexRectangle++]=x4;
		verticesBufRectangle[bufferIndexRectangle++]=y4;
		verticesBufRectangle[bufferIndexRectangle++]=r;
		verticesBufRectangle[bufferIndexRectangle++]=g;
		verticesBufRectangle[bufferIndexRectangle++]=b;
		verticesBufRectangle[bufferIndexRectangle++]=a;
		
		numRectangles++;
	}
	
	public void drawRoundRect(float x, float y, float width, float height, float cornerRadius, float r, float g, float b, float a){
		drawCircle(x-width/2+cornerRadius, y-height/2+cornerRadius, cornerRadius, r, g, b, a);
		drawCircle(x+width/2-cornerRadius, y-height/2+cornerRadius, cornerRadius, r, g, b, a);
		drawCircle(x+width/2-cornerRadius, y+height/2-cornerRadius, cornerRadius, r, g, b, a);
		drawCircle(x-width/2+cornerRadius, y+height/2-cornerRadius, cornerRadius, r, g, b, a);
		drawRectangle(x, y, width, height-cornerRadius*2, r, g, b, a);
		drawRectangle(x, y, width-cornerRadius*2, height, r, g, b, a);
	}
	
	public void drawCallout(float x, float y, float width, float height, float cornerRadius, 
			float arrowPointX, float arrowPointY, float arrowWidth, float r, float g, float b, float a){
		float arrowDist=(float)Math.sqrt((arrowPointX-x)*(arrowPointX-x)+(arrowPointY-y)*(arrowPointY-y));
		float arrowAngle;
		if((arrowPointX-x)*(arrowPointY-y)>0){
			arrowAngle=(float)Math.atan2(arrowPointX-x, arrowPointY-y)*Vector2.TO_DEGREES;
		}else{
		//atan2在第二象限与第四象限的值可能反了
			arrowAngle=(float)Math.atan2(-(arrowPointX-x), -(arrowPointY-y))*Vector2.TO_DEGREES;
		}
		if(arrowAngle<0){
			arrowAngle+=360;
		}
		arrowAngle-=90;
		//竖着的三角形变平躺
		
		drawTriangle((arrowPointX+x)/2, (arrowPointY+y)/2, arrowWidth, arrowDist, arrowAngle, r, g, b, a);
		drawRoundRect(x, y, width, height, cornerRadius, r, g, b, a);
	}
	
	public void drawCallout2(float x, float y, float width, float height, float cornerRadius, 
			float arrowWidth, float arrowDist, float arrowAngle, float r, float g, float b, float a){
		float arrowAngleRad=arrowAngle*Vector2.TO_RADIANS;
		float arrowPointX=x-arrowDist*(float)Math.sin(arrowAngleRad);
		float arrowPointY=y+arrowDist*(float)Math.cos(arrowAngleRad);
		
		drawTriangle((arrowPointX+x)/2, (arrowPointY+y)/2, arrowWidth, arrowDist, arrowAngle, r, g, b, a);
		drawRoundRect(x, y, width, height, cornerRadius, r, g, b, a);
	}
	
	public void drawCallout(float x, float y, float width, float height, float cornerRadius, 
			float arrowX, float arrowY, float arrowWidth, float arrowDist, float arrowAngle, 
			float r, float g, float b, float a){
		drawTriangle(arrowX, arrowY, arrowWidth, arrowDist, arrowAngle, r, g, b, a);
		drawRoundRect(x, y, width, height, cornerRadius, r, g, b, a);
	}
	
	public void drawCallout(Callout callout){
		drawCallout(callout.position.x, callout.position.y,
				callout.width, callout.height, callout.cornerRadius,
				callout.arrowPosition.x, callout.arrowPosition.y,
				callout.arrowWidth, callout.arrowDist, callout.arrowAngle, 
				callout.r, callout.g, callout.b, callout.alphaChnl);
	}
}
