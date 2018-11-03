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

import MICC.MICClet;
import MICC.chserver.ServerConnection;

class ConnectThread extends Thread
{
  final private GameControl      gameControl;
  final private ServerConnection connection;
  final private String           serverName;
  final private String           port;
  final private String           userName;
  final private String           password;

  public ConnectThread(ServerConnection connection,
                       String serverName,
                       String port,
                       String userName,
                       String password)
  {
    gameControl = MICClet.gameControl;
    this.connection = connection;
    this.serverName = serverName;
    this.port = port;
    this.userName = userName;
    this.password = password;
  }
  
  //[Thread]
  public void run()
  {
    String trueUserName = "";
    boolean guest = false;
    int status = connection.open(serverName, port, userName, password);
    
    if (status == ServerConnection.OPEN_OK) {
      guest = connection.isGuest();
      trueUserName = connection.getTrueUserName();
    }
    
//#mdebug
    System.out.println("ConnectThread: Open connection " + status); 
//#enddebug   
    gameControl.connectToServerCnf(status, trueUserName, guest);
//#mdebug
    System.out.println("Exiting ConnectThread.run"); 
//#enddebug
  }
}
