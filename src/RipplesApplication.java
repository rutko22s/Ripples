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
		fullScreen(P2D);
	}

	public void setup(){

		/*
		 * use this code to run your PApplet from data recorded by UPDRecorder 
		 */
		try {
			kinectReader = new KinectBodyDataProvider("test2.kinect", 1);
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
		
		for(int i=0; i<15; i++) {
			if (i < 8) {
				leftHandCircles.add(new HandCircle(this, i * 300));
				rightHandCircles.add(new HandCircle(this, i * 300));
				if (i < 4) {
					leftFootCircles.add(new FootCircle(this, i * 100));
					rightFootCircles.add(new FootCircle(this, i * 100));
				}
			}
			headCircles.add(new HeadCircle(this, i*900));
		}
		
		//headCircles.add(tempCircle);

	}
	public void draw(){
		//makes the window 2x2
		this.scale(width/3.0f, -height/2.0f);

		//make positive y up and center of window 0,0
		translate(1,-1);

		background(0,0,0);

		KinectBodyData bodyData = kinectReader.getMostRecentData();
		Body person = bodyData.getPerson(0);
		if(person != null){
			PVector head = person.getJoint(Body.HEAD);
			PVector footLeft = person.getJoint(Body.FOOT_LEFT);
			PVector footRight = person.getJoint(Body.FOOT_RIGHT);
			handLeft = person.getJoint(Body.HAND_LEFT);
			handRight = person.getJoint(Body.HAND_RIGHT);


			fill(255,255,255);
			noStroke();
			//commented out for now -- should be in final or no?
//			drawIfValid(head);
//			drawIfValid(footLeft);
//			drawIfValid(footRight);
			drawIfValid(handLeft);
			drawIfValid(handRight);
			
			noFill();
			strokeWeight(0.009f);
			for(FootCircle ripple : leftFootCircles) 
			{
				rippleIfValid(footLeft, ripple);
			}
			for(FootCircle ripple : rightFootCircles) 
			{
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
//			if(ripple.getClass().toString().equals("class HandCircle")) {
//				if(!checkIntersect()) {
//					((HandCircle)ripple).expandDiam(false);
//					ripple.update(vec.x, vec.y);
//				} else {
//					((HandCircle)ripple).expandDiam(true);
//					ripple.update(vec.x, vec.y);
//				}
//			} else {
//				ripple.update(vec.x, vec.y); 
//			}
			ripple.update(vec.x, vec.y);
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

	public static void main(String[] args) {
		PApplet.main(RipplesApplication.class.getName());
	}

}
