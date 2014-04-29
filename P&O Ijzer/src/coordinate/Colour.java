package coordinate;

import java.awt.Color;


public enum Colour {
	RED("red",'R', Color.red), 
	YELLOW("yellow",'Y', Color.yellow), 
	WHITE("white",'W', Color.white), 
	BLUE("blue",'B', Color.blue), 
	GREEN("green",'G', Color.green),
	BLACK("black", 'Z', Color.black),
	UNDETERMINED("undetermined", 'U', Color.pink);
	
	private final String name;
	private final char abbreviation;
	private final Color color;
	
	Colour(String name, char abbreviation, Color color){
	this.name = name;	
	this.abbreviation = abbreviation;
	this.color = color;
	}
	
	public String nameOfColor() { return name; }
	public char abbreviation() { return abbreviation; }
	public Color AWTColor(){return color;}
	
	
	
  }


