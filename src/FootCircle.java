import processing.core.PApplet;
import processing.core.PVector;
/**
 * Circles designed to come from the feet. Trigger only when the interactor's feet are on the ground.
 * @author Sara, Zhiling, Isabelle
 *
 */
public class FootCircle implements Circle {
	
	PApplet parent;
	private int color, rval, gval, bval;
	private float x = -1;
	private float y = -1;
	private float speed = .02f;
	private float diam;
	private static final float MAX_DIAM = 3f;
	private static final float INIT_DIAM = .1f;
	private long startTime, stagger;
	private int fadeRate = 2;

	//private float[] prevPos;
	private boolean onFloor = false;	//whether or not the foot is on the floor
	private boolean stop = false;
	
	private double[] distFromSpine;	//an array of values maintaining the distance between the spine and the foot for the past couple checks
	private double currentDist, average;	//the current distance between the spine and foot, and the average of distFromSpine's values
	private int index;	//the current place in the array to insert the latest distance


	public FootCircle(PApplet parent, long stagger) {
		startTime = System.currentTimeMillis();
		this.stagger = stagger;
		this.parent = parent;
		rval = 40;
		gval = 200;
		bval = 250;
		color = parent.color(rval,gval,bval);
		distFromSpine = new double[30];
		index = 0;
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
		//as long as we've waited the appropriate stagger time
		if (System.currentTimeMillis() - startTime > stagger) {
			// *** if the foot is on the ground it will ripple
			if ( (this.x == -1 || this.y == -1 ) || (onFloor) && (diam >= MAX_DIAM) &&
					!(x < -100 && y <-100)) 
			{
				//reset the ripple to its initial state
				this.x = x;
				this.y = y;
				diam = INIT_DIAM;
				rval = 40;
				gval = 200;
				bval = 250;
				color = parent.color(rval, gval, bval);
				startTime = System.currentTimeMillis();
			} else {
				//else expand the ripple and darken its color slightly
				diam += speed;
				rval -= fadeRate;
				if(rval < 0) rval = 0;
				gval -= fadeRate;
				if(gval < 0) gval = 0;
				bval -= fadeRate;
				if(bval < 0) bval = 0;
				color = parent.color(rval, gval, bval);
			}
		}
	}

	@Override
	public void display() {
		parent.stroke(color);
		parent.ellipse(x, y, diam, diam/2);
	}
	

	/**
	 * Determine whether the foot is currently where the floor is expected to be
	 * @param floor location of the floor
	 * @param foot the foot
	 */
	public void isOnFloor(float floor, PVector foot)
	{	
		//System.out.println(floor +"\t"+this.y + "\t" + (this.y <= floor));
		if(foot != null) {
			if(foot.mag() <= floor)
				onFloor= true;
			else
				onFloor = false;
		}

	}
	
	/**
	 * @param stop True to stop the ripple, false to make it continue
	 */
	public void stopRipples(boolean stop) {
		if(stop) 
			this.stop = true;
		else
			this.stop = false;
	}
}
