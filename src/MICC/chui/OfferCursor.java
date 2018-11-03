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

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import MICC.MICClet;
import MICC.chgame.*;

final class OfferCursor implements ICursor {
  private int               cursorPos = 0;
  private final int         xpos;
  private final int         ypos;
  private final int         size;
  private final GameControl gameControl;
  
  private static final int MAX_NO_OF_OFFERS = 6;
  
  public OfferCursor(int xpos, int ypos, int size) {
    this.xpos = xpos;
    this.ypos = ypos;
    this.size = size;
    gameControl = MICClet.gameControl;
  }
  
  public void resetPos() {
    cursorPos = 0;
  }
  
  private void move(int dy) {
    if (gameControl.getState() == GameControl.STATE_PLAYING ||
        gameControl.getState() == GameControl.STATE_PAUSED)  {
      final int newCursorPos = cursorPos + dy;
      if (0 <= newCursorPos && newCursorPos <= MAX_NO_OF_OFFERS - 1) cursorPos = newCursorPos;
    }
  }
  
  public void handleKey(int keyCode) {
    switch (keyCode) {
      case GameCanvas.KEY_NUM2:
        move(-1);
        break;
      
      case GameCanvas.KEY_NUM8:
        move(1);
        break;
        
      case GameCanvas.KEY_NUM0:
      case GameCanvas.KEY_NUM1:
      case GameCanvas.KEY_NUM5:
        Offer offer = (Offer)(gameControl.getOffers().elementAt(cursorPos));
        if (offer != null) {
          if ( keyCode == GameCanvas.KEY_NUM0)
            gameControl.offerResp(offer, Offer.DECLINE);
          else
            gameControl.offerResp(offer, Offer.ACCEPT);
        }
    }
  }
  
  public void paint(Graphics g) {
    final int cursorYPos = ypos + cursorPos * size;
    g.setColor(UIConst.CURSOR_COLOR);
    g.drawRect(xpos - size / 2, cursorYPos, size - 1, size - 1);
  }
  
}
