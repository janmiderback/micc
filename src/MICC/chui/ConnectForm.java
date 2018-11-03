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

import MICC.chgame.Settings;
import javax.microedition.lcdui.*;

public class ConnectForm extends Form {
  public Command m_cancelCommand  = new Command("Cancel", Command.EXIT, 1);
  public Command m_connectCommand = new Command("Connect", Command.OK, 1);
  
  private static final String FICSStr = "FICS";
  private static final String ICCStr  = "ICC";
  
  private int FICSIndex;
  private int ICCIndex;

  private ChoiceGroup m_cgServer   = new ChoiceGroup("Server", Choice.POPUP);
  private TextField   m_tfUserName = new TextField("User", "", 50, TextField.ANY);
  private TextField   m_tfPassword = new TextField("Password", "", 50, TextField.PASSWORD);
  
  // Constructor
  public ConnectForm()
  {
    super("Caissa - Connect");
    FICSIndex = m_cgServer.append(FICSStr, null);
    //ICCIndex = m_cgServer.append(ICCStr, null);
    append(m_cgServer);
    append(m_tfUserName);
    append(m_tfPassword);
    addCommand(m_cancelCommand);
    addCommand(m_connectCommand);
  }

  public void init(String userName, String password, int server) {
    if (server == Settings.FICS) m_cgServer.setSelectedIndex(FICSIndex, true);
    //if (server == Settings.ICC)  m_cgServer.setSelectedIndex(ICCIndex, true);
    m_tfUserName.setString(userName);
    m_tfPassword.setString(password);
  }
  
  public int getServer() {
    if (m_cgServer.getSelectedIndex() == FICSIndex) return Settings.FICS;
    //if (m_cgServer.getSelectedIndex() == ICCIndex)  return Settings.ICC;
    return -1;
  }
  
  public String getUserName() {
    return m_tfUserName.getString();
  }

  public String getPassword() {
    return m_tfPassword.getString();
  }  
}

