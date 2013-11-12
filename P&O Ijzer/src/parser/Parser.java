package parser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Parser {

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
	
	private Command parseSingleCommand(String commandString){
		 
		   List<String> elementsOfSubStringList = Arrays.asList(commandString.split(":"));
		   if (elementsOfSubStringList.size() !=2){
			   throw new IllegalSyntaxException();
		   }
		   String possibleCommandTypeString = elementsOfSubStringList.get(0);
		   CommandType tempType = this.parseStringToCommandType(possibleCommandTypeString);
		 
		   String possibleParameterString =elementsOfSubStringList.get(1);
		   Double tempDouble = this.parseStringToParameter(possibleParameterString);
		   Command tempCommand = new Command(tempType,tempDouble);
		   return tempCommand;
	}

	private CommandType parseStringToCommandType(String possibleCommandTypeString) throws IllegalSyntaxException{
		  for(CommandType tempCom:CommandType.values()){
			   if(tempCom.getCommandAbbrevString().equals(possibleCommandTypeString)){
				   return tempCom;
			   }
		   }
		  throw new IllegalSyntaxException(); 
	}
	private Double parseStringToParameter(String possibleParameterString) throws NumberFormatException{
			Double value = Double.parseDouble(possibleParameterString);
			return value;
	}
	
	
	
}
