/**
 * An interface to provide blueprints for all of the Circle types.
 * @author Sara, Zhiling, Isabelle
 *
 */
public interface Circle {
	
	/**
	 * @return the current color of the circle
	 */
	public int getColor();
	
	/**
	 * @return the rate at which the circle expands
	 */
	public float getSpeed();
	
	/**
	 * sets up the next state of the circle (size, color, location, visibility)
	 * @param x the x-coordinate of the hand vector
	 * @param y the y-coordinate of the hand vector
	 */
	public void update(float x, float y);
	
	/**
	 * paints the circle onto the screen
	 */
	public void display();
	
}
