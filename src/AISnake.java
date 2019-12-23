import java.awt.event.KeyEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class AISnake extends Snake{
	int food[][][]=new int[Field.SNAKE_VIS][Field.SNAKE_VIS][3];
	int wall[][][]=new int[Field.SNAKE_VIS][Field.SNAKE_VIS][3];
	
	public AISnake(AISnake prev) { this(prev,0.0);}	
	
	public AISnake(Snake prev, double mutation_rate)
	{
		super(12, 3, 3);
		if (!(prev instanceof AISnake)) return;
		AISnake aiprev = (AISnake) prev;
		Random rand = new Random();
		for(int i=0;i<Field.SNAKE_VIS;i++)
        {
        	for(int j=0;j<Field.SNAKE_VIS;j++)
        	{
        		for(int k=0;k<3;k++) 
        		{
        			wall[i][j][k] = aiprev.wall[i][j][k];
        			if (Math.random()<mutation_rate/2) wall[i][j][k]+=1;
        			if (Math.random()>1-mutation_rate/2) wall[i][j][k]-=1;
        			
        			food[i][j][k] = aiprev.food[i][j][k];
        			if (Math.random()<mutation_rate/2) food[i][j][k]+=1;
        			if (Math.random()>1-mutation_rate/2) food[i][j][k]-=1;
        		}
        			
        	}
        }
		
	}
	
	public AISnake(int start_x, int start_y, int len) {
		super(start_x, start_y, len);
		
		try {
	        FileReader fr = new FileReader("res/logic.txt");
	        Scanner scan = new Scanner(fr);
	            
	        for(int i=0;i<Field.SNAKE_VIS;i++)
	        {
	        	String line[] = scan.nextLine().split(" ");
	        	for(int j=0;j<Field.SNAKE_VIS;j++)
	        	{
	        		String vect[] = line[j].split(",");
	        		for(int k=0;k<3;k++) food[i][j][k] = Integer.parseInt(vect[k]);
	        	}
	        }
	        scan.nextLine();
	        
	        for(int i=0;i<Field.SNAKE_VIS;i++)
	        {
	        	String line[] = scan.nextLine().split(" ");
	        	for(int j=0;j<Field.SNAKE_VIS;j++)
	        	{
	        		String vect[] = line[j].split(",");
	        		for(int k=0;k<3;k++) wall[i][j][k] = Integer.parseInt(vect[k]);
	        	}
	        }
	        scan.close();
	        fr.close();
		}
		catch(Exception e){
			for (int i = 0; i <Field.SNAKE_VIS; i++) {
	            for (int j = 0; j < Field.SNAKE_VIS; j++) {
	            	for (int k = 0; k < 3; k++) 
	            	{
	            		food[i][j][k]=0;
	            		wall[i][j][k]=0;
	            	}
	            }
	        }
		}
    }
	
	public void saveChanges()
	{
		try {
	        FileWriter fw = new FileWriter("res/logic.txt");
	            
	        for(int i=0;i<Field.SNAKE_VIS;i++)
	        {
	        	for(int j=0;j<Field.SNAKE_VIS;j++)
	        	{
	        		fw.write(food[i][j][0]+","+food[i][j][1]+","+food[i][j][2]+" ");
	        	}
	        	fw.write("\n");
	        }
	        fw.write("\n");
	        for(int i=0;i<Field.SNAKE_VIS;i++)
	        {
	        	for(int j=0;j<Field.SNAKE_VIS;j++)
	        	{
	        		fw.write(wall[i][j][0]+","+wall[i][j][1]+","+wall[i][j][2]+" ");
	        	}
	        	fw.write("\n");
	        }
	        fw.close();
		}
		catch(Exception e){
			System.out.println("Writing error!");
		}
    }
	
	
	
	private Integer[] classify(char state[][])
	{
		/* вывод state
    	for (int i = 0; i <Field.SNAKE_VIS; i++) {
            for (int j = 0; j < Field.SNAKE_VIS; j++) {
            	if (state[i][j]==0) System.out.print(".");
            	else System.out.print(state[i][j]);
            }
            System.out.println();
        }*/
		
		int turnRight=0;
		int turnLeft=0;
		int forward=0;
		for (int i = 0; i < Field.SNAKE_VIS; i++)
		{
			for (int j = 0; j < Field.SNAKE_VIS; j++)
			{
				switch (state[i][j])
				{
					case 'a':
						turnRight+=food[i][j][2];
						turnLeft+=food[i][j][0];
						forward+=food[i][j][1];
						break;
						
					case 'w':
					case 's':
						turnRight+=wall[i][j][2];
						turnLeft+=wall[i][j][0];
						forward+=wall[i][j][1];
				}
			}
		}
		//System.out.println("fw: "+forward+"  l: "+turnLeft+"  r: "+turnRight);
		Integer[] array;
		if(turnRight>turnLeft && turnRight>forward) array = new Integer[] {1};
		else if(turnLeft>turnRight && turnLeft>forward) array = new Integer[] {-1};
		else if(forward>turnRight && forward>turnLeft) array = new Integer[] {0};
		else if(turnRight==forward && forward>turnLeft) array = new Integer[] {1,0};
		else if(turnLeft==forward && forward>turnRight) array = new Integer[] {-1,0};
		else if(turnRight==turnLeft && turnLeft>forward) array = new Integer[] {-1,1};
		else array = new Integer[] {-1,0,1};
		return array;
	}
	
	@Override
	public void react(char state[][])
    {
		super.react(state);
		Integer[] array = classify(state);
		direction = Direction.valueOf(direction.ordinal()+array[new Random().nextInt(array.length)]);
    }
	
	@Override
	public void react(char state[][], int key)
	{
		if (key != KeyEvent.VK_LEFT && key != KeyEvent.VK_RIGHT && key != KeyEvent.VK_UP && key != KeyEvent.VK_DOWN) return;
		int userTurn = ((key-37) - direction.ordinal());
		if (Math.abs(userTurn)%2==0) return;
		
		Integer[] array = classify(state);
		int k_c = 0, k_uc=0;
		if(Arrays.asList(array).contains(userTurn))
		{
			if (array.length==3) { k_c = 4; k_uc = -2;}
			else if (array.length==2) { k_c = 2; k_uc = -2;}
			else if (array.length==1) { k_c = 1;}; // все ок
		}
		else
		{
			if (array.length==2) { k_c = +4; k_uc = -2;}
			else if (array.length==1){ k_c = +2; k_uc = -2; }
		}
		//меняем веса
		for (int i = 0; i < Field.SNAKE_VIS; i++)
		{
			for (int j = 0; j < Field.SNAKE_VIS; j++)
			{
				switch (state[i][j])
				{
					case 'a':
						for(int k=-1;k<=1;k++)
							if(k==userTurn) food[i][j][k+1]+=k_c;
							else if(Arrays.asList(array).contains(k)) food[i][j][k+1]+=k_uc;
						break;
					case 'w': case 's':
						for(int k=-1;k<=1;k++)
							if(k==userTurn) wall[i][j][k+1]+=k_c/2;
							else if(Arrays.asList(array).contains(k)) wall[i][j][k+1]+=k_uc/2;
				}
			}
		}
		saveChanges();
		super.react(state,key);
	}
}
