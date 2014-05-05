package RabbitMQ;

import java.rmi.RemoteException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.management.openmbean.InvalidKeyException;

import simulator.Simulator;
import coordinate.GridPoint;
import zeppelin.MainProgramImpl;

public class SimulatorMessageParser {


	public SimulatorMessageParser(Simulator simulator){
		setSimulator(simulator);
	}
	
	private Simulator simulator;

	public Simulator getSimulator() {
		return simulator;
	}
	public void setSimulator(Simulator simulator) {
		this.simulator = simulator;
	}
	//routingKeyParser
	public String routingKeyDelimiter = "\\.";

	public void parse(String routingKey, String message) throws InvalidBindingKeyException, RemoteException{
		String[] routingKeyTokens = routingKey.split(routingKeyDelimiter);
		String zeppelinName = routingKeyTokens[0];   

		if(!groupNames.contains(zeppelinName)){
			throw new InvalidBindingKeyException("De binding key bevat een onbestaande naam van een zeppelin");
		}
//geldige naam niet gelijk aan staal
		if(! zeppelinName.equalsIgnoreCase(this.getSimulator().getName())){
			if(! simulator.getOtherKnownZeppelins().containsKey(zeppelinName)){
				simulator.addOtherKnownZeppelin(zeppelinName);
			}
			String commandName = routingKeyTokens[1];   
			if(!commandTypes.contains(commandName)){
				throw new InvalidBindingKeyException("De binding key bevat een onbestaand commando");
			}
			if(commandName.equals("info")){
				String infoType = routingKeyTokens[2];   
				if(infoType.equals("location")){
					message = message.replaceAll("\\s+", "");
					if(message.matches("\\d+,\\d+")){
						String[] coordinates = message.split(",");
					simulator.getOtherKnownZeppelins().get(zeppelinName).setPosition(new GridPoint( Double.parseDouble(coordinates[0])/100, Double.parseDouble(coordinates[1])/100));
					}
				}
				if (infoType.equals("height")){
					message = message.replaceAll("\\s+", "");
					if(message.matches("\\d+")){
					double height = Double.parseDouble(message)/100;
					simulator.getOtherKnownZeppelins().get(zeppelinName).setHeight(height);
					}
				}
			}if(commandName.equals("hcommand")){
				System.out.println("Commando hcommand bedoeld voor zeppelin "+ zeppelinName );
			}if(commandName.equals("lcommand")){
				System.out.println("Commando lcommand bedoeld voor zeppelin "+ zeppelinName );
			}if(commandName.equals("private")){
				System.out.println("Commando private bedoeld voor zeppelin "+zeppelinName);
			}
		}
//geldige naam gelijk aan ijzer
		if(zeppelinName.equalsIgnoreCase(this.getSimulator().getName())){
			String commandName = routingKeyTokens[1];   
			if(!commandTypes.contains(commandName)){
				throw new InvalidBindingKeyException("De binding key bevat een onbestaand commando");
			}
			if(commandName.equals("info")){
				System.out.println("Eigen info ontvangen");
			}if(commandName.equals("hcommand")){
				String hCommandType =  routingKeyTokens[2];   
				if(hCommandType.equals("move")){
					message = message.replaceAll("\\s+", "");
					if(message.matches("\\d+,\\d+")){
					String[] coordinates = message.split(",");
					simulator.setTargetPosition(new GridPoint( Double.parseDouble(coordinates[0])/100, Double.parseDouble(coordinates[1])/100));
					}
				}if(hCommandType.equals("elevate")){
					message = message.replaceAll("\\s+", "");
					if(message.matches("\\d+")){
					double height = Double.parseDouble(message)/100;
					simulator.setTargetHeight(height);
					}
				}
			}
			if(commandName.equals("lcommand")){
				System.out.println("lcommand's worden niet ondersteunt" );
			}
			if(commandName.equals("private")){
				
			}
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
		groupNames.add("staal");

		commandTypes.add("lcommand");
		commandTypes.add("hcommand");
		commandTypes.add("private");
		commandTypes.add("info");

	}


}
