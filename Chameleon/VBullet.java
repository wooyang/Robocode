//this class is implement by Paul Evans
package wilson;

class VBullet {
	String targetName;
	int guessIndex;				//represents the Guess Factor used
	int distanceIndex;			//represents the distance range
	double originX;				//the location where the VBullet was fired from
	double originY;
	double absBearing;			//and on which heading (0.0 up, +ve clockwise)
	long time;					//and at which time
	double power;				//and at what power
	
	VBullet (String targetName, int guessIndex, int distanceIndex, double originX, double originY, double absBearing, long time, double power) {
		this.targetName = targetName;
		this.guessIndex = guessIndex;
		this.distanceIndex = distanceIndex;
		this.originX = originX;
		this.originY = originY;
		this.absBearing = absBearing;
		this.time = time;
		this.power = Math.max(0.1,Math.min(3.0,power));
		}
	
	public String toString() {		//useful for debugging
		return ("targetName= "+targetName+"   guessIndex= "+guessIndex+"   distanceIndex= "+distanceIndex+"   originX = "+originX+"   originY = "+originY+"   absBearing = "+absBearing+"   time = "+time+"   power = "+power);
	}
	
	public double distance(double now) {  //double because targetWasHit uses double
		return (now - time) * (20.0 - 3.0*power);		//return the distance the bullet is away from the origin at the specified time
	}
	
	public double getX(double now) {		//returns the x position of the bullet at a particular time
		return originX + Math.sin(absBearing)*distance(now);
	}				

	public double getY(double now) {		//returns the y position of the bullet at a particular time
		return originY + Math.cos(absBearing)*distance(now);
	}
	
	public boolean targetWasHit(double targetX, double targetY, long now) {		//for this target, at this time does the bullet come within 30 units of the target over the last tick			
		
		if (range(targetX,targetY,now) > 50.0) return false;  // if we are no where near return immediatly
		for (double fraction = 0.0; fraction<1.05; fraction+=0.1) {  //if we are close check 10 positions of the bullet to make sure it does not hop over the target
			if (range(targetX,targetY,now-fraction) < 30.0) return true;
		}
		return false;  //close but not close enough over the last tick
	}
	
	public boolean outOfRange(double targetX, double targetY, long now) {		//has the bullet traveled beyond the range of the enemy
		double targetFromOrigin = Math.sqrt((targetX-originX)*(targetX-originX)+(targetY-originY)*(targetY-originY));
		if (range(originX, originY, now) > targetFromOrigin+30.0) return true; else return false;  //if our bullet is 30 units further away than our target (from where the bullet eas fired from) it will never hit the target
	}
	
	public double range( double targetX, double targetY, double now) {		//the distance between the bullet and the target at the specified time.
		double xo = targetX-this.getX(now);
		double yo = targetY-this.getY(now);
		return Math.sqrt( ( (xo)*(xo) ) + ( (yo)*(yo) ) );
	}

}
