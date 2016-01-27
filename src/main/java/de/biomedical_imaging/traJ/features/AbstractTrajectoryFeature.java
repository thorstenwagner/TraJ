package de.biomedical_imaging.traJ.features;

import de.biomedical_imaging.traJ.Trajectory;

public abstract class AbstractTrajectoryFeature {
	protected double[] result = null;
	
	/**
	 * @return return the result;
	 */
	public abstract double[] evaluate();
	
	/**
	 * @return Returns the result, but does not recalculate when it was calculated earlier
	 */
	public double[] getValue(){
		if(result==null){
			result = evaluate();
		}
		return result;
	}
	public abstract String getName();
	public abstract String getShortName();
	public abstract void setTrajectory(Trajectory t);
	
	

}
