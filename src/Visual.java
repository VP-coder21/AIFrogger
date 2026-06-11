
import java.awt.Color; 
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.awt.event.KeyEvent; 
import java.awt.event.KeyListener; 
import java.util.ArrayList;
import javax.swing.JFrame; 
import javax.swing.JPanel; 
import javax.swing.Timer;
import java.io.IOException;
    
 
 
public class Visual implements ActionListener, KeyListener , Constants {
 
    private JFrame frame;       //REQUIRED! The outside shell of the window
    public DrawingPanel panel;  //REQUIRED! The interior window
    private Timer visualtime;   //REQUIRED! Runs/Refreshes the screen. 
    
    
    //Adjust these values:
    
    
    //All other public data members go here:
    public boolean loading;
    public boolean saving;
    public ArrayList<Frog> jumpers;
    public Lanes[] road;
    public Water[] wet;
    public End[] safe;
    private double checkJump1;
    private double checkJump2;
    public int gametime;
    private int gen;
    public Visual()
    {
        //Adjust the name, but leave everything else alone.
        frame = new JFrame("Frogger");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new DrawingPanel();
        panel.setPreferredSize(new Dimension(WIDE, HIGH));
        frame.getContentPane().add(panel);
        panel.setFocusable(true);
        panel.requestFocus();
        panel.addKeyListener(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); 
        
        Initialize();
        
        //This block of code is fairly constant too -- always have it.
        visualtime = new Timer(20, this);     
        visualtime.start();
    } 
 
    public void Initialize()
    {
        //Initialize all data members here...
        jumpers = new ArrayList<Frog>();
        for(int n = 0; n < NOF; n++)
        	jumpers.add(new Frog());
        checkJump1 = 0;
        checkJump2 = 0;
        gen = 0;
        gametime = INGAMETIME;
        road = new Lanes[NUMROWC];
        wet = new Water[NUMROWW];
        safe = new End[NUMENDS];
        int randF = (int)(Math.random()*NUMENDS);
        for(int n  = 0; n < safe.length; n++) {
        	if(randF == n) {
        		safe[n] = new End(n,true);
        	}
        	else {
        		safe[n] = new End(n,false);
        	}
        }
        for(int n = 0; n < road.length; n++) {
        	road[n] = new Lanes(n);
        }
        for(int n = 0; n < wet.length; n++) {
        	wet[n] = new Water(n);
        }
    }
    public void actionPerformed(ActionEvent e)
    {    
    	gametime--;
    	//Once the new Visual() is launched, this method runs an infinite loop
    	//updaters
    	checkJump1+=.1;
    	for(int n = 0; n < road.length; n++) {
    		road[n].update();
    	}
    	for(int n = 0; n < wet.length; n++) {
    		wet[n].update();
    	}
    	int newFly = -1;
    	for(int n = 0; n < safe.length; n++) {
    		if(safe[n].hasFly) {
    			newFly = safe[n].updateFly(n);
    		}
    		if(newFly != -1) {
    			safe[newFly].hasFly = true;
    		}
    	}
    	for(int n = 0; n < NOF; n++) {
    		if(jumpers.get(n).alive) {
    			jumpers.get(n).whereMove(road, wet,safe);
    		}
    	}

    	//checkers
    	for(int n = 0; n < NOF; n++) {
    		if(jumpers.get(n).alive) {
    			if(jumpers.get(n).row >0 && jumpers.get(n).row < NUMROWC+1) {
    				for(int c = 0; c < road[jumpers.get(n).row-1].lane.size(); c++) {
    					jumpers.get(n).checkAliveCar(road[jumpers.get(n).row-1].lane.get(c).place);
    				}
    			}

    			else if(jumpers.get(n).row >NUMROWC+1 && jumpers.get(n).row < NUMROWC+NUMROWW+2) {
    				boolean alive = false;
    				//    			System.out.println(jumper.row);
    				if(wet[jumpers.get(n).row -NUMROWC-2].isTurt) {
    					for(int t = 0; t < wet[jumpers.get(n).row -NUMROWC-2].turts.size(); t++) {
    						if(jumpers.get(n).checkAliveTurt(wet[jumpers.get(n).row -NUMROWC-2].turts.get(t).place, wet[jumpers.get(n).row -NUMROWC-2].turts.get(t).isSunk)) {
    							alive = true;
    						}
    					}
    					if(!alive) {
    						jumpers.get(n).alive=false;
    					}
    					else {
    						jumpers.get(n).moveWithTurt(wet[jumpers.get(n).row -NUMROWC-2].turts.get(0).speed);
    					}
    				}


    				//Write Death Code
    				else {
    					for(int t = 0; t < wet[jumpers.get(n).row -NUMROWC-2].stream.size(); t++) {
    						if(jumpers.get(n).checkAliveLog(wet[jumpers.get(n).row -NUMROWC-2].stream.get(t).place, wet[jumpers.get(n).row -NUMROWC-2].stream.get(t).isDanger)) {
    							alive = true;
    						}
    					}
    					if(!alive) {
    						jumpers.get(n).alive=false;
    					}
    					else {
    						jumpers.get(n).moveWithLog(wet[jumpers.get(n).row -NUMROWC-2].stream.get(0).speed);
    					}
    				}


    			}
    			else if (jumpers.get(n).row == NUMROWC+NUMROWW+2) {
    				for(int s = 0; s < safe.length; s++) {
    					jumpers.get(n).checkWin(safe[s].place, safe[s].hasFly, gametime);
    				}
    				jumpers.get(n).alive = false;
    			}
    			jumpers.get(n).checkAlive();
    			if(!jumpers.get(n).alive) {
    				jumpers.get(n).score+=jumpers.get(n).greatestRow*100;
        			if(gametime/40 > 24) {
        				jumpers.get(n).score -= 150;
        			}
    			}
    		}
    	}
    	if(gametime <= 0) {
    		timeOut(false);
    	}
    	//Next Gen
    	boolean done = true;
    	for(int n = 0; n < NOF; n++)
    	{
    		if(jumpers.get(n).alive) 
    		{
    			done = false;
    		}
    	}

    	if(done) try { nextGeneration(); }catch(IOException e1) {}
    	panel.repaint();
    }

