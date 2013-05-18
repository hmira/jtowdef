import java.util.ArrayList;
import java.util.LinkedList;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.swing.JLabel;
import javax.vecmath.Tuple2d;
import javax.vecmath.Vector2d;


import com.jogamp.opengl.util.texture.Texture;


public class TowDefDrawing implements GLEventListener
{
	ArrayList<Enemy> enemies = null;
	ArrayList<Cannon> cannons = null;
	ArrayList<Bullet> bullets = null;
	RangePreviewer rangePreviewer = null;
	
	JLabel moneyLabel = null;
	Integer money = null;
	
	TextureFactory textureFactory = new TextureFactory();
	
	public void setRangePreviewer(RangePreviewer a_rangePreviewer)
	{
		rangePreviewer = a_rangePreviewer;
	}
	
	public void setEnemies(ArrayList<Enemy> a_enemies)
	{
		enemies = a_enemies;
	}
	
	public void setCannons(ArrayList<Cannon> a_cannons)
	{
		cannons = a_cannons;
	}
	
	public void setBullets(ArrayList<Bullet> a_bullets)
	{
		bullets = a_bullets;
	}
	
	public void setMoney(Integer a_money, JLabel a_moneyLabel)
	{
		money = a_money;
		moneyLabel = a_moneyLabel;
	}
	
	public void updateMoney(Integer price)
	{
		money += price;
		moneyLabel.setText("money: " + Integer.toString(money) + "$  ");
	}
	
	public void createCannon(int x, int y, int type)
	{
		Cannon c;
		
		if (type == 1)
			c = new Cannon1(x, y);
		else if (type == 2)
			c = new Cannon2(x, y);
		else if (type == 3)
			c = new Cannon3(x, y);
		else
			return;
		
		if (c.price <= money)
		{
			cannons.add(c);
			updateMoney(-c.price);
		}
	}
	
	public void loadTextures(GLAutoDrawable gLDrawable)
	{
        final GL2 gl = gLDrawable.getGL().getGL2();
        
        textureFactory.InitTextures(gl);

        background_tex = textureFactory.bg_tex;
        Enemy1.InitTexture(textureFactory.enemy1_tex);
        Enemy2.InitTexture(textureFactory.enemy2_tex);
        Enemy3.InitTexture(textureFactory.enemy3_tex);
        Cannon1.InitTexture(textureFactory.cannon1_tex);
        Cannon2.InitTexture(textureFactory.cannon2_tex);
        Cannon3.InitTexture(textureFactory.cannon3_tex);
        Bullet.InitTexture(textureFactory.bullet_tex);
        RangePreviewer.InitTexture(textureFactory.range_preview_tex);
	}

    Texture background_tex = null;
	//private GLU glu = new GLU();
	ArrayList<Vector2d> path = new ArrayList<Vector2d>();
	long elapsed = 0;
   
