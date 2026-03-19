
package gnu.regexp;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public class REMatchEnumeration implements Enumeration {
  private static final int YES = 1;
  private static final int MAYBE = 0;
  private static final int NO = -1;
  
  private int m_more;
  private REMatch m_match;
  private RE m_expr;
  private CharIndexed m_input;
  private int m_index;
  private int m_eflags;
  private StringBuffer m_buffer;

  // Package scope constructor is used by RE.getMatchEnumeration()
  REMatchEnumeration(RE expr, CharIndexed input, int index, int eflags) {
    m_more = MAYBE;
    m_expr = expr;
    m_input = input;
    m_index = index;
    m_eflags = eflags;
  }

  /** Returns true if there are more matches in the input text. */
  public boolean hasMoreElements() {
    return hasMoreMatches(null);
  }

  /** Returns true if there are more matches in the input text. */
  public boolean hasMoreMatches() {
    return hasMoreMatches(null);
  }

  /** Returns true if there are more matches in the input text.
   * Saves the text leading up to the match (or to the end of the input)
   * in the specified buffer.
   */
  public boolean hasMoreMatches(StringBuffer f_buffer) {
    if (m_more == MAYBE) {
      m_match = m_expr.getMatchImpl(m_input,m_index,m_eflags,f_buffer);
      if (m_match != null) {
	m_index = m_match.getEndIndex();
	m_input.move((m_match.end[0] > 0) ? m_match.end[0] : 1);
	m_more = YES;
      } else m_more = NO;
    }
    return (m_more == YES);
  }

  /** Returns the next match in the input text. */
  public Object nextElement() throws NoSuchElementException {
    return nextMatch();
  }

  /** 
   * Returns the next match in the input text. This method is provided
   * for convenience to avoid having to explicitly cast the return value
   * to class REMatch.
   */
  public REMatch nextMatch() throws NoSuchElementException {
    if (hasMoreElements()) {
      m_more = MAYBE;
      return m_match;
    }
    throw new NoSuchElementException();
  }
}
