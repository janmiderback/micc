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
import MICC.MICClet;
import MICC.chgame.*;
import javax.microedition.lcdui.game.GameCanvas;

public class LastMove {
  private final int squareSize;
  private final int xpos;
  private final int ypos;
  
  public LastMove(int squareSize, int xpos, int ypos) {
    this.squareSize = squareSize;
    this.xpos = xpos;
    this.ypos = ypos;
  }
  
  public void paint(Graphics g) {
    PlayedGame game = MICClet.gameControl.getPlayedGame();
    Move lastMove = game.previousMove;
            
    if (lastMove != null) {
      int fromFile;
      int fromRank;
      int toFile;
      int toRank;
      int moveKind = lastMove.getMoveKind();
      
      if (moveKind == Move.MOVE_NORMAL) {
        fromFile = lastMove.getFromFile();
        fromRank = lastMove.getFromRank();
        toFile = lastMove.getToFile();
        toRank = lastMove.getToRank();
      } else {
        // Which king castled?
        if (game.getColorToMove() == ChConst.BLACK) {
          // White castled
          fromFile = ChConst.FILE_E;
          fromRank = ChConst.RANK_1;
          if (game.getPiece(ChConst.FILE_G, ChConst.RANK_1) == ChConst.WHITE_KING) {
            toFile = ChConst.FILE_G;
            toRank = ChConst.RANK_1;
          } else {
            // Must have castled long.
            toFile = ChConst.FILE_C;
            toRank = ChConst.RANK_1;
          }
        } else {
          // Black castled
          fromFile = ChConst.FILE_E;
          fromRank = ChConst.RANK_8;
          if (game.getPiece(ChConst.FILE_G, ChConst.RANK_8) == ChConst.BLACK_KING) {
            toFile = ChConst.FILE_G;
            toRank = ChConst.RANK_8;
          } else {
            // Must have castled long.
            toFile = ChConst.FILE_C;
            toRank = ChConst.RANK_8;
          }
        }
      }

      g.setColor(UIConst.LAST_MOVE_COLOR);
      paintSquare(g, fromFile, fromRank);
      paintSquare(g, toFile, toRank);
    }
  }
    
  private void paintSquare(Graphics g, int file, int rank) {
    int xpos;
    int ypos;
    final PlayedGame game = MICClet.gameControl.getPlayedGame();
    
    if (game.getMyColor() == ChConst.WHITE) {
        xpos = this.squareSize * file;
        ypos = this.squareSize * (ChConst.RANK_8 - rank);
    } else {
      xpos = this.squareSize * (ChConst.FILE_H - file);
      ypos = this.squareSize * rank;
    }
    xpos += this.xpos;
    ypos += this.ypos;
    
    g.drawRect(xpos, ypos, this.squareSize - 1, this.squareSize - 1);
  }
  
}
