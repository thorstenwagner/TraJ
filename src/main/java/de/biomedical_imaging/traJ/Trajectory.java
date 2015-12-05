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

public  class Trajectory {
	
	private ArrayList<Point3d> positions;

	private int dimension;
	
	public Trajectory(int dimension) {
		this.dimension = dimension;
		positions = new ArrayList<Point3d>();
	}
	
	public  ArrayList<Point3d> getPositions(){
		return positions;
	}
	
	/**
	 * Adds an position the trajectory. Between two position should the same timelag.
	 * If there is a gap in the trajectory, please add null.
	 * @param pos The next position of the trajectory. Add null for a gap.
	 */
	public void addPosition(Point3d pos){
		positions.add(pos);
	}
	
	public int getDimension(){
		return dimension;
	}
	
}
