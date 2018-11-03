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

import javax.microedition.rms.*;

public class Settings {
  private static RecordStore rs;
  
  public static final int FICS = 0;
  public static final int ICC  = 1;
  
  public static final int PROMOTE_TO_QUEEN  = 0;
  public static final int PROMOTE_TO_ROOK   = 1;
  public static final int PROMOTE_TO_BISHOP = 2;
  public static final int PROMOTE_TO_KNIGHT = 3;
  
  private static final String storeName = "MICC_RS";
  private static final String TRUE_STR  = "1";
  private static final String FALSE_STR  = "0";
  
  // Version (only internal)
  private static int RID_VERSION;
  private static final String VERSION = TRUE_STR;
  
  // FICS server - DNS name
  private static int RID_FICS_DNSNAME;
  private static String ficsDNSName = "freechess.org";
  
  // FICS server - Port
  private static int RID_FICS_PORT;
  private static String ficsPort = "5000";
  
  // ICC server - DNS name
  private static int RID_ICC_DNSNAME;
  private static String iccDNSName = "chessclub.com";
  
  // ICC server - Port
  private static int RID_ICC_PORT;
  private static String iccPort = "5006";
  
  // Remember server, user and password between logins
  private static int RID_REMEMBER_LOGIN;
  private static String rememberLogin = TRUE_STR;
  
  // Server
  private static int RID_SERVER;
  private static String server = Integer.toString(FICS);
  
  // User name
  private static int RID_USERNAME;
  private static String userName = "";
  
  // Password
  private static int RID_PASSWORD;
  private static String password = "";
  
  // Seek - Time
  private static int RID_SEEK_TIME;
  private static String seekTime = "2";
  
  // Seek - Inc
  private static int RID_SEEK_INC;
  private static String seekInc = "12";
  
  // Seek - Rated
  private static int RID_SEEK_RATED;
  private static String seekRated = TRUE_STR;
  
  // Seek - Manual
  private static int RID_SEEK_MANUAL;
  private static String seekManual = FALSE_STR;
  
  // Seek - Formula
  private static int RID_SEEK_FORMULA;
  private static String seekFormula = FALSE_STR;
  
  // Seek - Color
  private static int RID_SEEK_COLOR;
  private static String seekColor = Integer.toString(Seek.AUTO);
  
  // Seek - Rating lower limit
  private static int RID_SEEK_RATING_LOWER;
  private static String seekRatingLower = "-1";
  
  // Seek - Rating lower limit
  private static int RID_SEEK_RATING_UPPER;
  private static String seekRatingUpper = "-1";
  
  // Auto-promote enable
  private static int RID_AUTOPROMOTE_ENABLE;
  private static String autoPromoteEnable = FALSE_STR;
  
  // Auto-promote piece
  private static int RID_AUTOPROMOTE_PIECE;
  private static String autoPromotePiece = Integer.toString(PROMOTE_TO_QUEEN);
  
