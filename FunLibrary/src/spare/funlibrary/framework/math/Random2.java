package spare.funlibrary.framework.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Random2 {
	public static final Random rand=new Random();
	public static List<Integer> newIntList(int size, HashMap<Integer, Float> hashmap){
		int mapSize=hashmap.size();
		float sum=0;
		List<Integer> keyList=new ArrayList<Integer>(hashmap.keySet());
		List<Float> oddList=new ArrayList<Float>(hashmap.values());
		for(int i=0; i<mapSize; i++){
			sum+=oddList.get(i);
			//不是get(0)！！
		}
		for(int i=0; i<mapSize; i++){
			oddList.set(i, oddList.get(i)/sum);
		}
		List<Float> oddSumList=new ArrayList<Float>();
		float oddSum=0;
		for(int i=0; i<mapSize; i++){
			oddSum+=oddList.get(i);
			oddSumList.add(oddSum);
		}
		List<Integer> list= new ArrayList<Integer>();
		for(int i=0; i<size; i++){
			float randOddSum=rand.nextFloat();
			int index=0;
			while (randOddSum>oddSumList.get(index)) {
				index++;
				if(index==mapSize-1){
					break;
				}
			}
			list.add(keyList.get(index));
		}
		return list;
	}
}
