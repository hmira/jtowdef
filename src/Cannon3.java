import javax.media.opengl.GL2;
import javax.vecmath.Vector2d;

import com.jogamp.opengl.util.texture.Texture;


public class Cannon3 extends Cannon {
	
	public Cannon3(double x, double y) {
		position = new Vector2d(x,y);
		power = 400;
		price = 10;
	}

	public static Texture tex;
	
	public static void InitTexture(Texture a_tex)
	{
		tex = a_tex;
	}
	
	public void Draw(GL2 gl)
	{
		Draw(gl, tex);
	}
}
