package enigma;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/** Class used to test the integration of the entire program.
 * @author William Tai
 * */
public class Integration {

    /**
     * Test to see if convert works.
     * */
    @Test
    public void testMachineIntConvert() {
        Alphabet az = new CharacterRange('A', 'Z');
        Rotor one = new Reflector("B",
                new Permutation("(AE) (BN) (CK) (DQ) (FU) "
                        + "(GY) (HW) (IJ) (LO) "
                        + "(MP) (RX) (SZ) (TV)", az));
        Rotor two = new FixedRotor("Beta",
                new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", az));
        Rotor three = new MovingRotor("I",
                new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
                        + "(DFG) (IV) (JZ) (S)", az),
                "Q");
        Rotor four = new MovingRotor("II",
                new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) "
                        + "(GR) (NT) (A) (Q)", az),
                "E");
        Rotor five = new MovingRotor("III",
                new Permutation("(ABDHPEJT) "
                        + "(CFLVMZOYQIRWUKXSG) (N)", az),
                "V");
        Permutation plugboard = new Permutation("(AQ) (EP)", az);
        String setting = "AAAA";
        Rotor[] machineRotors = {one, two, three, four, five};
        String[] rotors = {"B", "Beta", "I", "II", "III"};
        Machine mach = new Machine(az, 5, 3,
                new ArrayList<>(Arrays.asList(machineRotors)));
        mach.insertRotors(rotors);
        mach.setRotors(setting);

        assertEquals("AAAAA", getSetting(az, machineRotors));
        assertEquals('I', mach.convert('H'));
        assertEquals('L', mach.convert('E'));
        assertEquals('B', mach.convert('L'));
        assertEquals('D', mach.convert('L'));
        assertEquals('A', mach.convert('O'));
        assertEquals('A', mach.convert('W'));
        assertEquals('M', mach.convert('O'));
        assertEquals('T', mach.convert('R'));
        assertEquals('A', mach.convert('L'));
        assertEquals('Z', mach.convert('D'));
    }
    /**
     * Tests to see if the string converts works.
     * */
    @Test
    public void testMachineStringConvert() {
        Alphabet az = new CharacterRange('A', 'Z');
        Rotor one = new Reflector("B",
                new Permutation("(AE) (BN) (CK) (DQ) (FU) "
                        + "(GY) (HW) (IJ) (LO) "
                        + "(MP) (RX) (SZ) (TV)", az));
        Rotor two = new FixedRotor("Beta",
                new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", az));
        Rotor three = new MovingRotor("I",
                new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
                        + "(DFG) (IV) (JZ) (S)", az),
                "Q");
        Rotor four = new MovingRotor("II",
                new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) "
                        + "(GR) (NT) (A) (Q)", az),
                "E");
        Rotor five = new MovingRotor("III",
                new Permutation("(ABDHPEJT) "
                        + "(CFLVMZOYQIRWUKXSG) (N)", az),
                "V");
        Permutation plugboard = new Permutation("(AQ) (EP)", az);
        String setting = "AAAA";
        Rotor[] machineRotors = {one, two, three, four, five};
        String[] rotors = {"B", "Beta", "I", "II", "III"};
        Machine mach = new Machine(az, 5, 3,
                new ArrayList<>(Arrays.asList(machineRotors)));
        mach.insertRotors(rotors);
        mach.setRotors(setting);

        Machine mach1 = new Machine(az, 5, 3,
                new ArrayList<>(Arrays.asList(machineRotors)));
        mach1.setPlugboard(plugboard);
        mach1.insertRotors(rotors);
        mach1.setRotors(setting);

        assertEquals("IHBDQQMTQZ", mach1.convert("Hello world"));

    }

    /** Helper method to get the String.
     * representation of the current Rotor settings.
     * @param alph alphabet
     * @param machineRotors machine rotors
     * @return currSetting string. */
    private String getSetting(Alphabet alph, Rotor[] machineRotors) {
        String currSetting = "";
        for (Rotor r : machineRotors) {
            currSetting += alph.toChar(r.setting());
        }
        return currSetting;
    }
}
