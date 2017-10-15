import processing.core.*;
/**
 * Circles designed to come from the head. Typically triggered, but cease when the interactor brings their hands together.
 * @author Sara, Zhiling, Isabelle
 *
 */
public class HeadCircle implements Circle {

	PApplet parent;
	private int color;
	private int rval;
	private int gval;
	private int bval;
	private float x = -1;
	private float y = -1;
	private float speed = .01f;
	private float diam;
	private static final float MAX_DIAM = 2.7f;
	private static final float INIT_DIAM = .1f;
	private long startTime;
	private long stagger;
	private boolean stop;	//tells the circle whether to appear
	private float fadeRate = 1;	//since head circles are much larger, they deteriorate at the slowest rate possible
	
	private int alpha;
	
	public HeadCircle(PApplet parent, long stagger) {
		startTime = System.currentTimeMillis();
		this.stagger = stagger;
		this.parent = parent;
		rval = 255;
		gval = 215;
		bval = 0;
		alpha = 255;
		color = parent.color(rval,gval,bval);
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
		//once we have waited the appropriate amount of stagger time
		if (System.currentTimeMillis() - startTime > stagger) {
			//if the circle has reached its maximum diameter
			if ((this.x == -1 || this.y == -1 || diam > MAX_DIAM) && !(x < -1 && y <-1)) {
				//and the circle has not been told to stop
				if (!stop) {
					//reset to its beginning state
					this.x = x;
					this.y = y;
					diam = INIT_DIAM;
					rval = 255;
					gval = 215;
					color = parent.color(rval, gval, bval);
				} else {
					//otherwise reset the start time so each ripple is still staggered
					startTime = System.currentTimeMillis();
				}
			} else {
				//else continue to expand and fade out the ripple
				diam += speed;
				rval -= fadeRate;
				if(rval < 0) rval = 0;
				gval -= fadeRate;
				if(gval < 0) gval = 0;
				color = parent.color(rval, gval, bval);
			}
		}
	}
	
	/**
	 * @param stop true if we want the circle to stop its behavior, false if we want it to continue
	 */
	public void stopRipples(boolean stop) {
		if(stop) 
			this.stop = true;
		else
			this.stop = false;
	}

	@Override
	public void display() {
		parent.stroke(color, alpha);
		parent.ellipse(x, y, diam, diam);
	}

}
