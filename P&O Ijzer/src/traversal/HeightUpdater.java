package traversal;



import controllers.SensorController.TimeoutException;
import zeppelin.MainProgramImpl;

public class HeightUpdater implements Runnable {
	
	private MainProgramImpl zeppelin;
	
	public HeightUpdater(MainProgramImpl zeppelin)
	{
		this.zeppelin = zeppelin;
	}
	
	@Override
	public void run()
	{
		while (true)
		{
			try {
				this.getZeppelin().moveTowardsTargetHeight();
				Thread.sleep(10);
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public MainProgramImpl getZeppelin()
	{
		return this.zeppelin;
	}

}
