package spare.funpig;

import java.util.ArrayList;
import java.util.List;

import spare.funlibrary.framework.Music2;
import spare.funlibrary.framework.Sound2;
import spare.funlibrary.framework.gl.Animation2;
import spare.funlibrary.framework.gl.Font;
import spare.funlibrary.framework.gl.Texture2;
import spare.funlibrary.framework.gl.TextureRegion2;
import spare.funlibrary.framework.impl.GLGame2;
import android.graphics.Color;
import android.util.Log;

public class Assets {
	public static Texture2 bkg;
	public static TextureRegion2 bkgRegion;
	public static Texture2 ui01;
	public static TextureRegion2 ui01BtnPauseRegion;
	public static TextureRegion2 ui01BtnResumeRegion;
	public static Texture2 ui02;
	public static TextureRegion2 ui02FingerRegion;
	public static Texture2 ui03;
	public static TextureRegion2 ui03Region;
	
	public static Texture2 pigBody01;
	public static Animation2 pigBody01AnimCrawl;
	public static Texture2 pigBody02;
	public static Animation2 pigBody02AnimLookUp;
	public static Animation2 pigBody02AnimEat;
	public static Texture2 pigBody03;
	public static Animation2 pigBody03AnimJump;
	public static Animation2 pigBody03AnimFall;
	public static Animation2 pigBody03AnimMiss;
	public static Animation2 pigBody03AnimCatch;
	public static Texture2 pigBody04;
	public static Animation2 pigBody04AnimHello;
	public static Texture2 pigHead01;
	public static TextureRegion2 pigHead01Region;
	public static Texture2 pigHead02;
	public static Animation2 pigHead02AnimCatch;
	public static Texture2 pigArm01;
	public static Animation2 pigArm01AnimWave;
	public static Texture2 pigLeg01;
	public static Animation2 pigLeg01AnimCrawl;
	public static Texture2 bird;
	public static TextureRegion2 birdRegion;
	
	public static Music2 lastBgm;
	public static Music2 activeBgm;
	
	public static Music2 bgm01;
	public static Sound2 oink_02;
	public static Sound2 oink_01;
	public static Sound2 chirp;

	public static Font font;
	
	public static List<Texture2> tipTexts=new ArrayList<Texture2>();
	public static List<TextureRegion2> tipTextRegions=new ArrayList<TextureRegion2>();
	public static Texture2 tmpText;
	public static TextureRegion2 tmpTextRegion;
	
