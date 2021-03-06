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

package miacp.parse.ml;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import miacp.parse.types.Arc;
import miacp.parse.types.Token;
import miacp.types.IntArrayList;


/**
 * Defines a parsing model interface. 
 *
 */
public interface ParseModel extends Serializable {
	
	public int getIndex(String feat, boolean add);
	public void incrementCount();
	
	/**
	 * Returns the list of possible parse actions
	 */
	public List<String> getActions();
	
	/**
	 * Returns the list of possible parse actions for a pair of adjacent tokens
	 * @param tc - the token on the left
	 * @param tr - the token on the right (may be null)
	 * @param tokenToHead - (optional) gold standard arcs (only used in training) 
	 * @return
	 */
	public List<String> getActions(Token tc, Token tr, Arc[] tokenToHead);
	
	/**
	 * Adds a possible action between tokens with the given parts-of-speech
	 * @param pos1 - part-of-speech for left token
	 * @param pos2 - part-of-speech for right token
	 * @param action - the action that should exist between tokens with such part-of-speech tags
	 */
	public void addAction(String pos1, String pos2, String action);
	
	/**
	 * Returns the index of the parse action
	 * @param action - the parse action String
	 * @param addIfNecessary - adds the parse action to the internal list of actions if it isn't present
	 * @return
	 */
	public int getActionIndex(String action, boolean addIfNecessary);
	
	/**
	 * Updates the weight of a single feature
	 * @param actionIndex - indicating the action that is to be affected
	 * @param feat - indicates the feature whose value will be changing
	 * @param change - indicates the change that will be added to the value
	 */
	public void updateFeature(int actionIndex, int feat, double change);
	
	/**
	 * Updates the weights of multiple features
	 * @param action - the action that is to be affected
	 * @param feats - a collection of features whose values will be changing
	 * @param change - indidcates the change that will be added to each feature value
	 */
	public void update(String action, Collection<String> feats, double change); 
	
	/**
	 * Scoring function... may get refactored out of here. Uses non-averaged perceptron weights
	 * @param action - the actions being scored
	 * @param feats - the features of the instance between scored
	 * @param actionIndices - the indices of the actions being scored
	 * @param scores - a holder for the calculated scores
	 */
	public void scoreIntermediate(List<String> action, IntArrayList feats, int[] actionIndices, double[] scores);
	
	/**
	 * Scoring function.
	 * @param action - the actions being scored
	 * @param feats - the features of the instance between scored
	 * @param actionIndices - the indices of the actions being scored
	 * @param scores - a holder for the calculated scores
	 */
	public void score(List<String> action, IntArrayList feats, int[] actionIndices, double[] scores);
	
}