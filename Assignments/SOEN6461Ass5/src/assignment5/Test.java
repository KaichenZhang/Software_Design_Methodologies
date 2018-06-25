package assignment5;

public class Test {

    public static void main(String args[]) throws InterruptedException {
        
        System.out.println("Student Name: Kaichen Zhang");
        System.out.println("Student ID  : 40000160");
    	
    	
    	
        int[] var = {1, 2, 3, 4, 5 };
        Context ctxBS = new Context(new BubbleSort());
        ctxBS.arrange(var);
        
        Context ctxQS = new Context(new QuickSort());
        ctxQS.arrange(var);
        
        Context ctxSS = new Context(new SelectionSort());
        ctxSS.arrange(var);
        
        
        
    }

}
