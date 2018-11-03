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

import MICC.MICClet;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import MICC.chgame.*;

// The chess board graphical object.
final public class Board {
  // Horizontal pixel position of board left edge.
  private final int xpos;
  // Vertical pixel position of board top edge.
  private final int ypos;
  // Size of board square in pixels.
  private final int squareSize;
  // Size of board in pixels.
  private final int boardSize;
 
  private BoardCursor boardCursor;
  
  private static final int BLACK_SQUARE_COL = 0xb0b0ff;  // Light blue
  private static final int WHITE_SQUARE_COL = 0xf0f0f0;  // Light gray

  Board(int squareSize, int xpos, int ypos) {
    this.xpos = xpos;
    this.ypos = ypos;
    this.squareSize = squareSize;
    boardSize = 8 * this.squareSize;
  }

  public int getBoardSize() {
    return boardSize;
  }
  
  public void setBoardCursor(BoardCursor boardCursor) {
    this.boardCursor = boardCursor;
  }
  
  public void paintSquares(Graphics g) {
    int rank;
    int file;
    int xpos;
    int ypos;
    
    // Draw the squares
    for (rank = ChConst.RANK_1; rank <= ChConst.RANK_8; ++rank) {
      for (file = ChConst.FILE_A; file <= ChConst.FILE_H; ++file) {
        xpos = this.squareSize * file + this.xpos;;
        ypos = this.squareSize * (ChConst.RANK_8 - rank) + this.ypos;
        
        if (((file + rank) % 2) == 0) g.setColor(BLACK_SQUARE_COL);
        else g.setColor(WHITE_SQUARE_COL);
        
        g.fillRect(xpos, ypos, this.squareSize, this.squareSize);
      }
    }
  }
  
  public void paintPieces(Graphics g, PlayedGame game) {
    int file;
    int rank;
    int xpos;
    int ypos;
    int piece;
    final int cursorFromFile = this.boardCursor.getFromFile();
    final int cursorFromRank = this.boardCursor.getFromRank();
    final boolean isWhiteDown = game.getMyColor() == ChConst.WHITE;
    
    for (rank = ChConst.RANK_1; rank <= ChConst.RANK_8; ++rank) {
      for (file = ChConst.FILE_A; file <= ChConst.FILE_H; ++file) {
        if ((cursorFromFile == -1 && cursorFromRank == -1) ||
            (cursorFromFile != file || cursorFromRank != rank))
        {
          if (isWhiteDown) {
            xpos = this.squareSize * file;
            ypos = this.squareSize * (ChConst.RANK_8 - rank);
          }
          else {
            xpos = this.squareSize * (ChConst.FILE_H - file);
            ypos = this.squareSize * rank;
          }
          xpos += this.xpos;
          ypos += this.ypos;
          piece = game.getPiece(file, rank);
          if (MiccImages.m_pieceImages[piece] != null) {
            g.drawImage(MiccImages.m_pieceImages[piece], xpos, ypos, Graphics.TOP | Graphics.LEFT);
          }
        }
      }
    }
  }
}
