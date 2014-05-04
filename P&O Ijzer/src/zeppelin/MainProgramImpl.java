

/**
 * Implementatie van ZeppelinInterface. Het is absoluut essentieel dat het programma dat op de Pi draait toegang
 * heeft tot deze klasse.
 */

package zeppelin;

import java.io.IOException;

import java.security.NoSuchAlgorithmException;
//import java.rmi.RemoteException;
//import java.rmi.RemoteException;
//import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import positioning.Image;
import qrcode.DecodeQR;
import traversal.HeightUpdater;
import traversal.PositionUpdater;
import logger.LogWriter;
import movement.ForwardBackwardController;
import movement.HeightController;
import movement.RotationController;
import RabbitMQ.RabbitMQController;
import RabbitMQ.RabbitMQControllerZeppelin;



import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;

import controllers.CameraController;
import controllers.MotorController;
import controllers.SensorController;
import controllers.SensorController.TimeoutException;
import coordinate.Grid;
import coordinate.GridInitialiser;
import coordinate.GridPoint;
import coordinate.Tablet;
import RabbitMQ.*;
import qrcode.RSA;;

public class MainProgramImpl  implements IZeppelin, MainProgramInterface {

	private Map<String, Zeppelin> otherKnownZeppelins = new HashMap<String, Zeppelin>();	
	
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

	private static final long serialVersionUID = 1L;
	
	public static final GpioController gpio = GpioFactory.getInstance();

	// ======== Controllers ========
	private SensorController sensorController;
	private CameraController cameraController;
	private MotorController motorController;
	private HeightController heightController;
	private RotationController rotationController;
	private RabbitMQControllerZeppelin rabbitMQControllerZeppelin;

	// ======== Grid ========
	private Grid grid;
	
	// ======== Doelvariabelen ========
	private double targetHeight;
	private double targetAngle;
	private GridPoint targetPosition;
	
	// ======== State variabelen ========
	/**
	 * Meest recente uitlezing van de sensor.
	 */
	private double mostRecentAngle;
	private GridPoint position;
	
	// ======== Updatet de hoek en de huidige positie ========
	private PositionUpdater positionUpdater;
	
	// ======== Beweegt naar de doelpositie toe ========
	private TraversalHandler traversalHandler;
	
	private DecodeQR qrCodeReader;
	

	/**
	 * Geeft aan of de zeppelin zijn activiteiten moet stopzetten.
	 */
	private boolean exit = false;

	private boolean turning = false;
	
	private RSA RSA;