    public void display(GLAutoDrawable gLDrawable) 
    {
    	if (enemies != null)
    	{
    		for (Enemy enemy : enemies) {
				enemy.Move(elapsed, path);
			}
    	}
    	
    	if (bullets != null)
    	{
    		for (Bullet bullet : bullets) {
				bullet.Move(elapsed);
			}
    	}
    	
    	if (cannons != null)
    	{
    		for (Cannon cannon : cannons)
    		{
    			if (enemies != null)
    			{
    				for (Enemy enemy : enemies)
    				{
    					Vector2d aimTo = cannon.GetIntersect(enemy);
    					if (aimTo == null)
    					{
    						continue;
    					}
    					Vector2d dist = new Vector2d(aimTo);
    					dist.sub((Tuple2d)cannon.position);
    					if (cannon.range > dist.length())
    					{
    	    				cannon.CreateAim(enemy);
    						Bullet bullet = cannon.Shoot(enemy, elapsed);
    						if (bullet != null)
    							bullets.add(bullet);
    						break;
    					}
    				}
    			}
    		}
    	}
    	
    	if (bullets != null)
    	{
    		LinkedList<Bullet> garbage_bullets = new LinkedList<Bullet>();
    		for (int i = 0; i < bullets.size(); i++) {
				Bullet bullet = bullets.get(i);
				if (bullet.HitEnd) garbage_bullets.add(bullet);
			}
    		for (Bullet bullet : garbage_bullets) {
				bullet.enemy.Amo -= bullet.Strength;
				bullets.remove(bullet);
			}
    	}
    	
    	if (enemies != null)
    	{    
    		LinkedList<Enemy> garbage_enemies = new LinkedList<Enemy>();
    		for (Enemy enemy : enemies) 
    		{
    			if (enemy.Amo <= 0)
    			{
    				garbage_enemies.add(enemy);
    			}
			}
    		
    		for (Enemy enemy : garbage_enemies)
    		{
    			updateMoney(enemy.price);
				enemies.remove(enemy);
			}
    	}
    	
    	
        final GL2 gl = gLDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, 990, 480, 0, 0, 1);
        drawBackground(gl);

    	if (bullets != null)
    	{
    		for (Bullet bullet : bullets) {
				bullet.Draw(gl);
			}
    	}
    	
    	if (enemies != null)
    	{
    		for (Enemy enemy : enemies) {
				enemy.Draw(gl);
			}
    	}
    	
    	if (cannons != null)
    	{
    		for (Cannon cannon: cannons) {
    			if (enemies != null && enemies.size() > 0)
    			cannon.BulletPrediction(enemies.get(0));
				cannon.Draw(gl);
			}
    	}
    	
    	if (rangePreviewer.IsOn())
    	{
    		rangePreviewer.Draw(gl);
    	}
        
		gl.glFlush();
		
		
		elapsed++;
    }

	public void drawBackground(GL2 gl)
	{
		background_tex.enable(gl);
		background_tex.bind(gl);
		gl.glBegin(GL2.GL_QUADS);           	

		gl.glTexCoord2d(0, 0);gl.glVertex2d(0, 0);	
		gl.glTexCoord2d(0, 1);gl.glVertex2d(0, 480);	
		gl.glTexCoord2d(1, 1);gl.glVertex2d(990, 480);	
		gl.glTexCoord2d(1, 0);gl.glVertex2d(990, 0);	
		gl.glEnd();
		background_tex.disable(gl);
	}
    
    public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged, boolean deviceChanged) 
    {
    	System.out.println("displayChanged called");
    }
 
    public void init(GLAutoDrawable gLDrawable) 
    {
    	loadTextures(gLDrawable);
    	int MapX = 90;
    	int MapY = 80;
    	path.add(new Vector2d(45 + 1 * MapX, 30 + 0 * MapY));
    	path.add(new Vector2d(45 + 1 * MapX, 30 + 4 * MapY));
    	path.add(new Vector2d(45 + 3 * MapX, 30 + 4 * MapY));
    	path.add(new Vector2d(45 + 3 * MapX, 30 + 1 * MapY));
    	path.add(new Vector2d(45 + 5 * MapX, 30 + 1 * MapY));
    	path.add(new Vector2d(45 + 5 * MapX, 30 + 4 * MapY));
    	path.add(new Vector2d(45 + 7 * MapX, 30 + 4 * MapY));
    	path.add(new Vector2d(45 + 7 * MapX, 30 + 1 * MapY));
    	path.add(new Vector2d(45 + 9 * MapX, 30 + 1 * MapY));
    	path.add(new Vector2d(45 + 9 * MapX, 30 + 6 * MapY));

    	System.out.println("init() called");
        GL2 gl = gLDrawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL2.GL_FLAT);
    }
 
    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) 
    {}
 
 
	public void dispose(GLAutoDrawable arg0) 
	{
		System.out.println("dispose() called");
	}

}
