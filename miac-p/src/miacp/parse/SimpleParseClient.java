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

package miacp.parse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import miacp.parse.FullSystemWrapper.FullSystemResult;
import miacp.parse.types.Arc;
import miacp.parse.types.Parse;
import miacp.parse.types.Sentence;
import miacp.parse.types.Token;
import miacp.pos.PosTagger;


public class SimpleParseClient {
	
	private Socket mSocket;
	private ObjectInputStream mInputStream;
	private ObjectOutputStream mOutputStream;
	
	public SimpleParseClient(String address, int portNumber) throws IOException {
		mSocket = new Socket(address, portNumber);
		mInputStream = new ObjectInputStream(mSocket.getInputStream());
		mOutputStream = new ObjectOutputStream(mSocket.getOutputStream());
	}
	
	public SimpleParseServer.ParseResult sendRequest(SimpleParseServer.ParseRequest request) throws IOException, ClassNotFoundException {
		mOutputStream.writeObject(request);
		mOutputStream.flush();
		// Call reset() to avoid memory leak
		mOutputStream.reset();
		
		SimpleParseServer.ParseResult result = (SimpleParseServer.ParseResult)mInputStream.readObject();
		
		return result;
	}
	
	public void closeClient() throws IOException {
		mSocket.close();
	}
	
	public static void main(String[] args) throws Exception {
		int portNumber = Integer.parseInt(args[0]);
		SimpleParseClient client = new SimpleParseClient("127.0.0.1", portNumber);
		 
		try {
			// Silly little stress test
			while(true) {
				Sentence sentence = new Sentence(PosTagger.makeMeSomeTokens("The bear went over the mountain to see what he could see .".split("\\s+")));
				SimpleParseServer.ParseRequest request = null;
				//request = new SimpleParseServer.ParseRequest(sentence);
				request = new SimpleParseServer.ParseRequest(sentence, true, true, true, true, true, true, false);
				SimpleParseServer.ParseResult result = client.sendRequest(request);
				if(result.getException() != null) {
					System.err.println("Blah... something wrong");
					result.getException().printStackTrace();
					break;
				}
				else {
					FullSystemResult fullResult = result.getResult();
					Parse syntacticParse = fullResult.getParse();
					Parse srlLinks = fullResult.getSrlParse();
					Arc[] srlArcs = srlLinks == null ? null : srlLinks.getHeadArcs();
					Sentence sentenceCopy = syntacticParse.getSentence();
					for(Token t : sentenceCopy.getTokens()) {
						Arc arcHead = syntacticParse.getHeadArcs()[t.getIndex()];
						Arc srlArc = srlArcs == null ? null : srlArcs[t.getIndex()];
						System.err.println(t.getIndex() + "\t"
								+ t.getText() 
								+ "\t" + t.getPos() 
								+ "\t" + arcHead.getDependency()
								+ "\t" + arcHead.getHead().getIndex()
								+ "\t" + (t.getLexSense()==null?"_":t.getLexSense())
								+ "\t" + (srlArc==null?"_":srlArc.getSemanticAnnotation())
								+ "\t" + (srlArc==null?"_":srlArc.getHead().getIndex()));
					}
				}
			}
		}
		finally {
			client.closeClient();
		}
	}
	
}