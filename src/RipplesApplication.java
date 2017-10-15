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
	
	int index = 0;
	
	float[] floorArr;
	float floor =0;
	
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
			kinectReader = new KinectBodyDataProvider("test3.kinect", 3);
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
		
		floorArr = new float[100];
		
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
		int spineY =0;
		
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


			fill(255,255,255);
			noStroke();
			//commented out for now -- should be in final or no?
//			drawIfValid(head);
//			drawIfValid(footLeft);
//			drawIfValid(footRight);
//			drawIfValid(handLeft);
//			drawIfValid(handRight);
			
			noFill();
			strokeWeight(0.009f);
//			if (spineBase != null)
//				spineY = (int)spineBase.y;
			floor(footLeft, footRight);
			for(FootCircle ripple : leftFootCircles) 
			{
				ripple.floor(floor, footLeft);
				rippleIfValid(footLeft, ripple);
			}
			for(FootCircle ripple : rightFootCircles) 
			{
				ripple.floor(floor, footRight);
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
			ripple.update(-200, -200);
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
	
	
	public void floor(PVector footLeft, PVector footRight)
	{
		if(index >= floorArr.length)
			index = 0;
		if(footLeft != null && footRight != null)
		{
			float sum = 0;
			int notInArr = 0;
			floorArr[index] = footLeft.mag();
			floorArr[index+1] = footRight.mag();
			index += 2;
			
			for(int i =0; i < floorArr.length; i++)
			{
				if(floorArr[i] == 0)
					notInArr++;
				
				sum += floorArr[i];
			}
			floor = sum / (floorArr.length - notInArr);
			//System.out.println(floor);
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
