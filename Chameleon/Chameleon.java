package wilson;

import robocode.*;
import java.awt.Color;

public class Chameleon extends AbstractRobot
{
	public void run() {
		Color robotColor = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
		Color gunColor = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
		Color radarColor = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
		setColors(robotColor,gunColor,radarColor);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		do{
			turnRadarRightRadians(1);	
		}
		while(true);
	}
	
	public void doMovement(){
		if (nowEnemy.distance >250){
			Movement.roam(this,nowEnemy);
		} else {
			Movement.roam2(this,nowEnemy);
		}
	}
		
	public void doGun(){
		if(nowEnemy.distance>200){
				Gun.statisticAiming(this,nowEnemy);
			if (getGunHeat()==0 && getEnergy()>.2 && firePower>0){
				setFire(firePower);
			}
		}else {
			isFire = false;
			if (getGunHeat()==0 && getEnergy()>3) {
				isFire = setFireBullet(Math.min(vFirePower = Math.min(11/(dIndex+1) + .5,3),Math.min(nowEnemy.energy/4,(getEnergy()-3)/2))) != null;
			} else if (nowEnemy.distance<200) {
				setFire(getEnergy()/1.5);
			}
			Gun.vBulletAiming(this,nowEnemy);
		}
	}
	
	public void doRadar(){
		setTurnRadarRightRadians(Support.normaliseRelativeAngle(nowEnemy.absBearing - getRadarHeadingRadians())*2);
	}
	
	public void onSkippedTurn(SkippedTurnEvent e) {
		out.println("shit"+getTime());
	}
}
