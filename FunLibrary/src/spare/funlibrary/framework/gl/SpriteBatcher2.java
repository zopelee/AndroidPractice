package spare.funlibrary.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;
import spare.funlibrary.framework.impl.GLGraphics;
import spare.funlibrary.framework.math.Vector2;

public class SpriteBatcher2 {
	final float[] verticesBuffer;
	int bufferIndex;
	final Vertices vertices;
	int numSprites;
	private float rChnl;
	private float gChnl;
	private float bChnl;
	private float alphaChnl;
	
	public SpriteBatcher2(GLGraphics glGraphics, int maxSprites){
		//this.verticesBuffer=new float[maxSprites*4*4];
		//每sprite含4点，每点又含x,y,u,v四个坐标（另含ARGB共4值）
		this.verticesBuffer=new float[maxSprites*4*(4+4)];
		this.vertices=new Vertices(glGraphics, maxSprites*4, maxSprites*6, true, true);
		this.bufferIndex=0;
		this.numSprites=0;
		this.rChnl=1;
		this.gChnl=1;
		this.bChnl=1;
		this.alphaChnl=1;
		
		short[] indices=new short[maxSprites*6];
		int len=indices.length;
		short j=0;
		for(int i=0; i<len; i+=6, j+=4){
			indices[i+0]=(short)(j+0);
			indices[i+1]=(short)(j+1);
			indices[i+2]=(short)(j+2);
			indices[i+3]=(short)(j+2);
			indices[i+4]=(short)(j+3);
			indices[i+5]=(short)(j+0);
		}
		vertices.setIndices(indices, 0, indices.length);
		//step1
	}
	
	public void beginBatch(Texture2 texture){
		texture.bind();
		//导入的texture已经load、reload好了
		numSprites=0;
		bufferIndex=0;
		//虽构造时都已归零，但重复使用时仍需重新归零
	}
	
	public void endBatch(){
		vertices.setVertices(verticesBuffer, 0, bufferIndex);
		//step2
		//verticesBuffer是在drawSprite()中准备好的
		vertices.bind();
		vertices.draw(GL10.GL_TRIANGLES, 0, numSprites*6);
		//这才是真draw，不同于drawSprite
		vertices.unbind();
	}
	
	public void drawSprite(float x, float y, TextureRegion2 region, float virtualRatio, float alphaChnl, int directionX, int directionY){
		this.alphaChnl=alphaChnl;
		drawSprite(x, y, region, virtualRatio, directionX, directionY);
	}
	
	public void drawSprite(float x, float y, TextureRegion2 region, float virtualRatio, float alphaChnl){
		this.alphaChnl=alphaChnl;
		drawSprite(x, y, region, virtualRatio);
	}
	
	public void drawSprite(float x, float y, TextureRegion2 region, float virtualRatio, float zoomX, float zoomY, float aChnl){
		float width=region.width*virtualRatio;
		float height=region.height*virtualRatio;
		this.alphaChnl=aChnl;
		drawSprite(x, y, width*zoomX, height*zoomY, region);
	}
	
	public void drawSprite(float x, float y, TextureRegion2 region, float virtualRatio, float zoomX, float zoomY){
		float width=region.width*virtualRatio;
		float height=region.height*virtualRatio;
		drawSprite(x, y, width*zoomX, height*zoomY, region);
	}
	
	public void drawSprite(float x, float y, TextureRegion2 region, float virtualRatio, int directionX, int directionY){
		float width=region.width*virtualRatio;
		float height=region.height*virtualRatio;
		drawSprite(x, y, directionX*width, directionY*height, region);
	}
	
	public void drawSprite(float x, float y, TextureRegion2 region, float virtualRatio){
		float width=region.width*virtualRatio;
		float height=region.height*virtualRatio;
		drawSprite(x, y, width, height, region);
	}
	
	public void drawSprite(float x, float y, TextureRegion2 region, float referenceWidth, TextureRegion2 referenceRegion, float alphaChnl){
		this.alphaChnl=alphaChnl;
		drawSprite(x, y, region, referenceWidth, referenceRegion);
	}
	
	public void drawSprite(float x, float y, TextureRegion2 region, float referenceWidth, TextureRegion2 referenceRegion){
		float rate=referenceWidth/referenceRegion.width;
		float width=region.width*rate;
		float height=region.height*rate;
		drawSprite(x, y, width, height, region);
	}
	
	public void drawSprite(float x, float y, TextureRegion2 region, float referenceWidth, float referenceHeight, TextureRegion2 referenceRegion, float alphaChnl){
		this.alphaChnl=alphaChnl;
		drawSprite(x, y, region, referenceWidth, referenceHeight, referenceRegion);
	}
	
	public void drawSprite(float x, float y, TextureRegion2 region, float referenceWidth, float referenceHeight, TextureRegion2 referenceRegion){
		float width=referenceWidth*(region.width/referenceRegion.width);
		float height=referenceHeight*(region.height/referenceRegion.height);
		drawSprite(x, y, width, height, region);
	}
	
	public void drawSprite(float x, float y, float width, float height, TextureRegion2 region, 
			float rChnl, float gChnl, float bChnl, float alphaChnl){
		this.rChnl=rChnl;
		this.gChnl=gChnl;
		this.bChnl=bChnl;
		this.alphaChnl=alphaChnl;
		drawSprite(x, y, width, height, region);
	}
	
