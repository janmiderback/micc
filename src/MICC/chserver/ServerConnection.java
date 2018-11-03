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

package MICC.chserver;

import java.io.*;
import javax.microedition.io.*;
import MICC.chutil.LogUtil;

public final class ServerConnection {
  private InputStream         is;
  private OutputStream        os;
  private SocketConnection    sc;
  private StringBuffer        sb;
  private boolean             messageDone;
  private boolean             onNextCheckPrompt;
  private static final String PROMT_SEARCH = "\nfics% ";
  private int                 state = STATE_CLOSED;
  private boolean             guest;
  private String              trueUserName;
  
  // States
  private static final int STATE_CLOSED = 0;
  private static final int STATE_OPEN = 1;
  
  // Open request return status
  public static final int OPEN_OK             = 1;
  public static final int OPEN_CONNECT_FAILED = 2;
  public static final int OPEN_LOGIN_FAILED   = 3;
  public static final int OPEN_ABORTED        = 4;
  
  public ServerConnection() {    
    sb = new StringBuffer();
    messageDone = false;
    onNextCheckPrompt = false;
    state = STATE_CLOSED;
  }
  
  public synchronized int open(String name, String port, String userName, String password) {
    final String  findLogin[]       = {"login: "};
    final String  findPassword[]    = {"password: "};
    final String  findUserStatus[]  = {"is a registered name", "is not a registered name", "login: "};
    final String  newLineRequest[]  = {"Press return to enter the server as"};
    final String  findLoginStatus[] = {"**** Starting FICS session as ", "**** Invalid password! ****", "login: "};
    final String  startingSessionAs[] = {};
    
    int found;
    boolean loginOk = true;  // Assume success
    
    guest = false;
    
//#mdebug
    LogUtil.logPoint("Opening server connection");
    LogUtil.logPoint("  Server:   " + name);
    LogUtil.logPoint("  Port:     " + port);
    LogUtil.logPoint("  User:     " + userName);
    LogUtil.logPoint("  Password: " + password);
//#enddebug
    
    if (state != STATE_CLOSED) return OPEN_CONNECT_FAILED;
    
    try {
      sc = (SocketConnection)Connector.open("socket://" + name + ":" + port);
      is = sc.openInputStream();
      os = sc.openOutputStream();
      
//#mdebug
      LogUtil.logPoint("Socket opened");
//#enddebug
      searchInStream(findLogin);
//#mdebug
      LogUtil.logReceive("Login prompt");
//#enddebug
      
      sendLine(userName);
//#mdebug
      LogUtil.logSend("User name (" + userName + ")");
//#enddebug
      
      if (userName.toLowerCase().equals("guest")) {
        guest = true;
        searchInStream(newLineRequest);
//#mdebug
        LogUtil.logReceive("Newline request");
//#enddebug                
        // Server needs a newline to proceed.
        sendLine("");
//#mdebug
        LogUtil.logSend("Newline");
//#enddebug        
      }
      else {
        found = searchInStream(findUserStatus);
        
//#mdebug
        LogUtil.logReceive("User status");
//#enddebug           
        
        switch (found) {
          case 0:
//#mdebug
            LogUtil.logPoint("User name is registered");
//#enddebug           
            // "is a registered name"
            // For registered user send password.
            searchInStream(findPassword);
            sendLine(password);
            break;
            
          case 1:
//#mdebug
            LogUtil.logPoint("User name is not registered");
//#enddebug                       
            // "is not a registered name"
            guest = true;
            searchInStream(newLineRequest);
//#mdebug
            LogUtil.logReceive("Newline request");
//#enddebug                
            // Server needs a newline to proceed.
            sendLine("");
//#mdebug
            LogUtil.logSend("Newline");
//#enddebug             
            break;
            
          case 2:
          default:
            loginOk = false;
            break;
        }
      }
      
      if (loginOk) {
        // Here we find out the actual username assigned by the
        // server since the user provided name does not necessarily match
        // the server name.
        StringBuffer matchedLineBuf = new StringBuffer();
        
        found = searchInStreamLine(findLoginStatus, matchedLineBuf);
//#mdebug
        LogUtil.logReceive("Login status");
//#enddebug        
        
        loginOk = (found == 0);
        
        if (loginOk) {
          int i = 0;
//#mdebug
          LogUtil.logPoint("Login successful");
//#enddebug
          matchedLineBuf.delete(0, findLoginStatus[0].length());
          // Line is presented now as
          // "username(U)" - unregistered user, guest
          // "username " - registered user (following blank)
          // Find location of '(' or ' '.
          while (matchedLineBuf.charAt(i) != '(' && matchedLineBuf.charAt(i) != ' ') ++i;
          // Now all we have to do is to truncate from '(' or ' '.
          matchedLineBuf.setLength(i);
          trueUserName = matchedLineBuf.toString();
        }
      }
      
      if (loginOk) state = STATE_OPEN;
      else {
//#mdebug
          LogUtil.logPoint("Login failed");
//#enddebug 
        close();
      }

      return loginOk ? OPEN_OK : OPEN_LOGIN_FAILED;
    }
    catch (IOException ioe) {
//#mdebug
      System.out.println(ioe);
//#enddebug
      return OPEN_CONNECT_FAILED;
    }
  }
  
