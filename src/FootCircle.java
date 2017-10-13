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
	private static final float MAX_DIAM = 3f;
	private static final float INIT_DIAM = .1f;
	private long startTime;
	private long stagger;
	private int fadeRate = 2;
	private double[] distFromSpine;
	private double currentDist;
	private int index;
	private double average;
	
	//private int alpha;

	public FootCircle(PApplet parent, long stagger) {
		startTime = System.currentTimeMillis();
		this.stagger = stagger;
		this.parent = parent;
		rval = 40;
		gval = 200;
		bval = 250;
		color = parent.color(rval,gval,bval);
		distFromSpine = new double[100];
		index =0;
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
			if ((this.x == -1 || this.y == -1 || Math.abs(currentDist-average) > 2) && !(x < -1 && y <-1)) {
				this.x = x;
				this.y = y;
				diam = INIT_DIAM;
				rval = 40;
				gval = 200;
				bval = 250;
				color = parent.color(rval, gval, bval);
				startTime = System.currentTimeMillis();
			} else {
				diam += speed;
				rval -= fadeRate;
				gval -= fadeRate;
				bval -= fadeRate;
				color = parent.color(rval, gval, bval);
			}
		}
	}

	@Override
	public void display() {
		//parent.stroke(color, alpha);
		parent.stroke(color);
		parent.ellipse(x, y, diam, diam/2);
	}
	
	public void distanceAverage(int spineY)
	{
		int sum =0;
		index++;
		if(index >= distFromSpine.length)
			index = 0;
		
		currentDist = Math.abs(spineY- this.y);
		
		distFromSpine[index] = currentDist;
		
		for(int i =0; i < distFromSpine.length; i++)
		{
			sum += distFromSpine[i];
		}
		
		average = sum /distFromSpine.length;
	}

}
