package de.biomedical_imaging.traJ;
/**
 * Calculates the mean squared displacement
 * @author Thorsten Wagner (wagner at biomedical - imaging.de
 *
 */
public class MSDCaclulator {
	
	/**
	 * 
	 * Calculates the mean squared displacement (MSD) and corrects a global drift. Further more it calculates
	 * the relative variance of MSD according to:
	 * S. Huet, E. Karatekin, V. S. Tran, I. Fanget, S. Cribier, and J.-P. Henry, 
	 * “Analysis of transient behavior in complex trajectories: application to secretory vesicle dynamics.,” 
	 * Biophys. J., vol. 91, no. 9, pp. 3542–3559, 2006.
	 * 
	 * @param t Trajectory
	 * @param timelag Timeleg for msd caluclation (>= 1)
	 * @param drift Global dirft [0]=x, [1]=y, and [2] =z direction
	 * @return Double array [0] =MSD (in position unit squared) and [1] = Relative Variance (unitless)
	 */
	public static double[] getMeanSquaredDisplacment(Trajectory t, int timelag, double[] drift){
		double msd = 0;
		double[] result = new double[2];
		if(t.getPositions().size()==1){
			result[0] =0;
			result[1] =0;
			return result;
		}
		
		if(timelag<1){
			throw new IllegalArgumentException("Timelag can not be smaller than 1");
		}
		TrajectoryValidIndexTimelagIterator it = new TrajectoryValidIndexTimelagIterator(t, timelag);
		int N = 0;
		while(it.hasNext()){
			int i = it.next();
			msd = msd + 
					Math.pow(t.getPositions().get(i).getX()-t.getPositions().get(i+timelag).getX()+timelag*drift[0],2) + 
					Math.pow(t.getPositions().get(i).getY()-t.getPositions().get(i+timelag).getY()+timelag*drift[1],2) +
					Math.pow(t.getPositions().get(i).getZ()-t.getPositions().get(i+timelag).getZ()+timelag*drift[2],2);
			N++;
		}
		
		msd = msd/N; 
		
		result[0] = msd;
		result[1] = (timelag*(2*timelag*timelag+1.0))/(N-timelag+1.0); //Variance
		
		return result;
	}
	
}
