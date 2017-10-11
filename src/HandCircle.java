import processing.core.PApplet;

public class HandCircle implements Circle {

	PApplet parent;
	private int color;
	private int rval;
	private int gval;
	private int bval;
	private float x = -1;
	private float y = -1;
	private float speed = .005f;
	private float diam;
	private float maxDiam = .6f;
	private static final float INIT_DIAM = .1f;
	boolean stop = false;
	private long stagger;
	private long startTime;
	private float fadeRate = 2;
	
	public HandCircle(PApplet parent, long stagger) {
		startTime = System.currentTimeMillis();
		this.stagger = stagger;
		this.parent = parent;
		rval = 255;
		gval = 130;
		bval = 67;
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
		if (System.currentTimeMillis() - startTime > stagger) {
			if (this.x == -1 || this.y == -1 || diam > maxDiam) {
				this.x = x;
				this.y = y;
				diam = INIT_DIAM;
				rval = 255;
				gval = 130;
				bval = 67;
				color = parent.color(rval, gval, bval);
			} else {
				// if (!stop) {
				parent.stroke(color);
				diam += speed;
				rval -= fadeRate;
				gval -= fadeRate;
				bval -= fadeRate;
				if (bval < 0)
					bval = 0;
				color = parent.color(rval, gval, bval);
				// } else {
				// parent.noStroke();
				// }
			}
		}
	}
	
	public void expandDiam(boolean larger) {
		if(larger) {
			maxDiam = 7f;
			fadeRate = 1;
		} else {
			maxDiam = .6f;
			fadeRate = 2;
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
		else
			stop = false;
			
	}

}