  public static final void open() {
    byte   recordData[];
    String storedVersion;
    
    try {
      rs = RecordStore.openRecordStore(storeName, true);
      if (rs.getNumRecords() == 0) {
//#mdebug
        System.out.println("Storing default settings");
//#enddebug
        assignRIDs(true);
        return;
      }
      
      assignRIDs(false);
      storedVersion = new String(rs.getRecord(RID_VERSION));
      
      if (!storedVersion.equals(VERSION)) {
//#mdebug
        System.out.println("New store version. Storing default settings");
//#enddebug
        rs.closeRecordStore();
        RecordStore.deleteRecordStore(storeName);
        rs = RecordStore.openRecordStore(storeName, true);
        assignRIDs(true);
        return;
      } 
      
//#mdebug
      System.out.println("Reading settings");
//#enddebug
      // RID_FICS_DNSNAME
      ficsDNSName = new String(rs.getRecord(RID_FICS_DNSNAME));
      // RID_FICS_PORT
      ficsPort = new String(rs.getRecord(RID_FICS_PORT));
      // RID_ICC_DNSNAME
      iccDNSName = new String(rs.getRecord(RID_ICC_DNSNAME));
      // RID_ICC_PORT
      iccPort = new String(rs.getRecord(RID_ICC_PORT));
      // RID_REMEMBER_LOGIN
      rememberLogin = new String(rs.getRecord(RID_REMEMBER_LOGIN));
      // RID_SERVER
      server = new String(rs.getRecord(RID_SERVER));
      // RID_USERNAME
      recordData = rs.getRecord(RID_USERNAME);
      if (recordData != null) userName = new String(recordData);
      // RID_PASSWORD
      recordData = rs.getRecord(RID_PASSWORD);
      if (recordData != null) password = new String(recordData);
      // RID_SEEK_TIME
      seekTime = new String(rs.getRecord(RID_SEEK_TIME));
      // RID_SEEK_INC
      seekInc = new String(rs.getRecord(RID_SEEK_INC));
      // RID_SEEK_RATED
      seekRated = new String(rs.getRecord(RID_SEEK_RATED));
      // RID_SEEK_MANUAL
      seekManual = new String(rs.getRecord(RID_SEEK_MANUAL));
      // RID_SEEK_FORMULA
      seekFormula = new String(rs.getRecord(RID_SEEK_FORMULA));
      // RID_SEEK_COLOR
      seekColor = new String(rs.getRecord(RID_SEEK_COLOR));
      // RID_SEEK_RATING_LOWER
      seekRatingLower = new String(rs.getRecord(RID_SEEK_RATING_LOWER));
      // RID_SEEK_RATING_UPPER
      seekRatingUpper = new String(rs.getRecord(RID_SEEK_RATING_UPPER));
      // RID_AUTOPROMOTE_ENABLE
      autoPromoteEnable = new String(rs.getRecord(RID_AUTOPROMOTE_ENABLE));
      // RID_AUTOPROMOTE_PIECE
      autoPromotePiece = new String(rs.getRecord(RID_AUTOPROMOTE_PIECE));

//#mdebug
      System.out.println("Remember login: " + rememberLogin);
      System.out.println("Server:         " + ((getServer() == FICS) ? "FICS" : "ICC"));
      System.out.println("User name:      " + userName);
      System.out.println("Password:       " + password);
      System.out.println("Seek time:      " + seekTime);
      System.out.println("Seek inc:       " + seekInc);
      System.out.println("Seek rated:     " + seekRated);
      System.out.println("Seek manual:    " + seekManual);
      System.out.println("Seek formula:   " + seekFormula);
      {
        int seekFormulaInt = Integer.parseInt(seekFormula);
        String seekFormulaStr = "";
        if (seekFormulaInt == Seek.AUTO) seekFormulaStr = "AUTO";
        if (seekFormulaInt == Seek.WHITE) seekFormulaStr = "WHITE";
        if (seekFormulaInt == Seek.BLACK) seekFormulaStr = "BLACK";
        System.out.println("Seek color:     " + seekFormulaStr);
      }
      System.out.println("Seek rating:    " + seekRatingLower + "-" + seekRatingUpper);
      System.out.println("Autopromote:    " + autoPromoteEnable);
      {
        int autoPromotePieceInt = Integer.parseInt(autoPromotePiece);
        String autoPromotePieceStr = "";
        if (autoPromotePieceInt == PROMOTE_TO_QUEEN)  autoPromotePieceStr = "Queen";
        if (autoPromotePieceInt == PROMOTE_TO_ROOK)   autoPromotePieceStr = "Rook";
        if (autoPromotePieceInt == PROMOTE_TO_BISHOP) autoPromotePieceStr = "Bishop";
        if (autoPromotePieceInt == PROMOTE_TO_KNIGHT) autoPromotePieceStr = "Knight";
        System.out.println("Autopromote to: " + autoPromotePieceStr);
      }
//#enddebug
    } catch (Exception e) { }
  }
  
