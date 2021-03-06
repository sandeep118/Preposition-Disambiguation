/* Note: This message is to inform you that this code was modified by Stephen Tratz in early 2012 and that therefore this code will
 * be somewhat different from that made available at the Information Sciences Institute's website (unless similar changes are made there).
 * This message is here to comply with the terms of the Apache license ("You must cause any modified files to carry prominent notices stating that You changed the files").
 */

/*
 * Copyright 2011 University of Southern California 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0 
 *      
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package miacp.types;

import java.io.Serializable;
import java.util.Arrays;

/**
 * A growable array of <code>int</code>s
 */
public class IntArrayList implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int[] mData;
	private int mLength;
	
	public IntArrayList() {
		this(10);
	}
	
	public IntArrayList(int size) {
		mData = new int[size];
	}
	
	public void add(int b) {
		expandIfNecessary(mData, mLength);
		mData[mLength++] = b;
	}
	
	private void expandIfNecessary(int[] data, int neededLength) {
		if(data.length == neededLength) {
			int newSize = (data.length*3)/2+1;
			int[] newData = new int[newSize];
			System.arraycopy(data, 0, newData, 0, data.length);
			mData = newData;
		}
	}
	
	public int[] getData() {
		return mData;
	}
	
	public int[] toCompactArray() {
		int[] result = new int[mLength];
		System.arraycopy(mData, 0, result, 0, mLength);
		return result;
	}
	
	public int get(int i) {
		return mData[i];
	}
	
	public void set(int i, int b) {
		mData[i] = b;
	}
	
	public int size() {
		return mLength;
	}
	
	public void clear() {
		mLength = 0;
	}
	
	public void sort() {
		Arrays.sort(mData, 0, mLength);
	}
	
}
