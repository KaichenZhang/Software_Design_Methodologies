package assignment4;

import java.util.ArrayList;
import java.util.List;

public class Car implements Component{

	private String string;
	
	public List<Component> parts = new ArrayList<Component>();
	
	public Car(String name) {
		this.string = name;
	}

	public void printDetails(){
		System.out.println(string);
		for(Component element:parts){
			System.out.print("  ");
			element.printDetails();
			
		}
	}
	
	public void addPart(Part part){
		this.parts.add(part);
	}
}
