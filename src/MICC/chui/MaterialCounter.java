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
import MICC.MICClet;
import MICC.chgame.*;

public class MaterialCounter {
  private final int barWidth;
  private final int barXPos;
  private final int upperBarTopYPos;
  private final int upperBarBottomYPos;
  private final int lowerBarTopYPos;
  private final int lowerBarBottomYPos;
  
  public MaterialCounter(int xpos, int ypos, int width, int height) {
    barWidth = width / 2;
    barXPos = xpos;
    upperBarTopYPos = ypos + 3;
    upperBarBottomYPos = ypos + (height / 2) - 1;
    lowerBarTopYPos = ypos + (height / 2) + 3;
    lowerBarBottomYPos = ypos + height - 1;
  }
  
  public void paint(Graphics g) {
    final PlayedGame game = MICClet.gameControl.getPlayedGame();
    final int whiteMaterialCount = game.getMaterialCount(ChConst.WHITE);
    final int blackMaterialCount = game.getMaterialCount(ChConst.BLACK);
    final boolean whiteDown = game.getMyColor() == ChConst.WHITE;
    
    paintBar(g, upperBarTopYPos, upperBarBottomYPos, whiteDown ? blackMaterialCount : whiteMaterialCount);
    paintBar(g, lowerBarTopYPos, lowerBarBottomYPos, whiteDown ? whiteMaterialCount : blackMaterialCount);
  }
  
  private void paintBar(Graphics g, int barTopYPos, int barBottomYPos, int materialCount) {
    final int x1        = barXPos + barWidth - 1;
    final int x2        = barXPos + barWidth + 1;
    final int barLevel  = (100 * materialCount) / ChConst.MAX_MATERIAL_COUNT;
    final int startYPos = barTopYPos + ((100 - barLevel) * (barBottomYPos - barTopYPos)) / 100;
    int       ypos;
    
    for (ypos = startYPos; ypos <= barBottomYPos; ++ypos) {
      int greenLevel = (100 * (ypos - barTopYPos)) / (barBottomYPos - barTopYPos);  // %
      int redLevel   = 100 - greenLevel;
      g.setColor((redLevel * 0xff) / 100, (greenLevel * 0xff) / 100, 0x00);
      g.drawLine(barXPos, ypos, x1, ypos);
    }
    
    g.setColor(UIConst.GREY_COLOR);
    g.drawLine(barXPos, barTopYPos, x2, barTopYPos);
  }
}
