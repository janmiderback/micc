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

import MICC.chutil.*;

public class BoardData {
  // First index is file (col), second is rank (row)
  int m_squares[][] = new int[8][8];
  
  public BoardData() {
    setupDefault();
  }
  
  public void setupDefault() {
    int file;
    int rank;
    
    // Initialize to "no piece".
    clear();
    
    // Initialize the board with the normal setup.
    // White pieces
    setPiece(ChConst.FILE_A, ChConst.RANK_1, ChConst.WHITE_ROOK);
    setPiece(ChConst.FILE_B, ChConst.RANK_1, ChConst.WHITE_KNIGHT);
    setPiece(ChConst.FILE_C, ChConst.RANK_1, ChConst.WHITE_BISHOP);
    setPiece(ChConst.FILE_D, ChConst.RANK_1, ChConst.WHITE_QUEEN);
    setPiece(ChConst.FILE_E, ChConst.RANK_1, ChConst.WHITE_KING);
    setPiece(ChConst.FILE_F, ChConst.RANK_1, ChConst.WHITE_BISHOP);
    setPiece(ChConst.FILE_G, ChConst.RANK_1, ChConst.WHITE_KNIGHT);
    setPiece(ChConst.FILE_H, ChConst.RANK_1, ChConst.WHITE_ROOK);
    // White pawns
    for (file = ChConst.FILE_A; file <= ChConst.FILE_H; ++file) {
      setPiece(file, ChConst.RANK_2, ChConst.WHITE_PAWN);
    }
    // Black pieces
    setPiece(ChConst.FILE_A, ChConst.RANK_8, ChConst.BLACK_ROOK);
    setPiece(ChConst.FILE_B, ChConst.RANK_8, ChConst.BLACK_KNIGHT);
    setPiece(ChConst.FILE_C, ChConst.RANK_8, ChConst.BLACK_BISHOP);
    setPiece(ChConst.FILE_D, ChConst.RANK_8, ChConst.BLACK_QUEEN);
    setPiece(ChConst.FILE_E, ChConst.RANK_8, ChConst.BLACK_KING);
    setPiece(ChConst.FILE_F, ChConst.RANK_8, ChConst.BLACK_BISHOP);
    setPiece(ChConst.FILE_G, ChConst.RANK_8, ChConst.BLACK_KNIGHT);
    setPiece(ChConst.FILE_H, ChConst.RANK_8, ChConst.BLACK_ROOK);
    // Black pawns
    for (file = ChConst.FILE_A; file <= ChConst.FILE_H; ++file) {
      setPiece(file, ChConst.RANK_7, ChConst.BLACK_PAWN);
    }
  }
  
  public void clear() {
    int rank;
    int file;
    
    for (file = ChConst.FILE_A; file <= ChConst.FILE_H; ++file) {
      for (rank = ChConst.RANK_1; rank <= ChConst.RANK_8; ++rank) {
        setPiece(file, rank, ChConst.NO_PIECE);
      }
    }
  }
  
  public void setPiece(int file, int rank, int piece) {
    m_squares[file][rank] = piece;
  }

  public int getPiece(int file, int rank) {
    return m_squares[file][rank];
  }
  
  public boolean isMyPiece(int file, int rank, int myColor) {
    int      piece    = getPiece(file, rank);
    boolean  myPiece  = false;
    
    if (myColor == ChConst.WHITE) myPiece = ChConst.WHITE_PAWN <= piece && piece <= ChConst.WHITE_KING;
    else                          myPiece = ChConst.BLACK_PAWN <= piece && piece <= ChConst.BLACK_KING;    
    return myPiece;
  }
  
  public int getMaterialCount(int color) {
    int file;
    int rank;
    int retVal = 0;
    
    for (file = ChConst.FILE_A; file <= ChConst.FILE_H; ++file) {
      for (rank = ChConst.RANK_1; rank <= ChConst.RANK_8; ++rank) {
        int piece = getPiece(file, rank);
        if (color == ChConst.WHITE) {
          switch (piece) {
            case ChConst.WHITE_PAWN:
              retVal += 1;
              break;
            case ChConst.WHITE_KNIGHT:
            case ChConst.WHITE_BISHOP:
              retVal += 3;
              break;
            case ChConst.WHITE_ROOK:
              retVal += 5;
              break;
            case ChConst.WHITE_QUEEN:
              retVal += 9;
              break;
            default:
              break;
          }
        } else {
          switch (piece) {
            case ChConst.BLACK_PAWN:
              retVal += 1;
              break;
            case ChConst.BLACK_KNIGHT:
            case ChConst.BLACK_BISHOP:
              retVal += 3;
              break;
            case ChConst.BLACK_ROOK:
              retVal += 5;
              break;
            case ChConst.BLACK_QUEEN:
              retVal += 9;
              break;
            default:
              break;
          }
        }
      }
    }
    
    return Math.min(retVal, ChConst.MAX_MATERIAL_COUNT);
  }
}

