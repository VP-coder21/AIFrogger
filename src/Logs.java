import java.awt.Graphics;
import java.awt.Rectangle;

public class Logs implements Constants {
	public Rectangle place;
	public int row;
	public boolean isDanger;
	public int speed;
	public int type;
	
	public Logs(int r) {
		row = r;
		if(Math.random() < .05) {
			isDanger = true;
		}
		else {
			isDanger = false;
		}
//		System.out.println(row);
		if(row == 1) {
			place = new Rectangle(-THREELOGWIDTH + (int)(Math.random()*100), HIGH -  9*HIGH/TILEY,THREELOGWIDTH,LOGHEIGHT);
			speed = SMALLLOGSPEED;
			type = 3;
		}
		
		else if(row == 2) {
			place = new Rectangle(-SIXLOGWIDTH+(int)(Math.random()*100), HIGH -  10*HIGH/TILEY,SIXLOGWIDTH,LOGHEIGHT);
			speed = LARGELOGSPEED;
			type = 5;
		}
		else{
			place = new Rectangle(-FOURLOGWIDTH+(int)(Math.random()*100), HIGH -  12*HIGH/TILEY,FOURLOGWIDTH,LOGHEIGHT);
			speed = MEDIUMLOGSPEED;
			type = 4;
		}

	}

	public void swim() {
		if(row == 2)
			place.x+=speed;
		else if(row == 1) {
			place.x+=speed;
		}
		else
			place.x+=speed;
	}

	public void draw(Graphics g) {
		if(type == 3) {
			if(isDanger) {
				g.setColor(CrocColor);
				g.drawRect(place.x, place.y + MARGIN, place.width, place.height);
				g.fillRect(place.x, place.y + MARGIN, place.width, place.height);
			}
			else
				g.drawImage(THREELOG.getImage(),place.x, place.y + MARGIN, place.width, place.height,null);
		}
		if(type == 4) {
			if(isDanger) {
				g.setColor(CrocColor);
				g.drawRect(place.x, place.y + MARGIN, place.width, place.height);
				g.fillRect(place.x, place.y + MARGIN, place.width, place.height);
			}
			else
				g.drawImage(FOURLOG.getImage(),place.x, place.y + MARGIN, place.width, place.height,null);
		}
		if(type == 5) {
			if(isDanger) {
				g.setColor(CrocColor);
				g.drawRect(place.x, place.y + MARGIN, place.width, place.height);
				g.fillRect(place.x, place.y + MARGIN, place.width, place.height);
			}
			else
				g.drawImage(FIVELOG.getImage(),place.x, place.y + MARGIN, place.width, place.height,null);
		}
	}
}
