package assignment4;

public class Part implements Component  {

	private String string;


	public Part(String string) {
		this.string = string;
	}

	public void printDetails(){
		System.out.print(string+"\r\n");
	}
}
