import java.awt.Graphics;
import java.awt.Rectangle;

public class Cars implements Constants {
	
	public Rectangle place;
	public int row;
	public int speed;
	public int type;
	public Cars(int r) {
		row = r;
		if(row == 4) {
			place = new Rectangle(WIDE, HIGH -  2*HIGH/TILEY - row*(HIGH/TILEY), TRUCKWIDTH,CARHEIGHT);
			type = 2;
		}
		else if(row%2 == 0) {
			place = new Rectangle(WIDE, HIGH -  2*HIGH/TILEY - row*(HIGH/TILEY), CARWIDTH,CARHEIGHT);
			type = 1;
		}
		else {
			place = new Rectangle(-CARWIDTH,  HIGH - 2*HIGH/TILEY - row*(HIGH/TILEY), CARWIDTH,CARHEIGHT);
			type = 0;
		}
		if(row == 0) {
			speed = CARSPEED+1;
		}
		else if(row == 1) {
			speed = CARSPEED-1;
		}
		else if(row == 2) {
			speed = CARSPEED+2;
		}
		else if(row == 3) {
			speed = CARSPEED+1;
		}
		else {
			speed = TRUCKSPEED;
		}
		
	}
	
	public void drive() {
		if(row%2 == 0) {
			place.x-=speed;
		}
		else{
			place.x+=speed;
		}
	}
	
	
	
	public void draw(Graphics g) {
//		g.setColor(CarColor);
		if(type == 0) {
			g.drawImage(CAR1.getImage(),place.x, place.y + MARGIN, place.width, place.height,null);
		}
		else if(type == 1) {
			g.drawImage(CAR2.getImage(),place.x, place.y + MARGIN, place.width, place.height,null);
		}
		else{
			g.drawImage(BANANATRUCK.getImage(),place.x, place.y + MARGIN, place.width, place.height,null);
		}
//		g.fillRect(place.x, place.y + MARGIN, place.width, place.height);
	}

}
