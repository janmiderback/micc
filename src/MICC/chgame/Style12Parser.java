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
import MICC.chutil.*;

// Style 12 board parser. Style 12 is a computer friendly format for a board
// representation used by the FICS server.
// Check out FICS documentation for details.
public final class Style12Parser {
  private static final int COLOR_TO_MOVE_IDX        = 9;
  private static final int MY_RELATION_TO_GAME_IDX  = 19;
  private static final int INITIAL_TIME_IN_SEC_IDX  = 20;
  private static final int INCREMENT_IN_SEC_IDX     = 21;
  private static final int WHITE_REMAINING_TIME_IDX = 24;
  private static final int BLACK_REMAINING_TIME_IDX = 25;
  private static final int MOVE_NUMBER_IDX          = 26;
  private static final int PREVIOUS_MOVE_IDX        = 27;
  
  private static final String CASTLE_SHORT_STR = "o-o";
  private static final String CASTLE_LONG_STR  = "o-o-o";
  
  public Style12Parser() {
  }
  
  public void parse(String s, PlayedGame game) {
    Vector v = new Vector();
    int piece;
    int rank;
    Move prevMove;
    String prevMoveStr;
        
    StringUtil.splitString(s, ' ', v);
    
    // Skip "<12> " (v[0])
    
    // Color to move
    String toMoveStr = (String)(v.elementAt(COLOR_TO_MOVE_IDX));
    
    if (toMoveStr.equals("W")) game.colorToMove = ChConst.WHITE;
    else                       game.colorToMove = ChConst.BLACK;
    
    // Parse ranks (board)
    for (rank = ChConst.RANK_1; rank <= ChConst.RANK_8; ++rank) {
      int    file;
      String rankStr = (String)(v.elementAt(ChConst.RANK_8 - rank + 1));
      
      for (file = ChConst.FILE_A; file <= ChConst.FILE_H; ++file) {
        piece = ChConst.NO_PIECE;
        char pieceChar = rankStr.charAt(file);
        
        switch (pieceChar) {
          case 'p': piece = ChConst.BLACK_PAWN; break;
          case 'P': piece = ChConst.WHITE_PAWN; break;
          case 'r': piece = ChConst.BLACK_ROOK; break;
          case 'R': piece = ChConst.WHITE_ROOK; break;
          case 'n': piece = ChConst.BLACK_KNIGHT; break;
          case 'N': piece = ChConst.WHITE_KNIGHT; break;
          case 'b': piece = ChConst.BLACK_BISHOP; break;
          case 'B': piece = ChConst.WHITE_BISHOP; break;
          case 'q': piece = ChConst.BLACK_QUEEN; break;
          case 'Q': piece = ChConst.WHITE_QUEEN; break;
          case 'k': piece = ChConst.BLACK_KING; break;
          case 'K': piece = ChConst.WHITE_KING; break;
          case '-':
          default: piece = ChConst.NO_PIECE; break;
        }
        game.setPiece(file, rank, piece);
      }
    }
    
    game.whiteRemainingTime = 1000 * Integer.parseInt((String)(v.elementAt(WHITE_REMAINING_TIME_IDX)));
    game.blackRemainingTime = 1000 * Integer.parseInt((String)(v.elementAt(BLACK_REMAINING_TIME_IDX)));
    game.moveNo = Integer.parseInt((String)(v.elementAt(MOVE_NUMBER_IDX)));
    prevMoveStr = (String)(v.elementAt(PREVIOUS_MOVE_IDX));
    prevMove = parsePrevMove(prevMoveStr);
    if (prevMove != null) game.previousMove = prevMove;
  }
  
  private Move parsePrevMove(String moveStr) {
    Move retVal = null;
//#mdebug
    System.out.println("Move: " + moveStr);
//#enddebug
    if (moveStr.equals(CASTLE_SHORT_STR))     retVal = new Move(Move.MOVE_CASTLE_SHORT);
    else if (moveStr.equals(CASTLE_LONG_STR)) retVal = new Move(Move.MOVE_CASTLE_LONG);
    else if (!moveStr.equals("none")) retVal = new Move(moveStr.substring(2));
    return retVal;
  }
}
