package positioning;

import java.io.IOException;

import coordinate.Grid;
import coordinate.GridPoint;
import coordinate.GridTriangle;
import zeppelin.MainProgramImpl;

public class PositionUpdater implements Runnable {
	
	private MainProgramImpl zeppelin;
	
	public PositionUpdater(MainProgramImpl zeppelin)
	{
		this.zeppelin = zeppelin;
	}
	
	public void run()
	{
		while (true)
		{
			this.update();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public MainProgramImpl getZeppelin()
	{
		return this.zeppelin;
	}
	
	public void update()
	{
		try {
			Image img = this.getZeppelin().captureImage();

			ReadCouples readCouples = new ReadCouples(img);
			GridTriangle triangle = triangleMatch(this.getZeppelin().getGrid(), img, readCouples);

			Couple pictureCouple = null;
			Couple triangleCouple = null;

			for (Couple pictureCoupleFor : readCouples.getListCouples())
			{
				Couple triangleCoupleFor = triangle.getMatchingCouple(pictureCoupleFor);
				if (triangleCoupleFor == null)
				{
					continue;
				}
				pictureCouple = pictureCoupleFor;
				triangleCouple = triangleCoupleFor;
				break;
			}

			AngleCalculator calc = new AngleCalculator(img, pictureCouple, triangleCouple);
			double angle = calc.calculateAngle();

			PositionCalculator calcPos = new PositionCalculator(img, pictureCouple, triangleCouple);
			GridPoint position = calcPos.calculatePosition(angle);

			this.getZeppelin().setAngle(angle);
			this.getZeppelin().setPosition(position);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private GridTriangle triangleMatch(Grid grid, Image image, ReadCouples readCouples) throws IOException, InterruptedException {
		CoupleTriangleMatcher matcher = new CoupleTriangleMatcher(grid, readCouples, new GridPoint(0,0));
		GridTriangle triangle = matcher.matchCouplesWithTriangles();
		return triangle;
	}

}
