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
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import MICC.MICClet;
import MICC.chgame.*;

public class BoardCanvas extends GameCanvas {
  public final Command menuCommand = new Command("Menu", Command.ITEM, 1);
  public final Command exitCommand = new Command("Exit", Command.EXIT, 1);
  
  private int width;
  private int height;
  private int squareSize;
  private int boardSize;
  private int LRMargin;        // Left-right margin
  private int TBMargin;        // Top-bottom margin
  private int offerImageSize;
  
  private Image           miccImage;
  private Board           board;
  private BoardCursor     boardCursor;
  private OfferCursor     offerCursor;
  private ICursor         activeCursor;
  private LastMove        lastMove;
  private BoardInfo       boardInfoTop;
  private BoardInfo       playerInfoBottom;
  private MaterialCounter materialCounter;
  private OfferList       offerList;
  
  // This is the game beeing shown on the canvas.
  private PlayedGame activeGame = null;
  
  private long timeCount = 0;
  private int  cursorState = CURSOR_STATE_BOARD;
  
  private static final int CURSOR_STATE_BOARD  = 0;
  private static final int CURSOR_STATE_OFFERS = 1;
  
  public BoardCanvas() {
    super(false);
    setFullScreenMode(true);
    determineSizes();
    
    // Determine largest possible board font
    if (TBMargin >= UIConst.LARGE_FONT_HEIGHT)  BoardFont.setSize(BoardFont.SIZE_10);
    else if (TBMargin >= UIConst.MEDIUM_FONT_HEIGHT) BoardFont.setSize(BoardFont.SIZE_8);
    else BoardFont.setSize(BoardFont.SIZE_6);
    
    final int boardXPos = LRMargin;  // Left
    final int boardYPos = TBMargin;  // Top          
    final int playerInfoYOffset = (boardYPos - BoardFont.height()) / 2;       // Center
    //final int materialCounterXOffset = (boardXPos - m_LRMargin / 2);        // Center
    final int offerListXOffset = (boardXPos + boardSize + LRMargin / 2);  // Center
    
    // Load images
    MiccImages.loadImages(squareSize, LRMargin);
    offerImageSize = MiccImages.m_offerImages[Offer.DRAW].getHeight();
    
    board  = new Board(squareSize, boardXPos, boardYPos);
    lastMove = new LastMove(squareSize, boardXPos, boardYPos);
    
    // Span screen, center y
    boardInfoTop = new BoardInfo(playerInfoYOffset, width, BoardInfo.MODE_PLAYER);
    boardInfoTop.setPlayerColor(ChConst.BLACK);  // Initially
    // Span screen, center y
    playerInfoBottom = new BoardInfo(boardYPos + board.getBoardSize() + playerInfoYOffset, width, BoardInfo.MODE_PLAYER_AND_SEEK);
    boardInfoTop.setPlayerColor(ChConst.WHITE);  // Initially
    
    // Material counter
    // TODO: Change constr.???
    materialCounter = new MaterialCounter(0, TBMargin, LRMargin, boardSize);
    
    // Offer list (of icons)
    offerList = new OfferList(offerListXOffset, TBMargin, offerImageSize);
    
    // Cursors
    boardCursor = new BoardCursor(squareSize, boardXPos, boardYPos);
    board.setBoardCursor(boardCursor);
    offerCursor = new OfferCursor(offerListXOffset, TBMargin, offerImageSize);
    activeCursor = boardCursor;
    
    addCommand(menuCommand);
    addCommand(exitCommand);
    
    try {
      miccImage = Image.createImage("/Caissa_main.png");
    } catch (IOException ioe) { }
  }
  
  // This is a fix for some strange behavior. The canvas goes back
  // to non-fullscreen when a form has been displayed.
  protected void showNotify() {
    setFullScreenMode(true);
  }
  
