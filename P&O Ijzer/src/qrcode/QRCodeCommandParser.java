package qrcode;

import zeppelin.MainProgramImpl;
import coordinate.GridPoint;
import coordinate.Tablet;

public class QRCodeCommandParser {
	
	public QRCodeCommandParser(MainProgramImpl zeppelin){
		setZeppelin(zeppelin);
	}
	private MainProgramImpl zeppelin;

	public MainProgramImpl getZeppelin() {
		return zeppelin;
	}
	public void setZeppelin(MainProgramImpl zeppelin) {
		this.zeppelin = zeppelin;
	}
	
	public final String commandDelimiter = ":";
	
	public void parse(String command){
		String[] commandTokens = command.split(commandDelimiter);
		String commandType = commandTokens[0];
		
		if(commandType.equals("tablet")){
			if(commandTokens.length != 2){
				throw new IllegalArgumentException("Het meegegeven commando, beginnend met 'tablet', bevat meer dan 1 ':'");
			}
			String tabletId = commandTokens[1];
			if(!tabletId.matches("\\d+")){
				throw new IllegalArgumentException("achter 'tablet:' moet een getal staan");	
			}
			int tabletIdInt = Integer.parseInt(tabletId);
			Tablet destinationTablet = this.getZeppelin().getGrid().getTabletWithTabletId(tabletIdInt);
			if(destinationTablet.equals(null)){
				throw new IllegalArgumentException("De tablet in het commando komt niet overeen met een gekende tablet");
			}
			this.getZeppelin().setDestinationTab(destinationTablet);
		}
		if(commandType.equals("position")){
			if(commandTokens.length != 2){
				throw new IllegalArgumentException("Het meegegeven commando, beginnend met 'position', bevat meer dan 1 ':'");
			}
			String[] coordinates = commandTokens[1].split(",");
			if(coordinates.length != 2){
				throw new IllegalArgumentException("Het meegegeven commando, beginnend met 'position', bevat meer dan 1 ','");
			}
			String xCoord = coordinates[0];
			String yCoord = coordinates[1];			
			if(!xCoord.matches("\\d+") || !yCoord.matches("\\d+")){
				throw new IllegalArgumentException("De meegegeven x- of y-coordinaat komt niet overeen met een getal");
			}
			this.getZeppelin().setTargetPosition(new GridPoint(Double.parseDouble(coordinates[0])/100, Double.parseDouble(coordinates[1])/100));
		}	
	}
}
