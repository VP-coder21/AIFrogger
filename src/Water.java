import java.awt.Graphics;
import java.util.ArrayList;

public class Water implements Constants{
	public ArrayList<Lilypads> turts;
	public ArrayList<Logs> stream;
	public int row;
	public boolean isTurt;
	
	public Water(int r) {
		row = r;
		turts = new ArrayList<Lilypads>();
		stream = new ArrayList<Logs>();
		if(row == 0 || row == 3) {
			isTurt = true;
			turts.add(new Lilypads(row));
		}
		else {
			isTurt = false;
			stream.add(new Logs(row));
		}
	}
	
	
	public void update(){
		if(isTurt){
			for(int n = turts.size() - 1; n >= 0; n--) {
				turts.get(n).drift();
				if(turts.get(n).place.x < -turts.get(n).place.width) {
					if(turts.size() < 2) {
						turts.add(new Lilypads(row));
					}
					turts.remove(0);
				}
			}
			if(Math.random() <= .02 && turts.get(turts.size()-1).place.x<WIDE-3*FROGRAD-turts.get(0).place.width) {
				turts.add(new Lilypads(row));
			}
		}
		else {
			for(int n = stream.size() - 1; n >= 0; n--) {
				stream.get(n).swim();
				if(stream.get(n).place.x < -stream.get(n).place.width) {
					if(stream.size() < 2) {
						stream.add(new Logs(row));
					}
					stream.remove(0);
				}
			}
			if(Math.random() <= .1 && stream.get(stream.size()-1).place.x>3*FROGRAD+stream.get(0).place.width) {
				stream.add(new Logs(row));
			}

		}
	}

	public void draw(Graphics g) {
		g.setColor(WaterColor);
		if(isTurt) {
			g.drawRect(0, turts.get(0).place.y, WIDE, HIGH/TILEY);
			g.fillRect(0, turts.get(0).place.y, WIDE, HIGH/TILEY);
		}
		else {
			g.drawRect(0, stream.get(0).place.y, WIDE, HIGH/TILEY);
			g.fillRect(0, stream.get(0).place.y, WIDE, HIGH/TILEY);
		}
	}
}
