package parser;
import java.util.ArrayList;


public class Command {

	public CommandType getType() {
		return type;
	}

	public void setType(CommandType type) {
		this.type = type;
	}

	public Double getParameter() {
		return parameter;
	}

	public void setParameter(Double parameter) {
		this.parameter = parameter;
	}

	public CommandType type;
	public Double parameter;
	
	public Command(CommandType type, double parameter){
		this.type=type;
		this.parameter=parameter;
	} 
	
	
	
}
