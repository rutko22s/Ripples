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
	private static final float MAX_DIAM = 7f;
	private static final float INIT_DIAM = .1f;
	private long startTime;
	private long stagger;
	
	public HeadCircle(PApplet parent, long stagger) {
		startTime = System.currentTimeMillis();
		this.stagger = stagger;
		this.parent = parent;
		rval = 255;
		gval = 215;
		bval = 0;
		color = parent.color(255,215,0);
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
			if (this.x == -1 || this.y == -1 || diam > MAX_DIAM) {
				this.x = x;
				this.y = y;
				diam = INIT_DIAM;
				rval = 255;
				gval = 215;
				color = parent.color(rval, gval, bval);
			} else {
				diam += speed;
				rval -= 1;
				gval -= 1;
				color = parent.color(rval, gval, bval);
			}
		}
	}

	@Override
	public void display() {
		parent.stroke(color);
		parent.ellipse(x, y, diam, diam);
	}

}
