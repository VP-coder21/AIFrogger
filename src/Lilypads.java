import java.awt.Graphics;
import java.awt.Rectangle;

public class Lilypads implements Constants {
	public Rectangle place;
	public int row;
	public int sinkTime;
	public boolean sinking;
	public boolean isSunk;
	public boolean rising;
	public int speed;
	public int type;

	public Lilypads(int r) {
		row = r;
		if(row == 0) {
			place = new Rectangle(WIDE+THREETURTWIDTH, HIGH -  8*HIGH/TILEY,THREETURTWIDTH,TURTHEIGHT);
			type = 3;
		}
		else {
			place = new Rectangle(WIDE+TWOTURTWIDTH, HIGH -  11*HIGH/TILEY,TWOTURTWIDTH,TURTHEIGHT);
			type = 2;
		}
		sinkTime = (int)(Math.random()*7);
		sinking = false;
		rising = false;
		isSunk = false;
		speed = TURTSPEED;
	}

	public void drift() {
		if(Math.random() <= SINKCHANCE || sinking || rising) {
			if(rising) {
				sinkTime--;
				if(sinkTime == 0) {
					rising  = false;
				}
			}
			else if(sinkTime < 13 && sinkTime >= 6 && !rising) {
				sinking = true;
				sinkTime ++;
//				System.out.println("sinking");
			}
			else if(sinkTime == 20) {
				sinking = false;
				rising = true;
			}
			else
				sinkTime ++;
		}

		place.x-=speed;
	}



	public void draw(Graphics g) {
		g.setColor(TurtColor);
		if(sinkTime < 7) {
			if(type == 3) {
				g.drawImage(THREETURTTOP.getImage(),place.x, place.y + MARGIN, place.width, place.height,null);
			}
			else {
				g.drawRect(place.x, place.y + MARGIN, place.width, place.height);
				g.fillRect(place.x, place.y + MARGIN, place.width, place.height);
			}
		}
		else if(sinkTime < 13) {
			if(type == 3) {
				g.drawImage(THREETURTMID.getImage(),place.x, place.y + MARGIN, place.width, place.height,null);
			}
			else {
				g.drawRect(place.x, place.y + (place.height - SINKHEIGHT)/2 , place.width, SINKHEIGHT);
				g.fillRect(place.x, place.y + (place.height - SINKHEIGHT)/2 , place.width, SINKHEIGHT);
			}
			isSunk=false;
		}
		else{
			if(type == 3) {
				g.drawImage(THREETURTBOT.getImage(),place.x, place.y + MARGIN, place.width, place.height,null);
			}
			else {
				g.drawRect(place.x, place.y + (place.height - SUNKHEIGHT)/2 , place.width, SUNKHEIGHT);
				g.fillRect(place.x, place.y + (place.height - SUNKHEIGHT)/2, place.width, SUNKHEIGHT);
			}
			isSunk=true;
		}
	}
}
