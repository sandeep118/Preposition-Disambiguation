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

package miacp.featgen.wfr;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import miacp.parse.types.Parse;
import miacp.parse.types.Token;
import miacp.util.TreebankConstants;


/**
 * Heuristic word-finding rule for identifying the likely head of a prepositional phrase.
 *
 */
public class PrepositionComplementHeuristic extends AbstractWordFindingRule {
	
	private static final long serialVersionUID = 1L;

	@Override
	public Set<Token> getProductions(List<Token> tokenList, Parse parse, int headIndex) {
		Set<Token> results = new HashSet<Token>();
		int numTokens = tokenList.size();
		
		Token prevNoun = null;
		for(int i = headIndex+1; i < numTokens; i++) {
			Token tok = tokenList.get(i);
			String pos = tok.getPos();
			String text = tok.getText().toLowerCase();
			if(prevNoun != null) {
				if(pos.equals(TreebankConstants.POSSESSIVE_ENDING)) {
					prevNoun = null;
				}
				else if(pos.startsWith("NN") || (!prevNoun.getPos().startsWith("NN") // maybe we started with a DT
						&& pos.matches("JJ(R|S)?|RB.*"))) { //|NN.*|CD")) {
					prevNoun = tok;
				}
				else {
					results.add(prevNoun);
					break;
				}
			}
			else {
				if(pos.equals("VBG")||
				   pos.equals("WRB")||
				   pos.equals("PRP")||
				   pos.equals("WP")) {
					results.add(tok);
					break;
				}
				// added all, some, each, another
				else if(text.matches("this|these|those|that|all|some|few|each|another|most|more|less") ||
						(TreebankConstants.NOUN_LABELS.contains(pos))) {
					prevNoun = tok;
				}
			}
		}
		
		return results;
	}
	
}
