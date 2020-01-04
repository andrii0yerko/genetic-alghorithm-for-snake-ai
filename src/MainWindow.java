import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	static MainWindow mw;
	
	Timer timer;
	boolean timerPaused=false;
	Round round;
	
	JPanel control;
	JLabel roundInfo;
	JSlider snakes_num;
	JSlider parents_num;
	JSlider timer_speed;
	JSlider turns_limit;
	JSlider mutation_rate;
	JButton pause;
	JButton loadSaves;
	JTextField saves;
	
	
	public void actionPerformed(ActionEvent e) 
	{
		
		if(round.Ended()) 
    	{
			if(Field.vis)
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
    		for(GameField f : round.fields) remove(f);
    		Round nextround = new Round(round,snakes_num.getValue(),parents_num.getValue());
    		revalidate();
            repaint();
    		round = nextround;
    		if (Field.vis) for(GameField f : round.fields) add(f);
    		revalidate();
            repaint();
    	}
		else {
    	roundInfo.setText(round.Info());
    	round.update();
		}
    	
    	
    }
	
	public MainWindow(){
        setTitle("Snake");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(5*Field.WIDTH*Field.DOT_SIZE+16,3*Field.HEIGHT*Field.DOT_SIZE+39);
        //setLocation(10,10);
        setLayout(new GridLayout(3,5));
        
        round = new Round();
        
        ChangeListener listener = new sliderMove();
        
        JTabbedPane settings = new JTabbedPane();
        add(settings);
        
        GridBagConstraints c1 =  new GridBagConstraints();
        c1.anchor = GridBagConstraints.CENTER; 
        c1.fill   = GridBagConstraints.NONE;  
        c1.gridheight = 1;
        c1.gridwidth  = 1; 
        c1.gridx = 1; 
        c1.gridy = GridBagConstraints.RELATIVE; 
        c1.insets = new Insets(20, 0, 0, 0);
        c1.ipadx = 0;
        c1.ipady = 0;
        c1.weightx = 0.0;
        c1.weighty = 0.0;
        
        GridBagConstraints c2 =  new GridBagConstraints();
        c2.anchor = GridBagConstraints.NORTHEAST; 
        c2.fill   = GridBagConstraints.HORIZONTAL;  
        c2.gridheight = 1;
        c2.gridwidth  = 2; 
        c2.gridx = 2; 
        c2.gridy = GridBagConstraints.RELATIVE; 
        c2.insets = new Insets(20, 5, 0, 0);
        c2.ipadx = 0;
        c2.ipady = 0;
        c2.weightx = 0.0;
        c2.weighty = 0.0;
        
        
        //String saves_string[] = {Field.filename};
        JPanel menu = new JPanel(new GridBagLayout());
        menu.setBackground(Color.white);
        JButton loadSaves = new JButton("Load save:");
        loadSaves.addActionListener(new loadButton());
        pause = new JButton("Pause");
        pause.addActionListener(new pauseButton());
        saves = new JTextField(Field.filename);
        saves.setEditable(true);
        menu.add(loadSaves,c1);
        menu.add(saves,c2);
        menu.add(pause,c1);
        settings.add(menu,"Menu");
        
        
        snakes_num = new JSlider(JSlider.HORIZONTAL,1,Field.MAX_SNAKE_NUM,14);
        snakes_num.setMajorTickSpacing(2);
        snakes_num.setMinorTickSpacing(1);
        snakes_num.setPaintTicks(true);
        snakes_num.setPaintLabels(true);
        snakes_num.setSnapToTicks(true);
        snakes_num.addChangeListener(listener);
        
        parents_num = new JSlider(JSlider.HORIZONTAL,1,9,3);
        parents_num.setMajorTickSpacing(2);
        parents_num.setMinorTickSpacing(1);
        parents_num.setPaintTicks(true);
        parents_num.setPaintLabels(true);
        parents_num.setSnapToTicks(true);
        
        mutation_rate = new JSlider(JSlider.HORIZONTAL,0,100,5);
        mutation_rate.setMajorTickSpacing(50);
        mutation_rate.setMinorTickSpacing(5);
        mutation_rate.setPaintTicks(true);
        mutation_rate.setPaintLabels(true);
        mutation_rate.setSnapToTicks(true);
        mutation_rate.addChangeListener(listener);
        
        control = new JPanel();
        control.setBackground(Color.white);
        control.setLayout(new GridBagLayout());
        
        control.add(new JLabel("Number of snakes"),c1);
        control.add(snakes_num,c2);
        control.add(new JLabel("Number of parents"),c1);
        control.add(parents_num,c2);
        control.add(new JLabel("Mutation rate"),c1);
        control.add(mutation_rate,c2);
        
        
        turns_limit = new JSlider(JSlider.HORIZONTAL,50,800,450);
        turns_limit.setMajorTickSpacing(150);
        turns_limit.setMinorTickSpacing(50);
        turns_limit.setPaintTicks(true);
        turns_limit.setPaintLabels(true);
        turns_limit.setSnapToTicks(true);
        turns_limit.addChangeListener(listener);
        
        timer_speed = new JSlider(JSlider.HORIZONTAL,0,200,0);
        timer_speed.setMajorTickSpacing(50);
        timer_speed.setMinorTickSpacing(5);
        timer_speed.setPaintTicks(true);
        timer_speed.setPaintLabels(true);
        timer_speed.setSnapToTicks(true);
        timer_speed.addChangeListener(listener);
   
        roundInfo = new JLabel(round.Info());
        
        JButton vis = new JButton("Visible");
        vis.addActionListener(new visButton());
        
        JPanel instanceSet = new JPanel();
        instanceSet.setLayout(new GridBagLayout());
        instanceSet.setSize(Field.WIDTH*Field.DOT_SIZE,Field.HEIGHT*Field.DOT_SIZE);
        instanceSet.setBackground(Color.white);
        instanceSet.add(vis,c1);
        instanceSet.add(roundInfo,c2);
        instanceSet.add(new JLabel("Timer speed"),c1);
        instanceSet.add(timer_speed,c2);
        instanceSet.add(new JLabel("Turns limit"),c1);
        instanceSet.add(turns_limit,c2);
        settings.add(instanceSet,"Global");
        
    
        
        
        settings.add(control,"Round");
        
        
        JPanel about = new JPanel(new GridBagLayout());
        about.setBackground(Color.white);
        about.add( new JLabel("Made by Andrii Yerko"),c1);
        about.add(new JLabel(" IASA 2019"),c1);
        settings.add(about,"About");
        
        if (Field.vis) for(GameField f : round.fields) add(f);
        setVisible(true);
        
        timer = new Timer(0,this);
    	timer.start();
    	
    }

    public static void main(String[] args) {
       mw = new MainWindow();
    }
    
    class visButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Field.vis = !Field.vis;
        }
    }
    
    
    class pauseButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	if(timerPaused) {
        		timer.start();
        		pause.setText("Pause");
        	}
        	else {
        		timer.stop();
        		pause.setText("Unause");
        	}
        	timerPaused = !timerPaused;
        }
    }
    
    class loadButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	Field.filename = saves.getText();
        	timer.stop();
        	round = null;
        	mw.dispose();
        	mw = new MainWindow();
        	
        }
    }
    
    class sliderMove implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent arg0) {
			parents_num.setMaximum(snakes_num.getValue()/2+1);
        	timer.setDelay(timer_speed.getValue());
        	round.setTurnLimit(turns_limit.getValue()).setMutationRate(mutation_rate.getValue()/100);
			
		}
    }
}
