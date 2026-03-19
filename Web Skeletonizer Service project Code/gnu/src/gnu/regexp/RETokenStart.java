package gnu.regexp;

class RETokenStart extends REToken {
  private boolean newline; // matches after a newline
  
  RETokenStart(int f_subIndex, boolean f_newline) {
    super(f_subIndex);
    newline = f_newline;
  }
  
  int[] match(CharIndexed input, int index, int eflags, REMatch mymatch) {
    // charAt(index-1) may be unknown on an InputStream. FIXME
    // Match after a newline if in multiline mode
    if (newline && (mymatch.offset > 0) && (input.charAt(index - 1) == '\n')) 
      return next(input,index,eflags,mymatch);

    // Don't match at all if REG_NOTBOL is set.
    if ((eflags & RE.REG_NOTBOL) > 0) return null;
    
    if ((eflags & RE.REG_ANCHORINDEX) > 0)
      return (mymatch.anchor == mymatch.offset) ? 
	next(input,index,eflags,mymatch) : null;
    else
      return ((index == 0) && (mymatch.offset == 0)) ?
	next(input,index,eflags,mymatch) : null;
  }

  void dump(StringBuffer os) {
    os.append('^');
  }
}
