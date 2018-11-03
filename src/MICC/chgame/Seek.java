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

public class Seek {
  public int time;
  public int inc;
  public boolean rated;
  public boolean manual;
  public boolean formula;
  public int color;
  public int ratingLower;
  public int ratingUpper;
  
  public static final int AUTO  = 0;
  public static final int WHITE = 1;
  public static final int BLACK = 2;
  
  public Seek() {
  }
  
  public Seek clone() {
    Seek theClone = new Seek();
    theClone.time = this.time;
    theClone.inc = this.inc;
    theClone.rated = this.rated;
    theClone.manual = this.manual;
    theClone.formula = this.formula;
    theClone.color = this.color;
    theClone.ratingLower = this.ratingLower;
    theClone.ratingUpper = this.ratingUpper;
    return theClone;
  }
  
  public boolean equals(Seek seek_) {
    return seek_.time == this.time &&
           seek_.inc == this.inc &&
           seek_.rated == this.rated &&
           seek_.manual == this.manual &&
           seek_.formula == this.formula &&
           seek_.color == this.color &&
           seek_.ratingLower == this.ratingLower &&
           seek_.ratingUpper == this.ratingUpper;
  }
  
}
