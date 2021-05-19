package k1Sokoban;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class Hazard extends Actor {

    public Hazard(int x, int y) {
        super(x, y);
        URL loc = this.getClass().getResource("/res/ball.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage();
        this.setImage(image);
    }

   
}
