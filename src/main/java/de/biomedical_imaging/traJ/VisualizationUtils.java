package de.biomedical_imaging.traJ;

import java.util.ArrayList;

import org.knowm.xchart.Chart;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;

import de.biomedical_imaging.traJ.features.AbstractMeanSquaredDisplacmentEvaluator;
import de.biomedical_imaging.traJ.features.MeanSquaredDisplacmentFeature;

public class VisualizationUtils {
	
	/**
	 * Plots the trajectory
	 * @param title Title of the plot
	 * @param t Trajectory to be plotted
	 */
	public static void showTrajectory(String title, Trajectory t){
		if(t.getDimension()==2){
		 	double[] xData = new double[t.size()];
		    double[] yData = new double[t.size()];
		    for(int i = 0; i < t.size(); i++){
		    	xData[i] = t.get(i).x;
		    	yData[i] = t.get(i).y;
		    	
		    }
		    // Create Chart
		    Chart chart = QuickChart.getChart(title, "X", "Y", "y(x)", xData, yData);
	
		    //Show it
		    new SwingWrapper(chart).displayChart();
		} 
	}
	/**
	 * Plots the trajectory
	 */
	public static void showTrajectory(Trajectory t){
		showTrajectory("No Title", t);
	}

	public static void plotMSDLine(Trajectory t, int lagMin, int lagMax,
			AbstractMeanSquaredDisplacmentEvaluator msdeval) {

		double[] xData = new double[lagMax - lagMin + 1];
		double[] yData = new double[lagMax - lagMin + 1];
		msdeval.setTrajectory(t);
		msdeval.setTimelag(lagMin);
		for (int i = lagMin; i < lagMax + 1; i++) {
			msdeval.setTimelag(i);
			double msdhelp = msdeval.evaluate()[0];
			xData[i - lagMin] = i;
			yData[i - lagMin] = msdhelp;
		}

		// Create Chart
		Chart chart = QuickChart.getChart("MSD Line", "LAG", "MSD", "MSD",
				xData, yData);

		// Show it
		new SwingWrapper(chart).displayChart();
	}

	public static void plotMSDLineWithConfinedModel(Trajectory t, int lagMin,
			int lagMax, double timelag, double a, double b, double c, double d) {

		double[] xData = new double[lagMax - lagMin + 1];
		double[] yData = new double[lagMax - lagMin + 1];
		double[] modelData = new double[lagMax - lagMin + 1];
		MeanSquaredDisplacmentFeature msdeval = new MeanSquaredDisplacmentFeature(
				t, lagMin);
		msdeval.setTrajectory(t);
		msdeval.setTimelag(lagMin);
		for (int i = lagMin; i < lagMax + 1; i++) {
			msdeval.setTimelag(i);
			double msdhelp = msdeval.evaluate()[0];
			xData[i - lagMin] = i;
			yData[i - lagMin] = msdhelp;
			modelData[i - lagMin] = a
					* (1 - b * Math.exp((-4 * d) * ((i * timelag) / a) * c));
		}

		// Create Chart
		Chart chart = QuickChart.getChart("MSD Line", "LAG", "MSD", "MSD",
				xData, yData);
		chart.addSeries("Model", xData, modelData);

		// Show it
		new SwingWrapper(chart).displayChart();
	}

	public static void plotMSDLineWithPowerModel(Trajectory t, int lagMin,
			int lagMax, double timelag, double a, double D) {

		double[] xData = new double[lagMax - lagMin + 1];
		double[] yData = new double[lagMax - lagMin + 1];
		double[] modelData = new double[lagMax - lagMin + 1];
		MeanSquaredDisplacmentFeature msdeval = new MeanSquaredDisplacmentFeature(
				t, lagMin);
		msdeval.setTrajectory(t);
		msdeval.setTimelag(lagMin);
		for (int i = lagMin; i < lagMax + 1; i++) {
			msdeval.setTimelag(i);
			double msdhelp = msdeval.evaluate()[0];
			xData[i - lagMin] = i;
			yData[i - lagMin] = msdhelp;
			modelData[i - lagMin] = 4 * D * Math.pow(i * timelag, a);
		}

		// Create Chart
		Chart chart = QuickChart.getChart("MSD Line", "LAG", "MSD", "MSD",
				xData, yData);
		chart.addSeries("Model", xData, modelData);

		// Show it
		new SwingWrapper(chart).displayChart();
	}

	public static void plotMSDLine(ArrayList<Trajectory> t, int lagMin,
			int lagMax, AbstractMeanSquaredDisplacmentEvaluator msdeval) {

		double[] xData = new double[lagMax - lagMin + 1];
		double[] yData = new double[lagMax - lagMin + 1];
		for (int j = lagMin; j < lagMax + 1; j++) {
			double msd = 0;
			int N = 0;
			for (int i = 0; i < t.size(); i++) {
				if(t.get(i).size()>lagMax){
					msdeval.setTrajectory(t.get(i));
					msdeval.setTimelag(j);
					msd += msdeval.evaluate()[0];
					N++;
				}
			}
			msd = 1.0 / N * msd;
			xData[j - lagMin] = j;
			yData[j - lagMin] = msd;
		}

		// Create Chart
		Chart chart = QuickChart.getChart("MSD Line", "LAG", "MSD", "MSD",
				xData, yData);

		// Show it
		new SwingWrapper(chart).displayChart();
	}

	public static void plotMSDLine(Trajectory t, int lagMin, int lagMax) {
		plotMSDLine(t, lagMin, lagMax, new MeanSquaredDisplacmentFeature(t,
				lagMin));
	}

	public static void plotMSDLine(ArrayList<Trajectory> t, int lagMin,
			int lagMax) {
		plotMSDLine(t, lagMin, lagMax,
				new MeanSquaredDisplacmentFeature(t.get(0), lagMin));
	}

}
