import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * The starting point of the code, maintains all the Circles for display and
 * takes in data from the camera.
 * 
 * @author Sara, Zhiling, Isabelle
 * 
 */
public class RipplesApplication extends PApplet {

	public static float PROJECTOR_RATIO = 1080f / 1920.0f;
	KinectBodyDataProvider kinectReader;
	// lists containing all the Circles for each body part
	List<HeadCircle> headCircles;
	List<HandCircle> leftHandCircles;
	List<HandCircle> rightHandCircles;
	List<FootCircle> leftFootCircles;
	List<FootCircle> rightFootCircles;
	// remembers whether hands were last together or apart when one or both is no
	// longer detected
	private boolean wereTogether;

	int index = 0;

	float[] floorArr;
	float floor = 0;

	PVector handLeft;
	PVector handRight;

	/**
	 * Set the window to full screen at the appropriate size ratio
	 */
	public void settings() {
		// fullScreen(P2D);
		createWindow(true, true, .5f);
	}

	/**
	 * Preparation code to get the program ready to go
	 */
	public void setup() {
		/*
		 * use this code to run your PApplet from data recorded by UPDRecorder
		 */
		try {
			kinectReader = new KinectBodyDataProvider("test3.kinect", 3);
		} catch (IOException e) {
			System.out.println("Unable to create kinect producer");
		}

		// kinectReader = new KinectBodyDataProvider(8008);

		kinectReader.start();

		// initialize our global variables
		headCircles = new ArrayList<HeadCircle>();
		leftHandCircles = new ArrayList<HandCircle>();
		rightHandCircles = new ArrayList<HandCircle>();
		leftFootCircles = new ArrayList<FootCircle>();
		rightFootCircles = new ArrayList<FootCircle>();
		wereTogether = false;

		floorArr = new float[100];

		// populate the lists with new instances of Circles
		// each circle will be passed a different value for the stagger param, to ensure
		// they do not ripple at the same time
		// note the head has the most circles, then hands, then feet
		for (int i = 0; i < 10; i++) {
			if (i < 8) {
				leftHandCircles.add(new HandCircle(this, i));
				rightHandCircles.add(new HandCircle(this, i));
				if (i < 8) {
					leftFootCircles.add(new FootCircle(this, i * 200));
					rightFootCircles.add(new FootCircle(this, i * 200));
				}
			}
			headCircles.add(new HeadCircle(this, i * 1000));
		}

	}

	/**
	 * Continuously updates the location and behavior of all the ripples, based on
	 * limb data from the camera
	 */
	public void draw() {
		setScale(.5f);
		background(0);

		KinectBodyData bodyData = kinectReader.getMostRecentData();
		Body person = bodyData.getPerson(0);
		if (person != null) {
			// get all the data for the body parts
			PVector head = person.getJoint(Body.HEAD);
			PVector footLeft = person.getJoint(Body.FOOT_LEFT);
			PVector footRight = person.getJoint(Body.FOOT_RIGHT);
			handLeft = person.getJoint(Body.HAND_LEFT);
			handRight = person.getJoint(Body.HAND_RIGHT);

			noFill();
			strokeWeight(0.009f);

			floor(footLeft, footRight);

			// determine what to do with each of our ripples
			boolean handsTogether = checkIntersect(); // determine whether or not the hands are currently together
			for (FootCircle ripple : leftFootCircles) {
				ripple.stopRipples(handsTogether);
				ripple.isOnFloor(floor, footLeft);
				rippleIfValid(footLeft, ripple);
			}
			for (FootCircle ripple : rightFootCircles) {
				ripple.stopRipples(handsTogether);
				ripple.isOnFloor(floor, footRight);
				rippleIfValid(footRight, ripple);
			}
			for (HandCircle ripple : leftHandCircles) {
				ripple.expandDiam(handsTogether);
				rippleIfValid(handLeft, ripple);
			}
			for (HandCircle ripple : rightHandCircles) {
				ripple.expandDiam(handsTogether);
				rippleIfValid(handRight, ripple);
			}
			for (HeadCircle ripple : headCircles) {
				ripple.stopRipples(handsTogether);
				rippleIfValid(head, ripple);
			}
		}

	}

	/**
	 * Calls the draw and update method of a circle. Will do nothing is vec is null.
	 * This is handy because get joint will return null if the joint isn't tracked.
	 * 
	 * @param vec
	 */
	public void drawIfValid(PVector vec) {
		if (vec != null) {
			ellipse(vec.x, vec.y, .1f, .1f);
		}

	}

	/**
	 * Calls the display and update method of a circle. If the vec is null, circles
	 * will get reset to their initial state.
	 * 
	 * @param vec
	 */
	public void rippleIfValid(PVector vec, Circle ripple) {
		if (vec != null) {
			ripple.update(vec.x, vec.y);
			ripple.display();
		} else {
			ripple.update(-200, -200);
			ripple.display();
		}
	}

	/**
	 * Determine whether or not the right and left hand are currently together.
	 * 
	 * @return true if the hands are together
	 */
	public boolean checkIntersect() {
		float diam = .1f;
		if (handLeft != null && handRight != null) {
			// calculate the distance between the hands
			double distance = Math.sqrt((double) Math.pow((handLeft.x - handRight.x), 2)
					+ (double) Math.pow((handLeft.y - handRight.y), 2));

			// if the distance is less than the range we're checking for, we will read that
			// the hands are together
			if (distance <= diam) {
				wereTogether = true;
				return true;
			} else {
				wereTogether = false;
				return false;
			}
		} else {
			if (wereTogether)
				return true;
			else
				return false;
		}
	}

	/**
	 * Approximate where the floor is currently located based on the continued
	 * placement of both feet
	 * 
	 * @param footLeft
	 *            the left foot
	 * @param footRight
	 *            the right foot
	 */
	public void floor(PVector footLeft, PVector footRight) {
		// when the index reaches the end of the array, bring it back to the beginning
		if (index >= floorArr.length)
			index = 0;

		if (footLeft != null && footRight != null) {
			float sum = 0;
			int notInArr = 0;
			// store the magnitude of both feet in the array of possible floor values
			floorArr[index] = footLeft.mag();
			floorArr[index + 1] = footRight.mag();
			index += 2;

			for (int i = 0; i < floorArr.length; i++) {
				// if this value hasnt been populated yet (early on in the program life cycle),
				// make sure to ignore it when calculating floor average
				if (floorArr[i] == 0)
					notInArr++;
				sum += floorArr[i];
			}
			// approximate where the floor might be based on recent data
			floor = sum / (floorArr.length - notInArr);
		}
	}

	public void createWindow(boolean useP2D, boolean isFullscreen, float windowsScale) {
		if (useP2D) {
			if (isFullscreen) {
				fullScreen(P2D);
			} else {
				size((int) (1920 * windowsScale), (int) (1080 * windowsScale), P2D);
			}
		} else {
			if (isFullscreen) {
				fullScreen();
			} else {
				size((int) (1920 * windowsScale), (int) (1080 * windowsScale), P2D);
			}
		}
	}

	// use lower numbers to zoom out (show more of the world)
	// zoom of 1 means that the window is 2 meters wide and appox 1 meter tall.
	public void setScale(float zoom) {
		scale(zoom * width / 2.0f, zoom * -width / 2.0f);
		translate(1f / zoom, -PROJECTOR_RATIO / zoom);
	}

	public static void main(String[] args) {
		PApplet.main(RipplesApplication.class.getName());
	}

}
