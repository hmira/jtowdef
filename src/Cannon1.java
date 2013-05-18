import javax.media.opengl.GL2;
import javax.vecmath.Vector2d;

import com.jogamp.opengl.util.texture.Texture;


public class Cannon1 extends Cannon {
	
	public Cannon1(double x, double y) {
		position = new Vector2d(x,y);
		power = 25;
		price = 1;
	}

	public static Texture tex;
	int price = 1;
	
	public static void InitTexture(Texture a_tex)
	{
		tex = a_tex;
	}
	
	public void Draw(GL2 gl)
	{
		Draw(gl, tex);
	}
}
