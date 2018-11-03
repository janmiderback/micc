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


public class BoardInfo { 
  private final int ypos;
  private final int width;
  private final int mode;
  private       int playerColor  = ChConst.WHITE;
  private       int displayState = STATE_PLAYER;

  public static final int MODE_PLAYER = 0;
  public static final int MODE_PLAYER_AND_SEEK = 1;
  
  // State where player name, time etc is displayed.
  private static final int STATE_PLAYER = 0;
  // State where a posted seek is displayed.
  private static final int STATE_SEEK = 0;
  
  public BoardInfo(int ypos, int width, int mode) {
    this.ypos = ypos;
    this.width = width;
    this.mode = mode;
  }
  
  public void setPlayerColor(int color) {
    playerColor = color;
  }
  
  public void paint(Graphics g) {
    final int state = MICClet.gameControl.getState();
    final PlayedGame game = MICClet.gameControl.getPlayedGame();
    final boolean toMove = (playerColor == game.getColorToMove());
    final Seek seek = MICClet.gameControl.getActiveSeek();
    
    String  playerNameStr = "";
    String  remainingTimeStr = "";
 
    if (this.mode == MODE_PLAYER_AND_SEEK && seek != null) {
      // Active seek
      BoardFont.drawString(g, fmtSeek(seek), 0, this.ypos, BoardFont.LEFT);
    } else {
      // Player name
      switch (state) {
        case GameControl.STATE_IDLE:
        case GameControl.STATE_PLAYING:
        case GameControl.STATE_PAUSED:
          playerNameStr = game.getPlayerName(playerColor);
          break;  
      }
      BoardFont.drawString(g, playerNameStr, 0, this.ypos, BoardFont.LEFT);

      // Remaining time
      remainingTimeStr = fmtTime(game.getRemainingTime(playerColor) / 1000);
      BoardFont.drawString(g, remainingTimeStr, this.width - 1, this.ypos, BoardFont.RIGHT);
    }
  }
  
  private String fmtTime(int seconds) {
    int minutes = seconds / 60;
    int seconds2 = seconds % 60;
    String retVal = "";
    final boolean negative = seconds < 0;
    
    if (negative) seconds = Math.abs(seconds);
    minutes = seconds / 60;
    seconds2 = seconds % 60;
    
    if (negative) retVal = "-";
    retVal += String.valueOf(minutes) + ":";
    if (seconds2 <= 9) retVal += "0";
    retVal += String.valueOf(seconds2);
    return retVal;
  }
  
  private String fmtSeek(Seek seek) {
    StringBuffer sb = new StringBuffer("Seek ");
    String       s;
    
    sb.append(Integer.toString(seek.time) + " ");
    sb.append(Integer.toString(seek.inc) + " ");
    sb.append(seek.rated ? "r " : "u ");
    if (seek.color == Seek.AUTO) s = "auto ";
    else if (seek.color == Seek.WHITE) s = "white ";
    else s = "black ";
    if (seek.manual) sb.append("m");
    if (seek.formula) sb.append("f ");
    else sb.append(" ");
    if (seek.ratingLower != -1 && seek.ratingUpper != -1) {
      sb.append(Integer.toString(seek.ratingLower) + "-" + Integer.toString(seek.ratingUpper));
    }
    
    return sb.toString();
  }
}
