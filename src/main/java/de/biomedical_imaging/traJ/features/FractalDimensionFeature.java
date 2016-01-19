package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;
/**
 * Implements the path dimension according to
 * M. J. Katz and E. B. George, “Fractals and the analysis of growth paths,” Bull. Math. Biol., vol. 47, no. 2, pp. 273–286, 1985.
 * @author Thorsten Wagner
 *
 */
public class FractalDimensionFeature extends AbstractTrajectoryFeature{

	Trajectory t;
	public FractalDimensionFeature(Trajectory t) {
		this.t = t;
		if(t.getDimension() != 2){
			throw new IllegalArgumentException("The fractal dimension feature only supoorts planer (2D) trajetorys"); 
		}
	}
	
	@Override
	public double[] evaluate() {
		double largestDistance = Double.MIN_VALUE;
		double totalLength = 0;
		for(int i = 0; i < t.size(); i++){
			for(int j = i+1; j < t.size(); j++){
				double d = t.get(i).distance(t.get(j));
				if(d>largestDistance){
					largestDistance = d;
				}
			}
			if(i>0){
				totalLength += t.get(i).distance(t.get(i-1));
			}
		}
		double n = t.size()-1;
		double fractalDImension = Math.log(n)/(Math.log(n)+Math.log(largestDistance/totalLength));
		
		return new double[] {fractalDImension};
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Fractal Dimension";
	}
	
	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		if(t.getDimension() != 2){
			throw new IllegalArgumentException("The fractal dimension feature only supoorts planer (2D) trajetorys"); 
		}
		
	}

}
