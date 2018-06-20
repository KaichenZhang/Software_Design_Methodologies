package Java0305;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadCSV {
	
	ArrayList<String[]> data = new ArrayList<String[]>();
	HashMap<String,Integer> date = new HashMap<String,Integer>();
	
	
	public ArrayList<String[]> readData(String filePath) throws IOException {
       
		File file = new File(filePath);
        
        BufferedReader input = new BufferedReader(new FileReader(file));
        String s;
        String[] temp;
        int seq = 0;
        input.readLine();
        while ((s = input.readLine()) != null) {
        	
        	if(s.length() <= 9){
        		s += input.readLine();
        	}
        	temp = s.split(",");
            data.add(temp);
            date.put(temp[0],seq);
            seq++;
            
        }
        input.close();
 
        return data;
    }

}
