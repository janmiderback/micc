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

import java.util.Vector;
import javax.microedition.lcdui.*;
import MICC.MICClet;
import MICC.chgame.*;

public class OfferList {
  private final int    xpos;
  private final int    ypos;
  private final int    imageSize;
  private final Vector offers;
  
  public OfferList(int xpos, int ypos, int imageSize) {
    this.xpos = xpos;
    this.ypos = ypos;
    this.imageSize = imageSize;
    offers = MICClet.gameControl.getOffers();
  }
  
  public void paint(Graphics g) {
    int i;
    int imageYPos = ypos;
    
    for (i = 0; i < offers.size(); ++i) {
      int offerType = ((Offer)(offers.elementAt(i))).getOfferType();
      if (0 <= offerType && offerType <= Offer.NO_OF_INGAME_OFFERS - 1) {
        g.drawImage(MiccImages.m_offerImages[offerType], xpos, imageYPos, Graphics.HCENTER | Graphics.TOP);
        imageYPos += imageSize + 1;
      }
    }
  }
}
