package enigma;

import java.util.HashMap;
import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author William Tai
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */

    /** Holds all the cycles which will be turned into the permutations. */
    private String[] _cycles;

    /** Maps each character to the corresponding character. */
    private HashMap<Character, Character> _map;

    /** The inverse of the map. */
    private HashMap<Character, Character> _imap;

    /** Initializes the permutation.
     *
     * @param cycles cycle
     * @param alphabet the alphabet
     * */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _map = new HashMap<>();
        _imap = new HashMap<>();
        for (int i = 0; i < alphabet.size(); i++) {
            _map.put(alphabet.toChar(i), alphabet.toChar(i));
        }

        int counter = 0;
        int counter1 = 0;
        for (int i = 0; i < cycles.length(); i++) {
            if (cycles.charAt(i) == ')') {
                counter++;
            }
            if (cycles.charAt(i) == '(') {
                counter1++;
            }
        }
        if (counter != counter1) {
            throw EnigmaException.error("Bad configuration fool");
        }

        _cycles = cycles.split(" ");
        for (String cycle : _cycles) {
            int i = 0;
            while (cycle.contains(")(")) {
                String sub = cycle.substring(i, cycle.indexOf(")") + 1);
                i = cycle.indexOf(")") + 1;
                addCycle(sub);
                cycle = cycle.substring(i);
            }
            addCycle(cycle);
        }
        for (int i = 0; i < alphabet.size(); i++) {
            _imap.put(_map.get(alphabet.toChar(i)), alphabet.toChar(i));
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        if (!cycle.equals("")) {
            char[] full = cycle.toCharArray();
            char first = full[1];
            for (int i = 1; i < full.length - 1; i++) {
                if (full[i + 1] == ')') {
                    _map.put(full[i], first);
                } else {
                    _map.put(full[i], full[i + 1]);
                }
            }
        }

    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return _alphabet.toInt(_map.get(_alphabet.toChar(p)));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return _alphabet.toInt(_imap.get(_alphabet.toChar(c)));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return _map.get(p);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    int invert(char c) {
        return _imap.get(c);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i++) {
            char sub = _alphabet.toChar(i);
            if (sub == _map.get(sub)) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
}
