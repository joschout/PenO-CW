/**
 * Opsomming van alle mogelijke types van instructies.
 */

package parser;

public enum CommandType {
V("V"),
A("A"),
S("S"),
D("D"),
L("L"),
R("R"),
N("N");

public String getCommandAbbrevString() {
	return commandAbbrevString;
}

private final String commandAbbrevString;

CommandType(String commandAbbrevString){
	this.commandAbbrevString=commandAbbrevString;
}

}