	public MainProgramImpl(GridPoint startPosition) throws NoSuchAlgorithmException, IOException  {
		super();
		
		this.initialiseGrid();
		
		this.sensorController = new SensorController(RaspiPin.GPIO_03, RaspiPin.GPIO_06);
		this.cameraController = new CameraController();
		this.motorController = new MotorController();
		this.heightController = new HeightController(sensorController, motorController);
		this.rotationController = new RotationController(this, motorController);
		this.positionUpdater = new PositionUpdater(this);
		this.traversalHandler = new TraversalHandler(this);
		this.rabbitMQControllerZeppelin = new RabbitMQControllerZeppelin(this);
		this.RSA = new RSA();
		this.qrCodeReader = new DecodeQR(this.RSA);
		this.position = startPosition;
		
		this.setTargetPosition(new GridPoint(-1, -1));
		
		this.enforceGoodSensorReading();
		this.initialiseThreads();

		LogWriter.INSTANCE.writeToLog("------------ START NIEUWE SESSIE ------------- \n");

		try {
			this.targetHeight = sensorController.sensorReading();

		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	// ======== Getters ========
	
	@Override
	public HeightController getHeightController() {
		return heightController;
	}
	
	public CameraController getCameraController() {
		return this.cameraController;
	}
	
	public MotorController getMotorController() {
		return this.motorController;
	}
	
	public SensorController getSensorController() {
		return this.sensorController;
	}
	
	public RotationController getRotationController() {
		return this.rotationController;
	}
	
	public RabbitMQControllerZeppelin getRabbitMQControllerZeppelin(){
		return this.rabbitMQControllerZeppelin;
	}

	public Grid getGrid()
	{
		return this.grid;

	}

	public double getTargetAngle() {
		return this.targetAngle;
	}


	public double getTargetHeight() {
		return this.targetHeight;
	}
	
	public GridPoint getTargetPosition() {
		return this.targetPosition;
	}

	public double getMostRecentAngle() {
		return this.mostRecentAngle;
	}
	
	public PositionUpdater getPositionUpdater() {
		return this.positionUpdater;
	}
	
	public GridPoint getPosition()
	{
		return this.position;
	}
	
	public double getHeight() {
		return heightController.getHeight();
	}

	@Override
	public String readLog() {
		return LogWriter.INSTANCE.getLog();
	}
	
	@Override
	public boolean leftIsOn()  {
		return motorController.leftIsOn();
	}
	
	
	@Override
	public boolean rightIsOn() {
		return motorController.rightIsOn();
	}
	
	@Override
	public boolean downwardIsOn()  {
		return motorController.downwardIsOn();
	}
	
	public TraversalHandler getTraversalHandler() {
		return this.traversalHandler;

	}
	
	//TODO OBSOLETE
	/*
<<<<<<< HEAD
	@Override
	public void setKpHeight(double kp) {
		HEIGHT_CONTROLLER.setKpHeight(kp);
	}
	
	@Override
	public void setKdHeight(double kd) {
		HEIGHT_CONTROLLER.setKdHeight(kd);
	}
	
	@Override
	public void setKiHeight(double ki) {
		HEIGHT_CONTROLLER.setKiHeight(ki);
	}
	
	@Override
	public double getKpHeight() throws RemoteException {
		return HEIGHT_CONTROLLER.getKpHeight();
	}
	
	@Override
	public double getKiHeight() throws RemoteException {
		return HEIGHT_CONTROLLER.getKdHeight();
	}
	
	@Override
	public double getKdHeight() throws RemoteException {
		return HEIGHT_CONTROLLER.getKiHeight();
	}
	
	@Override
	public void setSafetyIntervalHeight(double safetyInterval) throws RemoteException {
		HEIGHT_CONTROLLER.setSafetyIntervalHeight(safetyInterval);
	}
	
	@Override
	public double getSafetyIntervalHeight() throws RemoteException {
		return HEIGHT_CONTROLLER.getSafetyIntervalHeight();
	}
	
	@Override
	public void setKpAngle(double kp) {
		ROTATION_CONTROLLER.setKpAngle(kp);
	}
	
	@Override
	public void setKdAngle(double kd) {
		ROTATION_CONTROLLER.setKdAngle(kd);
	}
	
	@Override
	public void setKiAngle(double ki) {
		ROTATION_CONTROLLER.setKiAngle(ki);
	}
	
	@Override
	public double getKpAngle() throws RemoteException {
		return ROTATION_CONTROLLER.getKpAngle();
	}
	
	@Override
	public double getKiAngle() throws RemoteException {
		return ROTATION_CONTROLLER.getKdAngle();
	}
	
	@Override
	public double getKdAngle() throws RemoteException {
		return ROTATION_CONTROLLER.getKiAngle();
	}
	
	@Override
	public void setSafetyIntervalAngle(double safetyInterval) throws RemoteException {
		ROTATION_CONTROLLER.setSafetyIntervalAngle(safetyInterval);
	}
	
	@Override
	public double getSafetyIntervalAngle() throws RemoteException {
		return ROTATION_CONTROLLER.getSafetyIntervalAngle();
	}
	*/
	

	
	//TODO OBSOLETE
	/*
	@Override
	public void setKpHeight(double kp) {
		HEIGHT_CONTROLLER.setKpHeight(kp);
	}

	@Override
	public void setKdHeight(double kd) {
		HEIGHT_CONTROLLER.setKdHeight(kd);
	}

	@Override
	public void setKiHeight(double ki) {
		HEIGHT_CONTROLLER.setKiHeight(ki);
	}

	@Override
	public double getKpHeight() throws RemoteException {
		return HEIGHT_CONTROLLER.getKpHeight();
	}

	@Override
	public double getKiHeight() throws RemoteException {
		return HEIGHT_CONTROLLER.getKdHeight();
	}

	@Override
	public double getKdHeight() throws RemoteException {
		return HEIGHT_CONTROLLER.getKiHeight();
	}

	@Override
	public void setSafetyIntervalHeight(double safetyInterval) throws RemoteException {
		HEIGHT_CONTROLLER.setSafetyIntervalHeight(safetyInterval);
	}

	@Override
	public double getSafetyIntervalHeight() throws RemoteException {
		return HEIGHT_CONTROLLER.getSafetyIntervalHeight();
	}

	@Override
	public void setKpAngle(double kp) {
		ROTATION_CONTROLLER.setKpAngle(kp);
	}

	@Override
	public void setKdAngle(double kd) {
		ROTATION_CONTROLLER.setKdAngle(kd);
	}

	@Override
	public void setKiAngle(double ki) {
		ROTATION_CONTROLLER.setKiAngle(ki);

	@Override
	public double getKpAngle() throws RemoteException {
		return ROTATION_CONTROLLER.getKpAngle();
	}

	@Override
	public double getKiAngle() throws RemoteException {
		return ROTATION_CONTROLLER.getKdAngle();
	}

	@Override
	public double getKdAngle() throws RemoteException {
		return ROTATION_CONTROLLER.getKiAngle();
	}

	@Override
	public void setSafetyIntervalAngle(double safetyInterval) throws RemoteException {
		ROTATION_CONTROLLER.setSafetyIntervalAngle(safetyInterval);
	}

	@Override
	public double getSafetyIntervalAngle() throws RemoteException {
		return ROTATION_CONTROLLER.getSafetyIntervalAngle();
	}

	*/
	
	// ======== Setters ========
	
	public void setHeight(double height) {
		heightController.setHeight(height);
	}

	public void setAngle(double angle) {
		System.out.println("Hoek gezet: " + angle);
		this.mostRecentAngle = angle;
	}
	
	public void setPosition(GridPoint point)
	{
		this.position = point;
		System.out.println("Positie gezet: " + point.toString());
	}
	
	public void setTargetPosition(GridPoint point)
	{
		this.targetPosition = point;
	}

	@Override
	public void setTargetHeight(double height) {
		this.targetHeight = height;
	}

	public void setTargetAngle(double targetAngle) {
		this.targetAngle = targetAngle;
	}

	public void setTurning(boolean value) {
		this.turning = value;
	}
	
	// ======== Applicatielogica ========

	public void startGameLoop() throws InterruptedException {
		System.out.println("Lus initialiseren; zeppelin is klaar om commando's uit te voeren.");
		this.gameLoop();
		System.out.println("Lus afgebroken; uitvoering is stopgezet.");
	}

	public void exit() {
		this.exit = true;
	}

	/**
	 * Returns the tablets that are less than "distance" cm away from the current position of the zeppelin
	 */
	public List<Tablet> tabletsInNeighbourhood(double distance){
	
		List<Tablet> tabletsInNeighbourhood = new ArrayList<Tablet>();
		for(Tablet tab: getGrid().getTablets()){
			double distanceBetweenTabAndZep = this.getPosition().distanceTo(tab.getPosition());
			if(distanceBetweenTabAndZep < distance){
				tabletsInNeighbourhood.add(tab);
			}
		}
		return tabletsInNeighbourhood;
	}
	
	private long timeSinceLastMessageSendToTablet;
	
	private Tablet destinationTab;
	
	public long getTimeSinceLastMessageSendToTablet() {
		return timeSinceLastMessageSendToTablet;
	}

	public void setTimeSinceLastMessageSendToTablet(
			long timeSinceLastMessageSendToTablet) {
		this.timeSinceLastMessageSendToTablet = timeSinceLastMessageSendToTablet;
	}

	/**
	 * Zolang de cliënt contact onderhoudt met de zeppelin, moet deze
	 * lus uitgevoerd worden. In elke iteratie wordt er actie genomen om
	 * de huidige doelhoogte en huidige doelhoek te bereiken.
	 *
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	private void gameLoop() throws InterruptedException {
		GridPoint dummyPoint = new GridPoint(-1, -1);
		
		boolean detectingQrCode = false;
		boolean qrCodeFound = false;
		boolean movedTowardsTarget = false;
		boolean tabletInNeighbourHood = false;
		
		while (!exit) {
//			try {
//				this.heightController.goToHeight(this.getTargetHeight());
//			} catch (RemoteException e2) {
//				e2.printStackTrace();
//			} catch (TimeoutException e2) {
//				e2.printStackTrace();
//			}
			this.getRabbitMQControllerZeppelin().getZeppelinSender().sendHeight();
			if (! detectingQrCode)
			{
				this.getPositionUpdater().update();
				this.getRabbitMQControllerZeppelin().getZeppelinSender().sendLocation();
			}
			if (this.getTargetPosition().equals(dummyPoint))
			{
				continue;
			}
			try {
				movedTowardsTarget = this.getTraversalHandler().moveTowardsPoint();
				detectingQrCode = (! movedTowardsTarget && ! qrCodeFound);
				while (detectingQrCode)
				{
					String fileName = Long.toString(System.currentTimeMillis());
					String result = qrCodeReader.decodeImage(CameraController.PICTURE_PATH + fileName);
					if (result != null)
					{
						detectingQrCode = false;
						qrCodeFound = true;
//						setTargetPosition(executeCommandPosition(result));
//						setTargetHeight(executeCommandHeight(result)); //TODO nog aanpassen voor te landen op juiste plaats!
					}
				}
//				if (! movedTowardsTarget && qrCodeFound)
//				{
//					this.setTargetHeight(0);
//				}
			}  catch (TimeoutException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		motorController.writeSoftPwmValues(0, 0, 0, 0);
		motorController.stopRightAndLeftMotor();
		motorController.stopHeightAdjustment();
		System.exit(0);
	}
	
	public Image captureImage() throws InterruptedException, IOException
	{
		String fileName = "huidigefoto";
		return this.cameraController.takePicture(fileName);
	}
	
	
	@Override
	public void goForward(){
		motorController.forward();
	}

	@Override
	public void goBackward() {
		motorController.backward();
	}

	@Override
	public void turnLeft()  {
		motorController.clientLeft();
	}

	@Override
	public void turnRight()  {
		motorController.clientRight();
	}

	@Override
	public void stopRightAndLeft()  {
		motorController.stopRightAndLeftMotor();
	}

	public void stopDownward()  {
		motorController.stopHeightAdjustment();
	}
	
	public void moveTowardsTargetHeight() throws TimeoutException, InterruptedException{
		this.getHeightController().goToHeight(this.getTargetHeight());
	}
	
	public void moveTowardsTargetAngle() throws InterruptedException, TimeoutException {
		this.getRotationController().goToAngle(this.getTargetAngle());
	}
	
	public boolean angleInAcceptableRange(double angle) {
		return this.rotationController.isInInterval(angle, this.getTargetAngle());
	}
	
	public double measureHeight() throws TimeoutException, InterruptedException
	{
		return this.getSensorController().sensorReading();

	}
	
	private void initialiseGrid()
	{
		GridInitialiser init = new GridInitialiser();
		try {

			this.grid = init.readGrid("/home/pi/grid.csv");

		} catch (IOException e) {
			System.err.println("WAARSCHUWING: kon grid niet initialiseren.");
			e.printStackTrace();
		}
	}
	

	private void initialiseThreads()
	{
		Thread heightUpdaterThread = new Thread(new HeightUpdater(this));
		heightUpdaterThread.start();
	}
	
	private void enforceGoodSensorReading() {
		boolean good = false;
		while (! good) {
			try {
				this.setHeight(this.getSensorController().sensorReading());
				good = true;
			} catch (TimeoutException e) {
				System.out.println("Exception in het begin bij goede sensor reading");
			} catch (InterruptedException e) {
				System.out.println("Exception in het begin bij goede sensor reading");
			}
		}

	}

	public RSA getRSA() {
		return RSA;
	}

	public void setRSA(RSA rSA) {
		RSA = rSA;
	}

	public Tablet getDestinationTab() {
		return destinationTab;
	}

	public void setDestinationTab(Tablet destinationTab) {
		this.destinationTab = destinationTab;
	}

}
