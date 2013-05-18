import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.vecmath.Tuple2d;
import javax.vecmath.Vector2d;

import com.jogamp.opengl.util.texture.Texture;


public class Bullet {

    private Vector2d from;
    private Vector2d to;
    private Vector2d direction;

    public Enemy enemy;
    public Vector2d position;
    public Vector2d target;
    public boolean HitEnd = false;
    public boolean Alive = true;
    public int Strength = 25;

    public static double speed = 2.5;
    public static Texture tex = null;

    int R_X = -5;
    int L_X = 5;
    int U_Y = -5;
    int D_Y = 5;
    
    public long spawned;
    
    Bullet(Vector2d From, Vector2d To, long a_spawned, int a_strength)
    {
    	this.Strength = a_strength;
    	
    	this.position = new Vector2d();
    	this.spawned = a_spawned;
		this.from = From;
		this.to = To;
		
		Vector2d tmp = new Vector2d(To);
		tmp.sub((Tuple2d)From);
		tmp.normalize();
		this.direction = tmp;
    }
    
	public static void InitTexture(Texture a_tex)
	{
		tex = a_tex;
	}
    
    void Move(long elapsed)
    {
        float t = elapsed - spawned;
        
        //position = From + t * speed * direction;
        position = new Vector2d(direction);
        position.scale(t * speed);
        position.add(from);

        Vector2d f1 = new Vector2d(position);
        f1.sub((Tuple2d)from);
        Vector2d f2 = new Vector2d(to);
        f2.sub((Tuple2d)from);
        
        if (f1.lengthSquared() > f2.lengthSquared())
        	HitEnd = true;
        
        //if ((pos - from).lengthSquared() > (to - from).lengthSquared())
        //	HitEnd = true;
    }

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
    
}
