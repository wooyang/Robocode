package wilson;

class EnemyInfo 
{
	public String name;
	public double absBearing;
	public double bearing;//the angle between my heading and the enemy's direction
	public double heading;
	public long cTime;
	public double velocity;
	public double x,y;
	public double distance;
	public double energy;
	public double headingChange;
	public double energyChange;
	public double velocityChange;
	public double xChange;
	public double yChange;
	public double estimateVelocity;
	public double estimateHeading;
	//public double timeChange;
	
	public Object clone(){
		EnemyInfo e = new EnemyInfo();
		e.name = this.name;
		e.absBearing = this.absBearing;
		e.bearing = this.bearing;
		e.heading = this.heading;
		e.cTime = this.cTime;
		e.velocity = this.velocity;
		e.x = this.x;
		e.y = this.y;
		e.distance = this.distance;
		e.energy = this.energy;
		e.headingChange = this.headingChange;
		e.energyChange = this.energyChange;
		e.velocityChange = this.velocityChange;
		e.xChange = this.xChange;
		e.yChange = this.yChange;
		e.estimateVelocity = this.estimateVelocity;
		e.estimateHeading = this.estimateHeading;
		return e;
	}
}			
