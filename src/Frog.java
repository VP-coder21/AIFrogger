import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Frog implements Constants {
	public int score;
	public boolean alive;
	public int posx;
	public int posy;
	public int foodN;
	public int row;
	public int greatestRow;
	public NeuralNetwork brain;
	
	public Frog() {
		score = 0;
		alive = true;
		posx = WIDE/2;
		posy = HIGH - 3*(HIGH/TILEY)/2;
		row =1;
		greatestRow = 1;
		
		
//		inputs(13)  0: posy  1: posx  2: posx of enemy to left, right edge  3: posx of enemy to right, left edge
//		4: posx of enemy to left above, right edge  5: posx of enemy to right above, left edge
//		6: posx of enemy to left below, right edge  7: posx of enemy to right below, left edge
//		8: posx of enemy in front of you left side  9: posx of enemy in front of you right side  
//		10: posx of enemy in behind of you left side  11: posx of enemy in behind of you right side  
//		12: posx of fly
		
				
		//outputs(5):  0: no move  1: move left  2: move right  3: move down  4: move up  
				
				
		brain = new NeuralNetwork(13,11,7,5);
	}
	public String toString() {
		return "" + score;
	}
	public void checkAlive(){
		if(posy > HIGH || posx > WIDE || posx < 0) {
			alive = false;
			score -=300;
		}
	}
	public void checkAliveCar(Rectangle c){
		Rectangle temp = new Rectangle(posx- FROGRAD, posy - FROGRAD, FROGRAD*2,FROGRAD*2);
		if(temp.intersects(c)) {
			alive = false;
			score-=50*(1.0/greatestRow);
		}
	}
	public boolean checkAliveTurt(Rectangle t, boolean sunk){
		Rectangle temp = new Rectangle(posx- FROGRAD, posy - FROGRAD, FROGRAD*2,FROGRAD*2);
		if(temp.intersects(t) && !sunk) {
			return true;
		}
		score-=225*(1.0/greatestRow);
		return false;
	}
	
	public boolean checkAliveLog(Rectangle l, boolean notLog){
		Rectangle temp = new Rectangle(posx- FROGRAD, posy - FROGRAD, FROGRAD*2,FROGRAD*2);
		if(temp.intersects(l) && !notLog) {
			return true;
		}
		score-=45*(1.0/greatestRow);
		return false;
	}
	
	public void moveWithTurt(int s){
		posx-=s;
	}
	public void moveWithLog(int s){
		posx+=s;
	}
	public void checkWin(Rectangle s, boolean fly, int time){
		Rectangle temp = new Rectangle(posx- FROGRAD, posy - FROGRAD, FROGRAD*2,FROGRAD*2);
		if(temp.intersects(s) && fly) {
			score += 1580;
			score += time/BASICALLYSECOND * 10;
		}
		else if(temp.intersects(s)) {
			score+=1080;
			score += time/BASICALLYSECOND * 10;
		}
		else {
			score-=20;
		}
	}
	
	public void whereMove(Lanes[] road,  Water[] wet, End[] safe){
//		inputs(13)  0: posy  1: posx  2: posx of enemy to left, right edge  3: posx of enemy to right, left edge
//		4: posx of enemy to left above, right edge  5: posx of enemy to right above, left edge
//		6: posx of enemy to left below, right edge  7: posx of enemy to right below, left edge
//		8: posx of enemy in front of you left side  9: posx of enemy in front of you right side  
//		10: posx of enemy in behind of you left side  11: posx of enemy in behind of you right side  
//		12: posx of fly
		
				
		
		Rectangle ahead = new Rectangle(posx- FROGRAD, posy - FROGRAD + HIGH/TILEY, FROGRAD*2,FROGRAD*2);
		Rectangle behind = new Rectangle(posx- FROGRAD, posy - FROGRAD - HIGH/TILEY, FROGRAD*2,FROGRAD*2);
		double[] senses = new double[13];
		Rectangle[][] enemies = new Rectangle[13][20];
		for(int r = 0; r < enemies.length;r++) {
			for(int c = 0; c < enemies[0].length; c++) {
				enemies[r][c] = null;
			}
		}
		boolean foundRight;
		boolean foundLeft;
		int closestRight;
		int closestLeft;
		//0 and 1
		senses[0] = posx/WIDE;
		senses[1] = posy/HIGH;
		for(int n = 0; n < safe.length; n++) {
			if(safe[n].hasFly) {
				senses[12] = safe[n].place.x + safe[n].place.width/2;
			}
		}
		
		
		for(int position = row - 1; position <= row + 1; position++) {
			if(position < 0) {
				position++;
			}
			else if(position > 12) {
				position++;
			}
			else {
				if(position < 6 && position > 0) {
					for(int n = 0; n < road[position-1].lane.size(); n++) {
						enemies[position][n] = road[position - 1].lane.get(n).place;
					}
				}
				else if(position > 6 && position < 12) {
					if(position == 10 || position == 7) {
						for(int n = 0; n < wet[position-7].turts.size(); n++) {
							if(wet[position-7].turts.get(n).isSunk) {
								enemies[position][enemies.length - 1-n] = wet[position-7].turts.get(n).place;
							}
						}
						if(wet[position-7].turts.get(0).place.x > 0) {
							enemies[position][0] = new Rectangle(0,wet[position - 7].turts.get(0).place.y,wet[position - 7].turts.get(0).place.x,wet[position - 7].turts.get(0).place.height);
						}
						for(int n = 0; n < wet[position-7].turts.size() - 1; n++) {
							enemies[position][n] = new Rectangle(wet[position - 7].turts.get(n).place.x + wet[position - 7].turts.get(n).place.width,wet[position - 7].turts.get(n).place.y,wet[position - 7].turts.get(n+1).place.x - (wet[position - 7].turts.get(n).place.x + wet[position - 7].turts.get(n).place.width),wet[position - 7].turts.get(0).place.height);
						}
						if(wet[position-7].turts.get(wet[position-7].turts.size() - 1).place.x+wet[position-7].turts.get(wet[position-7].turts.size() - 1).place.width < WIDE)
							enemies[position][wet[position-7].turts.size()] = new Rectangle(wet[position - 7].turts.get(wet[position-7].turts.size() - 1).place.x + wet[position - 7].turts.get(wet[position-7].turts.size() - 1).place.width,wet[position - 7].turts.get(0).place.y,WIDE-(wet[position - 7].turts.get(wet[position-7].turts.size() - 1).place.x + wet[position - 7].turts.get(wet[position-7].turts.size() - 1).place.width),wet[position - 7].turts.get(0).place.height);
						
					}
					else {
						for(int n = 0; n < wet[position-7].stream.size(); n++) {
							if(wet[position-7].stream.get(n).isDanger) {
								enemies[position][enemies.length - 1-n] = wet[position-7].stream.get(n).place;
							}
						}
						if(wet[position-7].stream.get(0).place.x + wet[position-7].stream.get(0).place.width < WIDE) {
							enemies[position][0] = new Rectangle(wet[position-7].stream.get(0).place.x + wet[position-7].stream.get(0).place.width,wet[position - 7].stream.get(0).place.y,WIDE - (wet[position-7].stream.get(0).place.x + wet[position-7].stream.get(0).place.width),wet[position - 7].stream.get(0).place.height);
						}
						for(int n = 1; n < wet[position-7].stream.size(); n++) {
							enemies[position][n] = new Rectangle(wet[position - 7].stream.get(n).place.x + wet[position-7].stream.get(n).place.width,wet[position - 7].stream.get(n).place.y,wet[position - 7].stream.get(n-1).place.x - (wet[position - 7].turts.get(n).place.x + wet[position-7].stream.get(n).place.width),wet[position - 7].stream.get(0).place.height);
						}
						if(wet[position-7].stream.get(wet[position-7].stream.size()-1).place.x > 0)
							enemies[position][wet[position-7].stream.size()] = new Rectangle(0,wet[position - 7].stream.get(0).place.y,wet[position - 7].stream.get(wet[position-7].stream.size()-1).place.x,wet[position - 7].stream.get(0).place.height);
					}
				}
				else if(position == 12) {
					enemies[position][0] = new Rectangle(0,safe[0].place.y,safe[0].place.x,safe[0].place.height);
					for(int n = 1; n<safe.length-1;n++) {
						enemies[position][n] = new Rectangle(safe[n].place.x+safe[n].place.width, safe[n].place.y,safe[n+1].place.x-(safe[n].place.x+safe[n].place.width),safe[n].place.height);
					}
					enemies[position][safe.length] = new Rectangle(safe[safe.length-1].place.x+safe[safe.length-1].place.width, safe[safe.length-1].place.y,WIDE - (safe[safe.length-1].place.x+safe[safe.length-1].place.width),safe[safe.length-1].place.height);
				}
			}

		}
		//2,3
		foundRight = false;
		foundLeft = false;
		closestRight = 0;
		closestLeft = WIDE;
		for(int n = 0; n < enemies[row].length; n++) {
			if(enemies[row][n] != null) {
				if(enemies[row][n].x + enemies[row][n].width < posx) {
					foundLeft = true;
					if(closestLeft < enemies[row][n].x + enemies[row][n].width)
						closestLeft = enemies[row][n].x + enemies[row][n].width;
				}
				else if(enemies[row][n].x > posx) {
					foundRight = true;
					if(closestRight > enemies[row][n].x)
						closestRight = enemies[row][n].x;
				}
			}
		}
		if(foundLeft)
			senses[2] = closestLeft/WIDE;
		else
			senses[2] = 0;
		if(foundRight) {
			senses[3] = closestRight/WIDE;
		}
		else {
			senses[3] = 1;
		}

		//4,5
		foundRight = false;
		foundLeft = false;
		closestRight = 0;
		closestLeft = WIDE;
		for(int n = 0; n < enemies[row+1].length; n++) {
			if(enemies[row+1][n] != null) {
				if(enemies[row+1][n].x + enemies[row+1][n].width < posx) {
					foundLeft = true;
					if(closestLeft < enemies[row+1][n].x + enemies[row+1][n].width)
						closestLeft = enemies[row+1][n].x + enemies[row+1][n].width;
				}
				else if(enemies[row+1][n].x > posx) {
					foundRight = true;
					if(closestRight > enemies[row+1][n].x)
						closestRight = enemies[row+1][n].x;
				}
			}
		}
		if(foundLeft)
			senses[4] = closestLeft/WIDE;
		else
			senses[4] = 0;
		if(foundRight) {
			senses[5] = closestRight/WIDE;
		}
		else {
			senses[5] = 1;
		}
		
		//6,7
		if(row == 0) {
			senses[6] = posx/WIDE;
			senses[7] = posx/WIDE;
		}
		else {
			foundRight = false;
			foundLeft = false;
			closestRight = 0;
			closestLeft = WIDE;
			for(int n = 0; n < enemies[row-1].length; n++) {
				if(enemies[row-1][n] != null) {
					if(enemies[row-1][n].x + enemies[row-1][n].width < posx) {
						foundLeft = true;
						if(closestLeft < enemies[row-1][n].x + enemies[row-1][n].width)
							closestLeft = enemies[row-1][n].x + enemies[row-1][n].width;
					}
					else if(enemies[row-1][n].x > posx) {
						foundRight = true;
						if(closestRight > enemies[row-1][n].x)
							closestRight = enemies[row-1][n].x;
					}
				}
			}
			if(foundLeft)
				senses[6] = closestLeft/WIDE;
			else
				senses[6] = 0;
			if(foundRight) {
				senses[7] = closestRight/WIDE;
			}
			else {
				senses[7] = 1;
			}
		}	
		//8,9
		senses[8] = -1;
		senses[9] = -1;
		for(int n = 0; n < enemies[row+1].length; n++) {
			if(enemies[row+1][n] != null) {
				if(ahead.intersects(enemies[row+1][n])) {
					senses[8] = (enemies[row+1][n].x)/WIDE;
					senses[9] = (enemies[row+1][n].x + enemies[row+1][n].width)/WIDE;
				}
			}
		}

		//10,11
		if(row == 0) {
			senses[10] = posx/WIDE;
			senses[11] = posx/WIDE;
		}
		else {
			senses[10] = -1;
			senses[11] = -1;
			for(int n = 0; n < enemies[row-1].length; n++) {
				if(enemies[row-1][n] != null) {
					if(behind.intersects(enemies[row-1][n])) {
						senses[10] = (enemies[row-1][n].x)/WIDE;
						senses[11] = (enemies[row-1][n].x + enemies[row+1][n].width)/WIDE;
					}
				}
			}
		}
		//outputs(5):  0: no move  1: move left  2: move right  3: move down  4: move up  
		double[] choices = brain.think(senses);
		if(choices[1] > choices[0] && choices[1] > choices[2] && choices[1] > choices[3] && choices[1] > choices[4]) {
			goLeft();
		}
		else if(choices[2] > choices[0] && choices[2] > choices[1] && choices[2] > choices[3] && choices[2] > choices[4]) {
			goRight();
		}
		else if(choices[3] > choices[0] && choices[3] > choices[1] && choices[3] > choices[2] && choices[3] > choices[4]) {
			goDown();
		}
		else if(choices[4] > choices[0] && choices[4] > choices[1] && choices[4] > choices[2] && choices[4] > choices[3]) {
			goUp();
		}
	}

	public void goUp() {
		posy-=HIGH/TILEY;
		row++;
		if (row > greatestRow) {
			greatestRow = row;
		}
		
	}
	public void goDown() {
		posy+=HIGH/TILEY;
		row--;
	}
	public void goLeft() {
		posx-=WIDE/TILEX/2;
	}
	public void goRight() {
		posx+=WIDE/TILEX/2;
	}
	
	public void draw(Graphics g) {
//		g.setColor(FrogColor);
		g.drawImage(FROG.getImage(),posx - FROGRAD, posy - FROGRAD, FROGRAD*2, FROGRAD*2,null);
//		g.fillOval(posx - FROGRAD, posy - FROGRAD, FROGRAD*2, FROGRAD*2);
	}
	
	public static Frog CopyOfBird(Frog f) 
	{ 
		Frog temp = new Frog();
		temp.brain.copy(f.brain);
		return temp; 
	}
	
	public static Frog MateThese(Frog a, Frog b) 
	{ 
		Frog temp = new Frog();
		temp.brain.combine(a.brain, b.brain);
		return temp; 
	}
	public void mutate()
	{
		brain.Weights_I_H1.mutateElements();
		brain.Weights_H1_H2.mutateElements();
		brain.Weights_H2_O.mutateElements();
		brain.Bias_H1.mutateElements();
		brain.Bias_H2.mutateElements();
		brain.Bias_O.mutateElements();
	}
	public void SaveToFile() throws IOException
	{
		System.out.println("Saving a Bird to the SavedBrain.txtfile.");
		FileWriter outfile = new
				FileWriter("SavedBrain.txt");
		BufferedWriter fout = new
				BufferedWriter(outfile);
		fout.write(""+brain.numinputs);
		fout.newLine();
		fout.write(""+brain.numhidden1);
		fout.newLine();
		fout.write(""+brain.numhidden2);
		fout.newLine();
		fout.write(""+brain.numoutputs);
		fout.newLine();
		for(int r = 0; r < brain.Weights_I_H1.rows; r++)
			for(int c = 0; c < brain.Weights_I_H1.cols;
					c++)
			{
				fout.write(""+brain.Weights_I_H1.elements[r][c]);
				fout.newLine();
			}
		for(int r = 0; r < brain.Weights_H1_H2.rows; r++)
			for(int c = 0; c < brain.Weights_H1_H2.cols;
					c++)
			{
				fout.write(""+brain.Weights_H1_H2.elements[r][c]);
				fout.newLine();
			}
		for(int r = 0; r < brain.Weights_H2_O.rows; r++)
			for(int c = 0; c < brain.Weights_H2_O.cols;
					c++)
			{
				fout.write(""+brain.Weights_H2_O.elements[r][c]);
				fout.newLine();
			}
		for(int r = 0; r < brain.Bias_H1.rows; r++)
			for(int c = 0; c < brain.Bias_H1.cols; c++)
			{
				fout.write(""+brain.Bias_H1.elements[r][c]);
				fout.newLine();
			}
		for(int r = 0; r < brain.Bias_H2.rows; r++)
			for(int c = 0; c < brain.Bias_H2.cols; c++)
			{
				fout.write(""+brain.Bias_H2.elements[r][c]);
				fout.newLine();
			}
		for(int r = 0; r < brain.Bias_O.rows; r++)
			for(int c = 0; c < brain.Bias_O.cols; c++)
			{
				fout.write(""+brain.Bias_O.elements[r][c]);
				fout.newLine();
			}
		fout.close();
	}
	public static Frog LoadFromFile() throws
	FileNotFoundException
	{
		FileReader inputfile = new
				FileReader("SavedBrain.txt");
		Scanner fin = new Scanner(inputfile);
		Frog ret = new Frog();
		int i, h1, h2, o;
		i = fin.nextInt();
		h1 = fin.nextInt();
		h2 = fin.nextInt();
		o = fin.nextInt();
		if(i != ret.brain.numinputs || h1 !=
				ret.brain.numhidden1 || h2 != ret.brain.numhidden2 || o !=
				ret.brain.numoutputs)
		{
			System.out.println("ERROR IN LOADING -- THE SAVE BRAIN DOES NOT FIT!!!!");
			return ret;
		}
		for(int r = 0; r < ret.brain.Weights_I_H1.rows;
				r++)
			for(int c = 0; c <
					ret.brain.Weights_I_H1.cols; c++)
				ret.brain.Weights_I_H1.elements[r][c] = fin.nextDouble();
		for(int r = 0; r < ret.brain.Weights_H1_H2.rows;
				r++)
			for(int c = 0; c <
					ret.brain.Weights_H1_H2.cols; c++)
				ret.brain.Weights_H1_H2.elements[r][c] = fin.nextDouble();
		for(int r = 0; r < ret.brain.Weights_H2_O.rows;
				r++)
			for(int c = 0; c <
					ret.brain.Weights_H2_O.cols; c++)
				ret.brain.Weights_H2_O.elements[r][c] = fin.nextDouble();
		for(int r = 0; r < ret.brain.Bias_H1.rows; r++)
			for(int c = 0; c < ret.brain.Bias_H1.cols;
					c++)
				ret.brain.Bias_H1.elements[r][c] =
				fin.nextDouble();
		for(int r = 0; r < ret.brain.Bias_H2.rows; r++)
			for(int c = 0; c < ret.brain.Bias_H2.cols;
					c++)
				ret.brain.Bias_H2.elements[r][c] =
				fin.nextDouble();
		for(int r = 0; r < ret.brain.Bias_O.rows; r++)
			for(int c = 0; c < ret.brain.Bias_O.cols;
					c++)
				ret.brain.Bias_O.elements[r][c] =
				fin.nextDouble();
		System.out.println("Brain loaded from file.");
		fin.close();
		return ret;
	}
}
