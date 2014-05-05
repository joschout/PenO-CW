package movement;

public class TurnSpeedCalculator {
	
	public int calculateTurnSpeed(double angleError) {
		return (int) (angleError * 0.2);
	}

}
