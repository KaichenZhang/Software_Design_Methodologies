package Java0305;

import java.util.ArrayList;
import java.util.Scanner;

public class MovingAverageDiaplay {
	
	ArrayList<String[]> data = new ArrayList<String[]>();
	ArrayList<String[]> result = new ArrayList<String[]>();
	
	public ArrayList<String[]> showAverage(int windowSize){
		
		SelectMA[] m = new SelectMA[6];
		for(int i = 0; i < 6; i++){
			m[i] = new SelectMA(windowSize);
		}
		for(String[] st : data){
			String[] re = new String[7];
			re[0] = st[0];
			for(int i = 0; i < 6; i++){
				re[i + 1] = m[i].next(Double.parseDouble(st[i + 1]));
				
			}
			result.add(re);
		}
		
		return result;
		
	}
	
	public int selectWindowSize(){
		
		Scanner reader = new Scanner(System.in);
		
		int win = Integer.parseInt(reader.nextLine());
		
		reader.close();
		return win;
	}
	

}
