package caseStudy2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Stock {
	
	static String[][] stockName = {{"3M","MMM"},{"American Express","AXP"},{"Apple","AAPL"},{"Boeing","BA"},{"Caterpillar","CAT"},
			{"Chevron","CVX"},{"Cisco Systems","CSCO"},{"Coca-Cola","KO"},{"DuPont","DD"},{"ExxonMobil","XOM"},
			{"General Electric","GE"},{"Goldman Sachs","GS"},{"The Home Depot","HD"},{"IBM","IBM"},{"Intel","INTC"},
			{"Johnson Johnson","JNJ"},{"JPMorgan Chase","JPM"},{"McDonalds","MCD"},{"Merck","MRK"},{"Microsoft","MSFT"},
			{"Nike","NKE"},{"Pfizer","PFE"},{"Procter Gamble","PG"},{"Travelers","TRV"},{"UnitedHealth Group","UNH"},
			{"United Technologies","UTX"},{"Verizon","VZ"},{"Visa","V"},{"Wal-Mart","WMT"},{"Walt Disney","DIS"}};

	String[] getStockName() {
		String[] name = new String[30];
		for(int i = 0; i < 30; i++) {
			name[i] = stockName[i][0];
		}
		return name;
	}
	
	String getSymbol(String name) {	
		String sym = "";
		for(int i = 0; i < 30; i++) {
			if(name.equals(stockName[i][0])) {
				sym = stockName[i][1];
				break;
			}
		}	
		return sym;	
	}
	
	

	void download(String stock, int YS, int MS, int DS, int YE, int ME, int DE) {

		String stringUrl = "http://chart.finance.yahoo.com/table.csv?s=" + getSymbol(stock) + "&a=" + (MS - 1) + "&b=" + DS + "&c=" + YS + "&d=" + (ME - 1) + 
				"&e=" + DE + "&f=" + YE + "&g=d&ignore=.csv";

		try {
			URL url = new URL(stringUrl);
			
			Scanner in = new Scanner(url.openStream());
			PrintWriter out = new PrintWriter(stock + ".csv");
			
			while(in.hasNext()) {
				out.println(in.nextLine());
			}
			
			in.close();
			out.close();
			
		} 
		catch (MalformedURLException e) {
			System.out.println("Invalid URL!");
		} 
		catch (IOException e) {
			System.out.println("Local Data" + stock + " is not exist!");
		}
	}

	String addToWatch(String stock) {
		
		try {
			
			FileWriter out = new FileWriter("Watch List.txt", true); 

			out.write(stock + "\r\n");
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.println("Watch-List is not exist!");
		} 
		return changeShowArea();
	}
	
	
	String clearAll() {
		
		File file = new File("Watch List.txt");
		
		if(file.exists()) {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Watch-List is not exist!");
			}
		}
		return changeShowArea();
	}
	
	String changeShowArea() {
		
		File file = new File("Watch List.txt");
		String show = "";
		
		try {
			Scanner in = new Scanner(file);
			
			while(in.hasNextLine()) {
				show = show + in.nextLine() + "\r\n";
			}
			
			in.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Watch-List is not exist!");
		}
		return show;	
	}
	
	void startProgram() {
		
		Chart chart = new Chart();
		ArrayList<String> list = new ArrayList<String>();
		
		File file = new File("Watch List.txt");
		
		try {
			Scanner in = new Scanner(file);
			
			while(in.hasNextLine()) {
				list.add(in.nextLine());
			}
			
			if(!list.isEmpty()) {
				for(String i : list) {
					download(i, 2016, 4, 3, 2017, 4, 3);
				}
			}
			
			in.close();
			chart.START(list, 20);
			chart.START(list, 100);
			
		} catch (FileNotFoundException e) {
			System.out.println("Watch List.txt");
		}
		
	}

}
