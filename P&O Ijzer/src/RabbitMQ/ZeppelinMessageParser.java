package RabbitMQ;


import java.util.LinkedHashSet;
import java.util.Set;



import coordinate.GridPoint;
import zeppelin.MainProgramImpl;

public class ZeppelinMessageParser {


	public ZeppelinMessageParser(MainProgramImpl zeppelin){
		setZeppelin(zeppelin);
	}
	MainProgramImpl zeppelin;

	public MainProgramImpl getZeppelin() {
		return zeppelin;
	}
	public void setZeppelin(MainProgramImpl zeppelin) {
		this.zeppelin = zeppelin;
	}
	//routingKeyParser
	public String routingKeyDelimiter = "\\.";

	public void parse(String routingKey, String message) throws InvalidBindingKeyException{
		String[] routingKeyTokens = routingKey.split(routingKeyDelimiter);
		String zeppelinName = routingKeyTokens[0];   

		if(!groupNames.contains(zeppelinName)){
			throw new InvalidBindingKeyException("De binding key bevat een onbestaande naam van een zeppelin");
		}
//geldige naam niet gelijk aan ijzer
		if(! zeppelinName.equals("ijzer")){
			if(! zeppelin.getOtherKnownZeppelins().containsKey(zeppelinName)){
				zeppelin.addOtherKnownZeppelin(zeppelinName);
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
					zeppelin.getOtherKnownZeppelins().get(zeppelinName).setPosition(new GridPoint( Double.parseDouble(coordinates[0])/100, Double.parseDouble(coordinates[1])/100));
					}
				}
				if (infoType.equals("height")){
					message = message.replaceAll("\\s+", "");
					if(message.matches("\\d+")){
					double height = Double.parseDouble(message)/100;
					zeppelin.getOtherKnownZeppelins().get(zeppelinName).setHeight(height);
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
		if(zeppelinName.equals("ijzer")){
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
					zeppelin.setTargetPosition(new GridPoint( Double.parseDouble(coordinates[0])/100, Double.parseDouble(coordinates[1])/100));
					}
				}if(hCommandType.equals("elevate")){
					message = message.replaceAll("\\s+", "");
					if(message.matches("\\d+")){
					double height = Double.parseDouble(message)/100;
					zeppelin.setTargetHeight(height);
					}
				}
			}
			if(commandName.equals("lcommand")){
				System.out.println("lcommand's worden niet ondersteunt" );
			}
			if(commandName.equals("private")){
				String privateCommandtype = routingKeyTokens[2];
				if(privateCommandtype.equals("log")){
					System.out.println("Het log-command is enkel bedoeld voor de client");
				}if(privateCommandtype.equals("exit")){
					zeppelin.exit();
				}if(privateCommandtype.equals("height")){
					String heightCommType = routingKeyTokens[3];
					if(heightCommType.equals("getP")){
						zeppelin.getRabbitMQControllerZeppelin().getZeppelinSender().sendPrivateMessage(PrivateRoutingKeyTypes.PID_HEIGHT_CURRENTP);
					}
					if(heightCommType.equals("getI")){
						zeppelin.getRabbitMQControllerZeppelin().getZeppelinSender().sendPrivateMessage(PrivateRoutingKeyTypes.PID_HEIGHT_CURRENTI);
					}
					if(heightCommType.equals("getD")){
						zeppelin.getRabbitMQControllerZeppelin().getZeppelinSender().sendPrivateMessage(PrivateRoutingKeyTypes.PID_HEIGHT_CURRENTD);
					}if(heightCommType.equals("setP")){
						message = message.replaceAll("\\s+", "");
						if(message.matches("\\d+")){
						zeppelin.getHeightController().getpController().setKp(Double.parseDouble(message));
						}
					}if(heightCommType.equals("setI")){
						message = message.replaceAll("\\s+", "");
						if(message.matches("\\d+")){
						zeppelin.getHeightController().getpController().setKi(Double.parseDouble(message));
						}
					}if(heightCommType.equals("setD")){
						message = message.replaceAll("\\s+", "");
						if(message.matches("\\d+")){
						zeppelin.getHeightController().getpController().setKd(Double.parseDouble(message));
						}
					}if(heightCommType.equals("currentP")){
						System.out.println("Informatie verzonden door deze zeppelin over Kp van height");
					}if(heightCommType.equals("currentI")){
						System.out.println("Informatie verzonden door deze zeppelin over Ki van height");
					}if(heightCommType.equals("currentD")){
						System.out.println("Informatie verzonden door deze zeppelin over Kd van height");
					}
				
					
				}if(privateCommandtype.equals("getTargetHeight")){
					zeppelin.getRabbitMQControllerZeppelin().getZeppelinSender().sendPrivateMessage(PrivateRoutingKeyTypes.CURRENTTARGETHEIGHT);
				}if(privateCommandtype.equals("setTargetHeight")){
					message = message.replaceAll("\\s+", "");
					if(message.matches("\\d+")){
					zeppelin.setTargetHeight(Double.parseDouble(message));;
					}
				}if(privateCommandtype.equals("currentTargetHeight")){
					System.out.println("Informatie verzonden door deze zeppelin over de targetHeight");
				}if(privateCommandtype.equals("angle")){
					String angleCommType = routingKeyTokens[3];
					if(angleCommType.equals("getP")){
						zeppelin.getRabbitMQControllerZeppelin().getZeppelinSender().sendPrivateMessage(PrivateRoutingKeyTypes.PID_ANGLE_CURRENTP);
					}
					if(angleCommType.equals("getI")){
						zeppelin.getRabbitMQControllerZeppelin().getZeppelinSender().sendPrivateMessage(PrivateRoutingKeyTypes.PID_ANGLE_CURRENTI);
					}
					if(angleCommType.equals("getD")){
						zeppelin.getRabbitMQControllerZeppelin().getZeppelinSender().sendPrivateMessage(PrivateRoutingKeyTypes.PID_ANGLE_CURRENTD);
					}if(angleCommType.equals("setP")){
						message = message.replaceAll("\\s+", "");
						if(message.matches("\\d+")){
						zeppelin.getRotationController().getpController().setKp(Double.parseDouble(message));
						}
					}if(angleCommType.equals("setI")){
						message = message.replaceAll("\\s+", "");
						if(message.matches("\\d+")){
						zeppelin.getRotationController().getpController().setKi(Double.parseDouble(message));
						}
					}if(angleCommType.equals("setD")){
						message = message.replaceAll("\\s+", "");
						if(message.matches("\\d+")){
						zeppelin.getRotationController().getpController().setKd(Double.parseDouble(message));
						}
					}if(angleCommType.equals("currentP")){
						System.out.println("Informatie verzonden door deze zeppelin over Kp van angle");
					}if(angleCommType.equals("currentI")){
						System.out.println("Informatie verzonden door deze zeppelin over Ki van angle");
					}if(angleCommType.equals("currentD")){
						System.out.println("Informatie verzonden door deze zeppelin over Kd van angle");
					}				
				}if(privateCommandtype.equals("getTargetAngle")){
					zeppelin.getRabbitMQControllerZeppelin().getZeppelinSender().sendPrivateMessage(PrivateRoutingKeyTypes.CURRENTTARGETANGLE);
				}if(privateCommandtype.equals("setTargetAngle")){
					message = message.replaceAll("\\s+", "");
					if(message.matches("\\d+")){
					zeppelin.setTargetAngle(Double.parseDouble(message));;
					}
				}if(privateCommandtype.equals("currentTargetAngle")){
					System.out.println("Informatie verzonden door deze zeppelin over de targetAngle");
				}
			}if (commandName.equals("tablets")) {
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
