import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.vecmath.Tuple2d;
import javax.vecmath.Vector2d;

import com.jogamp.opengl.util.texture.Texture;

/**
 * 
 * @author hmira
 * class that describes the path of the bullet
 * and relationship between the cannons and enemies
 */
public class Bullet {

	/** point where is shot from */
    private Vector2d from;
    
	/** point where is shot to */
    private Vector2d to;
    
    /** course of the bullet */
    private Vector2d direction;

    /** target to shot */
    public Enemy enemy;
    
    /** actual position */
    public Vector2d position;
    
    /** true if the enemy was hit */
    public boolean HitEnd = false;
    
    /** damage that can cause by hitting the enemy */
    public int Strength = 25;

    /** number of pixels per unit of time (frame) */
    public static double speed = 2.5;
    
    /** reference to texture */
    public static Texture tex = null;

    /** variables describing the dimensions */
    int R_X = -5;
    int L_X = 5;
    int U_Y = -5;
    int D_Y = 5;
    
    /** time when the bullet was shot */
    public long spawned;
    
    /**
     * @param From	position of the cannon
     * @param To	predicted position of the enemy
     * @param a_spawned		time, when the shot is shot
     * @param a_strength 	damage (according cannon)
     * */
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
    
    /**
     * texture setter
     */
	public static void InitTexture(Texture a_tex)
	{
		tex = a_tex;
	}
    
	/**
	 * @param elapsed	actual time of the frame
	 * position recalculation 
	 * */
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

    /**
     * drawing function
     * */
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
