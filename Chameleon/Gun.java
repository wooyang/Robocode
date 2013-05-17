package wilson;

import java.util.*;
import java.awt.geom.*;

/**
 * Gun - include targeting strategy
 */
public class Gun
{
	public static void vBulletAiming(AbstractRobot ar,EnemyInfo enemy) {
		Point2D.Double nowP = new Point2D.Double(ar.getX(),ar.getY());
		Iterator vbi = ar.vBulletList.iterator();
		while(vbi.hasNext()){
			VBullet vb = (VBullet) vbi.next();
			if (vb.targetWasHit(enemy.x,enemy.y,enemy.cTime)){
				AbstractRobot.data[vb.distanceIndex][vb.guessIndex] = AbstractRobot.data[vb.distanceIndex][vb.guessIndex]*.99+.01;
				vbi.remove();
			} else if (vb.outOfRange(enemy.x,enemy.y,enemy.cTime)){
				AbstractRobot.data[vb.distanceIndex][vb.guessIndex] *= .99;
				vbi.remove();
			}
		}
		
		//shoot virtual Bullet
		double vBulletVelocity = 20-3*ar.vFirePower;
		long time = Math.round(enemy.distance/vBulletVelocity);
		double v = Math.abs(enemy.estimateVelocity)<1 ? ar.enemyAverageVelocity : enemy.estimateVelocity;
		if (ar.isFire){
			for (int i=0;i<11;i++){
				double d = (i-5)/5.0d * v * time;
				Point2D.Double guessP = new Point2D.Double(enemy.x+d*Math.sin(enemy.estimateHeading),enemy.y+d*Math.cos(enemy.estimateHeading)); 
				double ang = Support.getAbsoluteBearing(nowP,guessP);
				ar.vBulletList.add(new VBullet(enemy.name,i,ar.dIndex,nowP.x,nowP.y,ang,enemy.cTime,ar.vFirePower));
			}
		}
		
		//select best factor
		if (ar.getGunHeat()<.4){
			for (int i=0;i<11;i++){
				if (AbstractRobot.data[ar.dIndex][i]>AbstractRobot.data[ar.dIndex][AbstractRobot.bestFactor]){
					AbstractRobot.bestFactor = i;
				}
			}
			double l = (AbstractRobot.bestFactor-5)/5.0d*v*time;
			double angle = Support.getAbsoluteBearing(nowP,new Point2D.Double(enemy.x+l*Math.sin(enemy.estimateHeading),enemy.y+l*Math.cos(enemy.estimateHeading)));
			ar.setTurnGunRightRadians(Support.normaliseRelativeAngle(angle-ar.getGunHeadingRadians()));
		} else {
			ar.setTurnGunRightRadians(Support.normaliseRelativeAngle(enemy.absBearing-ar.getGunHeadingRadians()));	
		}
	}	
	
