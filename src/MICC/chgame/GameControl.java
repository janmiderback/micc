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

import java.util.Vector;
import java.io.IOException;
import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.*;
import MICC.chserver.*;
import MICC.chutil.*;
import MICC.chui.*;

public final class GameControl extends Thread {
  private BoardCanvas      boardCanvas;   // Set by MICCLet
  private OfferForm        offerForm = null;
  private PromotionForm    promotionForm = null;
  private CommandForm      commandForm = null;
  private Display          display;  // Set by MICCLet
  private ServerConnection connection = new ServerConnection();
  private ConnectThread    connectThread = null;
  
  private Style12Parser m_style12Parser = new Style12Parser();
  private int           state = STATE_DISCONNECTED;
  private Vector        m_messageQueue = new Vector();
  private boolean       m_run;
  private String        userName;
  private String        password;
  private boolean       guest;
  private PlayedGame    activeGame;
  private Vector        games = new Vector();
  private Vector        offers = new Vector();
  private Vector        offersTo = new Vector();
  private Seek          activeSeek = null;
  private Seek          pendingSeek = null;
  private boolean       pendingCommand = false;
  private Move          m_promotionMove = null; 
  
  // Game states
  public static final int STATE_DISCONNECTED = 0;   // No connection to server
  public static final int STATE_CONNECTING   = 1;   // Connecting to server
  public static final int STATE_IDLE         = 2;   // Connected to server
  public static final int STATE_PLAYING      = 3;   // Game active
  public static final int STATE_PAUSED       = 4;   // Game paused
  
  public static final int GAMELOOP_INTERVAL  = 100;  // Milliseconds
  
  
  public GameControl() {
    setPriority(Thread.MAX_PRIORITY);
  }
   
  public void setMiccCanvas(BoardCanvas canvas) {
    boardCanvas = canvas;
  }
  
  public void setDisplay(Display display) {
    this.display = display;
  }
  
  public void setOfferForm(OfferForm form) {
    offerForm = form;
  }
  
  public void setPromotionForm(PromotionForm form) {
    promotionForm = form;
  }
  
  public void setCommandForm(CommandForm form) {
    commandForm = form;
  }
  
  public String getUserName() {
    return userName;
  }
  
  public Vector getOffers() {
    return offers;
  }
  
  public PlayedGame getActiveGame() {
    return activeGame;
  }
  
  //[Thread]
  public void run() {
    long elapsedTime;  // Elapsed time between two renderings
    long execTime;     // Total game loop execution time including rendering
    long startTime;    // Time at start of game loop
    long endTime;      // Time at end of updating game state but before rendering
    long prevEndTime = 0;
    
//#mdebug
    System.out.println(toString() + " is running");
//#enddebug
    
    m_run = true;
    while (m_run) {
      String   serverMsg;
      IMessage msg;

      startTime = System.currentTimeMillis();

      // Read message from server
      try {
        serverMsg = connection.readMessage();
        if (serverMsg != null) handleServerMsg(serverMsg);
      } catch (IOException ioe) {
        Alert a = new Alert("Connection lost");
        a.setTimeout(Alert.FOREVER);
        a.setString("Lost connection with server");
        this.display.setCurrent(a, boardCanvas);
        activeGame = null;
        state = STATE_DISCONNECTED;
      }

      // Handle queued message
      msg = pollMsg();
      if (msg != null) msg.handleMsg();

      // Handle pending offer
      if (this.display.getCurrent() == boardCanvas      &&
          state                != STATE_PLAYING &&
          state                != STATE_PAUSED) {
        if (offers.size() != 0) {
          Offer o = (Offer)(offers.firstElement());
          offers.removeElement(o);
          if (o.getOfferType() == Offer.SWITCH) {
            // Automatically decline switch offers
            offerResp(o, Offer.DECLINE);
          } else {
            offerForm.setOffer(o);
            this.display.setCurrent(offerForm);
          }
        }
      }
      
      // TODO: Should we deactivate active seek?
      // For now we can only seek when in STATE_IDLE.
      if (activeSeek != null && state != STATE_IDLE) {
        activeSeek = null;
      }
                
      endTime = System.currentTimeMillis();
      if (prevEndTime != 0) elapsedTime = endTime - prevEndTime;
      else elapsedTime = GAMELOOP_INTERVAL;
      prevEndTime = endTime;
      
      // Update time controls
      // This is FICS specific. Time starts as white has made first move. ICC is different.
      if (state == STATE_PLAYING && activeGame != null)
        if (activeGame.moveNo > 1) {  // TODO: Is this wrong?
          if (activeGame.colorToMove == ChConst.WHITE) activeGame.whiteRemainingTime -= elapsedTime;
          else activeGame.blackRemainingTime -= elapsedTime;
      }
      
      // Render
      boardCanvas.paint(elapsedTime);
      
      // Calculate game loop execution time.
      execTime = System.currentTimeMillis() - startTime;
      
      // Sleep remaining time
      try {
        sleep(Math.max(0, GAMELOOP_INTERVAL - execTime)); 
      } catch (InterruptedException e) { }
    }
    
    synchronized (this) {
      notify();
    }
//#mdebug
    System.out.println("Exiting GameControl.run"); 
//#enddebug
  }
  
