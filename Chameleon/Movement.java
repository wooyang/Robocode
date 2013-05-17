package wilson;

import java.awt.geom.*;
import java.util.*;

/**
 * Movement - include movement strategy
 */
public class Movement 
{
	//	the movement is similar with iiley's cigret which is an excellent bot
	static public void roam(AbstractRobot ar,EnemyInfo enemy) {
		if (Math.abs(ar.getDistanceRemaining())<53 && enemy.energyChange>0 && enemy.energyChange<=3){
			double fieldWidth = ar.getBattleFieldWidth();
			double fieldHeight  = ar.getBattleFieldHeight();
			RoundRectangle2D.Double rect = new RoundRectangle2D.Double(AbstractRobot.WALL_DISTANCE,AbstractRobot.WALL_DISTANCE,fieldWidth-2*AbstractRobot.WALL_DISTANCE,fieldHeight-2*AbstractRobot.WALL_DISTANCE,20,20);
			Point2D.Double nextP;
			Point2D.Double nowPoint = new Point2D.Double(ar.getX(),ar.getY());
			double angle;
			double absAngle; 
			double l;
			double distance = enemy.distance;
			do{
				angle = Support.getRandom(-.8,.8); 
				absAngle = enemy.absBearing + angle;
				if((l=distance) < 550d){
    				    	l/=Math.cos(angle);
		    		}
				distance -= 10d;
			}while(!rect.contains(nextP=Support.nextPoint(new Point2D.Double(enemy.x,enemy.y),absAngle,-l)));
			ar.setTurnRightRadiansOptimal(Support.getAbsoluteBearing(nowPoint,nextP)-ar.getRelativeHeadingRadians());
			ar.setAhead(nowPoint.distance(nextP)*ar.direction);
		}
    		ar.setMaxVelocity( Math.abs(ar.getTurnRemaining()) > 45 ? 0d : 8d );
	}
	
	static public void roam2(AbstractRobot ar,EnemyInfo enemy) {
		if (Math.abs(ar.getDistanceRemaining())<20 && (enemy.energyChange>0 && enemy.energyChange<=3)) {
			double minBearing = Support.HALF_PI + (enemy.distance>400 ? -.30 : .35);
			double maxBearing = Support.HALF_PI + (enemy.distance>400 ? .05 : .5);
			double distance = enemy.distance>400 ? Support.getRandom(10,.7*enemy.distance) : 40+Support.getRandom(0,enemy.distance/2);
			double fieldWidth = ar.getBattleFieldWidth();
			double fieldHeight  = ar.getBattleFieldHeight();
			RoundRectangle2D.Double rect = new RoundRectangle2D.Double(AbstractRobot.WALL_DISTANCE,AbstractRobot.WALL_DISTANCE,fieldWidth-2*AbstractRobot.WALL_DISTANCE,fieldHeight-2*AbstractRobot.WALL_DISTANCE,20,20);
			Point2D.Double nowPoint = new Point2D.Double(ar.getX(),ar.getY());
			Vector pointVector = new Vector();
			for(double i=minBearing;i<=maxBearing;i=i+.05){
				Point2D.Double p = Support.nextPoint(nowPoint,i+enemy.absBearing,distance);
				if (rect.contains(p)) pointVector.add(p);
				p = Support.nextPoint(nowPoint,-i+enemy.absBearing,distance);
				if (rect.contains(p)) pointVector.add(p);
			}
			Point2D.Double nextPoint = new Point2D.Double();
			if (pointVector.size() > 0){
				nextPoint = (Point2D.Double)pointVector.elementAt((int)Support.getRandom(0,pointVector.size()));
			} else {
				distance = Math.min(distance,250);
				double ang = minBearing;
				int sign = -1;
				boolean flag = true;
				while (flag) {
					if (sign==1) {
						sign = -1;
					}else {
						ang -= .05;
						sign = 1;
					}
					nextPoint = Support.nextPoint(nowPoint,sign*ang+enemy.absBearing,distance);
					if (rect.contains(nextPoint)) flag = false;
				}
			}
			ar.setTurnRightRadiansOptimal(Support.getAbsoluteBearing(nowPoint,nextPoint)-ar.getRelativeHeadingRadians());
			ar.setAhead(nowPoint.distance(nextPoint)*ar.direction);
		}
    		ar.setMaxVelocity( Math.abs(ar.getTurnRemaining()) > 45 ? 0d : 8d );
	}
}
