package Java0305;

import java.text.DecimalFormat;
import java.util.LinkedList;

public class SelectMA {
    
	private LinkedList<Double> dequeue = new LinkedList<>();  
    private int size;  
    private double sum;  
  
      
    public SelectMA(int size) {  
        this.size = size;  
    }  
      
    public String next(double days) {  
        
    	if (dequeue.size() == size) sum -= dequeue.removeFirst();  
        dequeue.addLast(days);  
        sum += days;  
        double result = sum / (double)dequeue.size();  
        return new DecimalFormat("#.00").format(result);
        
    }  
}
