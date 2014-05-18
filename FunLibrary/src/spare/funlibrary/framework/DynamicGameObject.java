package spare.funlibrary.framework;

import spare.funlibrary.framework.math.Vector2;

public class DynamicGameObject extends GameObject{
	public final Vector2 velocity;
	public final Vector2 accel;
	//怎可能final?
	//final修饰的引用型数据(包括数组)不变指的是它的栈内存地址不变
	//即永远指向那个堆里的对象，但是堆里的对象(属性)是可变的！
	
	public DynamicGameObject(float x, float y, float width, float height){
		super(x, y, width, height);
		velocity=new Vector2();
		accel=new Vector2();
	}
}
