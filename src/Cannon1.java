import javax.media.opengl.GL2;
import javax.vecmath.Vector2d;

import com.jogamp.opengl.util.texture.Texture;

/**
 * Cannon 1 listed as <b>weak cannon</b>
 * @author hmira
 *
 */
public class Cannon1 extends Cannon {
	
	/**
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 */
	public Cannon1(double x, double y) {
		position = new Vector2d(x,y);
		power = 25;
		price = 1;
	}

	public static Texture tex;
	int price = 1;
	
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