  private static void assignRIDs(boolean storeDefaults) {
    try {
      int RID = 1;
      // RID_VERSION
      if (storeDefaults) RID_VERSION = rs.addRecord(VERSION.getBytes(), 0, VERSION.length());
      else RID_VERSION = RID++;
      // RID_FICS_DNSNAME
      if (storeDefaults) RID_FICS_DNSNAME = rs.addRecord(ficsDNSName.getBytes(), 0, ficsDNSName.length());
      else RID_FICS_DNSNAME = RID++;
      // RID_FICS_PORT
      if (storeDefaults) RID_FICS_PORT = rs.addRecord(ficsPort.getBytes(), 0, ficsPort.length());
      else RID_FICS_PORT = RID++;
      // RID_ICC_DNSNAME
      if (storeDefaults) RID_ICC_DNSNAME = rs.addRecord(iccDNSName.getBytes(), 0, iccDNSName.length());
      else RID_ICC_DNSNAME = RID++;
      // RID_ICC_PORT
      if (storeDefaults) RID_ICC_PORT = rs.addRecord(iccPort.getBytes(), 0, iccPort.length());
      else RID_ICC_PORT = RID++;
      // RID_REMEMBER_LOGIN
      if (storeDefaults) RID_REMEMBER_LOGIN = rs.addRecord(rememberLogin.getBytes(), 0, rememberLogin.length());
      else RID_REMEMBER_LOGIN = RID++;
      // RID_SERVER
      if (storeDefaults) RID_SERVER = rs.addRecord(server.getBytes(), 0, server.length());
      else RID_SERVER = RID++;
      // RID_USERNAME
      if (storeDefaults) RID_USERNAME = rs.addRecord(userName.getBytes(), 0, userName.length());
      else RID_USERNAME = RID++;
      // RID_PASSWORD
      if (storeDefaults) RID_PASSWORD = rs.addRecord(password.getBytes(), 0, password.length());
      else RID_PASSWORD = RID++;
      // RID_SEEK_TIME
      if (storeDefaults) RID_SEEK_TIME = rs.addRecord(seekTime.getBytes(), 0, seekTime.length());
      else RID_SEEK_TIME = RID++;
      // RID_SEEK_INC
      if (storeDefaults) RID_SEEK_INC = rs.addRecord(seekInc.getBytes(), 0, seekInc.length());
      else RID_SEEK_INC = RID++;
      // RID_SEEK_RATED      
      if (storeDefaults) RID_SEEK_RATED = rs.addRecord(seekRated.getBytes(), 0, seekRated.length());
      else RID_SEEK_RATED = RID++;
      // RID_SEEK_MANUAL
      if (storeDefaults) RID_SEEK_MANUAL = rs.addRecord(seekManual.getBytes(), 0 , seekManual.length());
      else RID_SEEK_MANUAL = RID++;
      // RID_SEEK_FORMULA
      if (storeDefaults) RID_SEEK_FORMULA = rs.addRecord(seekFormula.getBytes(), 0, seekFormula.length());
      else RID_SEEK_FORMULA = RID++;
      // RID_SEEK_COLOR
      if (storeDefaults) RID_SEEK_COLOR = rs.addRecord(seekColor.getBytes(), 0, seekColor.length());
      else RID_SEEK_COLOR = RID++;
      // RID_SEEK_RATING_LOWER
      if (storeDefaults) RID_SEEK_RATING_LOWER = rs.addRecord(seekRatingLower.getBytes(), 0, seekRatingLower.length());
      else RID_SEEK_RATING_LOWER = RID++;
      // RID_SEEK_RATING_UPPER
      if (storeDefaults) RID_SEEK_RATING_UPPER = rs.addRecord(seekRatingUpper.getBytes(), 0, seekRatingUpper.length());
      else RID_SEEK_RATING_UPPER = RID++;
      // RID_AUTOPROMOTE_ENABLE
      if (storeDefaults) RID_AUTOPROMOTE_ENABLE = rs.addRecord(autoPromoteEnable.getBytes(), 0, autoPromoteEnable.length());
      else RID_AUTOPROMOTE_ENABLE = RID++;
      // RID_AUTOPROMOTE_PIECE
      if (storeDefaults) RID_AUTOPROMOTE_PIECE = rs.addRecord(autoPromotePiece.getBytes(), 0, autoPromotePiece.length());
      else RID_AUTOPROMOTE_PIECE = RID++;
    } catch (Exception e) { }
  }

  // Server settings
  
  public static void storeFICSDNSName(String name) {
     try {
      rs.setRecord(RID_FICS_DNSNAME, name.getBytes(), 0, name.length());
      ficsDNSName = name;
    } catch (Exception e) { }
  }
  
  public static String getFICSDNSName() {
    return ficsDNSName;
  }
  
  public static void storeFICSPort(int port) {
    if (port >= 0) {
      try {
        final String portStr = Integer.toString(port);
        rs.setRecord(RID_FICS_PORT, portStr.getBytes(), 0, portStr.length());
        ficsPort = portStr;
      } catch (Exception e) { }
    }
  }
  
  public static int getFICSPort() {
    return Integer.parseInt(ficsPort);
  }
  
  public static void storeICCDNSName(String name) {
     try {
      rs.setRecord(RID_ICC_DNSNAME, name.getBytes(), 0, name.length());
      iccDNSName = name;
    } catch (Exception e) { }
  }
  
  public static String getICCDNSName() {
    return iccDNSName;
  }
  
  public static void storeICCPort(int port) {
    if (port >= 0) {
      try {
        final String portStr = Integer.toString(port);
        rs.setRecord(RID_ICC_PORT, portStr.getBytes(), 0, portStr.length());
        iccPort = portStr;
      } catch (Exception e) { }
    }
  }
  
  public static int getICCPort() {
    return Integer.parseInt(iccPort);
  }
  
  // Login settings
  
  public static void storeRememberLogin(boolean remember) {
    try {
      String s = remember ? TRUE_STR : FALSE_STR;
      rs.setRecord(RID_REMEMBER_LOGIN, s.getBytes(), 0, s.length());
      seekRated = s;
    } catch (Exception e) { }
  }
  
  public static boolean getRememberLogin() {
    if (rememberLogin.equals("0")) return false;
    return true;
  }
  
