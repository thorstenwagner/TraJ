/*
The MIT License (MIT)

Copyright (c) 2015 Thorsten Wagner (wagner@biomedical-imaging.de)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package de.biomedical_imaging.traJ;
/**
 * Calculates the mean squared displacement
 * @author Thorsten Wagner (wagner at biomedical - imaging.de
 *
 */
public class MSDCalculator {
	
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
