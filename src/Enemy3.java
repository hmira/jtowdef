import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;


public class Enemy3 extends Enemy{
	public static Texture tex;

	public static void InitTexture(Texture a_tex)
	{
		tex = a_tex;
	}
	
	public void Draw(GL2 gl)
	{
		Draw(gl, tex);
	}
	
	public Enemy3(long spawned)
	{
		this.spawned = spawned;
		this.Amo = 1400;
		this.price = 3;
	}
}