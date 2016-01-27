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

import java.util.Random;

public class CentralRandomNumberGenerator extends Random {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static CentralRandomNumberGenerator instance = null;
	private static long seed = -1;
	
	public CentralRandomNumberGenerator() {
		super();
	}
	
	public CentralRandomNumberGenerator(long seed) {
		super();
		this.setSeed(seed);
	}
	
	public static CentralRandomNumberGenerator getInstance(){
		if(instance==null){
			if(seed==-1){
				instance = new CentralRandomNumberGenerator();
			}
			else{
				instance = new CentralRandomNumberGenerator(seed);
			}
		}
		return instance;
	}
	
	public long getSeed(){
		return seed;
	}
	
	/**
	 * 
	 * @return -1 or 1 randomly
	 */
	public int randomSign(){
		return (getInstance().nextDouble()>0.5?1:-1);
	}
	
	

}
