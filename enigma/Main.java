package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Arrays;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author William Tai
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine terminator = readConfig();
        String line = _input.nextLine();

        if (!line.contains("*")) {
            throw EnigmaException.error("Bad Setting");
        }

        setUp(terminator, line);

        while (_input.hasNextLine()) {
            String inp = _input.nextLine();
            if (inp.contains("*")) {
                setUp(terminator, inp);
                if (_input.hasNextLine()) {
                    String sub = _input.nextLine();
                    String message = terminator.convert(sub);
                    printMessageLine(message);
                }
            } else {
                String message = terminator.convert(inp);
                printMessageLine(message);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            ArrayList<Rotor> allRotors = new ArrayList<>();
            String alpha = _config.next();
            _alphabet = new CharacterRange(alpha.charAt(0),
                    alpha.charAt(alpha.length() - 1));
            numRotors = _config.nextInt();
            pawls = _config.nextInt();
            rname = _config.next().toUpperCase();
            while (_config.hasNext()) {
                allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, pawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            Rotor r;
            String permutation = "";
            String type = _config.next();
            String per = _config.next();
            while (per.contains(")")) {
                permutation += per + " ";
                if (_config.hasNext()) {
                    per = _config.next();
                } else {
                    break;
                }
            }
            if (type.equals("R")) {
                r = new Reflector(rname, new Permutation(permutation,
                        _alphabet));
            } else if (type.equals("N")) {
                r = new FixedRotor(rname, new Permutation(permutation,
                        _alphabet));
            } else {
                r = new MovingRotor(rname,
                        new Permutation(permutation, _alphabet),
                        type.substring(1));
            }
            rname = per.toUpperCase();
            return r;
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Permutation plugboard;
        String[] info = settings.split(" ");

        if (M.numRotors() > info.length - 1) {
            throw  EnigmaException.error("Incorrect format");
        }

        String[] insertedRotors = Arrays.copyOfRange(info, 1,
                numRotors + 1);

        if (numRotors + 1 >= info.length) {
            throw EnigmaException.error("Bad input");
        }

        String setting = info[numRotors + 1];

        M.insertRotors(insertedRotors);
        M.setRotors(setting);

        if (info.length > numRotors + 1) {
            String[] sub = Arrays.copyOfRange(info, numRotors + 2,
                    info.length);
            String perm = "";
            for (String s: sub) {
                perm += s + " ";
            }
            plugboard = new Permutation(perm, _alphabet);
            M.setPlugboard(plugboard);
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        char[] sub = msg.toCharArray();
        char[] chars = new char[5];
        String result = "";
        int total = 0;
        int count = 0;

        while (total < sub.length) {
            if (count < 5) {
                chars[count] = sub[total];
                count++;
                total++;
            } else {
                _output.print(new String(chars) + " ");
                count = 0;
                chars = new char[5];
            }
        }
        _output.println((new String(chars)).substring(0, count));
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** The number of Rotors. */
    private int numRotors;

    /** The number of pawls. */
    private int pawls;

    /** The name of the rotor currently. */
    private String rname;
}
