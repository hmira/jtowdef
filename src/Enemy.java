import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.vecmath.Tuple2d;
import javax.vecmath.Vector2d;

import com.jogamp.opengl.util.texture.Texture;

/**
 * @author hmira
 * class that describes behavior of enemies
 * */
abstract public class Enemy {

    /** variables describing the dimensions */
	int R_X = -18;
	int L_X = 17;
	int U_Y = -18;
	int D_Y = 17;

    /** actual position */
    Vector2d center = new Vector2d(0,0);
    /** next point that enemy hits */
    Vector2d next_checkpoint = new Vector2d();
    /** point that enemy hits right after hits next checkpoint */
    Vector2d next_checkpoint2 = new Vector2d();
    
    /** time(in frames) when was spawned */
    long spawned;
    
    /** money earned after defeating */
    int price;
    
    /** direction of movement */
    public Vector2d dir = new Vector2d();

    /** reference to path of enemy */
    public ArrayList<Vector2d> path;

    /** if enemy hits end, is true */
    public boolean HitEnd = false;
    
    /** if <b>Amo</b> is non-positive, is false */
    public boolean Alive = true;
    
    /** decreased after every hit of {@link Bullet} */
    public int Amo = 200;

    /** reference to texture */
    protected int texture;
    
    /** number of pixels per unit of time (frame) */
    public double Speed = 1;

    
	public Enemy()
	{
	}
	
	/**
	 * @param spawned	time starting in the game 
	 * */
	public Enemy(long spawned)
	{
		this.spawned = spawned;
	}
	
	/** set new position */
	public void Update(int x, int y)
	{
	    this.center.setX(x);
	    this.center.setY(y);
	}

	/**
	 * @param elapsed	actual time of the frame
	 * position recalculation 
	 * */
	public void Move(long elapsed, ArrayList<Vector2d> points) 
	{
		double lap = (elapsed - spawned);
		for (int i = 0; i < points.size() - 1; i++) {
			Vector2d q2 = new Vector2d(points.get(i + 1));
			Vector2d q = new Vector2d(points.get(i));
	
			q2.sub((Tuple2d)q);
			
			double length = q2.length();
			
	        if ( lap > length)
	        {
	        	lap -= length;
	        }
	        else
	        {
	        	double e = (double)lap / length;
	        	q2.scale(e);
	        	Vector2d actual = new Vector2d(points.get(i));

	        	actual.add(q2);
	        	
                next_checkpoint = new Vector2d(points.get(i + 1).x, points.get(i + 1).y);
                if ((i+2) != points.size())
                    next_checkpoint2 = new Vector2d(points.get(i + 2).x, points.get(i + 2).y);
	        	
                dir = new Vector2d(next_checkpoint);
                dir.sub((Tuple2d)next_checkpoint);
                
	            Update((int)actual.x, (int)actual.y);
	            break;
	        }
		}
		
	}

    /**
     * drawing function
     * */
	public void Draw(GL2 gl)
	{};

    /**
     * drawing function
     * */
	public void Draw(GL2 gl, Texture tex)
	{
        gl.glEnable(GL.GL_BLEND); 
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA); 
		tex.enable(gl);
		tex.bind(gl);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2d(0, 0); gl.glVertex2d(center.x + R_X, center.y + D_Y);
        gl.glTexCoord2d(1, 0); gl.glVertex2d(center.x + R_X, center.y + U_Y);
        gl.glTexCoord2d(1, 1); gl.glVertex2d(center.x + L_X, center.y + U_Y);
        gl.glTexCoord2d(0, 1); gl.glVertex2d(center.x + L_X, center.y + D_Y);
        gl.glEnd();

		tex.disable(gl);
		gl.glDisable(GL.GL_BLEND);
	}
	
	/**
	 * used for checking whether enemy hits the finish
	 * close ~ 0.5px
	 * */
	boolean IsCloseTo(Vector2d point)
	{
		Vector2d temp = new Vector2d(center);
		temp.sub(point);
		return (temp.length() < 0.5);
	}
	
}
