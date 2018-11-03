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

import java.io.IOException;
import javax.microedition.lcdui.Image;
import MICC.chgame.*;

public final class MiccImages {
  public static Image m_pieceImages[] = new Image[ChConst.NO_OF_PIECE_KINDS + 1];
  public static Image m_offerImages[] = new Image[Offer.NO_OF_INGAME_OFFERS];
  
  private static final int SMALL_OFFERICON_SIZE = 8;
  private static final int MID_OFFERICON_SIZE   = 10;
  
  private MiccImages() {
  }
  
  static boolean loadImages(int squareSize,
                            int rightMargin) {
    boolean retVal = false;
        
    try {
      // Load the pieces images
      if (squareSize == UIConst.SMALL_SQUARE_SIZE ||
          squareSize == UIConst.MID_SQUARE_SIZE) {
        m_pieceImages[ChConst.WHITE_PAWN] = Image.createImage("/pawn_w.png");
        m_pieceImages[ChConst.BLACK_PAWN] = Image.createImage("/pawn_b.png");
        m_pieceImages[ChConst.WHITE_KNIGHT] = Image.createImage("/knight_w.png");
        m_pieceImages[ChConst.BLACK_KNIGHT] = Image.createImage("/knight_b.png");
        m_pieceImages[ChConst.WHITE_BISHOP] = Image.createImage("/bishop_w.png");
        m_pieceImages[ChConst.BLACK_BISHOP] = Image.createImage("/bishop_b.png");
        m_pieceImages[ChConst.WHITE_ROOK] = Image.createImage("/rook_w.png");
        m_pieceImages[ChConst.BLACK_ROOK] = Image.createImage("/rook_b.png");
        m_pieceImages[ChConst.WHITE_QUEEN] = Image.createImage("/queen_w.png");
        m_pieceImages[ChConst.BLACK_QUEEN] = Image.createImage("/queen_b.png");
        m_pieceImages[ChConst.WHITE_KING] = Image.createImage("/king_w.png");
        m_pieceImages[ChConst.BLACK_KING] = Image.createImage("/king_b.png");
      } else if (squareSize == UIConst.LARGE_SQUARE_SIZE) {
        m_pieceImages[ChConst.WHITE_PAWN] = Image.createImage("/pawn_mid_w.png");
        m_pieceImages[ChConst.BLACK_PAWN] = Image.createImage("/pawn_mid_b.png");
        m_pieceImages[ChConst.WHITE_KNIGHT] = Image.createImage("/knight_mid_w.png");
        m_pieceImages[ChConst.BLACK_KNIGHT] = Image.createImage("/knight_mid_b.png");
        m_pieceImages[ChConst.WHITE_BISHOP] = Image.createImage("/bishop_mid_w.png");
        m_pieceImages[ChConst.BLACK_BISHOP] = Image.createImage("/bishop_mid_b.png");
        m_pieceImages[ChConst.WHITE_ROOK] = Image.createImage("/rook_mid_w.png");
        m_pieceImages[ChConst.BLACK_ROOK] = Image.createImage("/rook_mid_b.png");
        m_pieceImages[ChConst.WHITE_QUEEN] = Image.createImage("/queen_mid_w.png");
        m_pieceImages[ChConst.BLACK_QUEEN] = Image.createImage("/queen_mid_b.png");
        m_pieceImages[ChConst.WHITE_KING] = Image.createImage("/king_mid_w.png");
        m_pieceImages[ChConst.BLACK_KING] = Image.createImage("/king_mid_b.png");
      } else if (squareSize == UIConst.XLARGE_SQUARE_SIZE) {
        m_pieceImages[ChConst.WHITE_PAWN] = Image.createImage("/pawn_large_w.png");
        m_pieceImages[ChConst.BLACK_PAWN] = Image.createImage("/pawn_large_b.png");
        m_pieceImages[ChConst.WHITE_KNIGHT] = Image.createImage("/knight_large_w.png");
        m_pieceImages[ChConst.BLACK_KNIGHT] = Image.createImage("/knight_large_b.png");
        m_pieceImages[ChConst.WHITE_BISHOP] = Image.createImage("/bishop_large_w.png");
        m_pieceImages[ChConst.BLACK_BISHOP] = Image.createImage("/bishop_large_b.png");
        m_pieceImages[ChConst.WHITE_ROOK] = Image.createImage("/rook_large_w.png");
        m_pieceImages[ChConst.BLACK_ROOK] = Image.createImage("/rook_large_b.png");
        m_pieceImages[ChConst.WHITE_QUEEN] = Image.createImage("/queen_large_w.png");
        m_pieceImages[ChConst.BLACK_QUEEN] = Image.createImage("/queen_large_b.png");
        m_pieceImages[ChConst.WHITE_KING] = Image.createImage("/king_large_w.png");
        m_pieceImages[ChConst.BLACK_KING] = Image.createImage("/king_large_b.png");
      }
      
      // Load the offer images
      if (rightMargin >= MID_OFFERICON_SIZE) {
        m_offerImages[Offer.ABORT] = Image.createImage("/abort_offer_10.png");
        m_offerImages[Offer.DRAW] = Image.createImage("/draw_offer_10.png");
        m_offerImages[Offer.TAKEBACK] = Image.createImage("/takeback_offer_10.png");
        m_offerImages[Offer.PAUSE] = Image.createImage("/pause_offer_10.png");
        m_offerImages[Offer.UNPAUSE] = Image.createImage("/unpause_offer_10.png");
        m_offerImages[Offer.ADJOURN] = Image.createImage("/adjourn_offer_10.png");
      } else {
        m_offerImages[Offer.ABORT] = Image.createImage("/abort_offer_8.png");
        m_offerImages[Offer.DRAW] = Image.createImage("/draw_offer_8.png");
        m_offerImages[Offer.TAKEBACK] = Image.createImage("/takeback_offer_8.png");
        m_offerImages[Offer.PAUSE] = Image.createImage("/pause_offer_8.png");
        m_offerImages[Offer.UNPAUSE] = Image.createImage("/unpause_offer_8.png");
        m_offerImages[Offer.ADJOURN] = Image.createImage("/adjourn_offer_8.png");
      }
      
      retVal = true;
    }
    catch (IOException ioe) { }
    
    return retVal;
  }
  
}
