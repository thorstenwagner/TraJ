package de.biomedical_imaging.traJ.features;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import de.biomedical_imaging.traJ.Trajectory;
import de.biomedical_imaging.traj.math.ActiveTransportMSDLineFit;

/**
 * Fit the MSD data through the model:
 * MSD(dt) = 4*D*dt + (v*dt)^2
 * where D is the diffusion coefficient, dt is the timelag and v is the velocity
 * 
 * @author Thorsten Wagner
 *
 */
public class ActiveTransportParametersFeature extends AbstractTrajectoryFeature {

	private Trajectory t;
	private double timelag;

	/**
	 * 
	 * @param t Trajectory for which the features should be estimated.
	 * @param timelag Timelag between two steps
	 */
	public ActiveTransportParametersFeature(Trajectory t, double timelag) {
		this.t = t;
		this.timelag = timelag;
	}
	
	/**
	 * @return Returns an double array with the following elements [0]: diffusion coefficient [1]: Velocity
	 */
	@Override
	public double[] evaluate() {
		
		
		MeanSquaredDisplacmentFeature msdevaluator = new MeanSquaredDisplacmentFeature(t, 1);
		msdevaluator.setTrajectory(t);

		ArrayList<Double> xDataList = new ArrayList<Double>();
		ArrayList<Double> yDataList = new ArrayList<Double>();
		for(int i = 1; i < t.size()/3; i++){
			msdevaluator.setTimelag(i);
			double[] res = msdevaluator.evaluate();
			double msdhelp= res[0];
			int N = (int)res[2];
			for(int j = 0; j < N; j++){
				xDataList.add(i*timelag);
				yDataList.add(msdhelp) ;
			}
		}
		double[] xdata = ArrayUtils.toPrimitive(xDataList.toArray(new Double[0]));
		double[] ydata = ArrayUtils.toPrimitive(yDataList.toArray(new Double[0]));
		
		ActiveTransportMSDLineFit afit = new ActiveTransportMSDLineFit();
		afit.doFit(xdata, ydata);
		
		result = new double[]{afit.getDiffusionCoefficient(),afit.getVelocity(),afit.getFitGoodness()};
		return result;
	}

	@Override
	public String getName() {
		return "Active transport parameters";
	}

	@Override
	public String getShortName() {
		return "ACTPARAM";
	}

	@Override
	public void setTrajectory(Trajectory t) {
		this.t = t;
		
	}

}
