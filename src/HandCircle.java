import processing.core.PApplet;

public class HandCircle implements Circle {

	PApplet parent;
	private int color;
	private float x = -1;
	private float y = -1;
	private float speed = .02f;
	private float diam;
	private static final float MAX_DIAM = 7f;
	private static final float INIT_DIAM = .1f;
	boolean stop = false;
	
	public HandCircle(PApplet parent) {
		this.parent = parent;
		color = parent.color(255,130,67);
	}
	
	@Override
	public int getColor() {
		return color;
	}

	@Override
	public float getSpeed() {
		return speed;
	}

	@Override
	public void update(float x, float y) {
		if(this.x == -1 || this.y == -1 || diam > MAX_DIAM) {
			this.x = x;
			this.y = y;
			diam = INIT_DIAM;
		}
		if(!stop)
		{
			parent.stroke(color);
			diam += speed;
		}
		else {
			parent.noStroke();
		}
	}

	@Override
	public void display() {
		parent.stroke(color);
		parent.ellipse(x, y, diam, diam);
	}
	
	public void checkIntersect(float handX, float handY) {
		double distance = Math.sqrt( 
				(double)Math.pow((handX - this.x), 2) + 
				(double)Math.pow((handY - this.y), 2));
		
		if(distance <= INIT_DIAM)
			stop = true;
			
	}

}
