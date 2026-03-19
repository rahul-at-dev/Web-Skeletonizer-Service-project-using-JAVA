
package gnu.regexp;
import java.util.Vector;

class RETokenRepeated extends REToken {
  private REToken token;
  private int min,max;
  private boolean stingy;

  RETokenRepeated(int f_subIndex, REToken f_token, int f_min, int f_max) {
    super(f_subIndex);
    token = f_token;
    min = f_min;
    max = f_max;
  }
 
  void makeStingy() {
    stingy = true;
  }

  int getMinimumLength() {
    return (min * token.getMinimumLength());
  }

  int[] match(CharIndexed input, int index,int eflags,REMatch mymatch) {
    int numRepeats = 0;
    Vector positions = new Vector();
    int[] newIndex = new int[] { index };
    do {
      // positions.elementAt(i) == position [] in input after <<i>> matches
      positions.addElement(newIndex);
      
      // Check for stingy match for each possibility.
      if (stingy && (numRepeats >= min)) {
	for (int i = 0; i < newIndex.length; i++) {
	  int[] s = next(input,newIndex[i],eflags,mymatch);
	  if (s != null) return s;
	}
      }

      int[] doables = new int[0];
      int[] thisResult;
      for (int i = 0; i < newIndex.length; i++) {
	if ((thisResult = token.match(input,newIndex[i],eflags,mymatch)) != null) {
	  // add to doables array
	  int[] temp = new int[doables.length + thisResult.length];
	  System.arraycopy(doables,0,temp,0,doables.length);
	  for (int j = 0; j < thisResult.length; j++) {
	    temp[doables.length + j] = thisResult[j];
	  }
	  doables = temp;
	}
      }
      if (doables.length == 0) break;

      newIndex = doables;
    } while (numRepeats++ < max);

    // If there aren't enough repeats, then fail
    if (numRepeats < min) return null;
    
    // We're greedy, but ease off until a true match is found 
    int posIndex = positions.size();
    
    // At this point we've either got too many or just the right amount.
    // See if this numRepeats works with the rest of the regexp.
    int[] doneIndex;
    while (--posIndex >= min) {
      newIndex = (int[]) positions.elementAt(posIndex);
      // If rest of pattern matches
      for (int i = 0; i < newIndex.length; i++) 
	if ((doneIndex = next(input,newIndex[i],eflags,mymatch)) != null)
	  return doneIndex;
      
      // else did not match rest of the tokens, try again on smaller sample
    }
    return null;
  }

  void dump(StringBuffer os) {
    os.append('(');
    if (token.m_subIndex == 0)
      os.append("?:");
    token.dumpAll(os);
    os.append(')');
    if ((max == Integer.MAX_VALUE) && (min <= 1))
      os.append( (min == 0) ? '*' : '+' );
    else if ((min == 0) && (max == 1))
      os.append('?');
    else {
      os.append('{').append(min);
      if (max > min) {
	os.append(',');
	if (max != Integer.MAX_VALUE) os.append(max);
      }
      os.append('}');
    }
    if (stingy) os.append('?');
  }
}
