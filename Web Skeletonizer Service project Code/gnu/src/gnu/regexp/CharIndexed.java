package gnu.regexp;

interface CharIndexed {
  public static final char OUT_OF_BOUNDS = '\uFFFF';

  public char charAt(int index);
  public boolean move(int index);
  public boolean isValid();
}
