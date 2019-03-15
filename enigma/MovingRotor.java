package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author William Tai
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initially in its 0 setting (first character of its
     *  alphabet).
     */

    /** Notches that allow others to advance. */
    private String _notches;

    /** Inputted permutation. */
    private Permutation _permutation;

    /**
     * Initializes the a moving rotor.
     *
     * @param name Rotor name
     * @param perm Permutation
     * @param notches Rotor notches
     *
     * */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _permutation = perm;
        _notches = notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        set(permutation().wrap(setting() + 1));
    }

    @Override
    boolean atNotch() {
        char[] sub = _notches.toCharArray();
        for (char notch: sub) {
            if (notch == setting() + _permutation.alphabet().toChar(0)) {
                return true;
            }
        }
        return false;
    }

    @Override
    int convertForward(int p) {
        int temp = _permutation.wrap(p + setting());
        int temp1 = _permutation.permute(temp);
        return _permutation.wrap(temp1 - setting());
    }

    @Override
    int convertBackward(int e) {
        int temp = _permutation.wrap(e + setting());
        int temp1 = _permutation.invert(temp);
        return _permutation.wrap(temp1 - setting());
    }

}
