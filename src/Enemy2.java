import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;


public class Enemy2 extends Enemy{
	public static Texture tex;
	
	public static void InitTexture(Texture a_tex)
	{
		tex = a_tex;
	}
	
	public void Draw(GL2 gl)
	{
		Draw(gl, tex);
	}
	
	public Enemy2(long spawned)
	{
		this.spawned = spawned;
		this.Amo = 800;
		this.price = 2;
	}
}