package k1Sokoban;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;

import sf.Sound;
import sf.SoundFactory;

@SuppressWarnings("serial")
public final class Sokoban extends JFrame {

    private final int OFFSET = 30;
    public static int move = 0;
    static JLabel moves;
    static JLabel instructions;
    public final static String DIR = "src/res/";
    public final static String START = DIR + "start.wav";
    
    public Sokoban() {
        InitUI();
    }

    public void InitUI() {
    	 instructions = new JLabel("Press R to restart or Q to quit. Colliding with a Hazard ends the game, so be careful!");
         add(instructions, BorderLayout.NORTH);
         
         
    	 moves = new JLabel("Moves: 0");
         add(moves, BorderLayout.SOUTH);
         
        Board board = new Board();
        add(board);
       
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(board.getBoardWidth() + OFFSET,
                board.getBoardHeight() + 2*OFFSET);
        setLocationRelativeTo(null);
        setTitle("Sokoban");
     
    }


    public static void main(String[] args) {
    	
        Sokoban sokoban = new Sokoban();
        sokoban.setVisible(true);
        Sound sound = SoundFactory.getInstance(START);
        SoundFactory.play(sound);
        
    }
}