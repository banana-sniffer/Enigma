package enigma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author William Tai
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */

    /** The number of Rotors. */
    private int _numRotors;

    /** The number of pawls. */
    private int _pawls;

    /** All the rotors. */
    private HashMap<String, Rotor> _allRotors;

    /** Rotors that will be used. */
    private Rotor[] usedRotors;

    /** The plugboard. */
    private Permutation _plugboard;

    /** Initializes a Machine.
     *
     * @param alpha the alphabet
     * @param numRotors the number of Rotors
     * @param pawls the number of pawls
     * @param allRotors all the Rotors
     *
     * */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {

        if (!(numRotors > 1 && pawls >= 0 && pawls < numRotors)) {
            throw new EnigmaException("Bad rotor/pawl fool");
        }
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = new HashMap<>();
        usedRotors = new Rotor[numRotors];
        for (Rotor rotor: allRotors) {
            _allRotors.put(rotor.name(), rotor);
        }
        _plugboard = new Permutation("", alpha);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        /* Take the rotors in the string of rotors look inside the allRotors
        * collection and assign them to some rotor array that contains the
        * rotors that you are going to use and then apply all functions to
        * use usedRotors rather than allRotors*/
        int count = 0;
        for (String s: rotors) {
            if (!_allRotors.containsKey(s)) {
                throw EnigmaException.error("Rotor not found fool!");
            }
        }
        for (int i = 0; i < rotors.length; i++) {
            usedRotors[i] = _allRotors.get(rotors[i]);
            if (usedRotors[i].rotates()) {
                count += 1;
            }
        }
        if (numPawls() != count) {
            throw new EnigmaException("Pawls != moving rotors");
        }
        if (!usedRotors[0].reflecting()) {
            throw EnigmaException.error("First rotor should be reflector!");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        int count = 0;
        for (Rotor r: usedRotors) {
            if (!r.reflecting()) {
                count++;
            }
        }
        char[] temp = setting.toCharArray();
        for (int i = 1; i < numRotors(); i++) {
            usedRotors[i].set(temp[i - 1] - _alphabet.toChar(0));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    int convert(int c) {
        ArrayList<Rotor> sub = new ArrayList<Rotor>();
        sub.add(usedRotors[usedRotors.length - 1]);
        for (int i = usedRotors.length - 2; i > 0; i--) {
            if (usedRotors[i].rotates() && usedRotors[i + 1].atNotch()) {
                if (sub.contains(usedRotors[i + 1])) {
                    sub.add(usedRotors[i]);
                } else {
                    sub.add(usedRotors[i]);
                    sub.add(usedRotors[i + 1]);
                }
            }
        }
        for (Rotor r: sub) {
            r.advance();
        }

        int to = usedRotors[usedRotors.length - 1].convertForward(c
                - _alphabet.toChar(0));
        for (int i = usedRotors.length - 2; i > 0; i--) {
            to = usedRotors[i].convertForward(to);
        }

        int fro = usedRotors[0].permutation().permute(to);
        for (int i = 1; i < usedRotors.length; i++) {
            fro = usedRotors[i].convertBackward(fro);
        }

        return fro + _alphabet.toChar(0);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String msg1 = msg.toUpperCase().replaceAll("\\s+", "");
        char[] temp = msg1.toCharArray();
        char[] result = new char[msg1.length()];
        for (int i = 0; i < temp.length; i++) {
            char plug = _plugboard.permute(temp[i]);
            int conversion = convert(plug) - _alphabet.toChar(0);
            result[i] = _alphabet.toChar(_plugboard.permute(conversion));
        }
        return new String(result);
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
}
