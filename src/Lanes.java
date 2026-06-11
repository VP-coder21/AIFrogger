import java.awt.Graphics;
import java.util.ArrayList;

public class Lanes implements Constants{
	
	public ArrayList<Cars> lane;
	public int row;
	
	public Lanes(int r) {
		row = r;
		lane = new ArrayList<Cars>();
		lane.add(new Cars(row));
	}

	public void update(){
		for(int n = lane.size() - 1; n >= 0; n--) {
			lane.get(n).drive();
			if(lane.get(n).place.x < -lane.get(n).place.width|| lane.get(n).place.x > WIDE+lane.get(n).place.width) {
				if(lane.size() < 2) {
					lane.add(new Cars(row));
				}
				lane.remove(0);
			}
		}
		if(row == 4) {
			if(Math.random() <= .01 && lane.get(lane.size()-1).place.x<WIDE-3*FROGRAD-TRUCKWIDTH) {
				lane.add(new Cars(row));
			}
		}
		else if(row%2 == 1) {
			if(Math.random() <= .01 && lane.get(lane.size()-1).place.x>3*FROGRAD + CARWIDTH) {
				lane.add(new Cars(row));
			}
		}
		else {
			if(Math.random() <= .01 && lane.get(lane.size()-1).place.x<WIDE-3*FROGRAD-CARWIDTH) {
				lane.add(new Cars(row));
			}
		}
	}

	public void draw(Graphics g) {
		g.setColor(RoadColor);
		g.drawRect(0, lane.get(0).place.y, WIDE, HIGH/TILEY);
		g.fillRect(0, lane.get(0).place.y, WIDE, HIGH/TILEY);
	}
}
