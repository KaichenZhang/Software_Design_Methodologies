package Java0305;

import java.util.ArrayList;

public class SelectDate {

	ArrayList<String[]> newData = new ArrayList<String[]>();

	public void select(String start, String end, ReadCSV read) {

		int s = read.date.get(start);
		int e = read.date.get(end);
		for (int i = s; i >= e; i--) {
			String temp[] = read.data.get(i);
			newData.add(temp);
		}

	}
	
	public String getAdvice() {
		
		String returnString = " ";
    	
    	String[] after = newData.get(newData.size() - 1);
    	String[] before = newData.get(newData.size() - 2);
    	if(Double.parseDouble(after[5]) > Double.parseDouble(before[5])){
    		returnString = "You Can Buy It!";	
    	} else {
    		returnString = "You Need to Sell It!";
    	}
    	
    	return returnString;
		
	}

}