	public void drawSprite(float x, float y, float width, float height, TextureRegion2 region, float alphaChnl){
		this.alphaChnl=alphaChnl;
		drawSprite(x, y, width, height, region);
	}
	
	public void drawSprite(float x, float y, float width, float height, TextureRegion2 region){
		if(width==0){
			width=height/region.aspRatio;
		}
		if(height==0){
			height=width*region.aspRatio;
		}
		float halfWidth=width/2;
		float halfHeight=height/2;
		float x1=x-halfWidth;
		float y1=y-halfHeight;
		float x2=x+halfWidth;
		float y2=y+halfHeight;
		
		verticesBuffer[bufferIndex++]=x1;
		verticesBuffer[bufferIndex++]=y1;
		verticesBuffer[bufferIndex++]=rChnl;
		verticesBuffer[bufferIndex++]=gChnl;
		verticesBuffer[bufferIndex++]=bChnl;
		verticesBuffer[bufferIndex++]=alphaChnl;
		verticesBuffer[bufferIndex++]=region.u1;
		verticesBuffer[bufferIndex++]=region.v2;
		//图片纵坐标导致，故y1对应v2

		verticesBuffer[bufferIndex++]=x2;
		verticesBuffer[bufferIndex++]=y1;
		verticesBuffer[bufferIndex++]=rChnl;
		verticesBuffer[bufferIndex++]=gChnl;
		verticesBuffer[bufferIndex++]=bChnl;
		verticesBuffer[bufferIndex++]=alphaChnl;
		verticesBuffer[bufferIndex++]=region.u2;
		verticesBuffer[bufferIndex++]=region.v2;
		
		verticesBuffer[bufferIndex++]=x2;
		verticesBuffer[bufferIndex++]=y2;
		verticesBuffer[bufferIndex++]=rChnl;
		verticesBuffer[bufferIndex++]=gChnl;
		verticesBuffer[bufferIndex++]=bChnl;
		verticesBuffer[bufferIndex++]=alphaChnl;
		verticesBuffer[bufferIndex++]=region.u2;
		verticesBuffer[bufferIndex++]=region.v1;
		
		verticesBuffer[bufferIndex++]=x1;
		verticesBuffer[bufferIndex++]=y2;
		verticesBuffer[bufferIndex++]=rChnl;
		verticesBuffer[bufferIndex++]=gChnl;
		verticesBuffer[bufferIndex++]=bChnl;
		verticesBuffer[bufferIndex++]=alphaChnl;
		verticesBuffer[bufferIndex++]=region.u1;
		verticesBuffer[bufferIndex++]=region.v1;
		
		numSprites++;
		rChnl=1;
		gChnl=1;
		bChnl=1;
		alphaChnl=1;
	}

	public void drawSprite(float x, float y, float width, float height, float angle, TextureRegion2 region, float alphaChnl){
		this.alphaChnl=alphaChnl;
		drawSprite(x, y, width, height, angle, region);
	}
	
	public void drawSprite(float x, float y, float width, float height, float angle, TextureRegion2 region){
		if(width==0){
			width=height/region.aspRatio;
		}
		if(height==0){
			height=width*region.aspRatio;
		}
		
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
		
		verticesBuffer[bufferIndex++]=x1;
		verticesBuffer[bufferIndex++]=y1;
		verticesBuffer[bufferIndex++]=1;
		verticesBuffer[bufferIndex++]=1;
		verticesBuffer[bufferIndex++]=1;
		verticesBuffer[bufferIndex++]=alphaChnl;
		verticesBuffer[bufferIndex++]=region.u1;
		verticesBuffer[bufferIndex++]=region.v2;

		verticesBuffer[bufferIndex++]=x2;
		verticesBuffer[bufferIndex++]=y2;
		verticesBuffer[bufferIndex++]=1;
		verticesBuffer[bufferIndex++]=1;
		verticesBuffer[bufferIndex++]=1;
		verticesBuffer[bufferIndex++]=alphaChnl;
		verticesBuffer[bufferIndex++]=region.u2;
		verticesBuffer[bufferIndex++]=region.v2;

		verticesBuffer[bufferIndex++]=x3;
		verticesBuffer[bufferIndex++]=y3;
		verticesBuffer[bufferIndex++]=1;
		verticesBuffer[bufferIndex++]=1;
		verticesBuffer[bufferIndex++]=1;
		verticesBuffer[bufferIndex++]=alphaChnl;
		verticesBuffer[bufferIndex++]=region.u2;
		verticesBuffer[bufferIndex++]=region.v1;

		verticesBuffer[bufferIndex++]=x4;
		verticesBuffer[bufferIndex++]=y4;
		verticesBuffer[bufferIndex++]=1;
		verticesBuffer[bufferIndex++]=1;
		verticesBuffer[bufferIndex++]=1;
		verticesBuffer[bufferIndex++]=alphaChnl;
		verticesBuffer[bufferIndex++]=region.u1;
		verticesBuffer[bufferIndex++]=region.v1;

		numSprites++;
		alphaChnl=1;
	}
}
