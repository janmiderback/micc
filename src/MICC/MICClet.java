/*
* Copyright (c) 2007, Jan Miderbäck
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of the <organization> nor the
*       names of its contributors may be used to endorse or promote products
*       derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY Jan Miderbäck ``AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL Jan Miderbäck BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package MICC;

import javax.microedition.midlet.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.io.*;
import MICC.chserver.*;
import MICC.chgame.*;
import MICC.chui.*;

public class MICClet extends MIDlet implements CommandListener {
  
  // Central game control object.
  public static GameControl gameControl;
  
  private BoardCanvas   canvas;
  private MenuList      menuList;
  private ConnectForm   connectForm;
  private OfferForm     offerForm;
  private SeekForm      seekForm;
  private PromotionForm promotionForm;
  private CommandForm   commandForm;
  private Display       display;
  
  public MICClet() {
  }
  
  //[MIDlet]
  public void startApp() {
    // Open settings
    Settings.open();
    
    // Create objects
    gameControl = new GameControl();  // Create this object first.
    canvas = new BoardCanvas();
    menuList = new MenuList();
    connectForm = new ConnectForm();
    promotionForm = new PromotionForm();
    seekForm = null;
    offerForm = new OfferForm();
    commandForm = new CommandForm();
    display = Display.getDisplay(this);
    
    // Setup relations
    gameControl.setMiccCanvas(canvas);
    gameControl.setDisplay(display);
    gameControl.setOfferForm(offerForm);
    gameControl.setPromotionForm(promotionForm);
    gameControl.setCommandForm(commandForm);
    gameControl.setCommandForm(commandForm);
    
    // Setup command listeners
    canvas.setCommandListener(this);
    menuList.setCommandListener(this);
    connectForm.setCommandListener(this);
    offerForm.setCommandListener(this);
    promotionForm.setCommandListener(this);
    commandForm.setCommandListener(this);
    
    display.setCurrent(canvas);
    gameControl.start();
  }
  
  //[MIDlet]
  public void pauseApp() {
    // Do nothing.
  }
  
  //[MIDlet]
  public void destroyApp(boolean unconditional) {
    gameControl.disconnectFromServer();
    gameControl.stop();
    canvas = null;
    menuList = null;
    connectForm = null;
    display = null;
    
    // Reset and set singletons to null.
    // Seems to be restart problems if this is not handled.
    offerForm = null;
    promotionForm = null;
    seekForm = null;
    gameControl = null;
    
    //TODO: Try without this
    //GameControl.reset();
  }
  
  //[CommandListener]
  public void commandAction(Command c, Displayable d) {
    if (c == canvas.menuCommand) {
      final int state = MICClet.gameControl.getState();
     
      // When connecting do not display open any menu.
      if (state != gameControl.STATE_CONNECTING) {
        menuList.configure(gameControl.getState(), gameControl.isGuest());
        display.setCurrent(menuList);
      }
    }
    else if (c == canvas.exitCommand) {
      destroyApp(false);
      notifyDestroyed();
    }
    else if (c == menuList.m_selectCommand) handleMenuListSelect();
    else if (c == connectForm.m_connectCommand) {
      int server = connectForm.getServer();
      String serverName = "";
      int    port = 0;
      if (server == Settings.FICS) {
        serverName = Settings.getFICSDNSName();
        port       = Settings.getFICSPort();
      }
      else if (server == Settings.ICC) {
        serverName = Settings.getICCDNSName();
        port       = Settings.getICCPort();
      }
      gameControl.connectToServerReq(serverName, String.valueOf(port), connectForm.getUserName(), connectForm.getPassword());
      display.setCurrent(canvas);
    }
    else if (c == connectForm.m_cancelCommand) display.setCurrent(canvas);
    else if (c == offerForm.m_acceptCommand) {
      gameControl.offerResp(offerForm.getOffer(), Offer.ACCEPT);
      display.setCurrent(canvas);
    }
    else if (c == offerForm.m_declineCommand) {
      gameControl.offerResp(offerForm.getOffer(), Offer.DECLINE);
      display.setCurrent(canvas);
    }
    else if (c == seekForm.okCommand) {
      gameControl.seek(seekForm.getSeek());
      seekForm = null;
      display.setCurrent(canvas);
    }
    else if (c == promotionForm.okCommand) { 
      gameControl.promote(promotionForm.getPromotion());
      display.setCurrent(canvas);
    }
    else if (c == commandForm.m_sendCommand) {
      gameControl.command(commandForm.getCommand());
    } 
    else {
      display.setCurrent(canvas);
    }
    
  }
  
  private void handleMenuListSelect() {
    int selection = menuList.getSelection();
    
    switch (selection) {
      case MenuList.CONNECT:
        connectForm.init(Settings.getUserName(), Settings.getPassword(), Settings.getServer());
        display.setCurrent(connectForm);
        break;
      case MenuList.DISCONNECT:
        gameControl.disconnectFromServer();
        display.setCurrent(canvas);
        break;
      case MenuList.OFFER_DRAW:
        gameControl.draw();
        display.setCurrent(canvas);
        break;
      case MenuList.SEEK:
        seekForm = new SeekForm();
        seekForm.setCommandListener(this);
        display.setCurrent(seekForm);
        break;
      case MenuList.UNSEEK:
        gameControl.unseek();
        display.setCurrent(canvas);
        break;
      case MenuList.ABORT:
        gameControl.abort();
        display.setCurrent(canvas);
        break;
      case MenuList.SETTINGS:
        //TODO: SettingForm
        display.setCurrent(canvas);
        break;
      case MenuList.ABORT_CONNECT:
        gameControl.disconnectFromServer();
        display.setCurrent(canvas);
        break;
      case MenuList.MATCH:
        display.setCurrent(canvas);
        break;
      case MenuList.REMATCH:
        gameControl.rematch();
        display.setCurrent(canvas);
        break;
      case MenuList.FINGER:
        display.setCurrent(canvas);
        break;
      case MenuList.OBSERVE:
        display.setCurrent(canvas);
        break;
      case MenuList.UNOBSERVE:
        display.setCurrent(canvas);
        break;
      case MenuList.RESIGN:
        gameControl.resign();
        display.setCurrent(canvas);
        break;
      case MenuList.ADJOURN:
        gameControl.adjourn();
        display.setCurrent(canvas);
        break;
      case MenuList.FLAG:
        gameControl.flag();
        display.setCurrent(canvas);
        break;
      case MenuList.WITHDRAW:
        gameControl.withdraw();
        display.setCurrent(canvas);
        break;
      case MenuList.COMMAND:
        commandForm.init();
        display.setCurrent(commandForm);
        break;
      case MenuList.CLOSE_BOARD:
        gameControl.closeGame();
        break;
      default:
        display.setCurrent(canvas);
        break;
    }
  }
}
