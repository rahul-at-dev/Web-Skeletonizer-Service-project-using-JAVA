package gnu.regexp.util;
import gnu.regexp.RESyntax;

public class Egrep {
  private Egrep() { }

  /**
   * Invokes Grep.grep() using the RE_SYNTAX_EGREP syntax and the
   * command line arguments specified.  Output is sent to System.out.
   * For a list of options, use the argument "--help".
   */
  public static void main(String[] argv) {
    System.exit(Grep.grep(argv,RESyntax.RE_SYNTAX_EGREP,System.out));
  }
}

