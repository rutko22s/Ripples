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
	HeadCircle tempCircle;
	List<HeadCircle> headCircles;
	int count = 1;

	public void settings() {
		fullScreen(P2D);
	}

	public void setup(){

		/*
		 * use this code to run your PApplet from data recorded by UPDRecorder 
		 */
		try {
			kinectReader = new KinectBodyDataProvider("test.kinect", 2);
		} catch (IOException e) {
			System.out.println("Unable to create kinect producer");
		}
		 
		//kinectReader = new KinectBodyDataProvider(8008);
		kinectReader.start();
		
		tempCircle = new HeadCircle(this);
		headCircles = new ArrayList<HeadCircle>();
		headCircles.add(tempCircle);

	}
	public void draw(){
		//makes the window 2x2
		this.scale(width/3.0f, -height/2.0f);

		//make positive y up and center of window 0,0
		translate(1,-1);

		background(0,0,0);
		
		if( count < 10 && (frameCount == ((int)frameRate * count )))
		{
			headCircles.add(new HeadCircle(this));
			count++;
		}

		KinectBodyData bodyData = kinectReader.getMostRecentData();
		Body person = bodyData.getPerson(0);
		if(person != null){
			PVector head = person.getJoint(Body.HEAD);
			PVector footLeft = person.getJoint(Body.FOOT_LEFT);
			PVector footRight = person.getJoint(Body.FOOT_RIGHT);
			PVector handLeft = person.getJoint(Body.HAND_LEFT);
			PVector handRight = person.getJoint(Body.HAND_RIGHT);


			fill(255,255,255);
			noStroke();
			drawIfValid(head);
			drawIfValid(footLeft);
			drawIfValid(footRight);
			drawIfValid(handLeft);
			drawIfValid(handRight);
			
			noFill();
			strokeWeight(0.009f);
			for(HeadCircle ripple : headCircles)
			rippleIfValid(head, ripple);		

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
			ripple.display();
			ripple.update(vec.x, vec.y);
		}

	}


	public static void main(String[] args) {
		PApplet.main(RipplesApplication.class.getName());
	}

}
