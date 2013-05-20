import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.swing.JLabel;
import javax.vecmath.Tuple2d;
import javax.vecmath.Vector2d;

import com.jogamp.opengl.swt.GLCanvas;
import com.jogamp.opengl.util.texture.Texture;

/**
 * @author hmira
 * */
public class TowDefDrawing implements GLEventListener

{
	/**
	 * Gamestatus that describes the status of the gameplay
	 * */
	public enum GameStatus
	{
		WIN,
		LOSE,
		PLAYING
	}
	
	/**
	 * support for game-over-listener
	 * v prípade zostrelenia všetkých nepriateľov
	 * alebo dorazenia nepriateľa do cieľa
	 * sa vyvolá event, ktorý má v <b>TowerDef</b> listener
	 * */
	public PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	public GameStatus gameStatus = GameStatus.PLAYING;
	public void setGameStatus(GameStatus gameStatus)
	{
		GameStatus oldGame = this.gameStatus;
		this.gameStatus = gameStatus;
		this.pcs.firePropertyChange("gameStatus", oldGame, gameStatus);
    }
	
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
	
    /** list of enemies */
	ArrayList<Enemy> enemies = null;
    /** list of cannons */
	ArrayList<Cannon> cannons = null;
    /** list of bullets */
	ArrayList<Bullet> bullets = null;
	/** preview of the range after a right click anywhere on the game-field */
	RangePreviewer rangePreviewer = null;
	/** final point where are the enemies heading */
	Vector2d finishPoint = null;
	
	/** pointer to label that shows how much money are left for buying cannons */
	JLabel moneyLabel = null;
	/** pointer to amount of money */
	Integer money = null;
	
	/** member that provides and generates textures*/
	TextureFactory textureFactory = new TextureFactory();
	
	public TowDefDrawing()
	{}
	
	/**
	 * method called upon defeating all enemies
	 * */
	public void Win()
	{
		System.err.println("you win");
		setGameStatus(GameStatus.WIN);
	}
	
	/**
	 * method called upon escaping one of the enemies from the map
	 */
	public void Lose()
	{
		System.err.println("you lost");
		setGameStatus(GameStatus.LOSE);
	}
	
	/**
	 * @category setter
	 * setter for range previewer
	 * */
	public void setRangePreviewer(RangePreviewer a_rangePreviewer)
	{
		rangePreviewer = a_rangePreviewer;
	}
	
	/**
	 * @category setter
	 * setter for enemy list
	 * */
	public void setEnemies(ArrayList<Enemy> a_enemies)
	{
		enemies = a_enemies;
	}
	
	/**
	 * @category setter
	 * setter for cannon list
	 * */
	public void setCannons(ArrayList<Cannon> a_cannons)
	{
		cannons = a_cannons;
	}
	
	/**
	 * @category setter
	 * setter for bullet list
	 * */
	public void setBullets(ArrayList<Bullet> a_bullets)
	{
		bullets = a_bullets;
	}
	
	/**
	 * @category setter
	 * setter for last enemy point
	 * */
	public void setFinishPoint(Vector2d a_finishPoint)
	{
		finishPoint = a_finishPoint;
	}
	
	/**
	 * @category setter
	 * setter for money
	 * */
	public void setMoney(Integer a_money, JLabel a_moneyLabel)
	{
		money = a_money;
		moneyLabel = a_moneyLabel;
	}
	
	/**
	 * @param price		price
	 * money updater
	 * */
	public void updateMoney(Integer price)
	{
		money += price;
		moneyLabel.setText("money: " + Integer.toString(money) + "$  ");
	}
	
	/**
	 * @param x		X-coordinate
	 * @param y		Y-coordinate
	 * @param type	type of cannon [1,2,3]
	 */
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
	
	/**
	 * @param gLDrawable	pointer to {@link GLCanvas}
	 * initializing textures using {@link TextureFactory}
	 * */
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
	ArrayList<Vector2d> path = new ArrayList<Vector2d>();
	long elapsed = 0;
	
	/**
	 * @param gLDrawable	pointer to {@link GLCanvas}
	 * <b>iteration of loop in animation</b>
	 * frequency is set to 60 fps by default
	 * */
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
		
		/*RESOLUTION*/
		if (enemies != null)
    	{
    		if (enemies.size() == 0)
    		{
    			Win();
    			return;
    		}
    		for (Enemy enemy : enemies) 
    		{
    			if (enemy.IsCloseTo(finishPoint))
    			{
    				Lose();
    				return;
    			}
			}
    	}
		
		elapsed++;
    }

    /**
     * drawing a map 
     * */
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
    
 
	/**
	 * method called before animation starts
	 * initializing the path of the enemies
	 */
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
    	finishPoint = new Vector2d(45 + 9 * MapX, 30 + 6 * MapY);

        GL2 gl = gLDrawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL2.GL_FLAT);
    }
 
    /**
     * this method is required by the interface, but in current project is not used
     * */
    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) 
    {}
 
 
    /**
     * this method is required by the interface, but in current project is not used
     * */    
	public void dispose(GLAutoDrawable arg0) 
	{
		System.out.println("dispose() called");
	}

}
