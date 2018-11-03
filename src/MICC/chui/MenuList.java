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
import MICC.chgame.GameControl;

public class MenuList extends List {
  private static final String menuStrings[] = { };
  private static final String connectStr      = "Connect";
  private static final String disconnectStr   = "Disconnect";
  private static final String settingsStr     = "Settings";
  private static final String seekStr         = "Seek";
  private static final String matchStr        = "Match";
  private static final String rematchStr      = "Rematch";
  private static final String fingerStr       = "Finger";
  private static final String observeStr      = "Observe";
  private static final String unobserveStr    = "Unobserve";
  private static final String offerDrawStr    = "Offer draw";
  private static final String resignStr       = "Resign";
  private static final String abortStr        = "Abort";
  private static final String adjournStr      = "Adjourn";
  private static final String flagStr         = "Flag";
  private static final String pauseStr        = "Pause";
  private static final String unpauseStr      = "Unpause";
  private static final String withdrawStr     = "Withdraw (all)";
  private static final String unseekStr       = "Unseek";
  private static final String commandStr      = "Command";
  private static final String closeBoardStr   = "Close board";
  
  public static final int CONNECT    = 0;
  public static final int DISCONNECT = 1;
  public static final int SETTINGS   = 2;
  public static final int ABORT_CONNECT = 4;
  public static final int SEEK       = 5;
  public static final int MATCH      = 6;
  public static final int REMATCH    = 7;
  public static final int FINGER     = 8;
  public static final int OBSERVE    = 9;
  public static final int UNOBSERVE  = 10;
  public static final int OFFER_DRAW = 11;
  public static final int RESIGN     = 12;
  public static final int ABORT      = 13;
  public static final int ADJOURN    = 14;
  public static final int FLAG       = 15;
  public static final int PAUSE      = 16;
  public static final int UNPAUSE    = 17;
  public static final int WITHDRAW   = 18;
  public static final int UNSEEK     = 19;
  public static final int COMMAND    = 20;
  public static final int CLOSE_BOARD = 21;
  
  public Command m_selectCommand = new Command("Select", Command.ITEM, 1);
  public Command m_cancelCommand = new Command("Cancel", Command.EXIT, 1);
  
  public MenuList() {
    super("MICC", List.IMPLICIT, menuStrings, null);  
    setSelectCommand(m_selectCommand);
    addCommand(m_cancelCommand);
  }
  
  public void configure(int state, boolean guest) {
    deleteAll();
    
    switch (state) {
      case GameControl.STATE_DISCONNECTED:
        append(connectStr, null);
        append(settingsStr, null);
        break;
        
      case GameControl.STATE_IDLE:
        append(seekStr, null);
        append(unseekStr, null);
        //TODO:
        //append(m_matchStr, null);
        append(rematchStr, null);        
        //TODO:
        //append(m_observeStr, null);
        //TODO:
        //append(m_fingerStr, null);
        if (MICClet.gameControl.getPlayedGame() != null) {
          append(closeBoardStr, null);
        }
        append(commandStr, null);
        append(settingsStr, null);
        append(disconnectStr, null);
        
        break;
      
      case GameControl.STATE_PAUSED:
        append(unpauseStr, null);
      case GameControl.STATE_PLAYING:
        append(offerDrawStr, null);
        append(resignStr, null);
        if (state == GameControl.STATE_PLAYING) append(pauseStr, null);
        append(abortStr, null);
        if (!guest) append(adjournStr, null);
        append(withdrawStr, null);
        append(flagStr, null);
        append(commandStr, null);
        break;
        
      // TODO: Observing
        
      default:
        break;
    }
  }
  
  public int getSelection() {
    int    index       = getSelectedIndex();
    String selectedStr = getString(index);
    int    retVal      = -1;
    
    if (selectedStr.equals(connectStr))         retVal = CONNECT;
    else if (selectedStr.equals(disconnectStr)) retVal = DISCONNECT;
    else if (selectedStr.equals(settingsStr))   retVal = SETTINGS;
    else if (selectedStr.equals(seekStr))       retVal = SEEK;
    else if (selectedStr.equals(unseekStr))     retVal = UNSEEK;
    else if (selectedStr.equals(matchStr))      retVal = MATCH;
    else if (selectedStr.equals(observeStr))    retVal = OBSERVE;
    else if (selectedStr.equals(unobserveStr))  retVal = UNOBSERVE;
    else if (selectedStr.equals(offerDrawStr))  retVal = OFFER_DRAW;
    else if (selectedStr.equals(resignStr))     retVal = RESIGN;
    else if (selectedStr.equals(abortStr))      retVal = ABORT;
    else if (selectedStr.equals(adjournStr))    retVal = ADJOURN;
    else if (selectedStr.equals(flagStr))       retVal = FLAG;
    else if (selectedStr.equals(rematchStr))    retVal = REMATCH;
    else if (selectedStr.equals(fingerStr))     retVal = FINGER;
    else if (selectedStr.equals(pauseStr))      retVal = PAUSE;
    else if (selectedStr.equals(unpauseStr))    retVal = UNPAUSE;
    else if (selectedStr.equals(withdrawStr))   retVal = WITHDRAW;
    else if (selectedStr.equals(commandStr))    retVal = COMMAND;
    else if (selectedStr.equals(closeBoardStr)) retVal = CLOSE_BOARD;

    return retVal;
  }
}
