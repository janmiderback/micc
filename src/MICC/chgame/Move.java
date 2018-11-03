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

public class Move {
  private int m_fromFile;
  private int m_fromRank;
  private int m_toFile;
  private int m_toRank;
  private int m_piece;
  private int m_moveKind;
  
  public static final int MOVE_NORMAL       = 0;
  public static final int MOVE_CASTLE_SHORT = 1;
  public static final int MOVE_CASTLE_LONG  = 2;

  public Move(int fromFile,
              int fromRank,
              int toFile, 
              int toRank,
              int piece) {
    m_fromFile = fromFile;
    m_fromRank = fromRank;
    m_toFile = toFile;
    m_toRank = toRank;
    m_piece = piece;
    m_moveKind = MOVE_NORMAL;
  }
  
  public Move(int castle) {
    m_moveKind = castle;
  }
  
  public Move(String moveStr) {
    m_fromFile = moveStr.charAt(0) - 'a';
    m_fromRank = moveStr.charAt(1) - '1';
    m_toFile = moveStr.charAt(3) - 'a';
    m_toRank = moveStr.charAt(4) - '1';
    m_moveKind = MOVE_NORMAL;
  }
  
  public void setPiece(int piece) {
    m_piece = piece;
  }
  
  public int getMoveKind() { return m_moveKind; }
  public int getFromFile() { return m_fromFile; }
  public int getFromRank() { return m_fromRank; }
  public int getToFile()   { return m_toFile;   }
  public int getToRank()   { return m_toRank;   }
  public boolean isPromotion() {
    if (m_piece == ChConst.WHITE_PAWN && m_toRank == ChConst.RANK_8) return true;
    if (m_piece == ChConst.BLACK_PAWN && m_toRank == ChConst.RANK_1) return true;
    return false;
  }
  
  public String toString() {
    StringBuffer sb = new StringBuffer();
    if (m_moveKind == MOVE_NORMAL) {
      sb.append((char)('a' + m_fromFile));
      sb.append((char)('1' + m_fromRank));
      sb.append('-');
      sb.append((char)('a' + m_toFile));
      sb.append((char)('1' + m_toRank));
    } else if (m_moveKind == MOVE_CASTLE_SHORT) sb.append("o-o");
    else if (m_moveKind == MOVE_CASTLE_LONG) sb.append("o-o-o");
    return sb.toString();
  }
}
