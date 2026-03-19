package gnu.regexp;

class CharIndexedCharArray implements CharIndexed {
  private char[] s;
  private int m_index;

  CharIndexedCharArray(char[] str, int index) {
    s = str;
    m_index = index;
  }

  public char charAt(int index) {
    return ((m_index + index) < s.length) ? s[m_index + index] : CharIndexed.OUT_OF_BOUNDS;
  }

  public boolean isValid() {
    return (m_index < s.length);
  }

  public boolean move(int index) {
    return ((m_index += index) < s.length);
  }
}
