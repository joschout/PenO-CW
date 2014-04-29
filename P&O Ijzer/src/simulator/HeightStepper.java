package simulator;

public class HeightStepper {
	
	public double moveTowards(double height, double targetHeight, double step) {
		if (this.isInstantStep(height, targetHeight, step)) {
			return targetHeight;
		}
		double signum = Math.signum(targetHeight - height);
		return height + signum * step;
	}
	
	private boolean isInstantStep(double height, double targetHeight, double step) {
		return Math.abs(targetHeight - height) <= step;
	}
}
