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

public final class UIConst {
  // Screens
  public static final int ASSUMED_SCREEN_SIZE = 128;
  public static final int ASSUMED_SQUARE_SIZE = 14;
  public static final int SMALL_SCREEN_HEIGHT = 128;
  public static final int SMALL_SQUARE_SIZE = 14;
  public static final int SMALL_BOARD_SIZE = 8 * SMALL_SQUARE_SIZE;
  public static final int MID_SCREEN_HEIGHT = 160;
  public static final int MID_SQUARE_SIZE = 14;
  public static final int MID_BOARD_SIZE = 8 * MID_SQUARE_SIZE;
  public static final int LARGE_SCREEN_HEIGHT = 220;
  public static final int LARGE_SQUARE_SIZE = 19;
  public static final int LARGE_BOARD_SIZE = 8 * LARGE_SQUARE_SIZE;
  public static final int XLARGE_SCREEN_HEIGHT = 320;
  public static final int XLARGE_SQUARE_SIZE = 27;
  public static final int XLARGE_BOARD_SIZE = 8 * XLARGE_SQUARE_SIZE;
  
  public static final int MINIMUM_BOARD_MARGIN = 8;
  
  // MICC font properties
  public static final int SMALL_FONT_HEIGHT  = 8;
  public static final int MEDIUM_FONT_HEIGHT = 12;
  public static final int LARGE_FONT_HEIGHT  = 15;
  
  // System fonts
  public static final Font BIG_FONT   = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
  public static final Font SMALL_FONT = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
  
  // Colors
  public static final int WHITE_COLOR      = 0xffffff;
  public static final int LIGHT_BLUE_COLOR = 0xccccff;
  public static final int BLUE_COLOR       = 0x0000ff; 
  public static final int YELLOW_COLOR     = 0xffff00;
  public static final int GREY_COLOR       = 0xa0a0a0;
  public static final int RED_COLOR        = 0xff0000;
  public static final int GREEN_COLOR      = 0x00ff00;
  
  public static final int CURSOR_COLOR    = RED_COLOR;  // Red
  public static final int LAST_MOVE_COLOR = BLUE_COLOR; // Blue
  
  private UIConst() { }
}