  public synchronized void stop() {
    m_run = false;
    try { wait(); } catch (InterruptedException ie) { }
  }
  
  public int getState() {
    return state;
  }
  
  public PlayedGame getPlayedGame() {
    return activeGame;
  }
  
  public boolean isGuest() {
    return guest;
  }
  
  public Seek getActiveSeek() {
    return activeSeek;
  }
  
  private void sendMsg(IMessage msg) {
    synchronized (m_messageQueue) {
      m_messageQueue.addElement(msg);
      m_messageQueue.notify();
    }
  }
  
  private IMessage pollMsg() {
    IMessage msg = null;
    
    synchronized (m_messageQueue) {
      if (!m_messageQueue.isEmpty()) {
        msg = (IMessage) m_messageQueue.firstElement();
        m_messageQueue.removeElement(msg);
      }
    }
    
    return msg;
  }
  
  public void handleServerMsg(String msg) {
    Vector  lines = new Vector();
    int     i;

    StringUtil.splitString(msg, '\n', lines);
    
    for (i = 0; i < lines.size(); ++i) {
      String line = (String)lines.elementAt(i);
//#mdebug
      boolean lineMatched = true;
//#enddebug
      
      if (line.startsWith(ChConst.STYLE12_KEY))               handleStyle12Board(line);
      //else if (line.startsWith(ChConst.CHALLENGE_KEY))      handleChallenge(line);
      else if (line.startsWith(ChConst.GAME_CREATED_KEY))     handleGameCreated(line);
      else if (line.indexOf(ChConst.GAME_PAUSED_KEY) != -1)   handleGamePaused(line, true);
      else if (line.indexOf(ChConst.GAME_UNPAUSED_KEY) != -1) handleGamePaused(line, false);
      else if (line.startsWith(ChConst.OFFER_KEY))            handleOffer(line);
      else if (line.startsWith(ChConst.OFFER_TO_KEY))         handleOfferTo(line);
      else if (line.startsWith(ChConst.OFFER_REMOVED_KEY))    handleOfferRemoved(line);
      else if (line.startsWith(ChConst.GAME_STATE_KEY))       handleGameState(line);
      else if (line.startsWith(ChConst.SEEK_POSTED_KEY))      handleSeekPosted();
//#mdebug
      else lineMatched = false;
      if (lineMatched) System.out.println("! " + line);
      //else             System.out.println("> " + line);
//#enddebug
    }
    
    if (pendingCommand) {
      commandForm.setResponse(msg);
      pendingCommand = false;
    }
  }
  
