/*
* Copyright (c) 2007, Jan Miderbäck
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
*   1. Redistributions of source code must retain the above copyright notice,
*      this list of conditions and the following disclaimer. 
*   2. Redistributions in binary form must reproduce the above copyright notice,
*      this list of conditions and the following disclaimer in the documentation
*      and/or other materials provided with the distribution. 
*   3. The name of the author may not be used to endorse or promote products
*      derived from this software without specific prior written permission. 
* 
* THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR IMPLIED
* WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
* EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
* SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
* OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
* OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
* OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package MICC.chgame;

import java.util.Vector;
import MICC.MICClet;
import MICC.chutil.StringUtil;

public class Offer {
  private int    offerNo;
  private int    direction;
  private int    offerType;
  private String offerInfoStr;
  
  public static final int FROM = 0;
  public static final int TO   = 1;
  
  public static final int ABORT     = 0;
  public static final int DRAW      = 1;
  public static final int TAKEBACK  = 2;
  public static final int PAUSE     = 3;
  public static final int UNPAUSE   = 4;
  public static final int ADJOURN   = 5;
  public static final int SWITCH    = 6;
  public static final int MATCH     = 7;
  public static final int SIMUL     = 8;
  
  public static final int NO_OF_INGAME_OFFERS = 6;
  
  public static final int DECLINE = 0;
  public static final int ACCEPT = 1;
  
  public Offer(String line, int direction) {
    Vector v = new Vector();
    String offerTypeStr;
    int    infoIndex;
    
    this.direction = direction;
    StringUtil.splitString(line, ' ', v);
    offerNo = Integer.parseInt((String)(v.elementAt(1)));
    offerTypeStr = (String)(v.elementAt(3));
    infoIndex = line.indexOf("p=");
    
    // ... p=xxxxxxxxxx ???=yyyyy
    //     ^               ^
    //     infoIndex       nextEqCharIndex
    //
    if (infoIndex != -1) {
      int nextEqCharIndex = line.indexOf(line, infoIndex + 2);
      
      if (nextEqCharIndex != -1) {
        int endIndex = nextEqCharIndex;
        while (line.charAt(endIndex) != ' ') --endIndex;
        offerInfoStr = line.substring(infoIndex + 2, endIndex);
      }
      else offerInfoStr = line.substring(infoIndex + 2);
    }
    
    if (offerTypeStr.equals(ChConst.MATCH_OFFER_KEY))         offerType = MATCH;
    else if (offerTypeStr.equals(ChConst.ABORT_OFFER_KEY))    offerType = ABORT;
    else if (offerTypeStr.equals(ChConst.DRAW_OFFER_KEY))     offerType = DRAW;
    else if (offerTypeStr.equals(ChConst.TAKEBACK_OFFER_KEY)) offerType = TAKEBACK;
    else if (offerTypeStr.equals(ChConst.PAUSE_OFFER_KEY))    offerType = PAUSE;
    else if (offerTypeStr.equals(ChConst.UNPAUSE_OFFER_KEY))  offerType = UNPAUSE;
    else if (offerTypeStr.equals(ChConst.SWITCH_OFFER_KEY))   offerType = SWITCH;
    else if (offerTypeStr.equals(ChConst.SIMUL_OFFER_KEY))    offerType = SIMUL;
    else if (offerTypeStr.equals(ChConst.ADJOURN_OFFER_KEY))  offerType = ADJOURN;
  }
  
  public int getOfferNo() {
    return offerNo;
  }
  
  public int getDirection() {
    return this.direction;
  }
  
  public int getOfferType() {
    return offerType;
  }
  
  public String getOfferInfoStr() {
    return offerInfoStr;
  }
  
  public void accept() {
    MICClet.gameControl.offerResp(this, ACCEPT);
  }
  
  public void decline() {
    MICClet.gameControl.offerResp(this, DECLINE);
  } 
}