  private void determineSizes() {
    width  = getWidth();
    height = getHeight();
    final int XLARGE_SCREEN_LIMIT = UIConst.XLARGE_BOARD_SIZE + 2 * UIConst.MINIMUM_BOARD_MARGIN;
    final int LARGE_SCREEN_LIMIT  = UIConst.LARGE_BOARD_SIZE  + 2 * UIConst.MINIMUM_BOARD_MARGIN;
    final int MID_SCREEN_LIMIT    = UIConst.MID_BOARD_SIZE    + 2 * UIConst.MINIMUM_BOARD_MARGIN;
    final int SMALL_SCREEN_LIMIT  = UIConst.SMALL_BOARD_SIZE  + 2 * UIConst.MINIMUM_BOARD_MARGIN;
    
    // Which is the largest board we can display and still have room
    // for game info texts, icons etc?
    // First assume that fullscreen is not needed.
    if (width >= XLARGE_SCREEN_LIMIT && height >= XLARGE_SCREEN_LIMIT) {
      boardSize = UIConst.XLARGE_BOARD_SIZE;
    } else if (width >= LARGE_SCREEN_LIMIT && height >= LARGE_SCREEN_LIMIT) {
      boardSize = UIConst.LARGE_BOARD_SIZE;
    } else if (width >= MID_SCREEN_LIMIT && height >= MID_SCREEN_LIMIT) {
      boardSize = UIConst.MID_BOARD_SIZE;
    } else {
      width = getWidth();
      height = getHeight();
      boardSize = UIConst.SMALL_BOARD_SIZE;
    }
    
    squareSize = boardSize / 8;
    
    // Determine left-right and top-bottom margins.
    LRMargin = (width - boardSize) / 2;
    TBMargin = (height - boardSize) / 2;
  }
  
  protected void keyPressed(int keyCode) {
    int     state = MICClet.gameControl.getState();
    boolean keyCodeAssigned = true;
    PlayedGame    gameData = MICClet.gameControl.getPlayedGame();
    int     gameAction = getGameAction(keyCode);
    
    if (keyCode == KEY_POUND) {
      // Switch cursor
      if ( activeCursor == boardCursor &&
           !MICClet.gameControl.getOffers().isEmpty() ) {
        activeCursor = offerCursor;
        offerCursor.resetPos();
      }
      else {
        activeCursor = boardCursor;
      }
    }
    else {      
      switch (gameAction) {
        case UP:
          keyCode = KEY_NUM2;
          break;

        case DOWN:
          keyCode = KEY_NUM8;
          break;

        case LEFT:
          keyCode = KEY_NUM4;
          break;

        case RIGHT:
          keyCode = KEY_NUM6;
         break;

        case FIRE:
          keyCode = KEY_NUM5;
          break;

        default:
          break;
      }
      activeCursor.handleKey(keyCode);
    }
  }
  
  public void paint(long elapsedTime) {
    int xpos;
    int ypos;
    Graphics g = getGraphics();
    int state = MICClet.gameControl.getState();
    PlayedGame game = MICClet.gameControl.getActiveGame();

    if (activeCursor == offerCursor &&
        MICClet.gameControl.getState() != GameControl.STATE_PLAYING &&
        MICClet.gameControl.getState() != GameControl.STATE_PAUSED)
      activeCursor = boardCursor;
    
    g.setColor(0x0);
    g.fillRect(0, 0, width, height);
    
    if (state == GameControl.STATE_DISCONNECTED ||
        state == GameControl.STATE_CONNECTING   ||
        game == null)
    {
      xpos = width / 2;
      ypos = miccImage.getHeight() + 20;
      g.drawImage(miccImage, xpos, ypos, Graphics.BOTTOM | Graphics.HCENTER);
      g.setColor(UIConst.LIGHT_BLUE_COLOR);
      g.setFont(UIConst.SMALL_FONT);
      ypos += 5;
      g.drawString("Your Mobile Chess Pal", xpos, ypos, Graphics.TOP | Graphics.HCENTER);
      ypos = height - 20;
      
      if (state == GameControl.STATE_DISCONNECTED) {
        g.setColor(UIConst.GREY_COLOR);
        g.drawString("Disconnected", xpos, ypos, Graphics.BOTTOM | Graphics.HCENTER);
      }
      else if (state == GameControl.STATE_CONNECTING) {
        g.setColor(UIConst.YELLOW_COLOR);
        g.drawString("Connecting...", xpos, ypos, Graphics.BOTTOM | Graphics.HCENTER);
      } else {
        g.setColor(UIConst.LIGHT_BLUE_COLOR);
        g.drawString(MICClet.gameControl.getUserName(), xpos, ypos, Graphics.BOTTOM | Graphics.HCENTER);
        ypos -= 20;
        g.drawString("Connected as", xpos, ypos, Graphics.BOTTOM | Graphics.HCENTER);
      }
    }
    else if (state == GameControl.STATE_IDLE ||
             state == GameControl.STATE_PLAYING ||
             state == GameControl.STATE_PAUSED) {
      board.paintSquares(g);
      board.paintPieces(g, game);
      lastMove.paint(g);
      boardInfoTop.paint(g);
      playerInfoBottom.paint(g);
      materialCounter.paint(g);
      offerList.paint(g);
      activeCursor.paint(g);
    }
    
    flushGraphics();
  }
  
  public BoardInfo getPlayerInfoTop() {
    return boardInfoTop;
  }
  
  public BoardInfo getPlayerInfoBottom() {
    return playerInfoBottom;
  }
}

