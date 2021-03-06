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

package miacp.parse.train;

import java.util.List;

import miacp.parse.ParseAction;
import miacp.parse.types.Arc;
import miacp.parse.types.Token;
import miacp.parse.types.TokenPointer;


/**
 * Used for indicating a penalty for an action proposed during training.
 *
 */
public interface PenaltyFunction {
	
	/**
	 * 
	 * @param ptr - <code>TokenPointer</code> indicating the token at position L0
	 * @param action - <code>ParseAction</code> to be evaluated
	 * @param tokenToHead - parent <code>Arc</code>s (<code>Arc</code> at index 1 is from the
	 * 						<code>Token</code> at index 1 to its syntactic governor.
	 * @param goldParseIsIncomplete
	 * @param gold - array of <code>List&gt;Arc&lt;</code> indicating the <code>Arc</code>s descending 
	 * 				 from each token in the gold parse tree 
	 * @param current - similar to gold; holds the currently created arcs
	 * @param tokenToSubcomponentHead - mapping to the projective subcomponent heads (may be useful for swapping)
	 * @param projectiveIndices - index of the <code>Token</code>s after 
	 * 						    projectivization (order of visitation in depth-first-search)
	 * @return - If the action is illegal, a positive value should be returned.
	 */
	public double calculatePenalty(TokenPointer ptr, 
            ParseAction action, 
            boolean goldParseIsIncomplete,
            Arc[] tokenToHead, 
            List[] gold, 
            List[] current, 
            Token[] tokenToSubcomponentHead, 
            int[] projectiveIndices);
	
}