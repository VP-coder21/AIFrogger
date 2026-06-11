import java.awt.Color; 
import java.awt.Font;
import javax.swing.ImageIcon;

public interface Constants {
	//Timer
	public static final double TIMEWAIT = .3;
	public static final int BASICALLYSECOND = 40;
	public static final int INGAMETIME = 25*BASICALLYSECOND;
	
	//Screen
	public static final int WIDE = 1500;
    public static final int HIGH = 988;
    public static final int MARGIN = 5;
    
    public static final int TILEX = 15;
    public static final int TILEY = 13;
    
    public static final Color FontColor = new Color(53,59,225);
    public static final Font MediumFont = new Font("Courier", Font.BOLD, 30);
    
    //Neural Network
    public static final double MUTATERATE = .1;
    public static final double MUTATEVALUE = .65;
    
    //Frog
    public static final Color FrogColor = new Color(75,136,80);
    public static final int FROGRAD = (HIGH/TILEY)/2;
    public static final int NOF = 100;
    
    //Water
    public static final Color WaterColor = new Color (42,0,208);
    public static final int NUMROWW = 5;
    	//Turtles
    public static final Color TurtColor = new Color(234,53,13);
    public static final int THREETURTWIDTH = 288;
    public static final int TWOTURTWIDTH = 152;
    public static final int TURTHEIGHT = HIGH/TILEY - MARGIN*2;
    public static final int TURTSPEED = 11;
    public static final double SINKCHANCE = .04;
    public static final int SINKHEIGHT = TURTHEIGHT-25;
    public static final int SUNKHEIGHT = TURTHEIGHT-45;
    	//Logs
    public static final Color LogColor = new Color(97, 86, 57);
    public static final Color CrocColor = new Color(138, 123, 85);
    public static final int FOURLOGWIDTH = 384;
    public static final int SIXLOGWIDTH = 480;
    public static final int THREELOGWIDTH = 288;
    public static final int LARGELOGSPEED = 13;
    public static final int SMALLLOGSPEED = 9;
    public static final int MEDIUMLOGSPEED = 10;
    public static final int LOGHEIGHT = HIGH/TILEY - MARGIN*2;
    
    //Cars
    public static final Color CarColor = new Color(186, 160, 25);
    public static final Color RoadColor = new Color(0, 0, 0);
    public static final int NUMROWC = 5;
    public static final int CARWIDTH = 85;
    public static final int TRUCKWIDTH = 175;
    public static final int CARSPEED = 10;
    public static final int TRUCKSPEED = 9;
    public static final int CARHEIGHT = HIGH/TILEY - MARGIN*2;
    

    
    //End
    //use WaterColor for end color\
    public static final int BUFFER = 20;
    public static final int NUMENDS = 5;
    public static final int ENDHEIGHT =  HIGH/TILEY - BUFFER;
    public static final int ENDWIDTH = WIDE/TILEX;
    public static final int SPACER = WIDE/NUMENDS-ENDWIDTH;
    
    	//Fly
    public static final Color FlyColor1 = new Color(29, 207, 242);
    public static final Color FlyColor2 = new Color(54, 242, 29);
    public static final Color FlyColor3 = new Color(247, 152, 49);
    public static final int FLYRAD = FROGRAD-3*MARGIN;
    public static final double FLYAWAY = .005;
    
    
  //Graphics
    public static final ImageIcon FROG = new ImageIcon("Frog.gif");
    public static final ImageIcon CAR1 = new ImageIcon("Car1.png");
    public static final ImageIcon CAR2 = new ImageIcon("Car2.png");
    public static final ImageIcon BANANATRUCK = new ImageIcon("SchoolBus.png");
    public static final ImageIcon THREETURTTOP = new ImageIcon("TurtleTop.png");
    public static final ImageIcon THREETURTMID = new ImageIcon("TurtleMiddle.png");
    public static final ImageIcon THREETURTBOT = new ImageIcon("TurtleBot.png");
    public static final ImageIcon THREELOG = new ImageIcon("ThreeLongLog.png");
    public static final ImageIcon FOURLOG = new ImageIcon("FourLongLog.png");
    public static final ImageIcon FIVELOG = new ImageIcon("FiveLongLog.png");
    public static final ImageIcon PURPLEFLY = new ImageIcon("FlyPurple.png");
    public static final ImageIcon BLUEFLY = new ImageIcon("FlyBluePurple.png");
    public static final ImageIcon REDFLY = new ImageIcon("FlyRedOrange.png"); 
}
