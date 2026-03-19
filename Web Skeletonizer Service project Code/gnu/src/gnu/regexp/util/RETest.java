package gnu.regexp.util;
import gnu.regexp.*;

public class RETest {
  private RETest() { }

  /**
   * Invokes the test function with the command line arguments specified.
   * See class description for usage notes.
   *
   * @param argv
   * The command line arguments.
   *
   * @exception REException
   * There was an error compiling or executing the regular expression.
   */
  public static void main(String argv[]) throws REException {
    System.out.println("gnu.regexp version "+RE.version());
    
    int numRepeats = 1;
    if (argv.length == 3)
      numRepeats = Integer.parseInt(argv[2]);
    if (argv.length < 2) {
      System.out.println("usage: java gnu.regexp.util.RETest regExp inputString [numRepeats]");
      System.exit(1);
    }
    
    // Construct the regular expression

    RE expression = null;
    long begin = System.currentTimeMillis();

    for (int rpt = 0; rpt < numRepeats; rpt++)
      expression = new RE(argv[0]);

    long end = System.currentTimeMillis();
    
    if (numRepeats>1) {
      System.out.println("Compiling "+numRepeats+" times took "+(end-begin)+" ms");
      System.out.println("Average compile time: "+((end-begin)/numRepeats)+" ms");
    }

    // Display regular expression
    System.out.println("        Input Text: "+argv[1]);
    System.out.println("Regular Expression: "+argv[0]);
    System.out.println("     Compiled Form: "+expression);

    // Is the input in its entirety a match?
    
    System.out.println(" isMatch() returns: "+expression.isMatch(argv[1]));
    
    REMatch[] matches = expression.getAllMatches(argv[1]);
    System.out.println("   getAllMatches(): " + matches.length + " matches");
    for (int i = 0; i < matches.length; i++) {
      System.out.println("Match " + i + " (" + matches[i].getStartIndex()
			 + "," + matches[i].getEndIndex() + "): "
			 + matches[i]);
    }

    // Get the first match    
    REMatch match = null;

    begin = System.currentTimeMillis();

    for (int rpt = 0; rpt < numRepeats; rpt++)
      match = expression.getMatch(argv[1]);

    end = System.currentTimeMillis();

    if (numRepeats>1) {
      System.out.println("Finding first match "+numRepeats+" times took "+(end-begin)+" ms");
      System.out.println("Average match time: "+((end-begin)/numRepeats)+" ms");
    }

    if (match == null)
      System.out.println("Expression did not find a match.");
    else {
      // Report the full match indices

      System.out.println("Match found from position "
			 + match.getStartIndex() + " to position "
			 + match.getEndIndex());
      
      // Take advantage of REMatch.toString() to print match text
      
      System.out.println("Match was: '" + match + "'");
      
      // Report subexpression positions
      
      for (int i=1; i <= expression.getNumSubs(); i++) {
	if (match.getSubStartIndex(i) > -1) {
	  System.out.println("Subexpression #" + i + ": from position "
			     + match.getSubStartIndex(i) + " to position "
			     + match.getSubEndIndex(i));
		
	  // Note how the $n is constructed for substituteInto
		
	  System.out.println(match.substituteInto("The subexpression matched this text: '$"+i+"'"));
	}
      }
    }

    // Here's a substitute test.
    System.out.println("substitute(): " + expression.substitute(argv[1],"<!--$0-->"));
    System.out.println("substituteAll(): " + expression.substituteAll(argv[1],"<!--$0-->"));
  }
}
