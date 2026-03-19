
package gnu.regexp;

class RETokenRange extends REToken {
  private char lo, hi;
  private boolean insens;

  RETokenRange(int f_subIndex, char f_lo, char f_hi, boolean ins) {
    super(f_subIndex);
    lo = (insens = ins) ? Character.toLowerCase(f_lo) : f_lo;
    hi = ins ? Character.toLowerCase(f_hi) : f_hi;
  }

  int getMinimumLength() {
    return 1;
  }

  int[] match(CharIndexed input, int index, int eflags, REMatch mymatch) {
    char c = input.charAt(index);
    if (c == CharIndexed.OUT_OF_BOUNDS) return null;
    if (insens) c = Character.toLowerCase(c);
    return ((c >= lo) && (c <= hi)) ? 
      next(input,index+1,eflags,mymatch) : null;
  }

  void dump(StringBuffer os) {
    os.append(lo).append('-').append(hi);
  }
}

