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

/**
 * Represents a game played by the user against an opponent.
 */
public class PlayedGame extends Game {
  // The game number given by the server.
  public int gameNo;
  // White player name.
  public String whitePlayerName;
  // Black player name.
  public String blackPlayerName;
  // The color the user plays.
  public int userColor;
  // The color the opponent plays.
  public int opponentColor;
  // The color to move.
  public int colorToMove;
  // Remaning time of white player in milliseconds.
  public int whiteRemainingTime;
  // Remaning time of black player in milliseconds.
  public int blackRemainingTime;
  // The move number of the current move. Before any move has been made it is
  // 0 (zero).
  public int moveNo;
  // The previous move made. (The current move is the one waiting to be made.)
  public Move previousMove;
  // The result of the game. When the game is created and the result is not
  // known, the result is RESULT_ONGOING.
  public int result = RESULT_ONGOING;
  // The board representation of the game represented as an instance of
  // BoardData.
  private BoardData boardData = new BoardData();
          
  public static final int RESULT_ONGOING    = 0;
  public static final int RESULT_WHITE_WINS = 1;
  public static final int RESULT_BLACK_WINS = 2;
  public static final int RESULT_DRAW       = 3;
  public static final int RESULT_NONE       = 4;
  
  public PlayedGame() {
    super();
    gameNo = -1;
    whitePlayerName = "No player";
    blackPlayerName = "No player";
    colorToMove = ChConst.WHITE;
    whiteRemainingTime = 0;  // Milliseconds
    blackRemainingTime = 0;  // Milliseconds
    moveNo = 0;
    previousMove = null;
    result = RESULT_NONE;
  }
  
  public String getPlayerName(int color) {
    if (color == ChConst.WHITE) return whitePlayerName;
    else                        return blackPlayerName;
  }
  
  public int getColorToMove() {
    return colorToMove;
  }
  
  public int getMyColor() {
    return userColor;
  }
  
  public boolean isMyPiece(int file, int rank, int myColor) {
    return boardData.isMyPiece(file, rank, myColor);
  }
  
  public int getMaterialCount(int color) {
    return boardData.getMaterialCount(color);
  }
  
  public int getRemainingTime(int color) {
    if (color == ChConst.WHITE) return whiteRemainingTime;
    else                        return blackRemainingTime;
  }
  
  public void setPiece(int file, int rank, int piece) {
    boardData.setPiece(file, rank, piece);
  }
  
  public int getPiece(int file, int rank) {
    return boardData.getPiece(file, rank);
  }
}
