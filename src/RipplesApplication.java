import processing.core.PApplet;
import processing.core.PVector;

/**
 * @author Sara, Zhiling, Isabelle
 *
 */
public class RipplesApplication extends PApplet {

	KinectBodyDataProvider kinectReader;

	public void settings() {
		size(500,500, P2D);
	}

	public void setup(){

		/*
		 * use this code to run your PApplet from data recorded by UPDRecorder 
		 *
		try {
			kinectReader = new KinectBodyDataReader("recordedData.kinect");
		} catch (IOException e) {
			System.out.println("Unable to creat e kinect producer");
		}
		 */
		
		kinectReader = new KinectBodyDataProvider(8008);
		kinectReader.start();

	}
	public void draw(){
		//makes the window 2x2
		this.scale(width/2.0f, -height/2.0f);

		//make positive y up and center of window 0,0
		translate(1,-1);
		noStroke();

		background(0,0,0);

		// leave trails instead of clearing background \ 
		//noStroke();
		//fill(0,0,0, 10);
		//rect(-1,-1, 2, 2); //draw transparent rect of the window

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
