package RabbitMQ;

import java.util.LinkedHashSet;
import java.util.Set;

public class MessageParser {

	
//routingKeyParser
	public String routingKeyDelimiter =".";
	
	public void parseRoutingKey(String routingKey){
		String[] routingKeyTokens = routingKey.split(routingKeyDelimiter);
		String temp = routingKeyTokens[0];   
		if(!groupNames.contains(temp)){
		throw new InvalidBindingKeyNameException("De binding key bevat een onbestaande naam van een zeppelin");
		
		
	    }
	    	
	    	
	    	
	}
	
	private static final Set<String> groupNames= new LinkedHashSet<String>();
	private static final Set<String> commandTypes= new LinkedHashSet<String>();;
	static{
		
		groupNames.add("paars");
		groupNames.add("blauw");
		groupNames.add("rood");
		groupNames.add("groen");
		groupNames.add("wit");
		groupNames.add("geel");
		groupNames.add("indigo");
		groupNames.add("brons");
		groupNames.add("zilver");
		groupNames.add("goud");
		groupNames.add("koper");
		groupNames.add("platinum");
		groupNames.add("ijzer");
		
		commandTypes.add("lcommand");
		commandTypes.add("hcommand");
		commandTypes.add("private");
		commandTypes.add("info");
		
	}
	
	
}
