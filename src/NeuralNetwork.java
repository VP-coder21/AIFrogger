
public class NeuralNetwork implements Constants{
	
	public int numinputs;
	public int numhidden1;
	public int numhidden2;
	public int numoutputs;
	
	public Matrix Weights_I_H1;
	public Matrix Weights_H1_H2;
	public Matrix Weights_H2_O;  //Closer to 1  == a stronger connection
	
	public Matrix Bias_H1;
	public Matrix Bias_H2;
	public Matrix Bias_O;  //Strong Cells will try to fire regardless of their input
	
	
	public NeuralNetwork(int i, int h1, int h2, int o) {
		numinputs = i;
		numhidden1 = h1;
		numhidden2 = h2;
		numoutputs = o;
		
		//remeber when building these weight matrices, 
		//	the #rows = the # of exit nodes
		//	the #cols = the # of entry nodes
		Weights_I_H1 = new Matrix(numhidden1, numinputs);
		Weights_H1_H2 = new Matrix(numhidden2, numhidden1);
		Weights_H2_O = new Matrix(numoutputs, numhidden2);
		
		Bias_H1 = new Matrix(numhidden1, 1);
		Bias_H2 = new Matrix(numhidden2, 1);
		Bias_O = new Matrix(numoutputs, 1);
		
	}
	
	public double[] think(double[] inp) {
		Matrix inputs = Matrix.ConvertArrayToMatrix(inp);
		Matrix hidden1 = Matrix.MultiplyThese(Weights_I_H1, inputs);
		hidden1.add(Bias_H1);
		hidden1.applySigmoidal();
		
		Matrix hidden2 = Matrix.MultiplyThese(Weights_H1_H2, hidden1);
		hidden2.add(Bias_H2);
		hidden2.applySigmoidal();
		
		Matrix outputs = Matrix.MultiplyThese(Weights_H2_O, hidden2);
		outputs.add(Bias_O);
//		outputs.applySigmoidal(); skipping to keep my range of numbers :)
		
		return Matrix.ConvertMatrixToArray(outputs);
	}
	
	public void copy(NeuralNetwork nn) {
		Bias_H1 = Matrix.CopyOf(nn.Bias_H1);
		Bias_H2 = Matrix.CopyOf(nn.Bias_H2);
		Bias_O = Matrix.CopyOf(nn.Bias_O);
		Weights_I_H1 = Matrix.CopyOf(nn.Weights_I_H1);
		Weights_H1_H2 = Matrix.CopyOf(nn.Weights_H1_H2);
		Weights_H2_O = Matrix.CopyOf(nn.Weights_H2_O);
	}
	
	public void combine(NeuralNetwork one, NeuralNetwork two) {
		Bias_H1 = Matrix.AvergeThese(one.Bias_H1,two.Bias_H1);
		Bias_H2 = Matrix.AvergeThese(one.Bias_H2,two.Bias_H2);
		Bias_O = Matrix.AvergeThese(one.Bias_O,two.Bias_O);
		Weights_I_H1 = Matrix.CombineThese(one.Weights_I_H1,two.Weights_I_H1);
		Weights_H1_H2 = Matrix.CombineThese(one.Weights_H1_H2,two.Weights_H1_H2);
		Weights_H2_O = Matrix.CombineThese(one.Weights_H2_O,two.Weights_H2_O);
	}
	
	
	/*
	Supervised Learning... Like dog training, reward correct answers, punish wrong answers
	issues
		1) you need the right answer
		2) you need a lot of training data that you teach the AI
		3) Involves a lot of Calc 2, 3, and 4 to reteach the brain.
		
	Evolved Learning... Survival of the fittest.  Keep and mate the ones that are working and throw
	out the ones that don't.
	
	
	 */
	
	
	
	/*
	When doing the multiplication, it's
	 WEIGHTS * INPUTS = OUTPUT (order matters!!!)
	 
	 
	 When I build the weights matrix.
	 	#rows = #outputs
	 	#cols = #inputs
	 	
	 	1) Determine the inputs
	 	2) Right multiply by the Weights
	 	3) Add the Bias
	 	4) This will give the strength of the output
	 	5) Correct those strength (shrink the range back to -1 to 1  or  0 to 1)
	 	6) Repeat until you have the final outputs
	 	7) Those outputs give you a decision
	 */
}
