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

public final class ChConst
{
  public static final int BLACK = 0;
  public static final int WHITE = 1;
  
  // Pieces with color.
  public static final int NO_PIECE     = 0;
  public static final int WHITE_PAWN   = 1;
  public static final int WHITE_KNIGHT = 2;
  public static final int WHITE_BISHOP = 3;
  public static final int WHITE_ROOK   = 4;
  public static final int WHITE_QUEEN  = 5;
  public static final int WHITE_KING   = 6;
  public static final int BLACK_PAWN   = 7;
  public static final int BLACK_KNIGHT = 8;
  public static final int BLACK_BISHOP = 9;
  public static final int BLACK_ROOK   = 10;
  public static final int BLACK_QUEEN  = 11;
  public static final int BLACK_KING   = 12;
  public static final int NO_OF_PIECE_KINDS = 12;
  
  // Pieces. No color info.
  public static final int PAWN   = 0;
  public static final int KNIGHT = 1;
  public static final int BISHOP = 2;
  public static final int ROOK   = 3;
  public static final int QUEEN  = 4;
  public static final int KING   = 5;
  
  public static final int RANK_1 = 0;
  public static final int RANK_2 = 1;
  public static final int RANK_3 = 2;
  public static final int RANK_4 = 3;
  public static final int RANK_5 = 4;
  public static final int RANK_6 = 5;
  public static final int RANK_7 = 6;
  public static final int RANK_8 = 7;
  
  public static final int FILE_A = 0;
  public static final int FILE_B = 1;
  public static final int FILE_C = 2;
  public static final int FILE_D = 3;
  public static final int FILE_E = 4;
  public static final int FILE_F = 5;
  public static final int FILE_G = 6;
  public static final int FILE_H = 7;

  public static final int MAX_MATERIAL_COUNT = 39;
  
  //////////////////////////////////////////////////////////////////////////////
  // FICS specific constants
  
  // Server line keys
  public static final String GAME_CREATED_KEY  = "Creating:";
  public static final String GAME_STATE_KEY    = "{Game";
  public static final String STYLE12_KEY       = "<12>";
  public static final String OFFER_KEY         = "<pf>";
  public static final String OFFER_TO_KEY      = "<pt>";
  public static final String OFFER_REMOVED_KEY = "<pr>";
  public static final String GAME_PAUSED_KEY   = "Game clock paused";
  public static final String GAME_UNPAUSED_KEY = "Game clock resumed";
  public static final String SEEK_POSTED_KEY   = "Your seek has been posted";

  public static final String GAME_CREATING_KEY         = "Creating";
  public static final String GAME_CONTINUING_KEY       = "Continuing";
  public static final String GAME_CHECKMATED_KEY       = "checkmated";
  public static final String GAME_RESIGNS_KEY          = "resigns";
  public static final String GAME_DRAWN_KEY            = "Game dr";
  public static final String GAME_ABORTED_KEY          = "Game ab";
  public static final String GAME_ADJOURNED_KEY        = "Game ad";
  public static final String GAME_COURTESY_KEY         = "Game courtesy";
  public static final String GAME_FORFEITS_TIME_KEY    = "forfeits on t";
  public static final String GAME_FORFEITS_DISCONN_KEY = "forfeits by d";
  
  public static final String MATCH_OFFER_KEY    = "t=match";
  public static final String DRAW_OFFER_KEY     = "t=draw";
  public static final String PAUSE_OFFER_KEY    = "t=pause";
  public static final String UNPAUSE_OFFER_KEY  = "t=unpause";
  public static final String ABORT_OFFER_KEY    = "t=abort";
  public static final String TAKEBACK_OFFER_KEY = "t=takeback";
  public static final String SWITCH_OFFER_KEY   = "t=switch";
  public static final String SIMUL_OFFER_KEY    = "t=simul";
  public static final String ADJOURN_OFFER_KEY  = "t=adjourn";
  
  public static final String WHITE_WINS = "1-0";
  public static final String BLACK_WINS = "0-1";
  public static final String DRAW       = "1/2-1/2";
  public static final String NO_RESULT  = "*";
  
  // 
  private ChConst()
  {
  }
}
