package de.biomedical_imaging.traj.math;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import de.biomedical_imaging.traJ.Trajectory;

public class RadiusGyrationTensor2D {
	
	private Trajectory t;
	
	public RadiusGyrationTensor2D(Trajectory t) {
		this.t = t;
	}
	
	public Array2DRowRealMatrix getRadiusOfGyrationTensor(){
		return getRadiusOfGyrationTensor(t);
	}
	
	public static Array2DRowRealMatrix getRadiusOfGyrationTensor(Trajectory t){
		double meanx =0;
		double meany =0;
		for(int i = 0; i < t.size(); i++){
			meanx+= t.get(i).x;
			meany+= t.get(i).y;
		}
		meanx = meanx/t.size();
		meany = meany/t.size();
		
		double e11 = 0;
		double e12 = 0;
		double e21 = 0;
		double e22 = 0;
		
		for(int i = 0; i < t.size(); i++){
			e11 += Math.pow(t.get(i).x-meanx,2);
			e12 += (t.get(i).x-meanx)*(t.get(i).y-meany);
			e22 += Math.pow(t.get(i).y-meany,2);
		}
		e11 = e11 / t.size();
		e12 = e12 / t.size();
		e21 = e12;
		e22 = e22 / t.size();
		int rows = 2;
		int columns = 2;
		Array2DRowRealMatrix gyr = new Array2DRowRealMatrix(rows, columns); 
		
		gyr.addToEntry(0, 0, e11);
		gyr.addToEntry(0, 1, e12);
		gyr.addToEntry(1, 0, e21);
		gyr.addToEntry(1, 1, e22);
		
		return gyr;
	}

}
