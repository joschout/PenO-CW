package positioning;

import controllers.SensorController.TimeoutException;
import zeppelin.MainProgramImpl;

public class HeightUpdater implements Runnable {
	
	private MainProgramImpl zeppelin;
	
	public HeightUpdater(MainProgramImpl zeppelin)
	{
		this.zeppelin = zeppelin;
	}
	
	public void run()
	{
		while (true)
		{
			try {
				this.getZeppelin().setHeight(this.getZeppelin().measureHeight());
				this.getZeppelin().
				Thread.sleep(100);
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
