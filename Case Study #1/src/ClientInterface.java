import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;
import org.jfree.ui.RefineryUtilities;




public class ClientInterface {

	public void interfaceContol(){
		System.out.println("***********  choose options ***********");
		System.out.println("1. Stock Market Price");
		System.out.println("2. Moving Average Analysis");
		System.out.println("3. Exit");

		
		Scanner input = new Scanner(System.in);
		int userInput = 0;
		Boolean valid = false;
       
		while(!valid){
           
        	try {
            	userInput = input.nextInt();
                valid = true;
         
        	}
            catch(Exception e) {
                System.out.println("Invalid Input, please enter an Integer");
                valid = false;
                input.nextLine();
        
            }
       
		}
		
		
        valid = false;
        
        switch(userInput) {
        
        case 1: 
    		System.out.println("Please enter time period: from 19620102  to  20161004");
    		while(!valid){
    			try {
    				System.out.println("Please enter start date with format [YYYYMMDD]");
    				int start = input.nextInt();
    				System.out.println("Please enter end date with format [YYYYMMDD]");
    				int end = input.nextInt();
    				final Engine demo = new Engine("Selected date time period price chart",1, 1,start,end);
    		        demo.pack();
    		        RefineryUtilities.centerFrameOnScreen(demo);
    		        demo.setVisible(true);
    		        break;
    			} catch(Exception e) {
    				System.out.println("Invalid Input, please enter an Integer");
                    valid = false;
                    input.nextLine();
    			}
    		}
    		
    		new ClientInterface().interfaceContol();
    		
        	break;
        	
        case 2:
        	
    		System.out.println("Please enter [MA range] and [time period]: from 19620102  to  20161004");
    		while(!valid){
    			try {
    				System.out.println("Please enter MA range");
    				int range = input.nextInt();
    				System.out.println("Please enter start date with format [YYYYMMDD]");
    				int start = input.nextInt();
    				System.out.println("Please enter end date with format [YYYYMMDD]");
    				int end = input.nextInt();
    				final Engine demo = new Engine("Selected time period MA chart",2, range,start,end);
    		        demo.pack();
    		        RefineryUtilities.centerFrameOnScreen(demo);
    		        demo.setVisible(true);
    		        break;
    			} catch(Exception e) {
    				System.out.println("Invalid Input, please enter an Integer");
                    valid = false;
                    input.nextLine();
    			}
    		}
    		
    		new ClientInterface().interfaceContol();
        	break;
        	

        case 3:
        	System.out.println("Have a nice day!");
        	System.exit(0);	        	
        }
	}
	

    
	public static void main(String[] args) throws IOException, NumberFormatException, ParseException{
		while(true){
			new ClientInterface().interfaceContol();
			break;
		}	
	}
	
}
