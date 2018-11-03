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

package MICC.chui;

import javax.microedition.lcdui.*;
import MICC.chgame.Offer;

public class OfferForm extends Form
{
  private Offer            m_offer;
  public Command           m_declineCommand  = new Command("Decline", Command.CANCEL, 1);
  public Command           m_acceptCommand   = new Command("Accept", Command.OK, 1);  
  private StringItem       m_siOffer;
  
//  private static final String ABORT_REQ_STR        = "Abort requested";
//  private static final String DRAW_OFFER_STR       = "Draw offered";
  private static final String CHALLENGE_STR        = "Challenge";
//  private static final String PAUSE_REQ_STR        = "Pause requested";
//  private static final String UNPAUSE_REQ_STR      = "Unpause requested";
  private static final String SIMUL_CHALLENGE_STR  = "Simul challenge";
//  private static final String SWITCH_REQ_STR       = "Switch requested";
//  private static final String TAKEBACK_REQ_STR     = "Takeback requested";
//  private static final String ADJOURN_REQ_STR      = "Adjourn requested";
  
  public OfferForm() {
    super("Caissa - Challenge");

    m_siOffer = new StringItem(null, "");
    append(m_siOffer);
    addCommand(m_declineCommand);
    addCommand(m_acceptCommand);
  }
  
  public void setOffer(Offer offer) {
    String offerStr;
    
    m_offer = offer;
    
    switch (m_offer.getOfferType()) {
//      case Offer.ABORT:
//        offerStr = ABORT_REQ_STR;
//        break;
//      case Offer.DRAW:
//        offerStr = DRAW_OFFER_STR;
//        break;
      case Offer.MATCH: 
        offerStr = CHALLENGE_STR + "\n\n" + m_offer.getOfferInfoStr();
        break;
//      case Offer.PAUSE:
//        offerStr = PAUSE_REQ_STR;
//        break;
//      case Offer.UNPAUSE:
//        offerStr = UNPAUSE_REQ_STR;
//        break;  
      case Offer.SIMUL:
        offerStr = SIMUL_CHALLENGE_STR + "\n\n" + m_offer.getOfferInfoStr();
        break;
//      case Offer.SWITCH:
//        offerStr = SWITCH_REQ_STR;
//        break;
//      case Offer.TAKEBACK:
//        offerStr = TAKEBACK_REQ_STR + "\n\n" + "Moves: " + m_offer.getOfferInfoStr();
//        break;
//      case Offer.ADJOURN:
//        offerStr = ADJOURN_REQ_STR;
//        break;
      default:
        offerStr = "Unknown challenge!" + "\n\n" + m_offer.getOfferInfoStr();
        break;
    }
    m_siOffer.setText(offerStr);
  }
  
  public Offer getOffer() {
    return m_offer;
  }
}
