import javax.media.opengl.GL2;
import javax.vecmath.Vector2d;

import com.jogamp.opengl.util.texture.Texture;

/**
 * Cannon 3 listed as <b>super strong cannon</b>
 * @author hmira
 *
 */
public class Cannon3 extends Cannon {
	
	/**
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 */
	public Cannon3(double x, double y) {
		position = new Vector2d(x,y);
		power = 400;
		price = 10;
	}

	public static Texture tex;
	
	/**
	 * texture setter
	 * @param a_tex		reference to a texture
	 */
	public static void InitTexture(Texture a_tex)
	{
		tex = a_tex;
	}
	
	/**
	 * drawing function
	 */
	public void Draw(GL2 gl)
	{
		Draw(gl, tex);
	}
}
