package parser;

public enum CommandType {
V("V"),
A("A"),
S("S"),
D("D"),
L("L"),
R("R");

public String getCommandAbbrevString() {
	return commandAbbrevString;
}

private final String commandAbbrevString;

CommandType(String commandAbbrevString){
	this.commandAbbrevString=commandAbbrevString;
}

}
