package RabbitMQ;

public enum PrivateRoutingKeyTypes {
LOG(".log"), 
PID_HEIGHT_GETP(".height.getP"),
PID_HEIGHT_GETI(".height.getI"),
PID_HEIGHT_GETD(".height.getD"),
PID_HEIGHT_SETP(".height.setP"),
PID_HEIGHT_SETI(".height.setI"),
PID_HEIGHT_SETD(".height.setD"),

PID_ANGLE_GETP(".angle.getP"),
PID_ANGLE_GETI(".angle.getI"),
PID_ANGLE_GETD(".angle.getD"),
PID_ANGLE_SETP(".angle.setP"),
PID_ANGLE_SETI(".angle.setI"),
PID_ANGLE_SETD(".angle.setD");

private final String routingKeyPart;

private PrivateRoutingKeyTypes(String routingKeyPart){
	this.routingKeyPart=routingKeyPart;
}

public String getRoutingKeyPart(){
	return routingKeyPart;
}
}
