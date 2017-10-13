import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * @author Sara, Zhiling, Isabelle
 *
 */
public class RipplesApplication extends PApplet {

	public static float PROJECTOR_RATIO = 1080f/1920.0f;
	KinectBodyDataProvider kinectReader;
	//HeadCircle tempCircle;
	List<HeadCircle> headCircles;
	List<HandCircle> leftHandCircles;
	List<HandCircle> rightHandCircles;
	List<FootCircle> leftFootCircles;
	List<FootCircle> rightFootCircles;
	int count = 1;
	private boolean wereTogether;
	
	PVector handLeft;
	PVector handRight;

	public void settings() {
		//fullScreen(P2D);
		createWindow(true, false, .5f);
	}

	public void setup(){

		/*
		 * use this code to run your PApplet from data recorded by UPDRecorder 
		 */
		try {
			kinectReader = new KinectBodyDataProvider("test2.kinect", 3);
		} catch (IOException e) {
			System.out.println("Unable to create kinect producer");
		} 
		 
		//kinectReader = new KinectBodyDataProvider(8008);
		kinectReader.start();
		
		headCircles = new ArrayList<HeadCircle>();
		leftHandCircles = new ArrayList<HandCircle>();
		rightHandCircles = new ArrayList<HandCircle>();
		leftFootCircles = new ArrayList<FootCircle>();
		rightFootCircles = new ArrayList<FootCircle>();
		wereTogether = false;
		
		for(int i=0; i<10; i++) {
			if (i < 8) {
				leftHandCircles.add(new HandCircle(this, i));
				rightHandCircles.add(new HandCircle(this, i));
				if (i < 4) {
					leftFootCircles.add(new FootCircle(this, i * 200));
					rightFootCircles.add(new FootCircle(this, i * 200));
				}
			}
			headCircles.add(new HeadCircle(this, i*1000));
		}

	}
	public void draw(){
		//makes the window 2x2
		//this.scale(width/3.0f, -height/2.0f);

		//make positive y up and center of window 0,0
		//translate(1,-1);
		setScale(.5f);

		background(0);

		KinectBodyData bodyData = kinectReader.getMostRecentData();
		Body person = bodyData.getPerson(0);
		if(person != null){
			PVector head = person.getJoint(Body.HEAD);
			PVector spineBase = person.getJoint(Body.SPINE_BASE);
			PVector footLeft = person.getJoint(Body.FOOT_LEFT);
			PVector footRight = person.getJoint(Body.FOOT_RIGHT);
			handLeft = person.getJoint(Body.HAND_LEFT);
			handRight = person.getJoint(Body.HAND_RIGHT);


			//fill(255,255,255);
			//noStroke();
			//commented out for now -- should be in final or no?
//			drawIfValid(head);
//			drawIfValid(footLeft);
//			drawIfValid(footRight);
//			drawIfValid(handLeft);
//			drawIfValid(handRight);
			
			noFill();
			strokeWeight(0.009f);
			for(FootCircle ripple : leftFootCircles) 
			{
				//ripple.distanceAverage((int)spineBase.y);
				rippleIfValid(footLeft, ripple);
			}
			for(FootCircle ripple : rightFootCircles) 
			{
				//ripple.distanceAverage((int)spineBase.y);
				rippleIfValid(footRight, ripple);
			}
			boolean handsTogether = checkIntersect();
			for(HandCircle ripple : leftHandCircles)
			{
				ripple.expandDiam(handsTogether);
				rippleIfValid(handLeft, ripple);	
			}
			for(HandCircle ripple : rightHandCircles)
			{			
				ripple.expandDiam(handsTogether);
				rippleIfValid(handRight, ripple);	
			}
			for(HeadCircle ripple : headCircles)
			{
				ripple.stopRipples(handsTogether);
				rippleIfValid(head, ripple);	
			}
		}
		
	}

	/**
	 * Calls the draw and update method of a circle.
	 * Will do nothing is vec is null.  This is handy because get joint 
	 * will return null if the joint isn't tracked. 
	 * @param vec
	 */
	public void drawIfValid(PVector vec) {
		if(vec != null) {
			ellipse(vec.x, vec.y, .1f,.1f);
		}

	}
	
	/**
	 * Calls the display and update method of a circle.
	 * Will do nothing is vec is null.  This is handy because get joint 
	 * will return null if the joint isn't tracked. 
	 * @param vec
	 */
	public void rippleIfValid(PVector vec, Circle ripple) {
		if(vec != null) {
			ripple.update(vec.x, vec.y);
			ripple.display();
		}
		else
		{
			ripple.update(-2, -2);
			ripple.display();
		}

	}

	public boolean checkIntersect() {

		float diam = .1f;
		if (handLeft!=null && handRight!=null)	{
			double distance = Math.sqrt( 
					(double)Math.pow((handLeft.x - handRight.x), 2) + 
					(double)Math.pow((handLeft.y - handRight.y), 2));

			if(distance <= diam) {
				wereTogether = true;
				return true;
			} else {
				wereTogether = false;
				return false;
			}
		} else {
			if(wereTogether)
				return true;
			else
				return false;						
		}
	}
	
	
	
	
	public void createWindow(boolean useP2D, boolean isFullscreen, float windowsScale) {
		if (useP2D) {
			if(isFullscreen) {
				fullScreen(P2D);  			
			} else {
				size((int)(1920 * windowsScale), (int)(1080 * windowsScale), P2D);
			}
		} else {
			if(isFullscreen) {
				fullScreen();  			
			} else {
				size((int)(1920 * windowsScale), (int)(1080 * windowsScale), P2D);
			}
		}		
	}
	
	// use lower numbers to zoom out (show more of the world)
	// zoom of 1 means that the window is 2 meters wide and appox 1 meter tall.
	public void setScale(float zoom) {
		scale(zoom* width/2.0f, zoom * -width/2.0f);
		translate(1f/zoom , -PROJECTOR_RATIO/zoom );		
	}

	public static void main(String[] args) {
		PApplet.main(RipplesApplication.class.getName());
	}

}
