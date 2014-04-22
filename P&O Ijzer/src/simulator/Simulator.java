package simulator;

import java.util.ArrayList;
import java.util.List;

import coordinate.GridPoint;

public class Simulator {
	
	public Simulator(String name, GridPoint startPosition,
			GridPoint targetPosition, double startHeight,
			double targetHeight, int updateInterval,
			double positionStep, double heightStep) throws IllegalArgumentException {
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
	}
	
	public Simulator() {
		this("Serenity", new GridPoint(0, 0), new GridPoint(250, 250), 100, 160, 1000, 5, 5);
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
	}
	
	private class SimulatorRunner implements Runnable {
		public void run() {
			while (true) {
				GridPoint newPosition = Simulator.this.getPositionStepper()
						.moveTowards(Simulator.this.getPosition(), Simulator.this.getTargetPosition(), Simulator.this.getPositionStep());
				double newHeight = Simulator.this.getHeightStepper()
						.moveTowards(Simulator.this.getHeight(), Simulator.this.getTargetHeight(), Simulator.this.getHeightStep());
				Simulator.this.setPosition(newPosition);
				Simulator.this.setHeight(newHeight);
				Simulator.this.update();
				try {
					Thread.sleep(Simulator.this.getUpdateInterval());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
