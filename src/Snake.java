import java.awt.event.KeyEvent;

public class Snake {
	
	private boolean alive = true;
	
	private int[] x = new int[Field.ALL_DOTS];
    private int[] y = new int[Field.ALL_DOTS];
    private int length;
    protected Direction direction = Direction.RIGHT;
    
    public Direction getDirection(){ return direction; }
    public void setDirection(Direction dir) { direction = dir; }
    
    public int getX(int i){ return x[i]; }
    public int getY(int i){ return y[i]; }
    public int headX(){ return x[0]; }
    public int headY(){ return y[0]; }
    public int getLength(){ return length; }
    public void grow() {length++;}
    
    public boolean isAlive() {return alive;}
    public void death() {alive=false;}
    public int turns=0;
    
    
    public Snake(int start_x, int start_y, int len)
    {
    	length = len;
        for (int i = 0; i < length; i++) {
            x[i] = start_x - i;
            y[i] = start_y;
        }
    }
    
    public void move(){
        for (int i = length; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction)
        {
        	case LEFT:
        		x[0] --;
        		break;
        	case RIGHT:
        		x[0] ++;
        		break;
        	case UP:
        		y[0] --;
        		break;
        	case DOWN:
        		y[0] ++;
        		break;
        }
    }
    
    public void react(int key)
    {
    	turns++;
    	if(key == KeyEvent.VK_LEFT && direction != Direction.RIGHT){ direction = Direction.LEFT; }
        if(key == KeyEvent.VK_RIGHT && direction != Direction.LEFT){ direction = Direction.RIGHT; }
        if(key == KeyEvent.VK_UP && direction != Direction.DOWN){ direction = Direction.UP; }
        if(key == KeyEvent.VK_DOWN && direction != Direction.UP){ direction = Direction.DOWN; }
    }
	public void react(char[][] state) {
		turns++;
	}
	public void react(char[][] state, int key) {
		react(key);
	}
}
