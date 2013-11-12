package parser;

import java.util.ArrayList;
import java.util.List;

public class TestParser1 {


	public static void main(String[] args){
		String testString = "V:10;A:45;D:100";
		Parser parser = new Parser();
		List<Command> testList =parser.parse(testString);
		 
		
		if(testList.get(0).getType()==CommandType.V){
			System.out.println("type 1 arg ok");
		}else{
			System.out.println( "type 1 arg not ok");
		}
		
		if(testList.get(1).getType()==CommandType.A){
			System.out.println("type 2 arg ok");
		}else{
			System.out.println( "type 2 arg not ok");
		}
		
		if(testList.get(2).getType()==CommandType.D){
			System.out.println("type 3 arg ok");
		}else{
			System.out.println( "type 3 arg not ok");
		}
	}
	
	
}
