
package gnu.regexp;

class RETokenBackRef extends REToken {
  private int num;
  private boolean insens;
  
  RETokenBackRef(int f_subIndex, int mynum, boolean ins) {
    super(f_subIndex);
    insens = ins;
    num = mynum;
  }

  // should implement getMinimumLength() -- any ideas?

  int[] match(CharIndexed input, int index, int eflags, REMatch mymatch) {
    int b,e;
    b = mymatch.start[num];
    e = mymatch.end[num];
    if ((b==-1)||(e==-1)) return null; // this shouldn't happen, but...
    for (int i=b; i<e; i++) {
      if (input.charAt(index+i-b) != input.charAt(i)) return null;
    }

    return next(input,index+e-b,eflags,mymatch);
  }

  void dump(StringBuffer os) {
    os.append('\\').append(num);
  }
}


