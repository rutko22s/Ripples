import processing.core.PApplet;

/**
 * Circles specifically intended to come from the hands. Constantly triggered, but grow in size when the interactor
 * brings their hands together.
 * @author Sara, Zhiling, Isabelle
 *
 */
public class HandCircle implements Circle {

	PApplet parent;
	private int color;	//maintains the current color of the circle
	private int rval;	//current red value of the circle
	private int gval;	//green value of the circle
	private int bval;	//blue value of the circle
	private float x = -1;
	private float y = -1;
	private float speed = .005f;	//rate at which the circle expands
	private float diam;
	private float maxDiam = .7f;	//maximum diameter that the circle is allowed to reach before resetting
	private static final float INIT_DIAM = .1f;	//diameter that the circle begins at
	boolean stop = false;	//whether or not to stop the circle's current trajectory
	private long stagger;	//the amount of time to delay before displaying and updating the circle
	private long startTime;	//when the circle was first initialized (or if we are starting the circle over after a stop)
	private float fadeRate = 2;	//the rate at which the circle will fade into blackness	
	private int staggerNum;	//a ratio for determining stagger
	
	public HandCircle(PApplet parent, int staggerNum) {
		startTime = System.currentTimeMillis();
		this.stagger = staggerNum * 350;
		this.staggerNum = staggerNum;
		this.parent = parent;
		//initial r,g,b values
		rval = 255;
		gval = 130;
		bval = 67;
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
		//if we have waited the appropriate amount of stagger time
		if (System.currentTimeMillis() - startTime > stagger) {
			//if the circle has become large enough or the hands can't be found
			if ( (this.x == -1 || this.y == -1 || diam > maxDiam) && 
				  !(x < -1 && y < -1)) {
				//reset the circle to its initial state
				this.x = x;
				this.y = y;
				diam = INIT_DIAM;
				rval = 255;
				gval = 130;
				bval = 67;
				color = parent.color(rval, gval, bval);
			} else {
				//else expand the circle, and make it darker to create a fading effect
				parent.stroke(color);
				diam += speed;
				//delay the fade rate when the circles expand for a long time
				if(diam < 1.0f || diam > 2.5) {
					rval -= fadeRate;
					gval -= fadeRate;
					bval -= fadeRate;
				}
				//stop bval if it goes below 0
				if (bval < 0)
					bval = 0;
					
				//set the new color
				color = parent.color(rval, gval, bval);
			}
		}
	}
	
	/**
	 * Change the limit to which the circle expands
	 * @param larger true if we want the circle to be the larger size, false if we want the smaller size
	 */
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
		parent.stroke(color);
		parent.ellipse(x, y, diam, diam);
	}

}
