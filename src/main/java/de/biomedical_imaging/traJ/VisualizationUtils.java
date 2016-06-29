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
	 * Plots the trajectory t
	 * @param t Trajectory to be plotted
	 */
	public static void showTrajectory(Trajectory t){
		showTrajectory("No Title", t);
	}

	/**
	 * Plots the MSD curve for trajectory t
	 * @param t Trajectory to calculate the msd curve
	 * @param lagMin Minimum timelag (e.g. 1,2,3..) lagMin*timelag = elapsed time in seconds 
	 * @param lagMax Maximum timelag (e.g. 1,2,3..) lagMax*timelag = elapsed time in seconds 
	 * @param msdeval Evaluates the mean squared displacment
	 */
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

	/**
	 * Plots the MSD curve with the trajectory t and adds the fitted model for confined diffusion above.
	 * @param t Trajectory to calculate the msd curve
	 * @param lagMin Minimum timelag (e.g. 1,2,3..) lagMin*timelag = elapsed time in seconds 
	 * @param lagMax Maximum timelag (e.g. 1,2,3..) lagMax*timelag = elapsed time in seconds 
	 * @param timelag Elapsed time between two frames.
	 * @param a Parameter alpha
	 * @param b Shape parameter 1
	 * @param c Shape parameter 2
	 * @param d Diffusion coefficient
	 */
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
	
	/**
	 * Plots the MSD curve with the trajectory t and adds the fitted model for anomalous diffusion above.
	 * @param t
	 * @param lagMin Minimum timelag (e.g. 1,2,3..) lagMin*timelag = elapsed time in seconds 
	 * @param lagMax lagMax Maximum timelag (e.g. 1,2,3..) lagMax*timelag = elapsed time in seconds 
	 * @param timelag Elapsed time between two frames.
	 * @param a Exponent alpha of power law function
	 * @param D Diffusion coeffcient 
	 */
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

	/**
	 * Plots the mean MSD curve over all trajectories in t.
	 * @param t List of trajectories
	 * @param lagMin Minimum timelag (e.g. 1,2,3..) lagMin*timelag = elapsed time in seconds 
	 * @param lagMax Maximum timelag (e.g. 1,2,3..) lagMax*timelag = elapsed time in seconds 
	 * @param msdeval Evaluates the mean squared displacment
	 */
	public static void plotMSDLine(ArrayList<? extends Trajectory> t, int lagMin,
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

	/**
	 * Plots the  MSD curve for trajectory t.
	 * @param t List of trajectories
	 * @param lagMin Minimum timelag (e.g. 1,2,3..) lagMin*timelag = elapsed time in seconds 
	 * @param lagMax Maximum timelag (e.g. 1,2,3..) lagMax*timelag = elapsed time in seconds 
	 */
	public static void plotMSDLine(Trajectory t, int lagMin, int lagMax) {
		plotMSDLine(t, lagMin, lagMax, new MeanSquaredDisplacmentFeature(t,
				lagMin));
	}

	/**
	 * Plots the mean MSD curve over all trajectories in t.
	 * @param t List of trajectories
	 * @param lagMin Minimum timelag (e.g. 1,2,3..) lagMin*timelag = elapsed time in seconds 
	 * @param lagMax Maximum timelag (e.g. 1,2,3..) lagMax*timelag = elapsed time in seconds 
	 */
	public static void plotMSDLine(ArrayList<Trajectory> t, int lagMin,
			int lagMax) {
		plotMSDLine(t, lagMin, lagMax,
				new MeanSquaredDisplacmentFeature(t.get(0), lagMin));
	}

}
