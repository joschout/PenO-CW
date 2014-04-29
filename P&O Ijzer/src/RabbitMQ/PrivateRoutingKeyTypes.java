package RabbitMQ;

public enum PrivateRoutingKeyTypes {
LOG(".log"), 

PID_HEIGHT_GETP(".height.getP"),
PID_HEIGHT_GETI(".height.getI"),
PID_HEIGHT_GETD(".height.getD"),
PID_HEIGHT_SETP(".height.setP"),
PID_HEIGHT_SETI(".height.setI"),
PID_HEIGHT_SETD(".height.setD"),
PID_HEIGHT_CURRENTP(".height.currentP"),
PID_HEIGHT_CURRENTI(".height.currentI"),
PID_HEIGHT_CURRENTD(".height.currentD"),

GETTARGETHEIGHT("getTargetHeight"),
SETTARGETHEIGHT("setTargetHeight"),
CURRENTTARGETHEIGHT("currentTargetHeight"),

//PID_HEIGHT_GETSAFETYINTERVALHEIGHT(".height.getSafetyIntervalHeight"),
//PID_HEIGHT_SETSAFETYINTERVALHEIGHT(".height.setSafetyIntervalHeight"),
//PID_HEIGHT_CURRENTSAFETYINTERVALHEIGHT(".height.currentSafetyIntervalHeight"),




PID_ANGLE_GETP(".angle.getP"),
PID_ANGLE_GETI(".angle.getI"),
PID_ANGLE_GETD(".angle.getD"),
PID_ANGLE_SETP(".angle.setP"),
PID_ANGLE_SETI(".angle.setI"),
PID_ANGLE_SETD(".angle.setD"),
PID_ANGLE_CURRENTP(".angle.currentP"),
PID_ANGLE_CURRENTI(".angle.currentI"),
PID_ANGLE_CURRENTD(".angle.currentD"),

GETTARGETANGLE("getTargetAngle"),
SETTARGETANGLE("setTargetAngle"),
CURRENTTARGETANGLE("currentTargetAngle"),

//PID_ANGLE_GETSAFETYINTERVALANGLE(".angle.getSafetyIntervalAngle"),
//PID_ANGLE_SETSAFETYINTERVALANGLE(".angle.setSafetyIntervalHAngle"),
//PID_ANGLE_CURRENTSAFETYINTERVALHEIGHT(".angle.currentSafetyIntervalAngle"),

EXIT(".exit");




private final String routingKeyPart;

private PrivateRoutingKeyTypes(String routingKeyPart){
	this.routingKeyPart=routingKeyPart;
}

public String getRoutingKeyPart(){
	return routingKeyPart;
}
}
