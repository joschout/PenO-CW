package client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import coordinate.SwingApp;
import RabbitMQ.PrivateRoutingKeyTypes;
import RabbitMQ.RabbitMQControllerClient;
import zeppelin.*;

public class GuiControllerAlternative2{
	
	public GuiControllerAlternative2(SwingApp app, Zeppelin ourZeppelin){
		this.app = app;
		setZeppelin(ourZeppelin);
	}
	
	private Zeppelin zeppelin;
	private SwingApp app;
	private Map<String, Zeppelin> otherKnownZeppelins = new HashMap<String, Zeppelin>();
	private RabbitMQControllerClient rabbitMQControllerClient;

	
	public Zeppelin getZeppelin(){
		return this.zeppelin;
	}
	
	public void setZeppelin(Zeppelin zeppelin){
		this.zeppelin=zeppelin;
	}
	
	public RabbitMQControllerClient getRabbitMQControllerClient() {
		return rabbitMQControllerClient;
	}

	public void setRabbitMQControllerClient(RabbitMQControllerClient rabbitMQControllerClient) {
		this.rabbitMQControllerClient = rabbitMQControllerClient;
	}
	
	public Map<String, Zeppelin> getOtherKnownZeppelins() {
		return otherKnownZeppelins;
	}

	public void setOtherKnownZeppelins(Map<String, Zeppelin> otherKnownZeppelins) {
		this.otherKnownZeppelins = otherKnownZeppelins;
	}
	
	public void addOtherKnownZeppelin(String name) {
     	Zeppelin newZeppelin = new Zeppelin();
		this.getOtherKnownZeppelins().put(name, newZeppelin);
	}
	/**
	 * Vraagt de hoogte van de zeppelin op.
	 * @return
	  
	 */
	public double getHeight() {
		return this.zeppelin.getHeight();
	}

	
	
	private double TargetHeight;

	public void askTargetHeight(){
		this.app.rabbitMQControllerClient.getClientSender().sendPrivateMessage_getTargetHeight();
		
	}
	
	
	/**
	 * Haal de doelhoogte van de zeppelin.
	 */
	public double getTargetHeight()  {
		return TargetHeight;
	}
	
	/**
	 * Laat de zeppelin streven naar een nieuwe hoogte.
	 * @param height
	 *        Nieuwe hoogte.
	 */
	public void setTargetHeight(double height)  {
		this.app.getRabbitMQControllerClient().getClientSender().sendPrivateMessage_setTargetHeight(height);
	}
	
	
	
	private String logPart;
	
	public String getLogPart(){
		return logPart;
	}
	public void setLogPart(String logPart){
		this.logPart=logPart;
	}

	public void appendToLogPart(String appendix){
		setLogPart(getLogPart()+appendix);
	}
	
	/**
	 * Leest de log op de zeppelin.
	 * @throws IllegalStateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String readLog() throws IllegalStateException, FileNotFoundException, IOException {
		String logPartTemp = getLogPart();
		setLogPart("");
		return logPartTemp;
	}

	/**
	 * Laat de zeppelin zijn activiteiten stoppen; sluit achteraf het programma af.
	 */
	public void exit() {
		this.app.rabbitMQControllerClient.getClientSender().sendPrivateMessage_exit();
	}
	
	private double KpHeight;
	private double KiHeight;
	private double KdHeight;
	
	public double getKpHeight() {
		return KpHeight;
	}
	
	public double getKiHeight() {
		return KiHeight;
	}
	
	public double getKdHeight() {
		return KdHeight;
	}
	/**
	 * Haalt de procesconstante voor de hoogte.
	 */
	public void askKpHeight()  {
		this.app.rabbitMQControllerClient.getClientSender().sendPrivateMessage_PID_getKValue(PrivateRoutingKeyTypes.PID_HEIGHT_GETP);
		
	}

	/**
	 * Haalt de derivate constante voor de hoogte.
	 */
	public void askKdHeight()  {
		this.app.rabbitMQControllerClient.getClientSender().sendPrivateMessage_PID_getKValue(PrivateRoutingKeyTypes.PID_HEIGHT_GETD);
	}
	
