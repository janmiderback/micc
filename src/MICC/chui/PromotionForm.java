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
import MICC.chgame.ChConst;

public class PromotionForm extends Form {
  public Command      okCommand = new Command("Ok", Command.OK, 1);
  private ChoiceGroup group     = new ChoiceGroup("Promote to", Choice.EXCLUSIVE);

  private static final String queenStr  = "Queen";
  private static final String rookStr   = "Rook";
  private static final String bishopStr = "Bishop";
  private static final String knightStr = "Knight";
  
  public PromotionForm() {
    super("Caissa - Promote");
    group.append(queenStr, null);
    group.append(rookStr, null);
    group.append(bishopStr, null);
    group.append(knightStr, null);
    group.setSelectedIndex(0, true);
    append(group);
    addCommand(okCommand);
  }
  
  public int getPromotion() {
    final String selectedStr = group.getString(group.getSelectedIndex());
    if (selectedStr.equals(queenStr)) return ChConst.QUEEN;
    if (selectedStr.equals(rookStr))  return ChConst.ROOK;
    if (selectedStr.equals(bishopStr)) return ChConst.BISHOP;
    return ChConst.KNIGHT;
  }
}
