import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.media.nativewindow.WindowClosingProtocol.WindowClosingMode;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.GLProfile;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import com.jogamp.opengl.util.FPSAnimator;

/**
 * @author hmira
 *  
 * */
public class TowerDef {

	static int MapX = 90;
	static int MapY = 80;
	static boolean[][] map = new boolean[11][6];
	
	/**
	 * basic function showing GUI
	 *  */
	public static void createAndShowGUI()
	{
		mainMenu("Welcome to tower defense");
	}
	
	/**
	 * @param s	welcome text in label
	 * */
	public static void mainMenu(String s)
	{
        final JFrame jf = new JFrame( "Tower Defense" );
 		jf.pack();
 		
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(400, 100));
		p.setLayout(new BorderLayout());
		
		Button jb = new Button("press to play");
		jb.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				startGame();
				jf.dispose();
			}
		});

        jf.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });
		
		p.add(new JLabel(s), BorderLayout.NORTH);
		p.add(jb, BorderLayout.CENTER);

 		jf.getContentPane().add(p);
 		jf.setSize(320, 80);
 		jf.setVisible(true);	
	}
	
	/**
	 * function that starts a gameplay
	 * */
	public static void startGame()
	{
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(400, 100));
		p.setLayout(new BorderLayout());
		
		JToolBar jtb = new JToolBar();
		final JLabel moneyLabel = new JLabel("money: 3$  ");
		moneyLabel.setSize(200, 20);
		jtb.add(moneyLabel);
		jtb.setSize(990,20);

		final JRadioButton can1_button = new JRadioButton("Weak cannon 1$");
		can1_button.setSelected(true);
		final JRadioButton can2_button = new JRadioButton("Strong cannon 3$");
		final JRadioButton can3_button = new JRadioButton("Super strong cannon 10$");

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(can1_button);
		buttonGroup.add(can2_button);
		buttonGroup.add(can3_button);

		jtb.add(can1_button);
		jtb.add(can2_button);
		jtb.add(can3_button);
		p.add(jtb, BorderLayout.NORTH);
		
        final JFrame jf = new JFrame( "Tower Defense" );
 		jf.pack();
 		
        jf.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });
        
 		jf.getContentPane().add(p);
 		
 		jf.setSize(990, 540);
 		jf.setResizable(false);
 		jf.setVisible(true);
		
		final ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		final ArrayList<Cannon> cannons = new ArrayList<Cannon>();
		final ArrayList<Bullet> bullets = new ArrayList<Bullet>();
		final RangePreviewer rangePreviewer = new RangePreviewer();

		
		//list of the enemies and their time-to-get-ready
		enemies.add(new Enemy1(0));
		enemies.add(new Enemy1(100));
		enemies.add(new Enemy1(200));
		enemies.add(new Enemy1(300));
		enemies.add(new Enemy1(400));
		enemies.add(new Enemy1(500));
		enemies.add(new Enemy1(600));
		enemies.add(new Enemy1(700));
		enemies.add(new Enemy1(800));
		enemies.add(new Enemy1(900));
		enemies.add(new Enemy1(1000));
		enemies.add(new Enemy1(1100));
		enemies.add(new Enemy1(1200));
		enemies.add(new Enemy1(1300));

		enemies.add(new Enemy2(1600));
		enemies.add(new Enemy2(1700));
		enemies.add(new Enemy2(1800));
		enemies.add(new Enemy2(1900));
		enemies.add(new Enemy2(2000));
		enemies.add(new Enemy2(2100));
		enemies.add(new Enemy2(2200));

		enemies.add(new Enemy3(3000));
		enemies.add(new Enemy3(3100));
		enemies.add(new Enemy3(3200));
		enemies.add(new Enemy3(3300));
		enemies.add(new Enemy3(3400));
		enemies.add(new Enemy3(3500));
		enemies.add(new Enemy3(3600));

		enemies.add(new Enemy3(5000));
		enemies.add(new Enemy3(5050));
		enemies.add(new Enemy3(5100));
		enemies.add(new Enemy3(5150));
		enemies.add(new Enemy3(5200));
		enemies.add(new Enemy3(5250));
		enemies.add(new Enemy3(5300));
		enemies.add(new Enemy3(5350));
		enemies.add(new Enemy3(5400));
		enemies.add(new Enemy3(5450));
		enemies.add(new Enemy3(5500));
		enemies.add(new Enemy3(5550));
		enemies.add(new Enemy3(5600));
		enemies.add(new Enemy3(5650));
		
		GLProfile glp = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(glp);
 
    	// The canvas is the widget that's drawn in the JFrame
    	final GLCanvas glcanvas = new GLCanvas(capabilities);
    	
    	final FPSAnimator am = new FPSAnimator(glcanvas, 60);
        p.add( glcanvas, BorderLayout.CENTER);
    	final TowDefDrawing tdd = new TowDefDrawing();
    	tdd.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				System.err.println(arg0.getNewValue());
				if (arg0.getNewValue() == TowDefDrawing.GameStatus.LOSE)
				{
					am.stop();
					glcanvas.setDefaultCloseOperation(WindowClosingMode.DO_NOTHING_ON_CLOSE);
					glcanvas.getContext().getGL().getGL2().getContext().release();
					
					jf.setVisible(false);
					JOptionPane.showMessageDialog(null, "You Lost!");
					System.exit(0);
				}
				else if (arg0.getNewValue() == TowDefDrawing.GameStatus.WIN)
				{
					am.stop();
					glcanvas.setDefaultCloseOperation(WindowClosingMode.DO_NOTHING_ON_CLOSE);
					glcanvas.getContext().getGL().getGL2().getContext().release();
					
					jf.setVisible(false);
					JOptionPane.showMessageDialog(null, "You Win!");
					System.exit(0);
				}
			}
		});
    	
    	tdd.setMoney(3, moneyLabel);
    	tdd.setEnemies(enemies);
    	tdd.setCannons(cannons);
    	tdd.setBullets(bullets);
    	tdd.setRangePreviewer(rangePreviewer);
    	
    	glcanvas.addGLEventListener(tdd);
    	
    	glcanvas.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (SwingUtilities.isRightMouseButton(arg0))
				{
					rangePreviewer.SetOn(arg0.getX(), arg0.getY());
				}
				else if (SwingUtilities.isLeftMouseButton(arg0))
				{
					int x = arg0.getX() / MapX;
				    int y = arg0.getY() / MapY;
				    if (!map[x][y]) return;
					
				    int i = 1;

				    if (can1_button.isSelected()) i = 1;
				    if (can2_button.isSelected()) i = 2;
				    if (can3_button.isSelected()) i = 3;
				    
					tdd.createCannon(arg0.getX(), arg0.getY(), i);
				}
			}
		});
 		am.start();
	}
	
	public static void main(String[] args) {

		GLProfile.initSingleton();
		for (int i = 0; i < 11; ++i) {
	        for (int j = 0; j < 6; ++j) {
	            map[i][j] = false;
	        }
	    }

	    for (int i = 0; i < 5; ++i) {
	        map[0][i] = true;
	    }

	    for (int i = 2; i < 11; ++i) {
	        map[i][0] = true;
	    }

	    for (int i = 1; i < 6; ++i) {
	        map[10][i] = true;
	    }

	    for (int i = 0; i < 9; ++i) {
	        map[i][5] = true;
	    }

	    for (int i = 0; i < 3; ++i) {
	        map[2][i+1] = true;
	    }

	    for (int i = 0; i < 3; ++i) {
	        map[6][i+1] = true;
	    }

	    for (int i = 0; i < 3; ++i) {
	        map[4][i+2] = true;
	    }

	    for (int i = 0; i < 3; ++i) {
	        map[8][i+2] = true;
	    }
		
		
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
