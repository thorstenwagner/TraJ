package de.biomedical_imaging.traJ;


public class CovarianceDiffusionCoefficientEstimator extends AbstractDiffusionCoefficientEstimator {
	
	private double getDistanceProductX(Trajectory t, double driftx, int n,int m){
		double xn = t.getPositions().get(n).getX() - t.getPositions().get(n-1).getX() + driftx;
		double xm = t.getPositions().get(m).getX() - t.getPositions().get(m-1).getX() + driftx; 

		return xn*xm;
	}
	
	private double getDistanceProductY(Trajectory t, double drifty,int n,int m){
		double xn = t.getPositions().get(n).getY() - t.getPositions().get(n-1).getY() + drifty;
		double xm = t.getPositions().get(m).getY() - t.getPositions().get(m-1).getY() + drifty;
		return xn*xm;
	}
	
	private double getDistanceProductZ(Trajectory t, double driftz,int n,int m){
		double xn = t.getPositions().get(n).getZ() - t.getPositions().get(n-1).getZ() + driftz;
		double xm = t.getPositions().get(m).getZ() - t.getPositions().get(m-1).getZ() + driftz;
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
	
	private double[] getCovData(Trajectory track, double fps, double[] drift, double R){
		
		double sumX = 0;
		double sumX2 = 0;
		double sumY = 0;
		double sumY2 = 0;
		double sumZ = 0;
		double sumZ2 = 0;
		int N=0;
		int M=0;

		for(int i = 1; i < track.getPositions().size(); i++){
			sumX = sumX + getDistanceProductX(track,drift[0],i, i) ;
			sumY = sumY + getDistanceProductY(track,drift[1],i, i) ;
			sumZ = sumZ + getDistanceProductZ(track,drift[2],i, i) ;
			N++;
			if(i < (track.getPositions().size()-1)){
				sumX2 = sumX2 + getDistanceProductX(track,drift[0],i, i+1) ;
				sumY2 = sumY2 + getDistanceProductY(track,drift[1],i, i+1);
				sumZ2 = sumZ2 + getDistanceProductZ(track,drift[2],i, i+1);
				M++;
			}
		}
		
		double msdX = (sumX/(N));
		double msdY = (sumY/(N));
		double msdZ = (sumY/(N));
		
		double covX = (sumX2/(M) );
		double covY = (sumY2/(M) );
		double covZ = (sumY2/(M) );
		
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
