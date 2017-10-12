import processing.core.*;

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
	private boolean stop;
	private float fadeRate = 1;
	
	//private int alpha;
	
	public HeadCircle(PApplet parent, long stagger) {
		startTime = System.currentTimeMillis();
		this.stagger = stagger;
		this.parent = parent;
		rval = 255;
		gval = 215;
		bval = 0;
		//alpha = 255;
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
		if (System.currentTimeMillis() - startTime > stagger) {
			if ((this.x == -1 || this.y == -1 || diam > MAX_DIAM) && !(x < -1 && y <-1)) {
				if (!stop) {
					this.x = x;
					this.y = y;
					diam = INIT_DIAM;
					rval = 255;
					gval = 215;
					//alpha = 255;
					color = parent.color(rval, gval, bval);
				} else {
					startTime = System.currentTimeMillis();
				}
			} else {
				diam += speed;
				rval -= fadeRate;
				if(rval < 0) rval = 0;
				gval -= fadeRate;
				if(gval < 0) gval = 0;
				//alpha -= fadeRate;
				color = parent.color(rval, gval, bval);
			}
		}
	}
	
	public void stopRipples(boolean stop) {
		if(stop) 
			this.stop = true;
		else
			this.stop = false;
	}

	@Override
	public void display() {
		//parent.stroke(color, alpha);
		parent.stroke(color);
		parent.ellipse(x, y, diam, diam);
	}

}
