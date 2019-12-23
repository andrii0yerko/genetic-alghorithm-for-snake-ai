import javax.swing.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameField extends JButton {
	
	private static final long serialVersionUID = 5148083900744081231L;
	//private static final long serialVersionUID = 1L;
    
    private ArrayList<Apple> apples = new ArrayList<Apple>();
    int num_of_apples = 5;
    private ArrayList<Snake>snakes;
    
    boolean started = false;
    boolean pve = false;
    boolean supervised = true;

    public void containsSnake(Snake s)
    {
    	if (snakes.contains(s)) setBackground(Color.gray);
    	repaint();
    }
    
    public GameField(Snake snake)
    {
    	this();
        snakes=new ArrayList<Snake>();
        snakes.add(snake);
        initGame();
    	
    	}
    
    public GameField(ArrayList<Snake> snks){
    	this();
        snakes = snks;
        initGame();
    }
    
    public GameField() {
    	setSize(Field.DOT_SIZE*Field.WIDTH,Field.DOT_SIZE*(Field.HEIGHT));
        setBackground(Color.white);
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
        snakes = new ArrayList<Snake>();
	}

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (started)
	    {
	        for(Apple apple:apples)
	        g.drawImage(Field.apple_sprite,apple.X*Field.DOT_SIZE,apple.Y*Field.DOT_SIZE,this);
	        
	        boolean inGame = false;
	        
	        for(Snake snake:snakes)
	        {
	        	if (snake.isAlive()) inGame = true;
	        	if(supervised) g.drawRect(	//??
	        			Math.max(Field.DOT_SIZE*(snake.headX()-4),0),
	        			Math.max(Field.DOT_SIZE*(snake.headY()-4),0),
	        			Math.min(Field.DOT_SIZE*9,(Field.DOT_SIZE*Field.WIDTH-snake.headX())),
	        			Math.min(Field.DOT_SIZE*9,(Field.DOT_SIZE*Field.HEIGHT-snake.headY()))
	        			);
	            	
		        for (int i = 1; i < snake.getLength(); i++) {
		        	g.drawImage(Field.body_sprite,snake.getX(i)*Field.DOT_SIZE,snake.getY(i)*Field.DOT_SIZE,this);
		        }
		        g.drawImage(Field.head_sprite[snake.getDirection().ordinal()],snake.headX()*Field.DOT_SIZE,snake.headY()*Field.DOT_SIZE,this);
	        }
	        if(!inGame) {
	        	String str = "Ridiculous death...";
	            g.setColor(Color.black);
	            g.drawString(str,(Field.DOT_SIZE*Field.WIDTH - 6*str.length())/2,Field.DOT_SIZE*(Field.HEIGHT-1)/2);
	        }
        }
        g.drawRect(0,0,Field.DOT_SIZE*Field.WIDTH,Field.DOT_SIZE*Field.HEIGHT);
    }
    

    public void update()
    {
    	for(Snake snake:snakes) 
    	if(snake.isAlive()){
        	{
        	snake.react(getstate(snake));
        	snake.move();
            checkApple(snake);
            checkCollisions(snake);
        	}
        }
    	if (Field.vis) repaint();
    }
    
    class FieldKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            
            for(Snake snake:snakes) {
	            if(supervised)
	        	{
	            	snake.react(getstate(snake), key);
	            	snake.move();
	                checkApple(snake);
	                checkCollisions(snake);
	                repaint();
	        	}
	            
	            else 
	            {
	            	snake.react(key);
	            }
            }
        }
    }

    public void initGame()
    {
        if (pve) snakes.add( new Snake(6,8,3));
        for(int i=0;i<num_of_apples;i++) apples.add(new Apple());
        started = true;
    }
    
    public void checkApple(Snake snake){
    	
    	for(Apple apple:apples)
    	{
    		if(snake.headX() == apple.X && snake.headY() == apple.Y){
    			apple.respawn();
	            snake.grow();
	            continue;
	            
	        }
	        for (int i = 1; i < snake.getLength(); i++) {
	        	if(snake.getX(i) == apple.X && snake.getY(i) == apple.Y)
	        	{
	        		apple.respawn();
	        	}
	        }
    	}
	        
    }

    public void checkCollisions(Snake snake){
	        for (int i = snake.getLength(); i >3 ; i--) {
	            if(snake.headX() == snake.getX(i) && snake.headY() == snake.getY(i)){
	                snake.death();
	            }
	        }
	
	        if(snake.headX()>Field.WIDTH-1){
	        	snake.death();
	        }
	        if(snake.headX()<0){
	        	snake.death();
	        }
	        if(snake.headY()>Field.HEIGHT-1){
	        	snake.death();
	        }
	        if(snake.headY()<0){
	        	snake.death();
	        }
    }
    
    char[][] rotateAnticlockwise(char[][] state)
	{
		char rs[][] = new char[Field.SNAKE_VIS][Field.SNAKE_VIS];
		
		for(int i=0;i<Field.SNAKE_VIS;i++)
			for(int j=0;j<Field.SNAKE_VIS;j++)
				rs[i][j]=state[j][Field.SNAKE_VIS-i-1];
			
		return rs;
	}
    
    protected char[][] getstate(Snake snake)//ÏÅÐÅÄÅËÀÒÜ ÍÀÔÈÃ
    {	
    	char field[][] = new char[Field.HEIGHT][Field.WIDTH];
		field[snake.headY()][snake.headX()]='h';
    	for (int i = 1; i < snake.getLength(); i++) {
        	field[snake.getY(i)][snake.getX(i)]='s';
    	}
    	for(Apple apple:apples) field[apple.Y][apple.X]='a';
    	
    	char state[][] = new char[Field.SNAKE_VIS][Field.SNAKE_VIS];
    	
    	int cornerX = (snake.headX()-(Field.SNAKE_VIS-1)/2);
    	int cornerY = (snake.headY()-(Field.SNAKE_VIS-1)/2);
    	if(cornerY < 0)
    	{
    		for(int i=0;i<Field.SNAKE_VIS;i++)
    			state[Math.abs(cornerY)-1][i]='w';
    		for(int i=0;i<Field.SNAKE_VIS+cornerY;i++)
    		{
    			if(cornerX < 0) 
    			{
    				state[i-cornerY][Math.abs(cornerX)-1]='w';
    				for(int j=0;j<cornerX+Field.SNAKE_VIS;j++)
            		{
    					state[i-cornerY][j-cornerX]=field[i][j];
            		}
            	}
    			
    			else if(cornerX+Field.SNAKE_VIS > Field.WIDTH) 
    			{
    				state[i-cornerY][Field.WIDTH-cornerX]='w';
    				for(int j=cornerX;j<Field.WIDTH;j++)
            		{
    					state[i-cornerY][j-cornerX]=field[i][j];
            		}
    			}
    			
    			else if(cornerX >= 0) state[i-cornerY]=Arrays.copyOfRange(field[i], cornerX, cornerX+Field.SNAKE_VIS);
    		}
    	}
    	
    	else if(cornerY+Field.SNAKE_VIS>Field.HEIGHT)
    	{
    		for(int i=0;i<Field.SNAKE_VIS;i++)
    			state[Field.SNAKE_VIS-(cornerY+Field.SNAKE_VIS-Field.HEIGHT)][i]='w';
    		for(int i=cornerY;i<Field.HEIGHT;i++)
    		{
    			if(cornerX < 0) 
    			{
    				state[i-cornerY][Math.abs(cornerX)-1]='w';
    				for(int j=0;j<cornerX+Field.SNAKE_VIS;j++)
            		{
    					state[i-cornerY][j-cornerX]=field[i][j];
            		}
            	}
    			
    			else if(cornerX+Field.SNAKE_VIS > Field.WIDTH) 
    			{
    				state[i-cornerY][Field.WIDTH-cornerX]='w';
    				for(int j=cornerX;j<Field.WIDTH;j++)
            		{
    					state[i-cornerY][j-cornerX]=field[i][j];
            		}
    			}
    			
    			else if(cornerX >= 0) state[i-cornerY]=Arrays.copyOfRange(field[i], cornerX, cornerX+Field.SNAKE_VIS);
    		}
    	}
    	
    	else if(cornerY >= 0)
    	{
    		for(int i=0;i<Field.SNAKE_VIS;i++)
    		{
    			if(cornerX < 0) 
    			{
    				state[i][Math.abs(cornerX)-1]='w';
    				for(int j=0;j<cornerX+Field.SNAKE_VIS;j++)
            		{
    					state[i][j-cornerX]=field[cornerY+i][j];
            		}
            	}
    			
    			else if(cornerX+Field.SNAKE_VIS > Field.WIDTH) 
    			{
    				
    				state[i][Field.WIDTH-cornerX]='w';
    				for(int j=cornerX;j<Field.WIDTH;j++)
            		{
    					state[i][j-cornerX]=field[cornerY+i][j];
            		}
    			}
    			
    			else if(cornerX >= 0) state[i]=Arrays.copyOfRange(field[cornerY+i], cornerX, cornerX+Field.SNAKE_VIS);
    		}

    	}
    	
    	switch (snake.direction)
		{
			case LEFT:
				state = rotateAnticlockwise(state);
				state = rotateAnticlockwise(state);
				state = rotateAnticlockwise(state);
				break;
			case RIGHT:
				state = rotateAnticlockwise(state);
				break;
			case DOWN:
				state = rotateAnticlockwise(state);
				state = rotateAnticlockwise(state);
				break;
		}
		
    	
    	
    	return state;
    }
    



}
