package wilson;

import java.awt.geom.*;

/**
 * Support - some utility method we will use
 */ 
public class Support 
{
	public final static double PI = Math.PI;
	public final static double DOUBLE_PI = 2*Math.PI;
	public final static double HALF_PI = Math.PI/2;
 
	public static double normaliseRelativeAngle(double angle) {
		angle %= DOUBLE_PI;
		if (angle > PI) {
			angle -= DOUBLE_PI;
		} else if (angle < -PI) {
			angle +=DOUBLE_PI;
		}
		return angle;
	}
 
	public static double normaliseAbsoluteAngle(double angle) {
		angle %= DOUBLE_PI;
		return angle>=0 ? angle : angle+DOUBLE_PI;
	}
 
	public static double getAbsoluteBearing(Point2D.Double p1,Point2D.Double p2) {
		return Math.atan2(p2.x-p1.x,p2.y-p1.y);
	}
 
	public static double getAbsoluteBearing(double x1,double y1,double x2,double y2) {
		return Math.atan2(x2-x1,y2-y1);
	}
 
	public static double getDistance(Point2D.Double p1,Point2D.Double p2) {
		double x = p2.x - p1.x;
		double y = p2.y - p1.y;
		return Math.sqrt(x*x + y*y);
	}
 
	public static double getDistance(double x1,double y1,double x2,double y2) {
		double x = x2 - x1;
		double y = y2 - y1;
		return Math.sqrt(x*x + y*y);
	}
	
	public static double getRandom(double min,double max) {
		return min + Math.random() * (max - min);
	}
	
	public static Point2D.Double nextPoint(Point2D.Double originPoint,double angle,double distance){
		return new Point2D.Double(originPoint.x+Math.sin(angle)*distance,originPoint.y+Math.cos(angle)*distance);
	}
	
	public static Position getPointPosition(double angle,double distance){
		return new Position(Math.round(Math.sin(angle)*distance/40d),Math.round(Math.cos(angle)*distance/40d));
	}
	
	public static double nextPointDistanceToWall(Point2D.Double originPoint,double angle,double distance,double fieldWidth,double fieldHeight){
		double x = originPoint.x + Math.sin(angle)*distance;
		double y = originPoint.y + Math.cos(angle)*distance;
		return Math.min(Math.min(x,y),Math.min(fieldWidth-x,fieldHeight-y));
	}
	
	public static double distanceToWall(Point2D.Double p,double fieldWidth,double fieldHeight){
		return Math.min(Math.min(p.x,p.y),Math.min(fieldWidth-p.x,fieldHeight-p.y));
	}
}