  public boolean isGuest() {
    return guest;
  }
  
  /**
   * Return the true user name assigned by the server. The username assigned by
   * the server is not neccesarily the same as the user provided at login
   * especially for guest log in.
   *
   * @return the true user name assigned by the server.
   */
  public String getTrueUserName() {
    return trueUserName;
  }
  
  /** 
   * Write a line of text to the server. The operation itself adds \r\n
   * to the line so it does not have to be included in the argument.
   *
   * @param line the line to write to the server on the connection.
   */
  public synchronized void sendLine(String line) {
    byte theBytes[] = (line + "\r\n").getBytes();
    
    try {
      os.write(theBytes);
      os.flush();
    } catch (IOException ioe) {
//#mdebug
      System.out.println(ioe);
//#enddebug
    }
  }
  
  public String readMessage() throws IOException {
    String  s = null;
    boolean done = false;
    int     available;
    int     n;
    
    if (state != STATE_OPEN) return null;
    
    available = is.available();
    
    for (n = 1; n <= available && !messageDone; ++n)
    {
      int c = is.read();
      
      if (onNextCheckPrompt) {
        sb.append((char)c);
        if (c == ' ') messageDone = sb.toString().endsWith(PROMT_SEARCH);
        onNextCheckPrompt = false;
      } else {
        switch (c) {
          case '\r':
            // Ignore
            break;
            
          case -1:
            //TODO: Handle end of stream
            messageDone = true;
            break;
            
          case '%':
            onNextCheckPrompt = true;
            sb.append((char)c);
            break;
            
          default:
            // Add all other chars to line
            sb.append((char)c);
            break;
        }
      }
    }
    
    if (messageDone) {
      s = sb.toString();
      messageDone = false;
      sb.setLength(0);

      // Skip leading '\n' and the trailing prompt.
      {
        int i = 0;
        while (s.charAt(i) == '\n') ++i;
        s = s.substring(i, s.length() - PROMT_SEARCH.length());
      }
    }
//#mdebug
    //if (s != null) System.out.println("SrvMsg: " + s);
//#enddebug
    return s;
  }
  
  // Closes the server connection resources.
  public synchronized void close() {
    try { os.close(); } catch (IOException ioe) { }
    try { is.close(); } catch (IOException ioe) { }
    try { sc.close(); } catch (IOException ioe) { }
    os = null;
    is = null;
    sc = null;
    state = STATE_CLOSED;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Private operations
  
  /**
   * Reads and searches the input stream lines for strings given by the
   * searchStrings argument. The search is performed on a line-to-line
   * basis which means that it is not possible to search for strings containing
   * newline characters.
   *
   * @param searchStrings an array of strings to search for
   * @return              the array index of the string found
   */
  private int searchInStream(String searchStrings[]) throws IOException {
    final StringBuffer sb = new StringBuffer();
    boolean found = false;
    int foundIndex = -1;
    String total = null;
    
    while (!found) {
      int    i;
      char   c;
      
      c = (char)is.read();
      
      switch (c) {
        case '\r':
          // Ignore
          break;
          
        case '\n':
          sb.setLength(0);
          break;
          
        default:
          sb.append(c);
          total = sb.toString();

          for (i = 0; !found && (i < searchStrings.length); i++) {
            if (total.indexOf(searchStrings[i]) != -1) {
              foundIndex = i;
              found = true;
            }
          }
          
          break;
       }
    }
    
    return foundIndex;
  }
  
  /**
   * Reads and searches in the input stream for lines that matches strings
   * in the searchStrings argument. When a line is found, the foundLineBuf
   * string buffer contains the complete matching line.
   *
   * @param searchStrings  an array of strings to search for
   * @param matchedLineBuf a string buffer containing the matching line
   * @return               the array index of the string found
   */
  private int searchInStreamLine(String searchStrings[], StringBuffer matchedLineBuf) throws IOException {
    boolean done = false;
    boolean found = false;
    int foundIndex = -1;
    String total = null;
    
    while (!done) {
      int    i;
      char   c;
      
      c = (char)is.read();
      
      switch (c) {
        case '\r':
          // Ignore
          break;
          
        case '\n':
          if (!found) {
            // Line was ended but no match. Start over.
            matchedLineBuf.setLength(0);
          }
          else {
            done = true;
          }
          break;
          
        default:
          matchedLineBuf.append(c);
          
          if (!found) {
            total = matchedLineBuf.toString();

            for (i = 0; !found && (i < searchStrings.length); i++) {
              if (total.indexOf(searchStrings[i]) != -1) {
                foundIndex = i;
                found = true;
              }
            }
          }
          break;
       }
    }
    
    return foundIndex;
  }
  
}

