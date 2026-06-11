import java.awt.Rectangle;
import java.awt.Graphics;

public class End implements Constants{
	public int number; 
	public Rectangle place;
	public boolean isDanger;
	public boolean hasFly;
	public double rand;
	public End(int n, boolean f) {
		number = n;
		place = new Rectangle((number+1)*SPACER + (number)*ENDWIDTH/2,BUFFER,ENDWIDTH,ENDHEIGHT);//fix later

		isDanger = false;
		if(f) {
			hasFly = true;
		}
		else {
			hasFly = false;
		}
		rand = Math.random();
	}

	public int updateFly(int n){
		if(Math.random()<=FLYAWAY) {
			hasFly = false;
			int newFly = n;
			rand = Math.random();
			while(newFly == n) {
				newFly = (int)(Math.random()*NUMENDS);
			}
			return newFly;
		}
		else return -1;
	}
	
	public void draw(Graphics g) {

		g.setColor(WaterColor);
		g.drawRect(place.x, place.y, place.width, place.height);
		g.fillRect(place.x, place.y, place.width, place.height);
		if(hasFly) {
			double target = (1.0/3);
			if(rand < target) {
				g.drawImage(PURPLEFLY.getImage(),place.x + ENDWIDTH/2 - FLYRAD,place.y + ENDHEIGHT/2 - FLYRAD,FLYRAD*2,FLYRAD*2,null);
			}
			else if(rand >= target && rand < 2*target) {
				g.drawImage(REDFLY.getImage(),place.x + ENDWIDTH/2 - FLYRAD,place.y + ENDHEIGHT/2 - FLYRAD,FLYRAD*2,FLYRAD*2,null);
			}
			else {
				g.drawImage(BLUEFLY.getImage(),place.x + ENDWIDTH/2 - FLYRAD,place.y + ENDHEIGHT/2 - FLYRAD,FLYRAD*2,FLYRAD*2,null);
			}
		}
	}
}
