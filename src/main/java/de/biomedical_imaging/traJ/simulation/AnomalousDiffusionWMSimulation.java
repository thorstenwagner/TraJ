package de.biomedical_imaging.traJ.simulation;

import java.util.Random;

import javax.vecmath.Point3d;

import org.apache.commons.math3.ode.sampling.StepHandler;

import de.biomedical_imaging.traJ.Trajectory;
/*
 * Anomalosu Diffusion according to :
 * Anomalous Protein Diffusion in Living Cells as Seen by Fluorescence Correlation Spectroscopy
 * 
 * The spatial increments in x and y direction are choosen via the Weierstrass-Mandelbrot function 
 */
public class AnomalousDiffusionWMSimulation extends AbstractSimulator {

	private double diffusioncoefficient;
	private double timelag;
	private int dimension;
	private int numberOfSteps;
	private double alpha;
	
	public AnomalousDiffusionWMSimulation(double diffusioncoefficient, double timelag, int dimension,int numberOfSteps, double alpha) {
		this.diffusioncoefficient = diffusioncoefficient;
		this.timelag = timelag;
		this.numberOfSteps = numberOfSteps;
		this.dimension = dimension;
		this.alpha = alpha;
	}
	
	@Override
	public Trajectory generateTrajectory() {
		Trajectory t = new Trajectory(dimension);
		t.add(new Point3d(0, 0, 0));
		double[] incrx = generateIncrements();
		double[] incry = generateIncrements();
		double fact = Math.sqrt(diffusioncoefficient/1.36);
		for(int i = 1; i <= numberOfSteps; i++) {
			double steplength = Math.sqrt(incrx[i-1]*incrx[i-1] + incry[i-1]*incry[i-1]);
			Point3d pos = SimulationUtil.randomPosition(2, fact*steplength);
			pos.setX(t.get(i-1).x + fact*incrx[i-1]);
			pos.setY(t.get(i-1).y + fact*incry[i-1]);
			t.add(pos);
		}
		
		return t;
	}
	
	public double[] generateIncrements(){
		double gamma = Math.sqrt(Math.PI);
		double H = alpha/2;
		double[] wxs = new double[numberOfSteps];
		double[] increments = new double[numberOfSteps];
		
		double[] phasesx = new double[48+8+1];
		for(int j = 0; j < phasesx.length; j++){
			phasesx[j] = CentralRandomNumberGenerator.getInstance().nextDouble()*2*Math.PI;
		}
		
		for(int t = 1; t <= numberOfSteps; t++){
			double tStar = 2*Math.PI*t/numberOfSteps;
			double wx = 0;
			
			for(int n = -8; n <= 48; n++){
				double phasex = phasesx[n+8];
				wx += (Math.cos(phasex)-Math.cos(Math.pow(gamma, n) * tStar + phasex))/Math.pow(gamma, n*H);
			}
			double prevwx = (t-2)>=0?wxs[t-2]:0;
			wxs[t-1] = wx;
			increments[t-1] = wxs[t-1]-prevwx; 
		}
		
		return increments;
	}
	
	public double[] generateSteplengths2(){
		double gamma = Math.sqrt(Math.PI);
		double H = alpha/2;
		double[] wxs = new double[numberOfSteps];
		double[] wys = new double[numberOfSteps];
		double[] steplengths = new double[numberOfSteps];

		for(int t = 1; t <= numberOfSteps; t++){
			double tStar = 2*Math.PI*  ((1.0*t)/ (numberOfSteps));
			double wx = 0;
			double wy = 0;
			
			for(int n = -8; n <= 48; n++){
				double phasex = CentralRandomNumberGenerator.getInstance().nextDouble()*2*Math.PI;	
				wx += (Math.cos(phasex)-Math.cos(Math.pow(gamma, n) * tStar + phasex))/Math.pow(gamma, n*H);
				
				double phasey = CentralRandomNumberGenerator.getInstance().nextDouble()*2*Math.PI;
				wy += (Math.cos(phasey)-Math.cos(Math.pow(gamma, n) * tStar + phasey))/Math.pow(gamma, n*H);
			}
			double prevwx = (t-2)>=0?wxs[t-2]:0; //Wenn t-2 >= 0 dann gib mir das ELement wxs[t-2], ansonsten 0
			double prevwy = (t-2)>=0?wys[t-2]:0;
			wxs[t-1] = wx;
			wys[t-1] = wy;
			
			//steplengths[t-1] = Math.sqrt(Math.pow(wxs[t-1]-prevwx,2)+Math.pow(wys[t-1]-prevwy,2));
		}
		
		return steplengths;
	}
	
	
	

}
