package spare.funlibrary.framework.gl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import spare.funlibrary.framework.FileIO;
import spare.funlibrary.framework.impl.GLGame2;
import spare.funlibrary.framework.impl.GLGraphics;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLUtils;
import android.util.Log;

public class Texture2 {
	GLGraphics glGraphics;
	FileIO fileIO;
	String fileName;
	int textureId;
	int minFilter;
	int magFilter;
	public int width;
	public int height;
	public int srcWidth;
	public int srcHeight;
	public int dstWidth;
	public int dstHeight;
	
	String string;
	int textSize;
	int maxLineWidth;
	int lineSpacing;
	int bkgColor;
	int r, g, b, a;
	
	public Texture2(GLGame2 glGame, String fileName){
		this.glGraphics=glGame.getGLGraphics();
		this.fileIO=glGame.getFileIO();
		this.fileName=fileName;
		this.string=null;
		load();
	}
	
	public Texture2(GLGame2 glGame, String string, int textSize, int lineSpacing, int bkgColor, float r, float g, float b, float a) {
		this(glGame, string, textSize, 0, lineSpacing, bkgColor, r, g, b, a);
	}
	
	public Texture2(GLGame2 glGame, String string, int textSize, int bkgColor, float r, float g, float b, float a) {
		this(glGame, string, textSize, 0, 0, bkgColor, r, g, b, a);
	}
	
	public Texture2(GLGraphics glGraphics, String string, int textSize, int maxPxPerLine, int lineSpacing, int bkgColor, float r, float g, float b, float a) {
		this.glGraphics=glGraphics;
		this.string=string;
		this.textSize=textSize;
		this.maxLineWidth=maxPxPerLine;
		this.lineSpacing=lineSpacing;
		this.bkgColor=bkgColor;
		this.r=(int)(r*255);
		this.g=(int)(g*255);
		this.b=(int)(b*255);
		this.a=(int)(a*255);
		load();
	}
	
	public Texture2(GLGame2 glGame, String string, int textSize, int maxPxPerLine, int lineSpacing, int bkgColor, float r, float g, float b, float a) {
		this((GLGraphics) ((GLGame2)glGame).getGLGraphics(),string, textSize, maxPxPerLine, lineSpacing,bkgColor,r,g,b,a);
	}
	
	private void load() {
		GL10 gl=glGraphics.getGL();
		int[] textureIds=new int[1];
		gl.glGenTextures(1, textureIds, 0);
		textureId=textureIds[0];
		
		Bitmap bitmap=null;
		if(this.string==null){
			InputStream in=null;
			try{
				in=fileIO.readAsset(fileName);
				bitmap=BitmapFactory.decodeStream(in);
			}catch(IOException e){
				throw new RuntimeException("Failed loading texture: "+fileName,e);
			}finally{
				if(in!=null)
					try {in.close();}catch(IOException e){}
			}
		}else{
			String lineBr = "\n";
			Paint textPaint=new Paint();
			textPaint.setTextSize(textSize);
			if(maxLineWidth>0){
				int indexStart=0;
				int indexEnd=0;
				float lineWidth=0;
				String tmpLine="";
				while (indexEnd<string.length()-1) {
					while (lineWidth<=maxLineWidth) {
						indexEnd++;
						if(indexEnd==string.length()-1){
							break;
						}
						if(tmpLine.indexOf(lineBr) !=-1){
							indexStart+=tmpLine.indexOf(lineBr)+lineBr.length();
							indexEnd=indexStart;
						}
						tmpLine=string.substring(indexStart, indexEnd+1);
						lineWidth=textPaint.measureText(tmpLine);
					}
					if(indexEnd==string.length()-1){
						break;
					}
					indexEnd--;
					tmpLine=string.substring(indexStart, indexEnd+1);
					lineWidth=0;
					if(tmpLine.lastIndexOf(" ") !=-1){
						indexStart+=tmpLine.lastIndexOf(" ")+" ".length();
					}else{
						indexStart=indexEnd+1;
					}
					string=string.substring(0, indexStart-1+1)
							+lineBr
							+string.substring(indexStart, string.length());
					indexStart+=lineBr.length();
					indexEnd=indexStart;
					//Log.d("aaa", "string: "+string+"length: "+Integer.toString(string.length())+" start: "+Integer.toString(indexStart)+" end: "+Integer.toString(indexEnd));
				}
			}
			List<String> subStrings=new ArrayList<String>();
			for(String retval: string.split(lineBr)){
				subStrings.add(retval);
			}
			int maxSubStringWidth=0;
			if(maxLineWidth>0){
				maxSubStringWidth=maxLineWidth;
			}else{
				for(int i=0; i<subStrings.size(); i++){
					float stringWidth=textPaint.measureText(subStrings.get(i));
					if(stringWidth>maxSubStringWidth){
						maxSubStringWidth=(int) stringWidth;
					}
				}
			}
			bitmap=Bitmap.createBitmap(maxSubStringWidth+2, (textSize+lineSpacing)*subStrings.size()+2, Bitmap.Config.ARGB_4444);
			Canvas canvas=new Canvas(bitmap);
			bitmap.eraseColor(this.bkgColor);
			textPaint.setAntiAlias(true);
			textPaint.setARGB(a, r, g, b);
			int offsetY=0;
			for(int i=0; i<subStrings.size(); i++){
				canvas.drawText(subStrings.get(i), 1, (int)(textSize*0.8)+1+offsetY, textPaint);
				//x左至右，但y上之下！
				//若低至textSize如32，g的下半部显示不出，故从低至0.8倍高开始合适
				offsetY+=textSize+lineSpacing;
			}
		}

		width=srcWidth=bitmap.getWidth();
		height=srcHeight=bitmap.getHeight();
		dstWidth=2;
		dstHeight=2;
		while(dstWidth<srcWidth)
			dstWidth*=2;
		while(dstHeight<srcHeight)
			dstHeight*=2;
		
		while(dstWidth>2048)
			dstWidth=dstWidth/2;
		while(dstHeight>2048)
			dstHeight=dstHeight/2;
		Log.d("aaa", fileName+" width: "+Integer.toString(dstWidth));
		Log.d("aaa", fileName+" height: "+Integer.toString(dstHeight));
		bitmap=Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true);
		//尺寸硬拉到pow2
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);	//bind
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		setFilters(GL10.GL_NEAREST, GL10.GL_NEAREST);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);	//unbind
		
		bitmap.recycle();
		//书上无recycle
		//清RAM。清GPU的见后
	}
	
	public void reload(){
		load();
		bind();
		setFilters(minFilter, magFilter);
		glGraphics.getGL().glBindTexture(GL10.GL_TEXTURE_2D, 0);
	}

	public void setFilters(int minFilter, int magFilter) {
		this.minFilter=minFilter;
		this.magFilter=magFilter;
		GL10 gl=glGraphics.getGL();
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter);
	}
	
	public void bind() {
		GL10 gl=glGraphics.getGL();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
	}
	
	public void dispose(){
		GL10 gl=glGraphics.getGL();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		int[] textureIds={textureId};
		gl.glDeleteTextures(1, textureIds, 0);
		//清GPU
	}
}
