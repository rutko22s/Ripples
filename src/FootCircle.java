import processing.core.PApplet;

public class FootCircle implements Circle {
	
	PApplet parent;
	private int color;
	private int rval;
	private int gval;
	private int bval;
	private float x = -1;
	private float y = -1;
	private float speed = .02f;
	private float diam;
	private static final float MAX_DIAM = 5f;
	private static final float INIT_DIAM = .1f;
	private long startTime;
	private long stagger;
	private int fadeRate = 2;
	
	//private int alpha;

	public FootCircle(PApplet parent, long stagger) {
		startTime = System.currentTimeMillis();
		this.stagger = stagger;
		this.parent = parent;
		rval = 40;
		gval = 200;
		bval = 250;
		color = parent.color(rval,gval,bval);
		
		//alpha = 255;
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
				this.x = x;
				this.y = y;
				diam = INIT_DIAM;
				rval = 40;
				gval = 200;
				bval = 250;
				color = parent.color(rval, gval, bval);
				//alpha = 255;
			} else {
				if ((x - this.x < 5 || this.x - x < 5) && (y - this.y < 5)) {
					parent.stroke(color);
				} else {
					parent.noStroke();
				}

				diam += speed;
				rval -= fadeRate;
				gval -= fadeRate;
				bval -= fadeRate;
				color = parent.color(rval, gval, bval);
				//alpha -= fadeRate;
			}
		}
	}

	@Override
	public void display() {
		//parent.stroke(color, alpha);
		parent.stroke(color);
		parent.ellipse(x, y, diam, diam/2);
	}

}
