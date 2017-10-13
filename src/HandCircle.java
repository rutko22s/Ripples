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
	private float maxDiam = .7f;
	private static final float INIT_DIAM = .1f;
	boolean stop = false;
	private long stagger;
	private long startTime;
	private float fadeRate = 2;
	
	//private int alpha;
	
	private int staggerNum;
	
	public HandCircle(PApplet parent, int staggerNum) {
		startTime = System.currentTimeMillis();
		this.stagger = staggerNum * 350;
		this.staggerNum = staggerNum;
		this.parent = parent;
		rval = 255;
		gval = 130;
		bval = 67;
		//alpha = 255;
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
			if ( (this.x == -1 || this.y == -1 || diam > maxDiam) && 
				  !(x < -1 && y <-1)) {
				this.x = x;
				this.y = y;
				diam = INIT_DIAM;
				rval = 255;
				gval = 130;
				bval = 67;
				//alpha = 255;
				color = parent.color(rval, gval, bval);
			} else {
				parent.stroke(color);
				diam += speed;
				if(diam < 1.0f || diam > 2.5) {
					rval -= fadeRate;
					gval -= fadeRate;
					bval -= fadeRate;
				}
				if (bval < 0)
					bval = 0;
				//alpha -=  fadeRate;
					
				color = parent.color(rval, gval, bval);
			}
		}
	}
	
	public void expandDiam(boolean larger) {
		if(larger) {
			maxDiam = 2.7f;
			fadeRate = 1f;
			stagger = staggerNum * 1000;
		} else if (diam > maxDiam) {
			maxDiam = .7f;
			fadeRate = 3;
			stagger = staggerNum * 350;
		}
	}
 
	@Override
	public void display() {
		//parent.stroke(color, alpha);
		parent.stroke(color);
		parent.ellipse(x, y, diam, diam);
	}

}
