package parser;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import zeppelin.MainProgramImpl;

public class TestParser1 {


	public static void main(String[] args){
		String testString = "V:10;A:45;D:100";
		Parser parser = null;
		try {
			parser = new Parser(new MainProgramImpl());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		List<Command> testList =parser.parse(testString);
		 
		
		if(testList.get(0).getType()==CommandType.V){
			System.out.println("type 1 arg ok");
			System.out.println("parameter: " + testList.get(0).getParameter());
		}else{
			System.out.println( "type 1 arg not ok");
		}
		
		if(testList.get(1).getType()==CommandType.A){
			System.out.println("type 2 arg ok");
			System.out.println("parameter: " + testList.get(1).getParameter());
		}else{
			System.out.println( "type 2 arg not ok");
		}
		
		if(testList.get(2).getType()==CommandType.D){
			System.out.println("type 3 arg ok");
			System.out.println("parameter: " + testList.get(2).getParameter());
		}else{
			System.out.println( "type 3 arg not ok");
		}
	}
	
	
}
