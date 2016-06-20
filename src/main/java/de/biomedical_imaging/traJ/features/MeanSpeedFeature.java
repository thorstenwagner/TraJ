package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;

/**
 * Implements the mean curvilinear speed and the mean straight-line speed according to
 * http://doi.org/10.1016/B978-0-12-391857-4.00009-4
 * 
 * @author thorsten
 *
 */
public class MeanSpeedFeature extends AbstractTrajectoryFeature {

	private Trajectory t;
	private double timelag;
	
	/**
	 * 
	 * @param t Trajectory where the speed should be calculated for.
	 * @param timelag Timelag between two steps.
	 */
	public MeanSpeedFeature(Trajectory t,double timelag) {
		this.t = t;
		this.timelag = timelag;
	}
	
	@Override
	/**
	 * Calculates the mean curvlinear speed and the mean straight-line speed.
	 * @return Double array where the first element is the mean curvilinear speed and the second the mean straight-line speed.
	 */
	public double[] evaluate() {
		double sum = 0;
		for(int i = 1; i < t.size(); i++){
			sum += t.get(i-1).distance(t.get(i))/timelag;
		}
		
		double meanspeed = sum/(t.size()-1);
		
		double netDistance = t.get(0).distance(t.get(t.size()-1));
		double straightLineSpeed = netDistance/((t.size()-1)*timelag);
		
		return new double[]{meanspeed,straightLineSpeed};
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Instantaneous Speed Feature";
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "INSTANT.SPEED";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		// TODO Auto-generated method stub
		
	}

}
