import java.awt.Image;

import javax.swing.ImageIcon;

final class Field {
	public final static int WIDTH = 20;
	public final static int HEIGHT = 20;
	public final static int DOT_SIZE = 16;
	public final static int ALL_DOTS = WIDTH*HEIGHT;
	public final static int SNAKE_VIS=9;
	public final static int MAX_SNAKE_NUM=14;
	
	public final static Image head_sprite[] = {
			new ImageIcon("res/head_left.png").getImage(),
			new ImageIcon("res/head_up.png").getImage(),
			new ImageIcon("res/head_right.png").getImage(),
			new ImageIcon("res/head_down.png").getImage()
			};
	public final static Image body_sprite = new ImageIcon("res/body.png").getImage();
	public final static Image apple_sprite = new ImageIcon("res/apple.png").getImage();
	
	public static boolean vis=true;
}
