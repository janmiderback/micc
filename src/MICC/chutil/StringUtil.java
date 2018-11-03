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

package MICC.chutil;

import java.util.Vector;

public final class StringUtil
{
  private StringUtil()
  {
  }
  
  public static void splitString(String s, int splitChar, Vector sv) {
    int     beginIndex = 0;
    boolean done = false;
    String  line;
    
    do 
    {
      int nlIndex = s.indexOf(splitChar, beginIndex);
      
      if (nlIndex == -1)
      {
        // Add remaining string
        line = s.substring(beginIndex);
        done = true;
      }
      else
      {
        line = s.substring(beginIndex, nlIndex);
        beginIndex = nlIndex + 1;
      }
      
      if (line.length() != 0) sv.addElement(line);  // Ignore empty strings
      
    } while (!done);
  }
  
  public String formSquare(int file, int rank) {
    StringBuffer sb = new StringBuffer();
    sb.append((char)('a' + file));
    sb.append((char)('1' + rank));
    return sb.toString();
  }

}
