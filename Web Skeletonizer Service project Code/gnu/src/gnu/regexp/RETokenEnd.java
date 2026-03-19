package gnu.regexp;

class RETokenEnd extends REToken {
  private boolean newline;

  RETokenEnd(int f_subIndex,boolean f_newline) { 
    super(f_subIndex);
    newline = f_newline;
  }

  int[] match(CharIndexed input, int index, int eflags, REMatch mymatch) {
    // this may not work on systems that use \r\n as line separator. FIXME
    // use System.getProperty("line.separator");
    char ch = input.charAt(index);
    if (ch == CharIndexed.OUT_OF_BOUNDS)
      return ((eflags & RE.REG_NOTEOL)>0) ? 
	null : next(input,index,eflags,mymatch);
    return (newline && (ch == '\n')) ? 
      next(input,index,eflags,mymatch) : null;
  }

  void dump(StringBuffer os) {
    os.append('$');
  }
}
