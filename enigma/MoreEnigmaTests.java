package enigma;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;

/** Some extra tests for Enigma.
 *  @author William Tai
 */
public class MoreEnigmaTests {

    @Test
    public void testDoubleStep() {
        Alphabet ac = new CharacterRange('A', 'D');
        Rotor one = new Reflector("R1",
                new Permutation("(AC) (BD)", ac));
        Rotor two = new MovingRotor("R2",
                new Permutation("(ABCD)", ac), "C");
        Rotor three = new MovingRotor("R3",
                new Permutation("(ABCD)", ac), "C");
        Rotor four = new MovingRotor("R4",
                new Permutation("(ABCD)", ac), "C");
        String setting = "AAA";
        Rotor[] machineRotors = {one, two, three, four};
        String[] rotors = {"R1", "R2", "R3", "R4"};
        Machine mach = new Machine(ac, 4, 3,
                new ArrayList<>(Arrays.asList(machineRotors)));
        mach.insertRotors(rotors);
        mach.setRotors(setting);

        assertEquals("AAAA", getSetting(ac, machineRotors));
        mach.convert('A');
        assertEquals("AAAB", getSetting(ac, machineRotors));
        mach.convert('B');
        assertEquals("AAAC", getSetting(ac, machineRotors));
        mach.convert('C');
        assertEquals("AABD", getSetting(ac, machineRotors));
        mach.convert('D');
        assertEquals("AABA", getSetting(ac, machineRotors));
    }

    /** Helper method to get the String
     * representation of the current Rotor settings */
    private String getSetting(Alphabet alph, Rotor[] machineRotors) {
        String currSetting = "";
        for (Rotor r : machineRotors) {
            currSetting += alph.toChar(r.setting());
        }
        return currSetting;
    }
}
