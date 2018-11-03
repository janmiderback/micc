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

class BoardCursor implements ICursor {
  private final int squareSize;
  private final int xpos;
  private final int ypos;
  public int file = ChConst.FILE_E;
  public int rank = ChConst.RANK_4;
  public int selectedPiece = ChConst.NO_PIECE;
  private int fromFile = -1;
  private int fromRank = -1;
  
  public BoardCursor(int squareSize, int xpos, int ypos) {
    this.squareSize = squareSize;
    this.xpos = xpos;
    this.ypos = ypos;
  }
  
  private void move(int dFile, int dRank) {
    int newFilePos;
    int newRankPos;
    PlayedGame game = MICClet.gameControl.getPlayedGame();
    final int colorToMove = game.getColorToMove();
    final int myColor = game.getMyColor();
    
    if (MICClet.gameControl.getState() != GameControl.STATE_PLAYING ||
        colorToMove != myColor) return;
    
    if (myColor == ChConst.WHITE) {
      newFilePos = file + dFile;
      newRankPos = rank + dRank;
    } else {
      newFilePos = file - dFile;
      newRankPos = rank - dRank;
    }
    
    if (ChConst.FILE_A <= newFilePos && newFilePos <= ChConst.FILE_H &&
        ChConst.RANK_1 <= newRankPos && newRankPos <= ChConst.RANK_8) {
      file = newFilePos;
      rank = newRankPos;
    }
  }
  
  private void select() {
    PlayedGame game = MICClet.gameControl.getPlayedGame();
    
    if (selectedPiece == ChConst.NO_PIECE && 
        game.isMyPiece(file, rank, game.getMyColor())) {
      selectedPiece = game.getPiece(file, rank);
      fromFile = file;
      fromRank = rank;
    }
    else {
      game.setPiece(fromFile, fromRank, ChConst.NO_PIECE);
      game.setPiece(file, rank, selectedPiece);
      MICClet.gameControl.move(new Move(fromFile, fromRank, file, rank, selectedPiece));
      selectedPiece = ChConst.NO_PIECE;
      fromFile = -1;
      fromRank = -1;
    }
  }
  
  public void paint(Graphics g) {
    int xpos;
    int ypos;
    PlayedGame game = MICClet.gameControl.getPlayedGame();
    
    if (game.getColorToMove() != game.getMyColor()) return;
    
    if (game.getMyColor() == ChConst.WHITE) {
      xpos = this.squareSize * file;
      ypos = this.squareSize * (ChConst.RANK_8 - rank);
    } else {
      xpos = this.squareSize * (ChConst.FILE_H - file);
      ypos = this.squareSize * rank;
    }
    xpos += this.xpos;
    ypos += this.ypos;

    g.setColor(UIConst.CURSOR_COLOR);
    if (selectedPiece == ChConst.NO_PIECE) {
      int x1 = xpos;
      int y1 = ypos;
      int x2 = xpos + this.squareSize;
      int y2 = ypos;
      int x3 = xpos;
      int y3 = ypos + this.squareSize;
      int x4 = xpos + this.squareSize;
      int y4 = ypos + this.squareSize;

      g.fillTriangle(x1,     y1,
                     x1 + 4, y1,
                     x1,     y1 + 4);
      g.fillTriangle(x2,     y2,
                     x2 - 4, y2,
                     x2,     y2 + 4);
      g.fillTriangle(x3,     y3,
                     x3 + 5, y3,
                     x3,     y3 - 5);
      g.fillTriangle(x4,     y4,
                     x4 - 5, y4,
                     x4,     y4 - 5);
    } else {
      g.fillRect(xpos, ypos, this.squareSize, this.squareSize);
      // Piece is now part of cursor
      if (MiccImages.m_pieceImages[selectedPiece] != null) {
        g.drawImage(MiccImages.m_pieceImages[selectedPiece], xpos, ypos, Graphics.TOP | Graphics.LEFT);
      }
    }
  }
  
  public void handleKey(int keyCode) {
    PlayedGame game = MICClet.gameControl.getPlayedGame();
    
    if (game.getColorToMove() == game.getMyColor()) {
      switch (keyCode) {
        case GameCanvas.KEY_NUM1:
          move(-1, 1);
          break;

        case GameCanvas.KEY_NUM2:
          move(0, 1);
          break;

        case GameCanvas.KEY_NUM3:
          move(1, 1);
          break;

        case GameCanvas.KEY_NUM4:
          move(-1, 0);
          break;

        case GameCanvas.KEY_NUM5:
          select();
          break;

        case GameCanvas.KEY_NUM6:
          move(1, 0);
          break;

        case GameCanvas.KEY_NUM7:
          move(-1, -1);
          break;

        case GameCanvas.KEY_NUM8:
          move(0, -1);
          break;

        case GameCanvas.KEY_NUM9:
          move(1, -1);
          break;

        default:
          break;

      }
    }
  }
  
  public int getFromFile() {
    return fromFile;
  }
  
  public int getFromRank() {
    return fromRank;
  }
}
