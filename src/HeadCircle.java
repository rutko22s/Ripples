import processing.core.*;

public class HeadCircle implements Circle {

	PApplet parent;
	private int color;
	private float x = -1;
	private float y = -1;
	private float speed = .01f;
	private float diam;
	private static final float MAX_DIAM = 10f;
	private static final float INIT_DIAM = .1f;
	
	public HeadCircle(PApplet parent) {
		this.parent = parent;
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
		if(this.x < 0 || this.y < 0 || diam > MAX_DIAM) {
			this.x = x;
			this.y = y;
			diam = INIT_DIAM;
		}
		
		diam += speed;
		
	}

	@Override
	public void display() {
		parent.ellipse(x, y, diam, diam);
	}

}
