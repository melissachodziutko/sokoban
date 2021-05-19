package k1Sokoban;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Random;
import java.util.Scanner;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import sf.Sound;
import sf.SoundFactory;

@SuppressWarnings({"serial","rawtypes", "unchecked"})
public class Board extends JPanel implements ActionListener { 

    private final int OFFSET = 30;
    private final int SPACE = 40;
    private final int LEFT_COLLISION = 1;
    private final int RIGHT_COLLISION = 2;
    private final int TOP_COLLISION = 3;
    private final int BOTTOM_COLLISION = 4;
    private ArrayList walls = new ArrayList();
    private ArrayList baggs = new ArrayList();
    private ArrayList areas = new ArrayList();
    private ArrayList hazards = new ArrayList();
    private Player soko;
    File myFile;
    private int w = 0;
    private int num = 0;
    private int h = 0;
    private int fi = 0;
    private long start = 0;
    private String sec;
    private boolean completed = false;
    private boolean first = true;
    private String lev = "";
    

    public final static String DIR = "src/res/";
    public final static String MOVE = DIR + "tap.wav";
    public final static String ACC = DIR + "accomplish.wav";
    public final static String IN = DIR + "in.wav";
    public final static String START = DIR + "start.wav";


   
    
    private String level =
              "    ######\n"
            + "    ##   #\n"
            + "    ##$  #\n"
            + "  ####  $##\n"
            + "  ##  $ $ #\n"
            + "#### # ## #   ######\n"
            + "##^^ # ## #####  ..#\n"
            + "##^^  $          ..#\n"
            + "######$### #@##  ..#\n"
            + "    ##     #########\n"
            + "    ########\n";
    
    private String level2 =
    		  			  "########  \n"
    		            + "# ..#  #  \n"
    		  			+"# ..# $###\n"
    		            +"#  ##    #\n"
    		  			+"## $   $ #\n"
    		  			+" # ##  ###\n"
    		  			+" #   $##  \n"
    		  			+" ###  #   \n"
    		  			+"   #@ #   \n"
    		  			+"   ####   \n";
    private String level3 =
            "################ \n"
          + "#   #######    # \n"
          + "# #   ...## $$$##\n"
          + "# ##  . .   $@$ #\n"
          + "# ## #...## $$$ #\n"
          + "#    ######     #\n"
          + "#################\n";
    
