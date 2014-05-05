package simulator;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import qrcode.DecodeQR;
import qrcode.RSA;
import qrcode.RSAInterface;
import qrcode.RSAWindows;
import qrcode.SimulatorQRCommandParser;
import movement.HeightController;
import movement.RotationController;
import zeppelin.MainProgramInterface;
import zeppelin.Zeppelin;
import RabbitMQ.RabbitMQControllerSimulator;
import RabbitMQ.RabbitMQControllerZeppelin;
import coordinate.Grid;
import coordinate.GridInitialiser;
import coordinate.GridPoint;
import coordinate.Tablet;

public class Simulator {
	
	public static final String downloadPath = "http://192.168.2.134:5000/static/";
	
	public Simulator(String name, GridPoint startPosition,
			GridPoint targetPosition, double startHeight,
			double targetHeight, int updateInterval,
			double positionStep, double heightStep, String gridPath) throws IllegalArgumentException {
		if (startPosition == null) {
			throw new IllegalArgumentException("startPosition is geen geldige positie");
		}
		if (targetPosition == null) {
			throw new IllegalArgumentException("targetPosition is geen geldige positie");
		}
		if (startHeight < 0) {
			throw new IllegalArgumentException("startHeight is geen geldige hoogte");
		}
		if (targetHeight < 0) {
			throw new IllegalArgumentException("targetHeight is geen geldige hoogte");
		}
		if (updateInterval <= 0) {
			throw new IllegalArgumentException("updateInterval is geen geldige "
					+ "update interval");
		}
		if (positionStep <= 0) {
			throw new IllegalArgumentException("positionStep is geen geldige "
					+ "position step");
		}
		if (heightStep <= 0) {
			throw new IllegalArgumentException("heightStep is geen geldige "
					+ "height step");
		}
		
		this.name = name;
		this.position = startPosition;
		this.height = startHeight;
		this.targetPosition = targetPosition;
		this.targetHeight = targetHeight;
		this.updateInterval = updateInterval;
		this.positionStep = positionStep;
		this.heightStep = heightStep;
		this.positionStepper = new PositionStepper();
		this.heightStepper = new HeightStepper();
		this.observers = new ArrayList<SimulatorObserver>();
		this.mqController = new RabbitMQControllerSimulator(this);
		this.timeSinceLastSendQR = System.currentTimeMillis();
		
		GridInitialiser init = new GridInitialiser();
		try {
			this.grid = init.readGrid(gridPath);
			this.rsa = new RSAWindows();
			this.qrDecode = new DecodeQR(rsa);
			this.parser = new SimulatorQRCommandParser(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Simulator(String gridPath) {
		this("Staal", new GridPoint(0, 0), new GridPoint(250, 250), 100, 160, 1000, 5, 5, gridPath);
	}
	
	private final String name;
	
	public String getName() {
		return this.name;
	}
	
	private GridPoint position;
	
	public GridPoint getPosition() {
		return this.position;
	}
	
	private void setPosition(GridPoint position) throws IllegalArgumentException {
		if (! this.isValidPosition(position)) {
			throw new IllegalArgumentException("position is geen geldige positie");
		}
		this.position = position;
	}
	
	private GridPoint targetPosition;
	
	public GridPoint getTargetPosition() {
		return this.targetPosition;
	}
	
	public void setTargetPosition(GridPoint targetPosition) throws IllegalArgumentException {
		if (! this.isValidPosition(targetPosition)) {
			throw new IllegalArgumentException("targetPosition is geen geldige positie");
		}
		this.targetPosition = targetPosition;
	}
	
	public void setTargetPositionGUI(GridPoint targetPosition) {
		if (! this.isValidPosition(targetPosition)) {
			throw new IllegalArgumentException("targetPosition is geen geldige positie");
		}
		this.targetPosition = targetPosition;
		this.setTablet(-1);
	}
	
	public boolean isValidPosition(GridPoint position) {
		return targetPosition != null;
	}
	
	private double positionStep;
	
	public double getPositionStep() {
		return this.positionStep;
	}
	
	public void setPositionStep(double positionStep) {
		if (! this.isValidPositionStep(positionStep)) {
			throw new IllegalArgumentException("positionStep is geen geldige "
					+ "position step");
		}
		this.positionStep = positionStep;
	}
	
	public boolean isValidPositionStep(double positionStep) {
		return positionStep > 0;
	}
	
	private PositionStepper positionStepper;
	
	private PositionStepper getPositionStepper() {
		return this.positionStepper;
	}
	
	private double height;
	
	public double getHeight() {
		return this.height;
	}
	
	private void setHeight(double height) throws IllegalArgumentException {
		if (! this.isValidHeight(height)) {
			throw new IllegalArgumentException("height is geen geldige hoogte");
		}
		this.height = height;
	}
	
	private double targetHeight;
	
	public double getTargetHeight() {
		return this.targetHeight;
	}
	
	public void setTargetHeight(double height) throws IllegalArgumentException {
		if (! this.isValidHeight(height)) {
			throw new IllegalArgumentException("targetHeight is geen geldige hoogte");
		}
		this.targetHeight = height;
	}
	
	public boolean isValidHeight(double height) {
		return height >= 0;
	}
	
	private double heightStep;
	
	public double getHeightStep() {
		return this.heightStep;
	}
	
	private Grid grid;
	
	public Grid getGrid() {
		return this.grid;
	}
	
	private Tablet targetTablet;
	
	public Tablet getTargetTablet() {
		return this.targetTablet;
	}
	
	public void setTablet(int id) {
		if (id < 1) {
			this.targetTablet = null;
			return;
		}
		this.targetTablet = this.getGrid().getTabletWithTabletId(id);
		this.setTargetPosition(this.getTargetTablet().getPosition());
	}
	
	public void setHeightStep(double heightStep) {
		if (! this.isValidHeightStep(heightStep)) {
			throw new IllegalArgumentException("heightStep is geen geldige "
					+ "height step");
		}
		this.heightStep = heightStep;
	}
	
	public boolean isValidHeightStep(double heightStep) {
		return heightStep > 0;
	}
	
	private HeightStepper heightStepper;
	
	private HeightStepper getHeightStepper() {
		return this.heightStepper;
	}

	private int updateInterval;
	
	public int getUpdateInterval() {
		return this.updateInterval;
	}
	
	public void setUpdateInterval(int updateInterval) throws IllegalArgumentException {
		if (! this.isValidUpdateInterval(updateInterval)) {
			throw new IllegalArgumentException("updateInterval is geen geldige "
					+ "update interval");
		}
		this.updateInterval = updateInterval;
	}
	
	public boolean isValidUpdateInterval(int updateInterval) {
		return updateInterval > 0;
	}
	
	public synchronized void run() throws IllegalStateException {
		if (this.isRunning()) {
			throw new IllegalStateException("Er runt al een thread voor deze Simulator");
		}
		this.setRunning();
		Thread runner = new Thread(new SimulatorRunner());
		runner.start();
	}
	
	private boolean running = false;
	
	public boolean isRunning() {
		return this.running;
	}
	
	private void setRunning() {
		this.running = true;
	}
	
	List<SimulatorObserver> observers;
	
	private List<SimulatorObserver> getObservers() {
		return this.observers;
	}
	
	public void register(SimulatorObserver observer) {
		if (! this.isValidObserver(observer)) {
			throw new IllegalArgumentException("Kan geen null observer toevoegen"
					+ "aan simulator");
		}
		this.getObservers().add(observer);
	}
	
	private boolean isValidObserver(SimulatorObserver observer) {
		return observer != null;
	}
	
	private void update() {
		for (SimulatorObserver observer : this.getObservers()) {
			observer.update(this);
		}
		this.getMQController().getSender().sendLocation();
		this.getMQController().getSender().sendHeight();
	}
	
	private RabbitMQControllerSimulator mqController;
	
	public RabbitMQControllerSimulator getMQController() {
		return this.mqController;
	}
	
	private RSAInterface rsa;
	
	public RSAInterface getRSA() {
		return this.rsa;
	}
	
	private DecodeQR qrDecode;
	
	public DecodeQR getQRDecode() {
		return this.qrDecode;
	}
	
	private SimulatorQRCommandParser parser;
	
	public SimulatorQRCommandParser getParser() {
		return this.parser;
	}
	
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
	
	private long timeSinceLastSendQR;
	
	public long getTimeSinceLastSend() {
		return this.timeSinceLastSendQR;
	}
	
	public void setTimeSinceLastSend(long time) {
		this.timeSinceLastSendQR = time;
	}
	
	private class SimulatorRunner implements Runnable {
		public void run() {
			while (true) {
				try {
					GridPoint newPosition = Simulator.this.getPositionStepper()
							.moveTowards(Simulator.this.getPosition(), Simulator.this.getTargetPosition(), Simulator.this.getPositionStep());
					double newHeight = Simulator.this.getHeightStepper()
							.moveTowards(Simulator.this.getHeight(), Simulator.this.getTargetHeight(), Simulator.this.getHeightStep());
					Simulator.this.setPosition(newPosition);
					Simulator.this.setHeight(newHeight);
					Simulator.this.update();
					if (Simulator.this.getPosition().equals(Simulator.this.getTargetPosition())
							&& Simulator.this.getTargetTablet() != null) {
						this.readFromTablet();
					}
					else if (Simulator.this.getPosition().equals(Simulator.this.getTargetPosition())
							&& Simulator.this.getTargetTablet() == null) {
						Simulator.this.setTargetHeight(0);
					}
					try {
						Thread.sleep(Simulator.this.getUpdateInterval());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		private void readFromTablet() {
			if (Simulator.this.getTargetTablet() != null) {
				try {
					Simulator.this.getMQController().getSender().sendPublicKeysToTablet(Simulator.this.getTargetTablet().getName());
					Thread.sleep(Simulator.this.getUpdateInterval());
					while (System.currentTimeMillis() - Simulator.this.getTimeSinceLastSend() < 5000) {
						Thread.sleep(200);
					}
					BufferedImage qrImage = this.downloadImage();
					String result = Simulator.this.getQRDecode().decodeImage(qrImage);
					Simulator.this.getParser().parse(result);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		private BufferedImage downloadImage() {
			BufferedImage image = null;
			try {
				URL url = new URL(downloadPath + "ijzer" + Simulator.this.getTargetTablet().getId()
						+ ".png");
				image = ImageIO.read(url);
				return image;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return image;
		}
	}
}
