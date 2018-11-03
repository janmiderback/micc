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
import MICC.MICClet;
import MICC.chgame.*;

//Usage: seek [time inc] [rated|unrated] [white|black] [crazyhouse] [suicide]
//            [wild #] [auto|manual] [formula] [rating-range]
public class SeekForm extends Form {
  public Command okCommand     = new Command("Ok", Command.OK, 1);
  public Command cancelCommand = new Command("Cancel", Command.CANCEL, 1);
  
  private TextField   tfTime  = new TextField("Time", "", 3, TextField.NUMERIC);
  private TextField   tfInc   = new TextField("Inc",  "", 3, TextField.NUMERIC);
  private ChoiceGroup cgOptions = new ChoiceGroup("Options", Choice.MULTIPLE);
  private ChoiceGroup cgColor = new ChoiceGroup("Color", Choice.POPUP);
  private TextField   tfRange = new TextField("Rating range", "", 9, TextField.ANY);

  private int ratedIndex;
  private int manualIndex;
  private int formulaIndex;
  private int autoIndex;
  private int whiteIndex;
  private int blackIndex;
  private boolean isGuest;
  private Seek seekDefaults;
  
  public SeekForm() {
    super("Caissa - Seek");
    isGuest = MICClet.gameControl.isGuest();
    
    //TODO: Can not play rated games as guest.
    if (!isGuest) ratedIndex = cgOptions.append("Rated", null);
    manualIndex = cgOptions.append("Manual", null);
    if (!isGuest) formulaIndex = cgOptions.append("Formula", null);
    autoIndex = cgColor.append("Auto", null);
    whiteIndex = cgColor.append("White", null);
    blackIndex = cgColor.append("Black", null);
  
    append(tfTime);
    append(tfInc);
    append(cgOptions);
    append(cgColor);
    append(tfRange);
    
    addCommand(okCommand);
    addCommand(cancelCommand);
    
    seekDefaults = Settings.getSeek();
    
    tfTime.setString(Integer.toString(seekDefaults.time));
    tfInc.setString(Integer.toString(seekDefaults.inc));
    
    if (!isGuest) cgOptions.setSelectedIndex(ratedIndex, seekDefaults.rated);
    cgOptions.setSelectedIndex(manualIndex, seekDefaults.manual);
    if (!isGuest) cgOptions.setSelectedIndex(formulaIndex, seekDefaults.formula);
    
    if (seekDefaults.color == Seek.AUTO) cgColor.setSelectedIndex(autoIndex, true);
    else if (seekDefaults.color == Seek.WHITE) cgColor.setSelectedIndex(whiteIndex, true);
    else if (seekDefaults.color == Seek.BLACK) cgColor.setSelectedIndex(blackIndex, true);
    else cgColor.setSelectedIndex(autoIndex, true);
    
    if (seekDefaults.ratingLower != -1 && seekDefaults.ratingUpper != -1) {
      tfRange.setString(Integer.toString(seekDefaults.ratingLower) + "-" + Integer.toString(seekDefaults.ratingUpper));
    } else {
      tfRange.setString("");
    }
  }
  
  public Seek getSeek() {
    Seek    seek = new Seek();
    boolean selected[] = new boolean[cgOptions.size()];
    int     selectedColorIndex = cgColor.getSelectedIndex();
    String  range = tfRange.getString();
    int     dIndex = range.indexOf('-');
    
    cgOptions.getSelectedFlags(selected);
    
    seek.time = Integer.parseInt(tfTime.getString());
    seek.inc  = Integer.parseInt(tfInc.getString());
    seek.rated = isGuest ? false : selected[ratedIndex];
    seek.manual = selected[manualIndex];
    seek.formula = isGuest ? false : selected[formulaIndex];
    if (selectedColorIndex == autoIndex) seek.color = Seek.AUTO;
    if (selectedColorIndex == whiteIndex) seek.color = Seek.WHITE;
    if (selectedColorIndex == blackIndex) seek.color = Seek.BLACK;
    
    if (isGuest || range.length() == 0) {
      seek.ratingLower = -1;
      seek.ratingUpper = -1;
    } else if ( (dIndex != -1)                 &&
              (dIndex != range.length() - 1) &&
              (dIndex == range.lastIndexOf('-')) ) {
      String lowerPart = range.substring(0, dIndex);
      String upperPart = range.substring(dIndex + 1);
      try {
        seek.ratingLower = Integer.parseInt(lowerPart);
        seek.ratingUpper = Integer.parseInt(upperPart);
        if (seek.ratingLower > seek.ratingUpper ||
            seek.ratingLower < 0                ||
            seek.ratingUpper < 0) {
          seek = null;
        }
      } catch (NumberFormatException e) {
        seek = null;
      }
    } else {
      seek = null;
    }
    
    return seek;
  }
}
