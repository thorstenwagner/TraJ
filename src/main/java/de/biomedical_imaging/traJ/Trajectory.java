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
import javax.vecmath.Point3d;

import org.knowm.xchart.Chart;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;

import de.biomedical_imaging.traJ.features.AbstractTrajectoryFeature;

public  class Trajectory extends ArrayList<Point3d> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int dimension;					// Dimension of the trajectory
	private int relativeStartTimepoint;		// Relative start point (Frame number)
	private long id;
	private static long idCounter=1; 
	private String type = "";
	private ArrayList<AbstractTrajectoryFeature> features;
	
	/**
	 * @param dimension Dimension of the trajectory
	 */
	public Trajectory(int dimension) {
		this.dimension = dimension;
		relativeStartTimepoint = 0;
		id = idCounter++;
		features = new ArrayList<AbstractTrajectoryFeature>();
	}
	
	/**
	 * 
	 * @param dimension Dimension of the trajectory
	 * @param relativeStartTimepoint When (index) does the track starts in a image sequence
	 */
	public Trajectory(int dimension, int relativeStartTimepoint) {
		this.dimension = dimension;
		this.relativeStartTimepoint = relativeStartTimepoint;
		id = idCounter++;
		features = new ArrayList<AbstractTrajectoryFeature>();
	}
	
	public Trajectory() {
		relativeStartTimepoint = 0;
		features = new ArrayList<AbstractTrajectoryFeature>();
	}
	
	@Override
	public Trajectory subList(int fromIndex, int toIndex) {
		Trajectory t = new Trajectory(dimension);
		
		for(int i = fromIndex; i < toIndex; i++){
			t.add(this.get(i));
		}
		return t;
	}
	
	/**
	 * @return Returns a list of features.
	 */
	public ArrayList<AbstractTrajectoryFeature> getFeatures(){
		return features;
	}
	
	/**
	 * Adds a features to the feature list which belongs to the trajectory
	 * @param feature Some trajectory feature
	 */
	public void addFeature(AbstractTrajectoryFeature feature){
		
		features.add(feature);
	}
	
	/**
	 * Converts the positions to a 2D double array
	 * @return 2d double array [i][j], i=Time index, j=coordinate index
	 */
	public double[][] getPositionsAsArray(){
		double[][] posAsArr = new double[size()][3];
		for(int i = 0; i < size(); i++){
			if(get(i)!=null){
				posAsArr[i][0] = get(i).x;
				posAsArr[i][1] = get(i).y;
				posAsArr[i][2] = get(i).z;
			}
			else{
				posAsArr[i] = null;
			}
		}
		return posAsArr;
	}
	
	@Override
	public String toString() {
		String result="";
		for(int i = 0; i < size(); i++){
			result += " x: "+   get(i).x + " y: " +  get(i).y + " z: " + get(i).z + "\n";
		}
		return result;
	}
	
	/**
	 * Plots the trajectory
	 * @param title Title of the plot
	 */
	public void showTrajectory(String title){
		if(dimension==2){
		 	double[] xData = new double[this.size()];
		    double[] yData = new double[this.size()];
		    for(int i = 0; i < this.size(); i++){
		    	xData[i] = this.get(i).x;
		    	yData[i] = this.get(i).y;
		    	
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
	public void showTrajectory(){
		showTrajectory("No Title");
	}
	
	@Override
	/**
	 * Adds an position the trajectory. Between two position should the same timelag.
	 * If there is a gap in the trajectory, please add null.
	 * @param pos The next position of the trajectory. Add null for a gap.
	 */
	public boolean add(Point3d e) {
		// TODO Auto-generated method stub
		return super.add(e);
	}
	
	public void scale(double v){
		for(int i = 0; i < this.size(); i++){
			this.get(i).scale(v);;
		}
	}
	
	public boolean add(double x, double y, double z){
		return super.add(new Point3d(x, y, z));
	}
	
	public int getDimension(){
		return dimension;
	}
	
	public void setDimension(int dimension){
		this.dimension = dimension;
	}
	
	public long getID(){
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	public int getRelativeStartTimepoint(){
		return relativeStartTimepoint;
	}
	
	public void setRelativStartTimepoint(int timepoint){
		relativeStartTimepoint = timepoint;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getType(){
		return type;
	}
	
	public static void restIDCounter(){
		idCounter=1;
	}
}
