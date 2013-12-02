package movement;

import com.google.zxing.ResultPoint;

public class RotationController {
		
		public double calcRotation(ResultPoint[] points) {
			ResultPoint a= points[1];
		    ResultPoint b= points[2];
		    ResultPoint c= points[0];
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