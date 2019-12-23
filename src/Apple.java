import java.util.Random;

public class Apple {
	int X;
    int Y;
    
    Apple()
    {
    	respawn();
    }
    
    void respawn()
    {
    	X = new Random().nextInt(Field.WIDTH);
    	Y = new Random().nextInt(Field.HEIGHT);
    }
}
