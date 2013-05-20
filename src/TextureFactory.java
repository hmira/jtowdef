import java.io.IOException;

import javax.media.opengl.GL2;
import javax.media.opengl.GLException;

import com.jogamp.opengl.swt.GLCanvas;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * class that generates all textures needed
 * in the game JTowDef
 * 
 * @author hmira
 *
 */
public class TextureFactory {

	public Texture enemy1_tex = null;
	public Texture enemy2_tex = null;
	public Texture enemy3_tex = null;
	public Texture cannon1_tex = null;
	public Texture cannon2_tex = null;
	public Texture cannon3_tex = null;
	public Texture bg_tex = null;
	public Texture bullet_tex = null;
	public Texture range_preview_tex = null;
	
	/**
	 * textures initialization
	 * @param gl GL used as {@link GLCanvas} in animation loop
	 */
	public void InitTextures(GL2 gl) {
		InitEnemy1Texture(gl);
		InitEnemy2Texture(gl);
		InitEnemy3Texture(gl);
		InitCannon1Texture(gl);
		InitCannon2Texture(gl);
		InitCannon3Texture(gl);
		InitBackgroundTexture(gl);
		InitBulletTexture(gl);
		InitRangePreviewTexture(gl);
	}
	
	public void InitBackgroundTexture(GL2 gl) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        try {
        	bg_tex = TextureIO.newTexture(
					getClass().getResource("map.png"),
					false,
					TextureIO.PNG);
		} catch (GLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void InitEnemy1Texture(GL2 gl)
	{
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        try {
        	enemy1_tex = TextureIO.newTexture(
					getClass().getResource("enemy1.png"),
					false,
					TextureIO.PNG);
		} catch (GLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void InitEnemy2Texture(GL2 gl)
	{
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        try {
        	enemy2_tex = TextureIO.newTexture(
					getClass().getResource("enemy2.png"),
					false,
					TextureIO.PNG);
		} catch (GLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void InitEnemy3Texture(GL2 gl)
	{
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        try {
        	enemy3_tex = TextureIO.newTexture(
					getClass().getResource("enemy3.png"),
					false,
					TextureIO.PNG);
		} catch (GLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void InitBulletTexture(GL2 gl)
	{
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        try {
        	bullet_tex = TextureIO.newTexture(
					getClass().getResource("bullet.png"),
					false,
					TextureIO.PNG);
		} catch (GLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void InitCannon1Texture(GL2 gl)
	{
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        try {
        	cannon1_tex = TextureIO.newTexture(
					getClass().getResource("cannon1.png"),
					false,
					TextureIO.PNG);
		} catch (GLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void InitCannon2Texture(GL2 gl)
	{
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        try {
        	cannon2_tex = TextureIO.newTexture(
					getClass().getResource("cannon2.png"),
					false,
					TextureIO.PNG);
		} catch (GLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void InitCannon3Texture(GL2 gl)
	{
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        try {
        	cannon3_tex = TextureIO.newTexture(
					getClass().getResource("cannon3.png"),
					false,
					TextureIO.PNG);
		} catch (GLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void InitRangePreviewTexture(GL2 gl)
	{
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        try {
        	range_preview_tex = TextureIO.newTexture(
					getClass().getResource("range.png"),
					false,
					TextureIO.PNG);
		} catch (GLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
