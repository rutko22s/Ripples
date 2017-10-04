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

	public void settings() {
		//size(500,500, P2D);
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

	}
	public void draw(){
		//makes the window 2x2
		this.scale(width/3.0f, -height/2.0f);

		//make positive y up and center of window 0,0
		translate(1,-1);
		//noStroke();

		background(0,0,0);

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
			
			if(head != null) {
				noFill();
				strokeWeight(0.009f);
				tempCircle.update(head.x, head.y);
				tempCircle.display();
				
//				for(HeadCircle circle : headCircles) {
//					circle.update(head.x, head.y);
//					circle.display();
//				}
			}
			
			

		}
		
	}

	/**
	 * Draws an ellipse in the x,y position of the vector (it ignores z).
	 * Will do nothing is vec is null.  This is handy because get joint 
	 * will return null if the joint isn't tracked. 
	 * @param vec
	 */
	public void drawIfValid(PVector vec) {
		if(vec != null) {
			ellipse(vec.x, vec.y, .1f,.1f);
		}

	}


	public static void main(String[] args) {
		PApplet.main(RipplesApplication.class.getName());
	}

}
