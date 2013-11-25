package movement;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

public class RotationController {
		
		public double calcRotation(Result result) {
			ResultPoint[] points = result.getResultPoints();
			ResultPoint a= points[1];
		    ResultPoint b= points[2];
		    ResultPoint c= points[0];
		    System.out.println("X-co�rdinaat A: " + a.getX() + "; Y-co�rdinaat A: " + a.getY());
		    System.out.println("X-co�rdinaat B: " + b.getX() + "; Y-co�rdinaat B: " + b.getY());
		    System.out.println("X-co�rdinaat C: " + c.getX() + "; Y-co�rdinaat C: " + c.getY());
		    //Find the degree of the rotation that is needed
		    
		    
		    
		    double x = c.getX()-a.getX();
		    double y = c.getY()-a.getY();
		    double theta = Math.toDegrees(Math.atan2(x, -y));
		    theta += 180;
		    if(theta < 0)
		    	theta += 360;
		    return theta;
		}
	}