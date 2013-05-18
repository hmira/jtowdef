import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.vecmath.Tuple2d;
import javax.vecmath.Vector2d;

import com.jogamp.opengl.util.texture.Texture;


abstract public class Enemy {

	
	int R_X = -18;
	int L_X = 17;
	int U_Y = -18;
	int D_Y = 17;
    Vector2d center = new Vector2d(0,0);
    Vector2d next_checkpoint = new Vector2d();
    Vector2d next_checkpoint2 = new Vector2d();
    Integer price = 1;
    
    long spawned;
    
    public Vector2d dir = new Vector2d();

    public ArrayList<Vector2d> path;

    public boolean HitEnd = false;
    public boolean Alive = true;
    public int Amo = 200;

    public double Elapsed;

    protected int texture;
    public double Speed = 1;

    
	public Enemy()
	{
	}
	
	public Enemy(long spawned)
	{
		this.spawned = spawned;
	}
	
	public void Update(int x, int y)
	{
	    this.center.setX(x);
	    this.center.setY(y);
	}

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

	public void Draw(GL2 gl)
	{};
	
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
	
}