	public static void statisticAiming(AbstractRobot ar,EnemyInfo enemy){
		Point2D.Double nowP = new Point2D.Double(ar.getX(),ar.getY());
		Point2D.Double enemyP = new Point2D.Double(enemy.x,enemy.y);
		double fieldWidth = ar.getBattleFieldWidth();
		double fieldHeight = ar.getBattleFieldHeight();
		//ar.enemyInfoList.addElement(enemy.clone());
		if (ar.enemyInfoList.size()>0 && ar.getEnergy()>.3){
			EnemyInfo lastEnemy = (EnemyInfo)ar.enemyInfoList.lastElement();
			if (enemy.cTime-lastEnemy.cTime > 1){
				ar.enemyInfoList.clear();
				System.out.println("clear"+ar.getTime());
			} else {
				ar.enemyInfoList.addElement(enemy.clone());
				if (ar.enemyInfoList.size() == 81) {
					//update infomation array
					EnemyInfo tempEnemy = (EnemyInfo)ar.enemyInfoList.elementAt(0);
					int aIndex = tempEnemy.velocityChange>=1 ? 2 : (tempEnemy.velocityChange<=-1 ? 0 : 1);
					int vIndex = tempEnemy.velocity>=1 ? 2 :(tempEnemy.velocity<=-1 ? 0 : 1);
					int hIndex = tempEnemy.headingChange>=.06 ? 2 : (tempEnemy.headingChange<=-.06 ? 0 : 1);
					for (int i=0;i<AbstractRobot.ENTRY_NUMBER;i++){
						EnemyInfo ei = (EnemyInfo)ar.enemyInfoList.elementAt(i+1);
						double tempD = Support.getDistance(ei.x,ei.y,tempEnemy.x,tempEnemy.y);
						double tempA = Support.getAbsoluteBearing(tempEnemy.x,tempEnemy.y,ei.x,ei.y);
						Position p = Support.getPointPosition(tempA-tempEnemy.heading,tempD);
						HashMap tempMap = AbstractRobot.infoData[aIndex][vIndex][hIndex][i];
						Iterator vi = tempMap.values().iterator();
						while(vi.hasNext()){
							MyDouble tempMD = (MyDouble)vi.next();
							tempMD.value *= .99;
						}
						if (tempMap.containsKey(p)){
							MyDouble tempMD = (MyDouble)tempMap.get(p);
							tempMD.value +=.01;
						}else {
							tempMap.put(p,new MyDouble(.01));
						}
					}
					ar.enemyInfoList.remove(0);
				}
			}
		}else if (ar.enemyInfoList.size() == 0) ar.enemyInfoList.addElement(enemy.clone());
		
		if (ar.getGunHeat() < .4 && enemy.energy > 0){
			double maxPower = Math.max(.11,Math.min(enemy.energy/5,ar.getEnergy()>5 ? 3.1 : .21)); 
			double minPower = .01;
			int aIndex = enemy.velocityChange>=1 ? 2 : (enemy.velocityChange<=-1 ? 0 : 1);
			int vIndex = enemy.velocity>=1 ? 2 :(enemy.velocity<=-1 ? 0 : 1);
			int hIndex = enemy.headingChange>.06 ? 2 : (enemy.headingChange<-.06 ? 0 : 1);
			double bestEnergy = 0;
			double bestAngle = 0;
			double bestValue = Double.NEGATIVE_INFINITY;
			for (int i=(int)(enemy.distance/28);i<AbstractRobot.ENTRY_NUMBER;i++) {
				HashMap tempMap = AbstractRobot.infoData[aIndex][vIndex][hIndex][i];
				Iterator ki = tempMap.keySet().iterator();
				//Vector vp = new Vector();
				while(ki.hasNext()){
					Position pp = (Position)ki.next();
					double rate = ((MyDouble)tempMap.get(pp)).value;
					if (rate>.05){
						double tempD = 40*Support.getDistance(0,0,pp.x,pp.y);
						double tempA = Math.atan2(pp.x,pp.y);
						Point2D.Double nextP = Support.nextPoint(enemyP,tempA+enemy.heading,tempD);
						if (Support.distanceToWall(nextP,fieldWidth,fieldHeight) >= 20){
							double d = Support.getDistance(nextP,nowP);
							double bv = d/(i+1);
							double be = (20-bv)/3;
							if (be>minPower && be<maxPower){
							//I think this function to caculate 'value' is not the best. You can try another function . It may be more effective. 
								double value = rate>.09 ? 2*rate*be : rate*Math.min(be,2);
								//double value = rate>.1 ? 2*rate*be : rate*(Math.min(be,2));
								if (value>bestValue){
									bestEnergy = be;
									bestAngle = Support.getAbsoluteBearing(nowP,nextP);
									bestValue = value;
								}
							}
						}
					}
				}
			}
			//System.out.println(bestValue);
			if (bestEnergy == 0){
				ar.firePower = -1;
				ar.setTurnGunRightRadians(Support.normaliseRelativeAngle(enemy.absBearing-ar.getGunHeadingRadians()));	
			}else{
				ar.firePower = bestEnergy;
				ar.setTurnGunRightRadians(Support.normaliseRelativeAngle(bestAngle-ar.getGunHeadingRadians()));
			}	
		} else {
			int aIndex = enemy.velocityChange>=1 ? 2 : (enemy.velocityChange<=-1 ? 0 : 1);
			int vIndex = enemy.velocity>=1 ? 2 :(enemy.velocity<=-1 ? 0 : 1);
			int hIndex = enemy.headingChange>=.1 ? 2 : (enemy.headingChange<=-.1 ? 0 : 1);
			for (int i=1;i<AbstractRobot.ENTRY_NUMBER;i++){
				HashMap tempMap = AbstractRobot.infoData[aIndex][vIndex][hIndex][i];
				if (tempMap.size()>50){
					Iterator ki = tempMap.keySet().iterator();
					Vector delVec = new Vector();
					while (ki.hasNext()){
						Position p = (Position)ki.next();
						if (((MyDouble)tempMap.get(p)).value < .006){
							delVec.add(p);
						}
					}
					for (int j=0;j<delVec.size();j++) tempMap.remove(delVec.elementAt(j));
					//System.out.println(tempMap.size());
				}
			}
			ar.firePower = .1;
			ar.setTurnGunRightRadians(Support.normaliseRelativeAngle(enemy.absBearing-ar.getGunHeadingRadians()));	
		}
	}
				
}