  private Offer findOffer(int offerNo) {
    Offer   o = null;
    boolean found = false;
    int     i;
    
    for (i = 0; i < offers.size() && !found; ++i) {
      o = (Offer)(offers.elementAt(i));
      if (o.getOfferNo() == offerNo) found = true;
    }
    return o;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Thread queued message classes
  
  private class ConnectToServerReqMsg implements IMessage {
    String serverName;
    String port;
    String userName2;  // Do not hide outer field.
    String password2;
    
    public ConnectToServerReqMsg(String serverName, String port, String userName, String password) {
      this.serverName = serverName;
      this.port = port;
      userName2 = userName;
      password2 = password;
    }
    
    //[IMessage]
    public void handleMsg() {
      if (state == STATE_DISCONNECTED) {
        int status;
        connectThread = new ConnectThread(connection, this.serverName, this.port, userName2, password2);
        connectThread.start();
        userName = new String(userName2);
        password = new String(password2);
        state = STATE_CONNECTING;
      }
    }
  }
  
  private class ConnectToServerCnfMsg implements IMessage {
    int     status;
    String  userName_;
    boolean guest_;  // Avoid name conflict with outer class.
    
    public ConnectToServerCnfMsg(int status, String userName, boolean guest) {
      this.status = status;
      this.userName_ = userName;
      this.guest_ = guest;
    }
    
    //[IMessage]
    public void handleMsg() {
      if (state == STATE_CONNECTING) {
        if (status == ServerConnection.OPEN_OK) {
          guest = guest_;
          
          // Store the username given at login
          Settings.storeUserName(userName);
          
          // Update to true user name given by server
          userName = userName_;
          
          // Store the password.
          // Guest - Always store empty password.
          // Registered user - Store according user setting.
          if (guest_) Settings.storePassword("");
          else if (Settings.getRememberLogin()) Settings.storePassword(password);
          
          setMICCServerVars();
          state = STATE_IDLE;
        } 
        else {
          Alert a = new Alert("Connection failed");
          a.setTimeout(Alert.FOREVER);
          if (this.status == ServerConnection.OPEN_CONNECT_FAILED) a.setString("Could not open connection to server");
          else a.setString("Login failed");
          MICC.chgame.GameControl.this.display.setCurrent(a, boardCanvas);
          state = STATE_DISCONNECTED;
        }
      }
    }
  }
  
  private class SimpleMsg implements IMessage {
    private String cmd;
    public SimpleMsg(String cmd_) {
      cmd = cmd_;
    }
    
    public void handleMsg() {
      connection.sendLine(cmd);
    }
  }
  
  private class CommandMsg implements IMessage {
    private String m_cmd;
    public CommandMsg(String cmd) {
      m_cmd = cmd;
    }
    
    public void handleMsg() {
      if (state != STATE_DISCONNECTED &&
          state != STATE_CONNECTING) {
        connection.sendLine(m_cmd);
        pendingCommand = true;
      }
    }
  }
  
  private class AbortMsg implements IMessage {
    public void handleMsg() {
      if (state == STATE_PLAYING || state == STATE_PAUSED) {
        connection.sendLine("abort");
      }
    }
  }
  
  private class FlagMsg implements IMessage {
    public void handleMsg() {
      if (state == STATE_PLAYING || state == STATE_PAUSED) {
        connection.sendLine("flag");
      }
    }
  }
  
  private class DisconnectFromServerReqMsg implements IMessage {
    public void handleMsg() {
      synchronized (connection) {
        if (state != STATE_DISCONNECTED && state != STATE_CONNECTING) {
            connection.sendLine("quit");
            connection.close();
            state = STATE_DISCONNECTED;
        }
        else if (state == STATE_CONNECTING) {
          
        }
        connection.notify();
      }
    }
  }
  
  private class OfferRespMsg implements IMessage {
    private Offer   m_offer;
    private int     m_offerResp;
    
    public OfferRespMsg(Offer offer, int offerResp) {
      m_offer = offer;
      m_offerResp = offerResp;
    }
    
    public void handleMsg() {
      //TODO: No hard coded strings.
      if (m_offerResp == Offer.ACCEPT)       connection.sendLine("accept " + m_offer.getOfferNo());
      else if (m_offerResp == Offer.DECLINE) connection.sendLine("decline " + m_offer.getOfferNo());
    }
  }
  
  private class MoveMsg implements IMessage {
    private Move m_move;
  
    public MoveMsg(Move move) {
      m_move = move;
    }
    
    public void handleMsg() {
      if (state == STATE_PLAYING) {
        if (m_move.isPromotion()) {
          m_promotionMove = m_move;
          MICC.chgame.GameControl.this.display.setCurrent(promotionForm);
        } 
        else connection.sendLine(m_move.toString());
      }
    }
  }
  
  private class PromoteMsg implements IMessage {
    private int piece;
  
    public PromoteMsg(int piece_) {
      piece = piece_;
    }
    
    public void handleMsg() {
      if (state == STATE_PLAYING) {
        String pieceStr;
        
        switch (piece) {
          case ChConst.QUEEN:
            pieceStr = "q";
            break;
          case ChConst.ROOK:
            pieceStr = "r";
            break;
          case ChConst.BISHOP:
            pieceStr = "b";
            break;
          case ChConst.KNIGHT:
            pieceStr = "k";
            break;
          default:
            pieceStr = "q";
            break;
        }
        connection.sendLine("promote " + pieceStr);
        connection.sendLine(m_promotionMove.toString());
      }
    }
  }
  
  private class SeekMsg implements IMessage {
    Seek m_seek;
    
    public SeekMsg(Seek seek) {
      m_seek = seek;
    }
    
    public void handleMsg() {
      if (state == STATE_IDLE) {
        if (m_seek == null) {
          Alert  a = new Alert("Seek failed");
          a.setTimeout(Alert.FOREVER);
          a.setString("Incorrect seek parameters");
          return;
        }
        
        StringBuffer sb = new StringBuffer();
        sb.append("seek ");
        sb.append(Integer.toString(m_seek.time) + " ");
        sb.append(Integer.toString(m_seek.inc) + " ");
        sb.append(m_seek.rated ? "rated " : "unrated ");
        sb.append(m_seek.manual ? "manual "  : "auto ");
        sb.append(m_seek.formula ? "formula " : "");
        switch (m_seek.color) {
          case Seek.WHITE:
            sb.append("white ");
            break;
          case Seek.BLACK:
            sb.append("black ");
            break;
          case Seek.AUTO:
          default:
            break;
        }
        if (m_seek.ratingLower != -1 && m_seek.ratingUpper != -1) {
          sb.append(Integer.toString(m_seek.ratingLower) + "-" + Integer.toString(m_seek.ratingUpper));
        }
        connection.sendLine(sb.toString());
        pendingSeek = m_seek.clone();
      }
    }
  }
  
  private class UnseekMsg implements IMessage {
    public void handleMsg() {
      if (state == STATE_IDLE) {
        connection.sendLine("unseek");
        activeSeek = null;
      }
    }
  }
  
  private class WithdrawMsg implements IMessage {
    public void handleMsg() {
      if (state == STATE_PLAYING || state == STATE_PAUSED) {
        connection.sendLine("withdraw all");
      }
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Thread queued message operations
  
  public void connectToServerReq(String serverName,
          String port,
          String userName,
          String password) {
    ConnectToServerReqMsg msg = new ConnectToServerReqMsg(serverName, port, userName, password);
    sendMsg(msg);
  }
  
  public void connectToServerCnf(int status, String userName, boolean guest) {
    ConnectToServerCnfMsg msg = new ConnectToServerCnfMsg(status, userName, guest);
    sendMsg(msg);
  }
  
  public void disconnectFromServer() {
    synchronized (connection) {
      DisconnectFromServerReqMsg msg = new DisconnectFromServerReqMsg();
      sendMsg(msg);
      try { connection.wait(); }
      catch (InterruptedException ie) { }
    }
  }
  
  public void command(String cmd) {
    sendMsg(new CommandMsg(cmd));
  }
  
  public void offerResp(Offer offer, int offerResp) {
    sendMsg(new OfferRespMsg(offer, offerResp));
  }
  
  public void move(Move move) {
    sendMsg(new MoveMsg(move));
  }
  
  public void promote(int piece) {
    sendMsg(new PromoteMsg(piece));
  }
  
  public void draw() {
    sendMsg(
      new IMessage() {
        public void handleMsg() {
          if (state == STATE_PLAYING) {
            connection.sendLine("draw");
          }
        }
      }
    );
  }
  
  public void resign() {
    sendMsg(
      new IMessage() {
        public void handleMsg() {
          if (state == STATE_PLAYING || state == STATE_PAUSED) {
            connection.sendLine("resign");
          }
        }
      }
    );
  }
  
  public void adjourn() {
    sendMsg(
      new IMessage() {
        public void handleMsg() {
          if (state == STATE_PLAYING || state == STATE_PAUSED) {
            connection.sendLine("adjourn");
          }
        }
      }
    );
  }
  
  public void rematch() {
   sendMsg(
      new IMessage() {
        public void handleMsg() {
          if (state == STATE_IDLE) {
            connection.sendLine("rematch");
          }
        }
      }
    );
  }
  
  public void abort() {
    sendMsg(new AbortMsg());
  }
  
  public void flag() {
    sendMsg(new FlagMsg());
  }
  
  public void seek(Seek seek_) {
    sendMsg(new SeekMsg(seek_));
  }
  
  public void unseek() {
    sendMsg(new UnseekMsg());
  }
  
  public void withdraw() {
    sendMsg(new WithdrawMsg());
  }
  
  // Closes the active game by deleting the game object.
  // As a game is ended it is still active until closed.
  public void closeGame() {
    sendMsg(
      new IMessage() {
        public void handleMsg() {
          if (state != STATE_PLAYING && state != STATE_PAUSED) {
            activeGame = null;
          }
        }
      }
    );
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Server message handlers
  
  private void handleGameCreated(String line) {        
    Vector v = new Vector();
    String s;
    
    // Reset all game data
    activeGame = new PlayedGame();
    
    StringUtil.splitString(line, ' ', v);
    
    // "Creating: jannem ( 783) GuestRNJQ (++++) unrated blitz 2 12"
    
    activeGame.whitePlayerName = (String)v.elementAt(1);
    
    s = (String)v.elementAt(2);
    //TODO: Move these FICS line parsing details to another parser class.
    if (s.equals("(")) activeGame.blackPlayerName = (String)v.elementAt(4);
    else               activeGame.blackPlayerName = (String)v.elementAt(3);
    
    if (userName.equalsIgnoreCase(activeGame.whitePlayerName)) {
      activeGame.userColor = ChConst.WHITE;
      activeGame.opponentColor = ChConst.BLACK;
    } else {
      activeGame.userColor = ChConst.BLACK;
      activeGame.opponentColor = ChConst.WHITE;
    }
    
    boardCanvas.getPlayerInfoTop().setPlayerColor(activeGame.opponentColor);
    boardCanvas.getPlayerInfoBottom().setPlayerColor(activeGame.userColor);
    
    Alert  a = new Alert("Game starting");
    a.setTimeout(Alert.FOREVER);
    a.setString(line);
    this.display.setCurrent(a, boardCanvas);
    state = STATE_PLAYING;
  }
 
  private void handleGamePaused(String line, boolean paused) {
    if (state == STATE_PLAYING || state == STATE_PAUSED) {
      Vector v = new Vector();
      int    gameNo;
      String gameNoPartStr;
      
      StringUtil.splitString(line, ' ', v);
      gameNoPartStr = (String)(v.elementAt(1));
      gameNo = Integer.parseInt(gameNoPartStr.substring(0, gameNoPartStr.length() - 1));
      
      // Our game?
      if (gameNo == activeGame.gameNo) {
        state = paused ? STATE_PAUSED : STATE_PLAYING;
      }
    }
  }
  
  private void handleGameState(String line) {
    Vector v = new Vector();
    String gameNoStr;
    int    gameNo;

    StringUtil.splitString(line, ' ', v);
    gameNoStr = (String)v.elementAt(1);
    gameNo = Integer.parseInt(gameNoStr);
        
    if (line.indexOf(ChConst.GAME_CREATING_KEY) != -1 ||
        line.indexOf(ChConst.GAME_CONTINUING_KEY) != -1) {
      // Is it me?
      if (line.indexOf(userName) != -1) activeGame.gameNo = gameNo;
    }
    else if (line.indexOf(ChConst.GAME_CHECKMATED_KEY) != -1 || 
             line.indexOf(ChConst.GAME_RESIGNS_KEY) != -1 ||
             line.indexOf(ChConst.GAME_DRAWN_KEY) != -1 ||
             line.indexOf(ChConst.GAME_ABORTED_KEY) != -1 ||
             line.indexOf(ChConst.GAME_ADJOURNED_KEY) != -1 ||
             line.indexOf(ChConst.GAME_FORFEITS_TIME_KEY) != -1 ||
             line.indexOf(ChConst.GAME_FORFEITS_DISCONN_KEY) != -1 ||
             line.indexOf(ChConst.GAME_COURTESY_KEY) != -1) {
      // Is it me?
      if (activeGame.gameNo == gameNo) {
        String result;
        // Find result
        if (line.indexOf(ChConst.WHITE_WINS) != -1)      result = ChConst.WHITE_WINS;
        else if (line.indexOf(ChConst.BLACK_WINS) != -1) result = ChConst.BLACK_WINS;
        else if (line.indexOf(ChConst.DRAW) != -1)       result = ChConst.DRAW;
        else if (line.indexOf(ChConst.NO_RESULT) != -1)  result = ChConst.NO_RESULT;
        else result = "?";
        // Extract ending message
        String m = line.substring(1, line.indexOf('}'));
        Alert  a = new Alert("Game ended");
        a.setTimeout(Alert.FOREVER);
        a.setString("Result: " + result + "\n\n" + m);
        this.display.setCurrent(a, boardCanvas);
        state = STATE_IDLE;
      }
    }
  }
  
  private void handleStyle12Board(String line) {
    m_style12Parser.parse(line, activeGame);
  }
  
  private void handleOffer(String line) {    
    offers.addElement(new Offer(line, Offer.FROM));
  }
  
  private void handleOfferTo(String line) {
    offersTo.addElement(new Offer(line, Offer.TO));
  }
  
  private void handleOfferRemoved(String line) {
    Vector v = new Vector();
    int    removedOfferNo;
    Offer  offer;
    
    StringUtil.splitString(line, ' ', v);
    removedOfferNo = Integer.parseInt((String)(v.elementAt(1)));
    
    // Close offer form if it shows removed offer.
    if (this.display.getCurrent() == offerForm) {
      offer = offerForm.getOffer();
      if (offer.getOfferNo() == removedOfferNo) this.display.setCurrent(boardCanvas);
    }
    
    // Remove offer from list.
    offer = findOffer(removedOfferNo);
    if (offer != null) offers.removeElement(offer);
  }
  
  private void handleSeekPosted() {
    activeSeek = pendingSeek;
    Settings.storeSeek(activeSeek);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Server variable handling
  
  private void setMICCServerVars() {
    // Make sure we always find the prompt
    setIServerVar("defprompt", "1");
    // Turn of terminal highlight feature (?)
    setIServerVar("nohighlight", "1");
    //TODO: Set ivar 'compressmove' when delta boards supported
    setIServerVar("pendinfo", "1");
    setIServerVar("gameinfo", "1");
    // Lock further ivar modification.
    setIServerVar("lock", "1");
    // Turn off game starts and ends. Minimize traffic
    setServerVar("gin", "0");
    // Turn off channel messages. Minimize traffic
    setServerVar("chanoff", "1");
    // Turn off shouts/cshouts. Minimize traffic
    setServerVar("shout", "0");
    setServerVar("cshout", "0");
    // Turn off seek ads. Minimize traffic
    setServerVar("seek", "0");
    // Set board style to 12. Machine-friendly
    setServerVar("style", "12");
  }
  
  private void setServerVar(String var, String value) {
    connection.sendLine("set " + var + " " + value);
  }
  
  // Sets 'i' variables.
  private void setIServerVar(String ivar, String value) {
    connection.sendLine("iset " + ivar + " " + value);
  }
  
}

