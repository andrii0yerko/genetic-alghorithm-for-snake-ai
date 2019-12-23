import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;


public class Round
{
	private long round_number=0;
	private int snakes_number=14 ;
	
	ArrayList <Snake> snakes = new ArrayList<Snake>();
	ArrayList <GameField> fields = new ArrayList<GameField>();
	
	ArrayList <Snake> bestSnakes = new ArrayList<Snake>();
	private int number_of_parents=3;
	
	
	
	private int currentTurn=0;
	private int turnLimit=450;//Integer.MAX_VALUE;
	public Round setTurnLimit(int num) {turnLimit = num; return this;}
	
	private double mutationRate = 0.05;
	public Round setMutationRate(double num) {mutationRate = num; return this;}
	
	
	private double crossingoverRate = 0.05;
	private boolean ended=false;
	public boolean Ended() {return ended;}
	
	public String Info()
	{ 
		return "round_number : " + round_number +"\n currentTurn : " + currentTurn;
	}
	
	public Round(Round prev, int snakes_num, int parents_num)
	{
		snakes_number = snakes_num;
		number_of_parents =	parents_num;
		round_number = prev.round_number+1;
		for(int i=0;i<snakes_number;i++)
    	{
			AISnake s = new AISnake(prev.bestSnakes.get(new Random((long)(Math.random()*100)).nextInt(prev.bestSnakes.size())), mutationRate);
    		snakes.add(s);
    		fields.add(new GameField(s));
    	}
		for(int i=snakes_number;i<Field.MAX_SNAKE_NUM;i++)
    	{
    		fields.add(new GameField());
    	}
	}
	
	
	public Round()
	{
    	for(int i=0;i<snakes_number;i++)
    	{
    		AISnake s = new AISnake(12,3,3);
    		snakes.add(s);
    		fields.add(new GameField(s));
    	}
	}
	
	public Round update()
	{
		for(GameField f : fields) f.update();
		currentTurn++;
		
		if (currentTurn>turnLimit)
		{
			ended = true;
			for(int i=0;i<number_of_parents;i++)
			{
				Snake bestSnake = snakes.get(0);
				for(Snake snake : snakes)
				{	//fitness func
					if (snake.getLength()>bestSnake.getLength() || (snake.getLength()>5 && snake.getLength()==bestSnake.getLength() && snake.turns>bestSnake.turns)) bestSnake = snake;
				}
				
				snakes.remove(bestSnake);
				bestSnakes.add(bestSnake);
			}
			
			((AISnake) bestSnakes.get(new Random().nextInt(bestSnakes.size()))).saveChanges();
			
			for(Snake snake : snakes)
			{
				for(GameField f : fields)
				{
					f.containsSnake(snake);
				}
			}
		}
		return this;
	}
	
	
	
}
