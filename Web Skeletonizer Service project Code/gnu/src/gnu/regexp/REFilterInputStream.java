
package gnu.regexp;
import java.io.FilterInputStream;
import java.io.InputStream;


public class REFilterInputStream extends FilterInputStream {

  private RE m_expr;
  private String m_replace;
  private String m_buffer;
  private int m_bufpos;
  private int m_offset;
  private CharIndexedInputStream m_stream;

  public REFilterInputStream(InputStream f_stream, RE f_expr, String f_replace) {
    super(f_stream);
    m_stream = new CharIndexedInputStream(f_stream,0);
    m_expr = f_expr;
    m_replace = f_replace;
  }

  /**
   * Reads the next byte from the stream per the general contract of
   * InputStream.read().  Returns -1 on error or end of stream.
   */
  public int read() {
    // If we have buffered replace data, use it.
    if ((m_buffer != null) && (m_bufpos < m_buffer.length())) {
      return (int) m_buffer.charAt(m_bufpos++);
    }

    // check if input is at a valid position
    if (!m_stream.isValid()) return -1;

    REMatch mymatch = new REMatch(m_expr.getNumSubs(),m_offset);
    int[] result = m_expr.match(m_stream,0,0,mymatch);
    if (result != null) {
      mymatch.end[0] = result[0];
      mymatch.finish(m_stream);
      m_stream.move(mymatch.toString().length());
      m_offset += mymatch.toString().length();
      m_buffer = mymatch.substituteInto(m_replace);
      m_bufpos = 1;

      // This is prone to infinite loops if replace string turns out empty.
      return m_buffer.charAt(0);
    } else {
      char ch = m_stream.charAt(0);
      if (ch == CharIndexed.OUT_OF_BOUNDS) return -1;
      m_stream.move(1);
      m_offset++;
      return ch;
    }
  }

  /** 
   * Returns false.  REFilterInputStream does not support mark() and
   * reset() methods. 
   */
  public boolean markSupported() {
    return false;
  }

  /** Reads from the stream into the provided array. */
  public int read(byte[] b, int off, int len) {
    int i;
    int ok = 0;
    while (len-- > 0) {
      i = read();
      if (i == -1) return (ok == 0) ? -1 : ok;
      b[off++] = (byte) i;
      ok++;
    }
    return ok;
  }

  /** Reads from the stream into the provided array. */
  public int read(byte[] b) {
    return read(b,0,b.length);
  }
}
