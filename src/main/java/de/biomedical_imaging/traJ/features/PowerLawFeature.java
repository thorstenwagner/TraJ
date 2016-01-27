package de.biomedical_imaging.traJ.features;

import ij.measure.CurveFitter;
import de.biomedical_imaging.traJ.Trajectory;

public class PowerLawFeature extends AbstractTrajectoryFeature {

	private Trajectory t;
	private int minlag;
	private int maxlag;
	public PowerLawFeature(Trajectory t, int minlag, int maxlag) {
		this.t = t;
		this.minlag = minlag;
		this.maxlag = maxlag;
	}
	
	@Override
	public double[] evaluate() {
		int N = maxlag - minlag + 1;
		double[] xData = new double[N];
		double[] yData = new double[N];
		for(int i = minlag; i <= maxlag; i++){
			MeanSquaredDisplacmentFeature msdFeature = new MeanSquaredDisplacmentFeature(t, i);
			xData[i-minlag] = i;
			yData[i-minlag] = msdFeature.evaluate()[0];
			//System.out.print(minlag/30.0+",");
		}
		CurveFitter fitter = new CurveFitter(xData, yData);
		double[] start = {1,1};
		fitter.setInitialParameters(start);
		fitter.doFit(CurveFitter.POWER);

		double params[] = fitter.getParams();
		
		double exponent = params[1];
		//System.out.println("0: " + params[0] + " 1: " + params[1]);
		result = new double[] {exponent};
		return result;
	}

	@Override
	public String getName() {
		
		return "Power-Law-Feature";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		result = null;
		
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return "POWER";
	}

}
