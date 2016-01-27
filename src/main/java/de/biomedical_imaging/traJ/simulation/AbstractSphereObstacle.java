/*
The MIT License (MIT)

Copyright (c) 2015-2016 Thorsten Wagner (wagner@biomedical-imaging.de)

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

package de.biomedical_imaging.traJ.simulation;

public abstract class AbstractSphereObstacle {

	private double[] position;
	private double radius;
	private int dimension;
	
	public AbstractSphereObstacle(double[] position, double radius, int dimension) {
		this.radius = radius;
		this.position = position;
		this.dimension = dimension;
	}

	public double[] getPosition() {
		// TODO Auto-generated method stub
		return position;
	}
	/**
	 * 
	 */
	public boolean isInside(double[] pos) {
		double[] absDist = new double[dimension];
		double sumAbsDist = 0;
		for(int i = 0; i < dimension; i++){
			absDist[i] = Math.abs(pos[i]-position[i]);
			if(absDist[i]>radius){
				
				return false;
			}
			sumAbsDist += absDist[i];
			//distance += Math.pow(dist, 2);
		}
		if(sumAbsDist < radius){
			return true;
		}
		
		double sumDistSquared = 0;
		for(int i = 0; i < absDist.length; i++){
			sumDistSquared += absDist[i]*absDist[i];
		}
		
		//distance = Math.sqrt(distance);
		return sumDistSquared<radius*radius;
	}

	public abstract void updatePosition(AnomalousDiffusionScene s);

	public boolean insideSzeneBoundaries(AnomalousDiffusionScene s) {
		for(int i = 0; i < s.getSize().length; i++){
			if(position[i]+radius >= s.getSize()[i] ||
					position[i]-radius < 0){
				return false;
			}
		}
		return true;
	}

	/**
	 * For 2D: Implements formula 14 of http://mathworld.wolfram.com/Circle-CircleIntersection.html 
	 * For 3D: Implements formula 16 of http://mathworld.wolfram.com/Sphere-SphereIntersection.html
	 */
	public double intersectionVolume(AbstractSphereObstacle o) {
		
		if(dimension != o.getDimension()){
			throw new IllegalArgumentException("Obstacles have different dimensions");
		}
		double result = 0;
		double distance = 0;
		for(int i = 0; i < dimension; i++){
			distance += Math.pow(position[i]-o.getPosition()[i],2);
		}
		distance = Math.sqrt(distance);
		if(distance<0.0000000001){
			return  Math.min(getVolume(), o.getVolume());
		}
		
		double r = getRadius();
		double r_ = o.getRadius();
		if(distance> Math.max(r, r_)){
			return 0;
		}
		double d = distance;
		if(dimension==2){
			result = r*r*Math.acos( (d*d+r*r-r_*r_)/(2*d*r) )+
					r_*r_*Math.acos( (d*d+r_*r_-r*r)/(2*d*r_) )-
					0.5*Math.sqrt( (-d+r+r_)*(d+r-r_)*(d-r+r_)*(d+r+r_) );
		}
		else if(dimension==3){
			
			result = Math.PI*Math.pow(r_+r-d,2)*(d*d+2*d*r-3*r*r+2*d*r_+6*r*r_-3*r_*r_)/(12*d);
		}
		return result;
	}

	public double getRadius() {
		// TODO Auto-generated method stub
		return radius;
	}
	
	public double getVolume() {
		double v=0;
		if(dimension==2){
			v = Math.PI*radius*radius;
		}
		else if(dimension == 3){
			v = 4.0/3.0*Math.PI*radius*radius*radius;
		}
		return v;
	}

	public int getDimension() {
		// TODO Auto-generated method stub
		return dimension;
	}

}
