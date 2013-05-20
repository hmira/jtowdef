import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;

/**
 * @author hmira
 *
 */
public class Enemy2 extends Enemy{
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
	
	/**
	 * @param spawned	time starting in the game 
	 * */
	public Enemy2(long spawned)
	{
		this.spawned = spawned;
		this.Amo = 800;
		this.price = 2;
	}
}