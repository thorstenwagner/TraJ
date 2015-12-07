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
 * 
 * @author Thorsten Wagner (wagner@biomedical-imaging.de)
 *
 */
public class CovarianceDiffusionCoefficientEstimator extends AbstractDiffusionCoefficientEstimator {
	
	private double getDistanceProductX(Trajectory t, double driftx, int n,int m){
		double xn = t.getPositions().get(n+1).getX() - t.getPositions().get(n).getX() - driftx;
		double xm = t.getPositions().get(m+1).getX() - t.getPositions().get(m).getX() - driftx; 

		return xn*xm;
	}
	
	private double getDistanceProductY(Trajectory t, double drifty,int n,int m){
		double xn = t.getPositions().get(n+1).getY() - t.getPositions().get(n).getY() - drifty;
		double xm = t.getPositions().get(m+1).getY() - t.getPositions().get(m).getY() - drifty;
		return xn*xm;
	}
	
	private double getDistanceProductZ(Trajectory t, double driftz,int n,int m){
		double xn = t.getPositions().get(n+1).getZ() - t.getPositions().get(n).getZ() - driftz;
		double xm = t.getPositions().get(m+1).getZ() - t.getPositions().get(m).getZ() - driftz;
		return xn*xm;
	}
	
	
	@Override
	/**
	 * @return [0] diffusion coefficient [1] localization noise in x-direction [2] loc. noise in y-diretction [3] loc. noise in z-direction
	 */
	public double[] getDiffusionCoefficient(Trajectory t, double fps, double[] drift) {
		double[] cov = getCovData(t, fps, drift,0);
		return cov;
	}
	
	/*
	 * TODO: Add supoort for gaps
	 */
	private double[] getCovData(Trajectory track, double fps, double[] drift, double R){
		
		double sumX = 0;
		double sumX2 = 0;
		double sumY = 0;
		double sumY2 = 0;
		double sumZ = 0;
		double sumZ2 = 0;
		int N=0;
		int M=0;
		TrajectoryValidIndexTimelagIterator it = new TrajectoryValidIndexTimelagIterator(track, 1);
		while(it.hasNext()){
			int i = it.next();
			sumX = sumX + getDistanceProductX(track,drift[0],i, i) ;
			sumY = sumY + getDistanceProductY(track,drift[1],i, i) ;
			sumZ = sumZ + getDistanceProductZ(track,drift[2],i, i) ;
			N++;
			if((i+2) < track.getPositions().size() &&  track.getPositions().get(i+2) !=null){
				sumX2 = sumX2 + getDistanceProductX(track,drift[0],i, i+1) ;
				sumY2 = sumY2 + getDistanceProductY(track,drift[1],i, i+1);
				sumZ2 = sumZ2 + getDistanceProductZ(track,drift[2],i, i+1);
				M++;
			}
		}
		
		double msdX = (sumX/(N));
		
		double msdY = (sumY/(N));
		double msdZ = (sumZ/(N));
		
		double covX = (sumX2/(M) );
		
		double covY = (sumY2/(M) );
		double covZ = (sumZ2/(M) );
		
		double termXA = msdX/2 * fps;
		double termXB = covX * fps ;
		double termYA = msdY/2 * fps;
		double termYB = covY * fps;
		double termZA = msdZ/2 * fps;
		double termZB = covZ * fps;
		
		double DX = termXA+termXB;	
		double DY = termYA+termYB;
		double DZ = termZA+termZB;
		double D;
		D= (DX+DY+DZ)/track.getDimension();
	
		
		double[] data  = new double[4]; //[0] = Diffusioncoefficient, [1] = LocNoiseX, [2] = LocNoiseY
		data[0] = D;
		data[1] = R*msdX + (2*R-1)+covX;
		data[2] = R*msdY + (2*R-1)+covY;
		data[3] = R*msdZ + (2*R-1)+covZ;
		
		return data;
	}
		
}