    public Board() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        initWorld();
        start = System.currentTimeMillis();
        Timer timer = new Timer(secondsTimer(), this);
        timer.start();
        
    }
    
    
   

    public int getBoardWidth() {
        return this.w;
    }

    public int getBoardHeight() {
        return this.h;
    }

    public final void initWorld() {
    	
        
        int x = OFFSET;
        int y = OFFSET;
        
        Wall wall;
        Baggage b;
        Area a;
        Hazard u;
        lev = level;
        if (first == false) {
        double ran = Math.round((Math.random() * ((3 - 1) + 1)));
        if (ran == 1){
        lev = level;
        }else if (ran == 2) {
        lev = level2;
        }else{
        lev = level3;
        }
        }
        first = false;
        for (int i = 0; i < lev.length(); i++) {

            char item = lev.charAt(i);

            if (item == '\n') {
                y += SPACE;
                if (this.w < x) {
                    this.w = x;
                }

                x = OFFSET;
            } else if (item == '#') {
                wall = new Wall(x, y);
                walls.add(wall);
                x += SPACE;
            } else if (item == '$') {
                b = new Baggage(x, y);
                baggs.add(b);
                x += SPACE;
            } else if (item == '^') {
                u = new Hazard(x, y);
                hazards.add(u);
                x += SPACE;
            }else if (item == '.') {
                a = new Area(x, y);
                areas.add(a);
                x += SPACE;
            } else if (item == '@') {
                soko = new Player(x, y);
                x += SPACE;
            } else if (item == ' ') {
                x += SPACE;
            }

            h = y;
        }
    }

    public void buildWorld(Graphics g) {
    	
        g.setColor(new Color(250, 240, 170));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if(lev == level) {
        myFile = new File("hs.txt");
        }if (lev == level2) {
        	myFile = new File("hs2.txt");
        }if (lev == level3) {
        	myFile = new File("hs3.txt");	
        }
        try
	    {      
	      Scanner input = new Scanner(myFile);
	      
	      fi = input.nextInt();
	     g.setColor(new Color(0, 0, 0));
        g.drawString("High Score: " + fi, 25, 20);
	    } catch(FileNotFoundException ex)
	    {
		      System.out.println("no high score txt found"); 
		    }
        ArrayList world = new ArrayList();
        world.addAll(walls);
        world.addAll(areas);
        world.addAll(baggs);
        world.addAll(hazards);
        world.add(soko);

        for (int i = 0; i < world.size(); i++) {

            Actor item = (Actor) world.get(i);

            if ((item instanceof Player)
                    || (item instanceof Baggage)) {
                g.drawImage(item.getImage(), item.x() + 2, item.y() + 2, this);
            } else {
                g.drawImage(item.getImage(), item.x(), item.y(), this);
            }

            if (completed) {
                g.setColor(new Color(0, 0, 0));
                g.drawString("Completed", 25, 20);
            }
            g.setColor(new Color(0, 0, 0));
            g.drawString(elapsed(), w - 100, 30);
          

        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        buildWorld(g);
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
        	 int key = e.getKeyCode();
        	/*if (key == KeyEvent.VK_R) {
                restartLevel();
            } else*/
        	 if (completed) {
                return;
            }

            
           
            
            

            if (key == KeyEvent.VK_LEFT) {
            	
                if (checkWallCollision(soko,
                        LEFT_COLLISION)) {
                    return;
                }

                if (checkBagCollision(LEFT_COLLISION)) {
                    return;
                }
                if (checkHazardCollision(soko, LEFT_COLLISION)) {
                	System.exit(0);
                }
                key();
                soko.move(-SPACE, 0);

            } else if (key == KeyEvent.VK_RIGHT) {

                if (checkWallCollision(soko,
                        RIGHT_COLLISION)) {
                    return;
                }

                if (checkBagCollision(RIGHT_COLLISION)) {
                    return;
                }
                if (checkHazardCollision(soko, RIGHT_COLLISION)) {
                	System.exit(0);
                }
                key();
                soko.move(SPACE, 0);

            } else if (key == KeyEvent.VK_UP) {
            	
                if (checkWallCollision(soko,
                        TOP_COLLISION)) {
                    return;
                }

                if (checkBagCollision(TOP_COLLISION)) {
                    return;
                }
                if (checkHazardCollision(soko, TOP_COLLISION)) {
                	System.exit(0);
                }
                key();
                soko.move(0, -SPACE);

            } else if (key == KeyEvent.VK_DOWN) {
            	
                if (checkWallCollision(soko,
                        BOTTOM_COLLISION)) {
                    return;
                }

                if (checkBagCollision(BOTTOM_COLLISION)) {
                    return;
                }
                if (checkHazardCollision(soko, BOTTOM_COLLISION)) {
                	System.exit(0);
                }
                key();
                soko.move(0, SPACE);

            } else if (key == KeyEvent.VK_R) {
                restartLevel();
                Sokoban.move = 0;
                Sokoban.moves.setText("Moves: " + Sokoban.move);
            }
            
            
            else if (key == KeyEvent.VK_Q) {
                System.exit(0);
            }

            repaint();
        }
    }

    private boolean checkHazardCollision(Actor actor, int type) {

        if (type == LEFT_COLLISION) {

            for (int i = 0; i < hazards.size(); i++) {
            	Hazard haz = (Hazard) hazards.get(i);
                if (actor.isLeftCollision(haz)) {
                    return true;
                }
            }
            return false;
            

        } else if (type == RIGHT_COLLISION) {

            for (int i = 0; i < hazards.size(); i++) {
            	Hazard haz = (Hazard) hazards.get(i);
                if (actor.isRightCollision(haz)) {
                    return true;
                }
            }
            return false;

        } else if (type == TOP_COLLISION) {

            for (int i = 0; i < hazards.size(); i++) {
            	Hazard haz = (Hazard) hazards.get(i);
                if (actor.isTopCollision(haz)) {
                    return true;
                }
            }
            return false;

        } else if (type == BOTTOM_COLLISION) {

            for (int i = 0; i < hazards.size(); i++) {
                Hazard haz = (Hazard) hazards.get(i);
                if (actor.isBottomCollision(haz)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    private boolean checkWallCollision(Actor actor, int type) {

        if (type == LEFT_COLLISION) {

            for (int i = 0; i < walls.size(); i++) {
                Wall wall = (Wall) walls.get(i);
                if (actor.isLeftCollision(wall)) {
                    return true;
                }
            }
            return false;
            

        } else if (type == RIGHT_COLLISION) {

            for (int i = 0; i < walls.size(); i++) {
                Wall wall = (Wall) walls.get(i);
                if (actor.isRightCollision(wall)) {
                    return true;
                }
            }
            return false;

        } else if (type == TOP_COLLISION) {

            for (int i = 0; i < walls.size(); i++) {
                Wall wall = (Wall) walls.get(i);
                if (actor.isTopCollision(wall)) {
                    return true;
                }
            }
            return false;

        } else if (type == BOTTOM_COLLISION) {

            for (int i = 0; i < walls.size(); i++) {
                Wall wall = (Wall) walls.get(i);
                if (actor.isBottomCollision(wall)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private boolean checkBagCollision(int type) {

        if (type == LEFT_COLLISION) {

            for (int i = 0; i < baggs.size(); i++) {

                Baggage bag = (Baggage) baggs.get(i);
                if (soko.isLeftCollision(bag)) {

                    for (int j=0; j < baggs.size(); j++) {
                        Baggage item = (Baggage) baggs.get(j);
                        if (!bag.equals(item)) {
                            if (bag.isLeftCollision(item)) {
                                return true;
                            }
                        }
                        if (checkWallCollision(bag,
                                LEFT_COLLISION)) {
                            return true;
                        }
                    }
                    bag.move(-SPACE, 0);
                    isCompleted();
                }
            }
            return false;

        } else if (type == RIGHT_COLLISION) {

            for (int i = 0; i < baggs.size(); i++) {

                Baggage bag = (Baggage) baggs.get(i);
                if (soko.isRightCollision(bag)) {
                    for (int j=0; j < baggs.size(); j++) {

                        Baggage item = (Baggage) baggs.get(j);
                        if (!bag.equals(item)) {
                            if (bag.isRightCollision(item)) {
                                return true;
                            }
                        }
                        if (checkWallCollision(bag,
                                RIGHT_COLLISION)) {
                            return true;
                        }
                    }
                    bag.move(SPACE, 0);
                    isCompleted();                   
                }
            }
            return false;

        } else if (type == TOP_COLLISION) {

            for (int i = 0; i < baggs.size(); i++) {

                Baggage bag = (Baggage) baggs.get(i);
                if (soko.isTopCollision(bag)) {
                    for (int j = 0; j < baggs.size(); j++) {

                        Baggage item = (Baggage) baggs.get(j);
                        if (!bag.equals(item)) {
                            if (bag.isTopCollision(item)) {
                                return true;
                            }
                        }
                        if (checkWallCollision(bag,
                                TOP_COLLISION)) {
                            return true;
                        }
                    }
                    bag.move(0, -SPACE);
                    isCompleted();
                }
            }
            return false;

        } else if (type == BOTTOM_COLLISION) {
        
            for (int i = 0; i < baggs.size(); i++) {

                Baggage bag = (Baggage) baggs.get(i);
                if (soko.isBottomCollision(bag)) {
                    for (int j = 0; j < baggs.size(); j++) {

                        Baggage item = (Baggage) baggs.get(j);
                        if (!bag.equals(item)) {
                            if (bag.isBottomCollision(item)) {
                                return true;
                            }
                        }
                        if (checkWallCollision(bag,
                                BOTTOM_COLLISION)) {
                            return true;
                        }
                    }
                    bag.move(0, SPACE);
                    isCompleted();
                }
            }
        }
        return false;
    }
   
    public void isCompleted() {

    	num = baggs.size();
        int compl = 0;

        for (int i = 0; i < num; i++) {
        	
            Baggage bag = (Baggage) baggs.get(i);
            for (int j = 0; j < num; j++) {
                Area area = (Area) areas.get(j);
                if (bag.x() == area.x()
                        && bag.y() == area.y()) {
                	/*
                	Sound sound = SoundFactory.getInstance(IN);
                    SoundFactory.play(sound);
                    */
                    compl += 1;
                }
            }
        }

        if (compl == num) {
        	Sound sound = SoundFactory.getInstance(ACC);
            SoundFactory.play(sound);
            completed = true;
            doHighScore();
            repaint();
        }
 
    }

    public void restartLevel() {
    	
        areas.clear();
        baggs.clear();
        walls.clear();
        hazards.clear();
        num = 0;
        initWorld();
        Sound sound = SoundFactory.getInstance(START);
        SoundFactory.play(sound);
        Sokoban.move = 0;
        Sokoban.moves.setText("Moves: " + Sokoban.move);
        if (completed) {
            completed = false;
        }
    }
    public void key() {

    	Sokoban.move++;
        Sokoban.moves.setText("Moves: " + Sokoban.move);
        Sound sound = SoundFactory.getInstance(MOVE);
        SoundFactory.play(sound);
    }
    private String elapsed() {
    	return "Time:" + Long.toString((System.currentTimeMillis() - start)/60000) + ":" + Long.toString((System.currentTimeMillis() - start)/1000);
    	
    }
    private int secondsTimer() {
    	return (int)((System.currentTimeMillis() - start)/1000);
    	
    }




	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
		
	}
	public void doHighScore()
    {
        if (fi > Sokoban.move)
        {
        	try {
        		// append = false
        		int write = Sokoban.move;
        		String wr = Integer.toString(write);
        		FileWriter fw = new FileWriter(myFile, false);
        		fw.write(wr);
        		fw.close();
        	} catch (IOException e) {
        		e.printStackTrace();
        	} 
        	//PlayerPrefs.SetInt("HighScore", fi);  
        }
       
        
        
    }
 
}