    public void timeOut(boolean manualy) {
    	for(int n = 0; n < NOF; n++) {
    		if(jumpers.get(n).alive = true) {
    			jumpers.get(n).alive=false;
    			if(!manualy) {
    				jumpers.get(n).score -= (INGAMETIME - gametime)/BASICALLYSECOND * 10;
    			}
    			else{
    				jumpers.get(n).score -= (INGAMETIME)/BASICALLYSECOND * 10;
    			}
    		}
    	}
    }

    public void nextGeneration() throws IOException
    {

    	boolean sorted = false;
    	while(!sorted)
    	{
    		sorted = true;
    		for(int n = 0; n < NOF - 1; n++)
    		{
    			if(jumpers.get(n).score < jumpers.get(n+1).score)
    			{
    				sorted = false;
    				Frog temp = jumpers.remove(n);
    				jumpers.add(n+1, temp);
    			}
    		}
    	}
    	System.out.println(jumpers);
    	if(saving) jumpers.get(0).SaveToFile();
    	for(int n = NOF - 1; n >= 10; n--) {
    		if(jumpers.get(n).score < 1) 
    			jumpers.remove(n);
    	}
    	
    	ArrayList<Frog> tempjumpers = new ArrayList<Frog>();
    	
    	for(int n = 0; n < 10; n++)
    		tempjumpers.add(Frog.CopyOfBird(jumpers.get(n)));
    	
    	for(int n = 10; n < .9*NOF; n++)
    	{
    		int i = (int)(Math.random()*jumpers.size());
    		int j = (int)(Math.random()*jumpers.size());
    		tempjumpers.add(Frog.MateThese(jumpers.get(i), jumpers.get(j)));
    	}
    		
    	while(tempjumpers.size() < NOF)
    		tempjumpers.add(new Frog());
    	
    	jumpers = tempjumpers;
    	for(int n = 0; n < NOF; n++)
    		if(Math.random() < .25) jumpers.get(n).mutate();
    	
    	road = new Lanes[NUMROWC];
        wet = new Water[NUMROWW];
        safe = new End[NUMENDS];
        int randF = (int)(Math.random()*NUMENDS);
        for(int n  = 0; n < safe.length; n++) {
        	if(randF == n) {
        		safe[n] = new End(n,true);
        	}
        	else {
        		safe[n] = new End(n,false);
        	}
        }
        for(int n = 0; n < road.length; n++) {
        	road[n] = new Lanes(n);
        }
        for(int n = 0; n < wet.length; n++) {
        	wet[n] = new Water(n);
        }
    	gen++;
    	gametime = INGAMETIME;
    	
    	if(loading) {
    		jumpers.remove(jumpers.size()-1);
    		jumpers.add(Frog.LoadFromFile());
    	}
    	saving = false;
    	loading = false;
    }
    