  public static void storeServer(int server_) {
    if (server_ == FICS || server_ == ICC) {
       try {
        final String serverStr = Integer.toString(server_);
        rs.setRecord(RID_SERVER, serverStr.getBytes(), 0, serverStr.length());
        server = serverStr;
      } catch (Exception e) { }
    }
  }
  
  public static int getServer() {
    return Integer.parseInt(server);
  }
  
  public static void storeUserName(String userName_) {
    try {
      rs.setRecord(RID_USERNAME, userName_.getBytes(), 0, userName_.length());
      userName = userName_;
    } catch (Exception e) { }
  }
  
  public static String getUserName() {
    return userName;
  }
  
  public static void storePassword(String password_) {
    try {
      rs.setRecord(RID_PASSWORD, password_.getBytes(), 0, password_.length());
      password = password_;
    } catch (Exception e) { }
  }
  
  public static String getPassword() {
    return password;
  }
  
  // Seek settings
  
  public static void storeSeek(Seek seek) {
    String s;
    try {
      // Seek time
      if (seek.time >= 0) {
        s = Integer.toString(seek.time);
        rs.setRecord(RID_SEEK_TIME, s.getBytes(), 0, s.length());
        seekTime = s;
      }
      
      // Seek inc
      if (seek.inc >= 0) {
        s = Integer.toString(seek.inc);
        rs.setRecord(RID_SEEK_INC, s.getBytes(), 0, s.length());
        seekInc = s;
      }
      
      // Seek rated
      s = seek.rated ? TRUE_STR : FALSE_STR;
      rs.setRecord(RID_SEEK_RATED, s.getBytes(), 0, s.length());
      seekRated = s;
      
      // Seek manual
      s = seek.manual ? TRUE_STR : FALSE_STR;
      rs.setRecord(RID_SEEK_MANUAL, s.getBytes(), 0, s.length());
      seekManual = s;
      
      // Seek formula
      s = seek.formula ? TRUE_STR : FALSE_STR;
      rs.setRecord(RID_SEEK_FORMULA, s.getBytes(), 0, s.length());
      seekFormula = s;
      
      // Seek color
      if (seek.color == Seek.AUTO || seek.color == Seek.WHITE || seek.color == Seek.BLACK) {
        s = Integer.toString(seek.color);
        rs.setRecord(RID_SEEK_COLOR, s.getBytes(), 0, s.length());
        seekColor = s;
      }
      
      // Seek rating lower
      if (seek.ratingLower >= -1) {
        s = Integer.toString(seek.ratingLower);
        rs.setRecord(RID_SEEK_RATING_LOWER, s.getBytes(), 0, s.length());
        seekRatingLower = s;
      }
      
      // Seek rating upper
      if (seek.ratingUpper >= -1) {
        s = Integer.toString(seek.ratingUpper);
        rs.setRecord(RID_SEEK_RATING_UPPER, s.getBytes(), 0, s.length());
        seekRatingUpper = s;
      }
      
    } catch (Exception e) { }
  }
  
  public static Seek getSeek() {
    Seek seek = new Seek();
    seek.time = Integer.parseInt(seekTime);
    seek.inc = Integer.parseInt(seekInc);
    seek.rated = !seekRated.equals(FALSE_STR);
    seek.manual = !seekManual.equals(FALSE_STR);
    seek.formula = !seekFormula.equals(FALSE_STR);
    seek.color = Integer.parseInt(seekColor);
    seek.ratingLower = Integer.parseInt(seekRatingLower);
    seek.ratingUpper = Integer.parseInt(seekRatingUpper);
    return seek;
  }
  
  // Auto-promote settings
  
  public static void storeAutoPromoteEnable(boolean enable) {
    try {
      String s = enable ? TRUE_STR : FALSE_STR;
      rs.setRecord(RID_AUTOPROMOTE_ENABLE, s.getBytes(), 0, s.length());
      autoPromoteEnable = s;
    } catch (Exception e) { }
  }
  
  public static boolean autoPromoteEnable(boolean enable) {
    return !autoPromoteEnable.equals(FALSE_STR);
  }
  
  public static void storeAutoPromotePiece(int piece) {
    if (piece == PROMOTE_TO_QUEEN ||
        piece == PROMOTE_TO_ROOK ||
        piece == PROMOTE_TO_BISHOP ||
        piece == PROMOTE_TO_KNIGHT) {
      try {
        String s = Integer.toString(piece);
        rs.setRecord(RID_AUTOPROMOTE_PIECE, s.getBytes(), 0, s.length());
        autoPromotePiece = s;
      } catch (Exception e) { }
    }
  }
  
  public static int getAutoPromotePiece() {
    return Integer.parseInt(autoPromotePiece);
  }
  
  private Settings() {
  }
}
