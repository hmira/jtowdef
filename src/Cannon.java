import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.vecmath.Tuple2d;
import javax.vecmath.Vector2d;

import com.jogamp.opengl.util.texture.Texture;

/** 
 * @author hmira
 *
 */
public abstract class Cannon {

	/** rotation of the cannon */
	Vector2d Aim = new Vector2d(1,0);
	
	/** money needed for constructing a Cannon */
	protected int price = 1;
	
	/** maximum range in pixels */
    int range = 200;
    
    /** parameter used in construction {@link Bullet} */
    int power = 25;
    
    /** time of last shot */
    long lastshot = Integer.MIN_VALUE;
    
    /** least possible interval of shoots */
    int shotinterval = 100;

	int R_X = -30;
	int L_X = 30;
	int U_Y = -30;
	int D_Y = 30;

    Vector2d R_Down, R_Up, L_Down, L_Up;
    Vector2d position = new Vector2d();

    public Cannon()
    {
    	position = new Vector2d();
    }
	
    /**
     * 
     * @param x	X coordinate
     * @param y	Y coordinate
     */
	public Cannon(double x, double y)
	{
		position = new Vector2d(x,y);
	}

    /**
     * drawing function
     * */
	public void Draw(GL2 gl)
	{}

    /**
     * drawing function
     * */
	public void Draw(GL2 gl, Texture tex)
	{
        Vector2d AimN = Aim;
        Vector2d x = this.position;
        Vector2d a = new Vector2d(-Aim.y, AimN.x);
        Vector2d Helpv1 = new Vector2d(Aim);
        Helpv1.add((Tuple2d) a);
        Vector2d Helpv2 = new Vector2d(Aim);
        Helpv2.sub((Tuple2d) a);

        Helpv1.scale(30);
        Helpv2.scale(30);
        
        R_Down = new Vector2d(x);
        R_Down.add(Helpv2);
        L_Up = new Vector2d(x);
        L_Up.sub(Helpv2);
        R_Up = new Vector2d(x);
        R_Up.sub(Helpv1);
        L_Down = new Vector2d(x);
        L_Down.add(Helpv1);
        
        gl.glEnable(GL.GL_BLEND); 
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA); 
		tex.enable(gl);
		tex.bind(gl);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2d(0, 0); gl.glVertex2d(R_Down.x, R_Down.y);
        gl.glTexCoord2d(1, 0); gl.glVertex2d(L_Down.x, L_Down.y);
        gl.glTexCoord2d(1, 1); gl.glVertex2d(L_Up.x, L_Up.y);
        gl.glTexCoord2d(0, 1); gl.glVertex2d(R_Up.x, R_Up.y);
        gl.glEnd();
		tex.disable(gl);
		gl.glDisable(GL.GL_BLEND);
	}
	
	/**
	 * @param enemy
	 * @return		the predicted position of collision the bullet and enemy
	 * */
	public Vector2d BulletPrediction(Enemy enemy)
	{
		Vector2d x1 = new Vector2d(enemy.center);
		x1.scale(enemy.Speed);
		Vector2d x2 = new Vector2d(position);
		x2.scale(Bullet.speed);
		
		Vector2d xx = new Vector2d(x1);
		xx.sub((Tuple2d)position);
		double d = xx.length();
		
		Vector2d result = new Vector2d(enemy.dir);
		result.scale(Bullet.speed * d);
		result.add(enemy.center);
		return result;
	}
	
	/**
	 * @param enemy
	 * setting <b>Aim</b> to be pointing on <b>enemy</b>
	 */
	public void CreateAim(Enemy enemy)
	{
		Vector2d v = BulletPrediction(enemy);
		Aim = new Vector2d(v);
		Aim.sub((Tuple2d)position);
		Aim.negate();
		Aim.normalize();
	}
	
	/**
	 * If cannon could not reach the enemy, returns null
	 * @param enemy		the item to be shot
	 * @param elapsed	game-time elapsed (in frames)
	 * @return			Bullet with parameters according the type of <b>Cannon</b>
	 */
	public Bullet Shoot(Enemy enemy, long elapsed)
    {
		if (elapsed - lastshot < shotinterval)
		{
			return null;
		}
		
        Vector2d Target = GetIntersect(enemy);
        if (Target == null)
        	return null;
        
        Bullet ret = new Bullet(position, Target, elapsed, power);
        ret.enemy = enemy;
		lastshot = elapsed;
        return ret;
    }

	/**
	 * calculate the predicted point of collision of
	 * bullet and enemy
	 * @param enemy		the item to be shot
	 * @return			point of collision
	 */
	public Vector2d GetIntersect(Enemy enemy)
	{
		Vector2d Target = new Vector2d();
		Target = GetIntersect1(enemy.center, enemy.next_checkpoint,
		            Bullet.speed, enemy.Speed,
		            enemy.center, this.position);
		
		if ((Target.x == 0) && (Target.y == 0))
		{
			Target = GetIntersect2(enemy.next_checkpoint, enemy.next_checkpoint2,
		                Bullet.speed, enemy.Speed,
		                enemy.center, this.position);
		}
		
		if ((Target.x == 0) && (Target.y == 0))
		{
			return null;
		}
		
		if (Target.x > 990 || Target.y > 440 || Target.x < 0 || Target.y < 0)
		return null;
		
		Vector2d dist = new Vector2d(Target);
		dist.sub(position);
		if ( range < dist.length())
		{
			return null;
		}
		return Target;
	}
	
	/**
	 * helper to calculate the predicted point of collision
	 * case that enemy <b>does not</b> change direction of movement
	 * @param A				path point A
	 * @param B				path point B
	 * @param CannonSpeed	speed of Cannon
	 * @param EnemySpeed	speed of Enemy
	 * @param EnemyPos		position of Enemy
	 * @param CanPos		position of Cannon
	 * @return
	 */
	static Vector2d GetIntersect1(
            Vector2d A,
            Vector2d B,
            double CannonSpeed,
            double EnemySpeed,
            Vector2d EnemyPos,
            Vector2d CanPos)
        {
            double Cannon_dist1, Enemy_dist1, Cannon_dist2, Enemy_dist2;

            while (true)
            {
            	//Vector2d C = (A + B) * 0.5;
            	Vector2d C = new Vector2d(A);
                C.add((Tuple2d)B);
                C.scale(0.5);

                // Cannon_dist1 = (A - CanPos).Length * EnemySpeed;
                Vector2d A1 = new Vector2d(A);
                A1.sub((Tuple2d) CanPos);
                Cannon_dist1 = A1.length() * EnemySpeed;
                
                //Enemy_dist1 = (A - EnemyPos).Length * CannonSpeed;                
                Vector2d A2 = new Vector2d(A);
                A2.sub((Tuple2d)EnemyPos);
                Enemy_dist1 = (A2.length()) * CannonSpeed;

                //Cannon_dist2 = (C - CanPos).Length * EnemySpeed;
                Vector2d C1 = new Vector2d(C);
                C1.sub((Tuple2d)CanPos);
                Cannon_dist2 = C1.length() * EnemySpeed;

                //Enemy_dist2 = (C - EnemyPos).Length * CannonSpeed;                
                Vector2d C2 = new Vector2d(C);
                C2.sub((Tuple2d)EnemyPos);
                Enemy_dist2 = (C2.length()) * CannonSpeed;
                
                if (Enemy_dist1 <= Cannon_dist1 && Enemy_dist2 >= Cannon_dist2)
                {
                    B = C;
                    if (Math.abs(Enemy_dist1 - Enemy_dist2) < 0.5)
                    {
                        return C;
                    }
                    continue;
                }

                Cannon_dist1 = Cannon_dist2;
                Enemy_dist1 = Enemy_dist2;
                //Cannon_dist2 = (B - CanPos).Length * EnemySpeed;
                Vector2d TCannon_dist2 = new Vector2d(B);
                TCannon_dist2.sub(CanPos);
                Cannon_dist2 = TCannon_dist2.length() * EnemySpeed;
                
                //Enemy_dist2 = (B - EnemyPos).Length * CannonSpeed;
                Vector2d TEnemy_dist2 = new Vector2d(B);
                TEnemy_dist2.sub(EnemyPos);
                Enemy_dist2 = TEnemy_dist2.length() * CannonSpeed;
                
                if (Enemy_dist1 <= Cannon_dist1 && Enemy_dist2 >= Cannon_dist2)
                {
                    A = C;
                    if (Math.abs(Enemy_dist1 - Enemy_dist2) < 0.5)
                    {
                        return C;
                    }
                    continue;
                }
                break;
            }
            return new Vector2d();
        }
	
		/**
		 * helper to calculate the predicted point of collision
		 * case that enemy <b>does</b> change direction of movement
		 * @param AA			path point A
		 * @param BB			path point B
		 * @param CannonSpeed	speed of Cannon
		 * @param EnemySpeed	speed of Enemy
		 * @param EnemyPos		position of Enemy
		 * @param CanPos		position of Cannon
		 * @return
		 */
		Vector2d GetIntersect2(
            Vector2d AA,
            Vector2d BB,
            double CannonSpeed,
            double EnemySpeed,
            Vector2d EnemyPos,
            Vector2d CanPos)
        {
			double rm = 0;
			Vector2d nx2 = new Vector2d(AA);
			nx2.sub(EnemyPos);
			rm = nx2.length();

		    double Cannon_dist1, Enemy_dist1, Cannon_dist2, Enemy_dist2;
		    Vector2d A = new Vector2d(AA);
		    Vector2d B = new Vector2d(BB);

		    while (true)
		    {
		        Vector2d C = new Vector2d(A);
		        C.add(B);
		        C.scale(0.5);//(A + B) * 0.5;

		        Vector2d Cannon_dist1_v = new Vector2d(A);
		        Cannon_dist1_v.sub(CanPos);
		        Cannon_dist1 =  (Cannon_dist1_v.length()) * EnemySpeed;
		        
		        Vector2d Enemy_dist1_v = new Vector2d(A);
		        Enemy_dist1_v.sub(AA);
		        Enemy_dist1 = (Enemy_dist1_v.length() + rm) * CannonSpeed;
		        
		        Vector2d Cannon_dist2_v = new Vector2d(C);
		        Cannon_dist2_v.sub(CanPos);
		        Cannon_dist2 = (Cannon_dist2_v.length()) * EnemySpeed;
		        
		        Vector2d Enemy_dist2_v = new Vector2d(C);
		        Enemy_dist2_v.sub(AA);
		        Enemy_dist2 = (Enemy_dist2_v.length() + rm) * CannonSpeed;

		        if (Enemy_dist1 <= Cannon_dist1 && Enemy_dist2 >= Cannon_dist2)
		        {
		            B = C;
		            if (Math.abs(Enemy_dist1 - Enemy_dist2) < 0.5)
		            {
		                return new Vector2d(C.x, C.y);
		            }
		            continue;
		        }

		        Cannon_dist1 = Cannon_dist2;
		        Enemy_dist1 = Enemy_dist2;

		        Cannon_dist2_v = new Vector2d(B);
		        Cannon_dist2_v.sub(CanPos);
		        Cannon_dist2 = (Cannon_dist2_v.length()) * EnemySpeed;
		        
		        Enemy_dist2_v = new Vector2d(B);
		        Enemy_dist2_v.sub(AA);
		        Enemy_dist2 = (Enemy_dist2_v.length() + rm) * CannonSpeed;

		        if (Enemy_dist1 <= Cannon_dist1 && Enemy_dist2 >= Cannon_dist2)
		        {
		            A = C;
		            if (Math.abs(Enemy_dist1 - Enemy_dist2) < 0.5)
		            {
		                return new Vector2d(C.x, C.y);
		            }
		            continue;
		        }
		        break;
		    }
		    return new Vector2d();
        }
}