    public void keyPressed(KeyEvent e)
    {      
    	if(e.getKeyCode() == KeyEvent.VK_HOME)
    		Initialize();

    	if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
    		System.exit(0); 

    	if(e.getKeyCode() == KeyEvent.VK_SPACE) {
    		timeOut(true);
    	}
    	if(e.getKeyCode() == KeyEvent.VK_L)
    		loading = true;
    	if(e.getKeyCode() == KeyEvent.VK_S)
    		saving = true;
    	//    	if(e.getKeyCode() == KeyEvent.VK_UP) {
    	//    		if(checkJump1 - TIMEWAIT >= checkJump2) {
//    			jumper.goUp();
//    			checkJump2 = checkJump1;
//    		}
//
//
//    	}
//    	if(e.getKeyCode() == KeyEvent.VK_DOWN) {
//    		
//    		if(checkJump1 - TIMEWAIT >= checkJump2) {
//    			jumper.goDown();
//    			checkJump2 = checkJump1;
//    		}
//    	}
//    	if(e.getKeyCode() == KeyEvent.VK_LEFT) {
//    		
//    		if(checkJump1 - TIMEWAIT >= checkJump2) {
//    			jumper.goLeft();
//    			checkJump2 = checkJump1;
//    		}
//    	}
//    	if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
//    		if(checkJump1 - TIMEWAIT >= checkJump2) {
//    			jumper.goRight();
//    			checkJump2 = checkJump1;
//    		}
//    	}
    }

    public void keyTyped(KeyEvent e) {  }   //not used
    public void keyReleased(KeyEvent e) {  }//not used

//BIG NOTE:  The coordinate system for the output screen is as follows:     
//  (x,y) = (0, 0) is the TOP LEFT corner of the output screen;    
//  (x,y) = (WIDE, 0) is the TOP RIGHT corner of the output screen;     
//  (x,y) = (0, HIGH) is the BOTTOM LEFT corner of the screen;   
//  (x,y) = (WIDE, HIGH) is the BOTTOM RIGHT corner of the screen;


//REMEMBER::
// Strings are referenced from their BOTTOM LEFT corner.
// Virtually all other objects (Rectangles, Ovals, Images...) 
//    are referenced from their TOP LEFT corner.


    private class DrawingPanel extends JPanel { 
    	public void paintComponent(Graphics g)         
    	{
    		super.paintComponent(g);
    		panel.setBackground(Color.GREEN);
    		for(int l = 0; l < road.length; l++) {
    			road[l].draw(g);
    			for(int c = 0; c < road[l].lane.size(); c++) {
    				road[l].lane.get(c).draw(g);
    			}
    		}
    		for(int w = 0; w < wet.length; w++) {
    			wet[w].draw(g);
    			if(wet[w].isTurt) {
    				for(int t = 0; t < wet[w].turts.size();t++) {
    					wet[w].turts.get(t).draw(g);
    				}
    			}
    			else {
    				for(int l = 0; l < wet[w].stream.size();l++) {
    					wet[w].stream.get(l).draw(g);
    				}
    			}
    			
    		}
    		for(int e = 0; e < safe.length; e++) {
    			safe[e].draw(g);
    		}
    		for(int n = 0; n < NOF; n++) {
    			if(jumpers.get(n).alive) {
    				jumpers.get(n).draw(g);
    			}
    		}
    		g.setFont(MediumFont);
    		g.setColor(FontColor);
    		g.drawString("Gen: "+gen, WIDE/4 - 50, 70);
    		g.drawString("Time: "+gametime/40, WIDE/3 + 55, 70);
    		//this is where you draw items on the screen.            
    	}
    }
}


