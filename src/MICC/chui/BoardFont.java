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

public class BoardFont {
  public static final int SIZE_6      = 0;
  public static final int SIZE_8      = 1;
  public static final int SIZE_10     = 2;
  public static final int NO_OF_SIZES = 3;
  public static final int LEFT        = 0;
  public static final int RIGHT       = 1;
  
  private static Image[] m_fontImages = new Image[NO_OF_SIZES];
  private static Image   m_currImage = null;
  private static int[]   m_fontWidths = { 5 , 7 , 9};
  private static int[]   m_fontHeights = { 8 , 12 , 15};
  private static int     m_size = -1;
  
  private BoardFont() {
  }
  
  public static void setSize(int size) {
    m_size = size;
    if (m_fontImages[m_size] == null) load(m_size);
    m_currImage = m_fontImages[m_size];
  }

  public static void drawString(Graphics g, String s, int xpos, int ypos, int anchor) {
    if (g != null && s != null && m_currImage != null) {
      int    i;
      char[] chars = s.toCharArray();
      
      if (anchor == RIGHT) xpos -= width(s);
      
      for (i = 0; i < chars.length; ++i) {
        int index = Math.max(chars[i] - ' ', 0);
        index = Math.min(index, 0x7f);
        g.drawRegion(m_currImage, index * m_fontWidths[m_size], 0, m_fontWidths[m_size], m_currImage.getHeight(), 0,
                     xpos, ypos, Graphics.TOP | Graphics.LEFT);
        xpos += m_fontWidths[m_size];
      }
    }
  }
  
  private static void load(int size) {
    try {
      String fontImageFileName;
      if (size == SIZE_6)       fontImageFileName = "/miccfont_white_6.png";
      else if (size == SIZE_8)  fontImageFileName = "/miccfont_white_8.png";
      else if (size == SIZE_10) fontImageFileName = "/miccfont_white_10.png";
      else fontImageFileName = "/miccfont_white_10.png";
      m_fontImages[size] = Image.createImage(fontImageFileName);
    } catch (IOException ioe) { }
  }
  
  private static int width(String s) {
    return m_fontWidths[m_size] * s.length();
  }
  
  public static int height() {
    if (m_size != -1) return m_fontHeights[m_size];
    else return -1;
  }
}