	public static void load(GLGame2 game){
		Log.d("aaa", "start loading assets!");
		tipTexts.clear();
		tipTextRegions.clear();
		tipTexts.add(new Texture2(game, "Hi!", 32, 32*4, 0, Color.TRANSPARENT, 0.1f,0.1f,0.1f,1));
		tipTextRegions.add(new TextureRegion2(tipTexts.get(tipTexts.size()-1), 0, 0, tipTexts.get(tipTexts.size()-1).width, tipTexts.get(tipTexts.size()-1).height));
		tipTexts.add(new Texture2(game, "Hug!", 32, 32*4, 0, Color.TRANSPARENT, 0.1f,0.1f,0.1f,1));
		tipTextRegions.add(new TextureRegion2(tipTexts.get(tipTexts.size()-1), 0, 0, tipTexts.get(tipTexts.size()-1).width, tipTexts.get(tipTexts.size()-1).height));
		tipTexts.add(new Texture2(game, "Friendship:", 32, 0, 0, Color.TRANSPARENT, 0.9f,0.9f,0.6f,1));
		tipTextRegions.add(new TextureRegion2(tipTexts.get(tipTexts.size()-1), 0, 0, tipTexts.get(tipTexts.size()-1).width, tipTexts.get(tipTexts.size()-1).height));
		tmpText=new Texture2(game, "0", 32, 0, 0, Color.TRANSPARENT, 0.9f,0.9f,0.6f,1);
		tmpTextRegion=new TextureRegion2(tmpText, 0, 0, tmpText.width, tmpText.height);
		
		bkg=new Texture2(game, "sty_01.jpg");
		bkgRegion=new TextureRegion2(bkg,0,0,bkg.width,bkg.height);
		
		ui01=new Texture2(game, "ui_01.png");
		ui01BtnPauseRegion=new TextureRegion2(ui01, 2*80*1, 0, 2*80, ui01.height);
		ui01BtnResumeRegion=new TextureRegion2(ui01, 2*80*0, 0, 2*80, ui01.height);
		ui02=new Texture2(game, "ui_02.png");
		ui02FingerRegion=new TextureRegion2(ui02, 0, 0, ui02.width, ui02.height);
		ui03=new Texture2(game, "ui_03.png");
		ui03Region=new TextureRegion2(ui03, 0, 0, ui03.width, ui03.height);
		
		pigBody01=new Texture2(game, "pig_body_01.png");
		pigBody01AnimCrawl=new Animation2(0.2f, 
				new TextureRegion2(pigBody01, 5*80*0, 0, 5*80, pigBody01.height),
				new TextureRegion2(pigBody01, 5*80*1, 0, 5*80, pigBody01.height),
				new TextureRegion2(pigBody01, 5*80*2, 0, 5*80, pigBody01.height),
				new TextureRegion2(pigBody01, 5*80*2, 0, 5*80, pigBody01.height),
				new TextureRegion2(pigBody01, 5*80*2, 0, 5*80, pigBody01.height),
				new TextureRegion2(pigBody01, 5*80*3, 0, 5*80, pigBody01.height),
				new TextureRegion2(pigBody01, 5*80*4, 0, 5*80, pigBody01.height),
				new TextureRegion2(pigBody01, 5*80*5, 0, 5*80, pigBody01.height)
		);
		pigBody02=new Texture2(game, "pig_body_02.png");
		pigBody02AnimLookUp=new Animation2(0.1f, 
				new TextureRegion2(pigBody02, 5*80*0, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*1, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*1, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*1, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*1, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*1, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*1, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*0, 0, 5*80, pigBody02.height)
				);
		pigBody02AnimEat=new Animation2(0.1f, 
				new TextureRegion2(pigBody02, 5*80*2, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*3, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*4, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*5, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*6, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*7, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*8, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*9, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*10, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*11, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*12, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*13, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*1, 0, 5*80, pigBody02.height),
				new TextureRegion2(pigBody02, 5*80*0, 0, 5*80, pigBody02.height)
		);
		pigBody03=new Texture2(game, "pig_body_03.png");
		pigBody03AnimJump=new Animation2(0.1f, 
				new TextureRegion2(pigBody03, 5*80*0, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*1, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*2, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*3, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*4, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*5, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*6, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*7, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*6, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*5, 0, 5*80, pigBody03.height)
		);
		pigBody03AnimFall=new Animation2(0.1f, 
				new TextureRegion2(pigBody03, 5*80*8, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*5, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*4, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*5, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*4, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*5, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*4, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*3, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*2, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*1, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*0, 0, 5*80, pigBody03.height)
				);
		pigBody03=new Texture2(game, "pig_body_03.png");
		pigBody03AnimMiss=new Animation2(0.03f, 
				new TextureRegion2(pigBody03, 5*80*8, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*9, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*10, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*11, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*12, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*13, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*13, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*13, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*12, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*11, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*10, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*9, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*8, 0, 5*80, pigBody03.height)
				);
		pigBody03AnimCatch=new Animation2(0.05f, 
				new TextureRegion2(pigBody03, 5*80*8, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*9, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*10, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*11, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*12, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*12, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*12, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*12, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*12, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*12, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*12, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*12, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*12, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*11, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*10, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*9, 0, 5*80, pigBody03.height),
				new TextureRegion2(pigBody03, 5*80*8, 0, 5*80, pigBody03.height)
				);
		pigBody04=new Texture2(game, "pig_body_04.png");
		pigBody04AnimHello=new Animation2(0.1f, 
				new TextureRegion2(pigBody04, 6*80*0, 0, 6*80, pigBody04.height),
				new TextureRegion2(pigBody04, 6*80*1, 0, 6*80, pigBody04.height),
				new TextureRegion2(pigBody04, 6*80*2, 0, 6*80, pigBody04.height),
				new TextureRegion2(pigBody04, 6*80*3, 0, 6*80, pigBody04.height),
				new TextureRegion2(pigBody04, 6*80*3, 0, 6*80, pigBody04.height),
				new TextureRegion2(pigBody04, 6*80*3, 0, 6*80, pigBody04.height),
				new TextureRegion2(pigBody04, 6*80*3, 0, 6*80, pigBody04.height),
				new TextureRegion2(pigBody04, 6*80*3, 0, 6*80, pigBody04.height),
				new TextureRegion2(pigBody04, 6*80*2, 0, 6*80, pigBody04.height),
				new TextureRegion2(pigBody04, 6*80*1, 0, 6*80, pigBody04.height),
				new TextureRegion2(pigBody04, 6*80*0, 0, 6*80, pigBody04.height)
				);
		pigArm01=new Texture2(game, "pig_arm_01.png");
		pigArm01AnimWave=new Animation2(0.1f, 
				new TextureRegion2(pigArm01, 0, 0, 0, 0),
				new TextureRegion2(pigArm01, 0, 0, 0, 0),
				new TextureRegion2(pigArm01, 0, 0, 0, 0),
				new TextureRegion2(pigArm01, 2*80*0, 0, 2*80, pigArm01.height),
				new TextureRegion2(pigArm01, 2*80*1, 0, 2*80, pigArm01.height),
				new TextureRegion2(pigArm01, 2*80*2, 0, 2*80, pigArm01.height),
				new TextureRegion2(pigArm01, 2*80*1, 0, 2*80, pigArm01.height),
				new TextureRegion2(pigArm01, 2*80*0, 0, 2*80, pigArm01.height),
				new TextureRegion2(pigArm01, 0, 0, 0, 0),
				new TextureRegion2(pigArm01, 0, 0, 0, 0),
				new TextureRegion2(pigArm01, 0, 0, 0, 0)
				);
		pigHead01=new Texture2(game, "pig_head_01.png");
		pigHead01Region=new TextureRegion2(pigHead01, 0, 0, pigHead01.width, pigHead01.height);
		pigHead02=new Texture2(game, "pig_head_02.png");
		pigHead02AnimCatch=new Animation2(0.1f, 
				new TextureRegion2(pigHead02, 3*80*0, 0, 3*80, pigHead02.height),
				new TextureRegion2(pigHead02, 3*80*1, 0, 3*80, pigHead02.height),
				new TextureRegion2(pigHead02, 3*80*2, 0, 3*80, pigHead02.height),
				new TextureRegion2(pigHead02, 3*80*3, 0, 3*80, pigHead02.height),
				new TextureRegion2(pigHead02, 3*80*4, 0, 3*80, pigHead02.height)
		);
		pigLeg01=new Texture2(game, "pig_leg_01.png");
		pigLeg01AnimCrawl=new Animation2(0.2f, 
				new TextureRegion2(pigLeg01, 5*80*0, 0, 5*80, pigLeg01.height),
				new TextureRegion2(pigLeg01, 5*80*1, 0, 5*80, pigLeg01.height),
				new TextureRegion2(pigLeg01, 5*80*2, 0, 5*80, pigLeg01.height),
				new TextureRegion2(pigLeg01, 5*80*3, 0, 5*80, pigLeg01.height),
				new TextureRegion2(pigLeg01, 5*80*4, 0, 5*80, pigLeg01.height),
				new TextureRegion2(pigLeg01, 5*80*5, 0, 5*80, pigLeg01.height),
				new TextureRegion2(pigLeg01, 5*80*6, 0, 5*80, pigLeg01.height),
				new TextureRegion2(pigLeg01, 5*80*6, 0, 5*80, pigLeg01.height)
		);
		bird=new Texture2(game, "bird.png");
		birdRegion=new TextureRegion2(bird, 0, 0, 3*80, bird.height);
		
		bgm01=game.getAudio().newMusic("audio/bgm_01.mid");
		bgm01.setLooping(true);
		bgm01.setVolume(0.5f);
		bgm01.setBpm(135);
		
		oink_01=game.getAudio().newSound("audio/oink_01.ogg");
		oink_02=game.getAudio().newSound("audio/oink_02.ogg");
		chirp=game.getAudio().newSound("audio/chirp.ogg");
	}
	
	public static void reload(){
		tmpText.reload();
		for(int i=0; i<tipTexts.size(); i++){
			tipTexts.get(i).reload();
		}
		bkg.reload();
		ui01.reload();
		ui02.reload();
		ui03.reload();
		pigBody01.reload();
		pigBody02.reload();
		pigBody03.reload();
		pigBody04.reload();
		pigArm01.reload();
		pigHead01.reload();
		pigLeg01.reload();
		bird.reload();
	}
}
