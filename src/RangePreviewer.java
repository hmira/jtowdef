import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.vecmath.Vector2d;

import com.jogamp.opengl.util.texture.Texture;


public class RangePreviewer {
	int x = 0;
	int y = 0;
    int R_X = -200;
    int L_X = 200;
    int U_Y = -200;
    int D_Y = 200;
    int elapsed = 0;
    
    Vector2d position = new Vector2d();
    
    public static Texture tex = null;
    
	public void Draw(GL2 gl)
	{
        gl.glEnable(GL.GL_BLEND); 
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA); 
		tex.enable(gl);
		tex.bind(gl);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2d(0, 0); gl.glVertex2d(position.x + R_X, position.y + D_Y);
        gl.glTexCoord2d(1, 0); gl.glVertex2d(position.x + R_X, position.y + U_Y);
        gl.glTexCoord2d(1, 1); gl.glVertex2d(position.x + L_X, position.y + U_Y);
        gl.glTexCoord2d(0, 1); gl.glVertex2d(position.x + L_X, position.y + D_Y);
        gl.glEnd();
		tex.disable(gl);
		gl.glDisable(GL.GL_BLEND);
	}
    
	public static void InitTexture(Texture a_tex)
	{
		tex = a_tex;
	}
	
	public boolean IsOn()
	{
		if (elapsed > 0)
		{
			elapsed--;
			return true;
		}
		
		return false;
	}
	
	public void SetOn(int x, int y)
	{
		this.position.set(x, y);
		elapsed = 50;
	}
}
