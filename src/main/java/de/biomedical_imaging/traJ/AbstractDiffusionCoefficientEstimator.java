package de.biomedical_imaging.traJ;



public abstract class AbstractDiffusionCoefficientEstimator {
	
	/**
	 * 
	 * 
	 * @param t Trajectory
	 * @param fps Frames per second [Hz]
	 * @param drift [0] = global drift in x direction, [1] = global drift in y direction, [2] = global drift in z direction
	 * @return Returns the diffusion coefficent
	 */
	abstract double[] getDiffusionCoefficient(Trajectory t, double fps, double[] drift);

}
