/**
 * Implementatie van ZeppelinInterface. Het is absoluut essentieel dat het programma dat op de Pi draait toegang
 * heeft tot deze klasse.
 */

package zeppelin;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import positioning.Image;
import qrcode.DecodeQR;
import traversal.HeightUpdater;
import traversal.PositionUpdater;
import logger.LogWriter;
import movement.ForwardBackwardController;
import movement.HeightController;
import movement.RotationController;

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

public class MainProgramImpl extends UnicastRemoteObject implements MainProgramInterface {

	private static final long serialVersionUID = 1L;
	
	public static final GpioController gpio = GpioFactory.getInstance();

	// ======== Controllers ========
	private SensorController sensorController;
	private CameraController cameraController;
	private MotorController motorController;
	private HeightController heightController;
	private RotationController rotationController;

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
	private GridPoint position; //TODO Moet juist geinnitialiseerd worden!
	
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

	public MainProgramImpl() throws RemoteException {
		super();
		
		this.initialiseGrid();
		
		this.sensorController = new SensorController(RaspiPin.GPIO_03, RaspiPin.GPIO_06);
		this.cameraController = new CameraController();
		this.motorController = new MotorController();
		this.heightController = new HeightController(sensorController, motorController);
		this.rotationController = new RotationController(this, motorController);
		this.positionUpdater = new PositionUpdater(this);
		this.traversalHandler = new TraversalHandler(this);
		
		this.setTargetPosition(new GridPoint(-1, -1));
		
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

	public Grid getGrid()
	{
		return this.grid;
	}

	public double getTargetAngle() {
		return this.targetAngle;
	}

	public double getTargetHeight() throws RemoteException {
		return this.targetHeight;
	}
	
	public GridPoint getTargetPosition() {
		return this.targetPosition;
	}

	public double getMostRecentAngle() {
		return this.mostRecentAngle;
	}
	
	public PositionUpdater getPositionUpdater() {
		return this.getPositionUpdater();
	}
	
	public GridPoint getPosition()
	{
		return this.position;
	}
	
	public double getHeight() {
		return heightController.getHeight();
	}

	@Override
	public String readLog() throws RemoteException {
		return LogWriter.INSTANCE.getLog();
	}
	
	@Override
	public boolean leftIsOn() throws RemoteException {
		return motorController.leftIsOn();
	}
	
	
	@Override
	public boolean rightIsOn() throws RemoteException {
		return motorController.rightIsOn();
	}
	
	@Override
	public boolean downwardIsOn() throws RemoteException {
		return motorController.downwardIsOn();
	}
	
	public TraversalHandler getTraversalHandler() {
		return this.traversalHandler;
	}
	
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
	
	// ======== Setters ========
	
	public void setHeight(double height) {
		heightController.setHeight(height);
	}

	public void setAngle(double angle) {
		this.mostRecentAngle = angle;
	}
	
	public void setPosition(GridPoint point)
	{
		this.position = point;
	}
	
	public void setTargetPosition(GridPoint point)
	{
		this.targetPosition = point;
	}

	@Override
	public void setTargetHeight(double height) throws RemoteException {
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
	 * Zolang de cliënt contact onderhoudt met de zeppelin, moet deze
	 * lus uitgevoerd worden. In elke iteratie wordt er actie genomen om
	 * de huidige doelhoogte en huidige doelhoek te bereiken.
	 *
	 * @throws InterruptedException 
	 */
	private void gameLoop() throws InterruptedException {
		GridPoint dummyPoint = new GridPoint(-1, -1);
		
		boolean detectingQrCode = false;
		boolean qrCodeFound = false;
		boolean movedTowardsTarget = false; 
		
		while (!exit) {
			if (! detectingQrCode)
			{
				this.getPositionUpdater().update();
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
					}
				}
				if (! movedTowardsTarget && qrCodeFound)
				{
					this.setTargetHeight(0);
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (TimeoutException e1) {
				e1.printStackTrace();
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
		String fileName = Long.toString(System.currentTimeMillis());
		return this.cameraController.takePicture(fileName);
	}
	
	
	@Override
	public void goForward() throws RemoteException {
		motorController.forward();
	}

	@Override
	public void goBackward() throws RemoteException {
		motorController.backward();
	}

	@Override
	public void turnLeft() throws RemoteException {
		motorController.clientLeft();
	}

	@Override
	public void turnRight() throws RemoteException {
		motorController.clientRight();
	}

	@Override
	public void stopRightAndLeft() throws RemoteException {
		motorController.stopRightAndLeftMotor();
	}

	public void stopDownward() throws RemoteException {
		motorController.stopHeightAdjustment();
	}
	
	public void moveTowardsTargetHeight() throws RemoteException, TimeoutException, InterruptedException {
		this.getHeightController().goToHeight(this.getTargetHeight());
	}
	
	public void moveTowardsTargetAngle() throws RemoteException, InterruptedException, TimeoutException {
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
			this.grid = init.readGrid("grid");
		} catch (IOException e) {
			System.err.println("WAARSCHUWING: kon grid niet initialiseren.");
			e.printStackTrace();
		}
	}
	
	private void initialiseThreads()
	{
		Thread heightUpdaterThread = new Thread(new HeightUpdater(this));
		heightUpdaterThread.run();
	}

}
