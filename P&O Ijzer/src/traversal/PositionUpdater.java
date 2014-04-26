package traversal;

import java.io.IOException;

import positioning.AngleCalculator;
import positioning.Couple;
import positioning.CoupleTriangleMatcher;
import positioning.Image;
import positioning.PositionCalculator;
import positioning.ReadCouples;
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
			//TODO triangle == null als er geen is gevonden in eens straal van 50 cm. ==> Alles behouden.
			if(triangle == null) {
				double angle = this.getZeppelin().getMostRecentAngle();
				GridPoint position = this.getZeppelin().getPosition();
				this.getZeppelin().setAngle(angle);
				this.getZeppelin().setPosition(position);
			}
			else {
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
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private GridTriangle triangleMatch(Grid grid, Image image, ReadCouples readCouples) throws IOException, InterruptedException {
		CoupleTriangleMatcher matcher = new CoupleTriangleMatcher(grid, readCouples, zeppelin.getPosition());
		GridTriangle triangle = matcher.matchCouplesWithTriangles();
		return triangle;
	}

}
