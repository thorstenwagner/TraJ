package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;

/**
 * 
 * Relates the squared net dispalcement to the sum of squared step lengths
 */
public class EfficiencyFeature extends AbstractTrajectoryFeature {
	
	private Trajectory t;
	
	public EfficiencyFeature(Trajectory t) {
		this.t = t;
	}
	
	@Override
	public double[] evaluate() {
		
		return new double[]{getEfficiency()};
	}
	
	public double getEfficiency(){
		double sum = 0;
		for(int i = 1; i < t.size(); i++){
			sum += t.get(i).distance(t.get(i-1))*t.get(i).distance(t.get(i-1));
		}
		if(sum<Math.pow(10, -10)){
			return 0;
		}
		double straightness = (t.get(0).distance(t.get(t.size()-1)) * t.get(0).distance(t.get(t.size()-1)))/(t.size()*sum);
		return straightness;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Efficiency";
	}

	@Override
	public String getShortName() {
		
		return "EFFICENCY";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}