	/**
	 * Haalt de integraalconstante voor de hoogte.
	 */
	public void askKiHeight(){
		this.app.rabbitMQControllerClient.getClientSender().sendPrivateMessage_PID_getKValue(PrivateRoutingKeyTypes.PID_HEIGHT_GETI);
	}
	
/**
	 * Update de procesconstante voor de hoogte.
	 * @param kp
	 */
	public void setKpHeight(double kp) {
		this.app.getRabbitMQControllerClient().getClientSender().sendPrivateMessag_PID_setKValue(PrivateRoutingKeyTypes.PID_HEIGHT_SETP, kp);
	}

	/**
	 * Update de integraalconstante voor de hoogte.
	 * @param ki
	 */
	public void setKiHeight(double ki) {
		this.app.getRabbitMQControllerClient().getClientSender().sendPrivateMessag_PID_setKValue(PrivateRoutingKeyTypes.PID_HEIGHT_SETI, ki);
	}

	/**
	 * Update de derivative constante voor de hoogte.
	 * @param kd
	 */
	public void setKdHeight(double kd) {
		this.app.getRabbitMQControllerClient().getClientSender().sendPrivateMessag_PID_setKValue(PrivateRoutingKeyTypes.PID_HEIGHT_SETD, kd);
	}

	
	private double KpAngle;
	private double KiAngle;
	private double KdAngle;
	
	public double getKpAngle() {
		return KpAngle;
	}
	
	public double getKiAngle() {
		return KiAngle;
	}
	
	public double getKdAngle() {
		return KdAngle;
	}
	
	
	


	/**
		 * Haal de procesconstante voor de hoek.
		 * 
		 */
		public void askKpAngle() {
			this.app.rabbitMQControllerClient.getClientSender().sendPrivateMessage_PID_getKValue(PrivateRoutingKeyTypes.PID_ANGLE_GETP);
		}

	/**
		 * Haal de derivative constante voor de hoek.
		 * 
		 */
		public void askKdAngle()  {
			this.app.rabbitMQControllerClient.getClientSender().sendPrivateMessage_PID_getKValue(PrivateRoutingKeyTypes.PID_ANGLE_GETD);
		}

	/**
		 * Haal de integraalconstante voor de hoek.
		 *
		 */
		public void askKiAngle() {
			this.app.rabbitMQControllerClient.getClientSender().sendPrivateMessage_PID_getKValue(PrivateRoutingKeyTypes.PID_ANGLE_GETI);
		}

	//	// wordt deze methode zelfs gebruikt?
//	/**
//	 * Zet het interval rond de doelhoogte waar de verticale motor alle activiteit
//	 * moet stoppen.
//	 * @param safetyInterval
//	 *        Waarde die een interval maakt volgens [getTargetHeight - safetyInterval, getTargetHeight + safetyInterval]
//	  
//	 */
//	public void setSafetyIntervalHeight(double safetyInterval) throws RemoteException {
//		if (checkRegistryFound()&&checkZeppelinFound()) {
//		zeppelin.getHeightController().setSafetyIntervalHeight(safetyInterval);
//		}
//	}
//	
//	/**
//	 * Haalt het veiligheidsinterval voor de hoogte.
//	  
//	 */
//	public double getSafetyIntervalHeight() throws RemoteException {
//		if (checkRegistryFound()&&checkZeppelinFound()) {
//		return zeppelin.getHeightController().getSafetyIntervalHeight();
//		} else return 0;
//	}
//	
	/**
	 * Update de procesconstante voor de hoek.
	 * @param kp
	 */
	public void setKpAngle(double kp) {
		this.app.getRabbitMQControllerClient().getClientSender().sendPrivateMessag_PID_setKValue(PrivateRoutingKeyTypes.PID_ANGLE_SETP, kp);
	}
	
	/**
	 * Update de integraalconstante voor de hoek.
	 * @param ki
	 */
	public void setKiAngle(double ki) {
		this.app.getRabbitMQControllerClient().getClientSender().sendPrivateMessag_PID_setKValue(PrivateRoutingKeyTypes.PID_ANGLE_SETI, ki);
	}
	
	/**
	 * Update de derivative constante voor de hoek.
	 * @param kd
	 */
	public void setKdAngle(double kd) {
		this.app.getRabbitMQControllerClient().getClientSender().sendPrivateMessag_PID_setKValue(PrivateRoutingKeyTypes.PID_ANGLE_SETD, kd);
	}
	
}