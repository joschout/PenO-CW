package RabbitMQ;

import java.rmi.RemoteException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.management.openmbean.InvalidKeyException;

import coordinate.GridPoint;
//import zeppelin.MainProgramImpl;
import coordinate.SwingApp;

public class ClientMessageParser {

	private SwingApp app;

	public ClientMessageParser(SwingApp app){
		this.app = app;
	}

	//routingKeyParser
	public String routingKeyDelimiter = "\\.";

	public void parse(String routingKey, String message) throws InvalidBindingKeyException, RemoteException{
		String[] routingKeyTokens = routingKey.split(routingKeyDelimiter);
		String zeppelinName = routingKeyTokens[0];   

		if(!groupNames.contains(zeppelinName)){
			throw new InvalidBindingKeyException("De binding key bevat een onbestaande naam van een zeppelin");
		}
//geldige naam niet gelijk aan ijzer
		if(! zeppelinName.equals("ijzer")){
			if(! app.getGuiController().getOtherKnownZeppelins().containsKey(zeppelinName)){
				app.getGuiController().addOtherKnownZeppelin(zeppelinName);
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
						app.getGuiController().getOtherKnownZeppelins().get(zeppelinName).setPosition(new GridPoint( Double.parseDouble(coordinates[0])/100, Double.parseDouble(coordinates[1])/100));
					}
				}
				if (infoType.equals("height")){
					message = message.replaceAll("\\s+", "");
					if(message.matches("\\d+")){
					double height = Double.parseDouble(message)/100;
					app.getGuiController().getOtherKnownZeppelins().get(zeppelinName).setHeight(height);
					}
				}
			}if(commandName.equals("hcommand")){
				System.out.println("Commando hcommand bedoeld voor zeppelin"+ zeppelinName );
			}if(commandName.equals("lcommand")){
				System.out.println("Commando lcommand bedoeld voor zeppelin"+ zeppelinName );
			}if(commandName.equals("private")){
				System.out.println("Commando private bedoeld voor zeppelin"+ zeppelinName );
			}
		}
//geldige naam gelijk aan ijzer
		if(zeppelinName.equals("ijzer")){
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
						app.getGuiController().getZeppelin().setPosition(new GridPoint( Double.parseDouble(coordinates[0])/100, Double.parseDouble(coordinates[1])/100));
					}
				}
				if (infoType.equals("height")){
					message = message.replaceAll("\\s+", "");
					if(message.matches("\\d+")){
					double height = Double.parseDouble(message)/100;
					app.getGuiController().getZeppelin().setHeight(height);
					}
				}				
			}if(commandName.equals("hcommand")){
				System.out.println("Eigen hcommand ontvangen");
			}
			if(commandName.equals("lcommand")){
				System.out.println("Eigen lcommand ontvangen");
			}
			if(commandName.equals("private")){
				String privateCommandtype = routingKeyTokens[2];
				if(privateCommandtype.equals("log")){
					app.getGuiController().appendToLogPart(message);
				}if(privateCommandtype.equals("height")){
					String heightCommType = routingKeyTokens[3];
					if(heightCommType.equals("getP")){
						System.out.println("Commando private.height.getP bedoeld voor de zeppelin, verzonden door deze client");
					}
					if(heightCommType.equals("getI")){
						System.out.println("Commando private.height.getI bedoeld voor de zeppelin, verzonden door deze client");					
					}if(heightCommType.equals("getD")){
						System.out.println("Commando private.height.getD bedoeld voor de zeppelin, verzonden door deze client");
					}if(heightCommType.equals("setP")){
						System.out.println("Commando private.height.setP bedoeld voor de zeppelin, verzonden door deze client");
					}if(heightCommType.equals("setI")){
						System.out.println("Commando private.height.setI bedoeld voor de zeppelin, verzonden door deze client");
					}if(heightCommType.equals("setD")){
						System.out.println("Commando private.height.setD bedoeld voor de zeppelin, verzonden door deze client");
					}if(heightCommType.equals("currentP")){
						message = message.replaceAll("\\s+", "");
						if(message.matches("\\d+")){
							double kp = Double.parseDouble(message);
							this.app.getGuiController().setKpHeight(kp);
						}
					}if(heightCommType.equals("currentI")){
						message = message.replaceAll("\\s+", "");
						if(message.matches("\\d+")){
							double ki = Double.parseDouble(message);
							this.app.getGuiController().setKiHeight(ki);
						}
					}if(heightCommType.equals("currentD")){
						message = message.replaceAll("\\s+", "");
						if(message.matches("\\d+")){
							double kd = Double.parseDouble(message);
							this.app.getGuiController().setKdHeight(kd);
						}
					}
					
					
				}if(privateCommandtype.equals("angle")){
					String angleCommType = routingKeyTokens[3];
					if(angleCommType.equals("getP")){
						System.out.println("Commando private.angle.getP bedoeld voor de zeppelin, verzonden door deze client");
					}if(angleCommType.equals("getI")){
						System.out.println("Commando private.angle.getI bedoeld voor de zeppelin, verzonden door deze client");					
					}if(angleCommType.equals("getD")){
						System.out.println("Commando private.angle.getD bedoeld voor de zeppelin, verzonden door deze client");
					}if(angleCommType.equals("setP")){
						System.out.println("Commando private.angle.setP bedoeld voor de zeppelin, verzonden door deze client");
					}if(angleCommType.equals("setI")){
						System.out.println("Commando private.angle.setI bedoeld voor de zeppelin, verzonden door deze client");
					}if(angleCommType.equals("setD")){
						System.out.println("Commando private.angle.getD bedoeld voor de zeppelin, verzonden door deze client");
					}if(angleCommType.equals("currentP")){
						message = message.replaceAll("\\s+", "");
						if(message.matches("\\d+")){
							double kp = Double.parseDouble(message);
							this.app.getGuiController().setKpAngle(kp);
						}
					}if(angleCommType.equals("currentI")){
						message = message.replaceAll("\\s+", "");
						if(message.matches("\\d+")){
							double ki = Double.parseDouble(message);
							this.app.getGuiController().setKiAngle(ki);
						}
					}if(angleCommType.equals("currentD")){
						message = message.replaceAll("\\s+", "");
						if(message.matches("\\d+")){
							double kd = Double.parseDouble(message);
							this.app.getGuiController().setKdAngle(kd);
						}
					}					
				}
			}if(commandName.equals("tablets")){
				String tabletName= routingKeyTokens[2];
				System.out.println("Commando 'ijzer.tablets."+ tabletName + " bedoeld voor tablet " + tabletName);
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
		commandTypes.add("tablets");

	}


}
