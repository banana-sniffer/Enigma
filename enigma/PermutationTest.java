package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testInvertChar() {
        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)",
                new CharacterRange('A', 'Z'));
        Permutation p1 = new Permutation("()", new CharacterRange('A', 'Z'));
        assertEquals(p.invert('B'), 'A');
        assertEquals(p.invert('G'), 'G');
        assertEquals(p.invert('P'), 'H');
        assertEquals(p.invert('J'), 'C');
        assertEquals(p.invert('C'), 'J');
        assertEquals(p1.invert('W'), 'W');

        Permutation p2 = new Permutation("(WHZ) (QRSTUV) (ILSM)",
                new CharacterRange('A', 'Z'));
        assertEquals(p2.invert('L'), 'I');
        assertEquals(p2.invert('D'), 'D');
        assertEquals(p2.invert('J'), 'J');
    }

    @Test
    public void testPermuteChar() {
        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)",
                new CharacterRange('A', 'Z'));
        Permutation p1 = new Permutation("()",
                new CharacterRange('A', 'Z'));
        assertEquals(p.permute('P'), 'N');
        assertEquals(p.permute('G'), 'G');
        assertEquals(p.permute('A'), 'B');
        assertEquals(p.permute('J'), 'C');
        assertEquals(p.permute('C'), 'J');
        assertEquals(p1.permute('Q'), 'Q');
        assertEquals(p1.permute('J'), 'J');
        assertEquals(p1.permute('W'), 'W');
    }

    @Test
    public void testDerangement() {
        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)",
                new CharacterRange('A', 'Z'));
        Permutation p1 = new Permutation("(ABCDEFGHIJKLMNOPQRSTUVWXYZ)",
                new CharacterRange('A', 'Z'));
        assertFalse(p.derangement());
        assertTrue(p1.derangement());
    }

}
