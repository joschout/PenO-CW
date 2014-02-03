package parser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import zeppelin.MainProgramImpl;

public class Parser {
	
	private MainProgramImpl zeppelin;
	
	public Parser(MainProgramImpl zeppelin) {
		this.zeppelin = zeppelin;
	}

	/**
	 * Parse een set van instructies.
	 * @return Een lijst van uitvoerbare commando's.
	 */
	public List<Command> parse(String commandString)throws IllegalSyntaxException{
		 List<Command> commandList = new ArrayList<Command>();   
		
		 
		  commandString = commandString.replaceAll(" ","");
		 
		 // splitsen de inputstring in stukjes, gespitst door ;
		   List<String> possibeCommandStringList = Arrays.asList(commandString.split(";"));
		   for(String singleCommandString:possibeCommandStringList){
			   Command tempCommand = parseSingleCommand(singleCommandString);
			   commandList.add(tempCommand);
		   }
		   return commandList;

		  
		   
	}
	
	/**
	 * Parse één enkele instructie.
	 * @param commandString
	 * @return
	 */
	private Command parseSingleCommand(String commandString){
		 
		   List<String> elementsOfSubStringList = Arrays.asList(commandString.split(":"));
		   if (elementsOfSubStringList.size() !=2){
			   throw new IllegalSyntaxException();
		   }
		   String possibleCommandTypeString = elementsOfSubStringList.get(0);
		   CommandType tempType = this.parseStringToCommandType(possibleCommandTypeString);
		 
		   String possibleParameterString =elementsOfSubStringList.get(1);
		   double tempDouble = this.parseStringToParameter(possibleParameterString);
		   Command tempCommand = new Command(tempType,tempDouble,this.zeppelin);
		   return tempCommand;
	}

	/**
	 * Leidt het type van commando af dat door de gegeven instructie voorgesteld wordt.
	 * @param possibleCommandTypeString
	 * @return
	 * @throws IllegalSyntaxException
	 */
	private CommandType parseStringToCommandType(String possibleCommandTypeString) throws IllegalSyntaxException{
		  for(CommandType tempCom:CommandType.values()){
			   if(tempCom.getCommandAbbrevString().equals(possibleCommandTypeString)){
				   return tempCom;
			   }
		   }
		  throw new IllegalSyntaxException(); 
	}
	
	/**
	 * Leidt de parameter af die hoort bij de gegeven instructie.
	 * @param possibleParameterString
	 * @return
	 * @throws NumberFormatException
	 */
	private double parseStringToParameter(String possibleParameterString) throws NumberFormatException{
			double value = Double.parseDouble(possibleParameterString);
			return value;
	}
	
	
	
}
