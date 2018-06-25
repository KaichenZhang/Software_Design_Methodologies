package assignment4;

public class Dealer {
	public static void main(String[] args) {

		// separately creating parts which are leaf nodes
		Part engine = new Part("Engine");
		Part wheels = new Part("Wheels");
		Part doors = new Part("doors");

		// Creating a composite object i.e. Car which can have multiple
		// accessories i.e. it is composed of multiples objects
		Car car = new Car("Honda");
		car.addPart(engine);
		car.addPart(wheels);

		// Provide output:
		// Your name:
		//  Your student-id 
		System.out.println("Name:       Kaichen Zhang");
		System.out.println("Student-ID: 40000160");
		

		/// we are performing operation on leaf.
		engine.printDetails();

		// performing operation on composite object
		car.printDetails();
	}
}
