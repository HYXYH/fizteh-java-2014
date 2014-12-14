package ru.fizteh.fivt.students.oscar_nasibullin.Storeable;

import ru.fizteh.fivt.students.oscar_nasibullin.Storeable.Comands.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *  Created by Oskar on 23.11.14.
 */
public class Shell {
    List<Command> commandList;

    public Shell(List<Command> commands, String[] args) {
        commandList = new ArrayList<Command>(commands);
        if (args.length > 0) {
            runBatch(args);
        } else {
            runInteractive();
        }
    }

    public void runInteractive() {
        List<List<String>> commands = new ArrayList<>();
        try (Scanner in = new Scanner(System.in)) {
            // "Try with resources" doesn't have to have a catch block.
            while (true) {
                System.err.flush();
                System.out.flush();
                System.out.print("$ ");
                String input = null;
                input = in.nextLine();
                String[] args = new String[1];
                args[0] = input;
                commands = parse(args);
                activator(commands, false);
            }
        }
    }


    public void runBatch(final String[] args) {
        List<List<String>> commands = new ArrayList<>();

        commands = parse(args);
        activator(commands, true);

        System.exit(0);
    }


    public void activator(final List<List<String>> parsed, final boolean batchMode) {
        String rezultMessage = null;
        try {
            for (int i = 0; i < parsed.size(); i++) {
                boolean executed = false;
                for (Command command : commandList) {
                    if (command.getName().equals(parsed.get(i).get(0))) {
                        rezultMessage = command.run(parsed.get(i));
                        executed = true;
                        break;
                    }
                }
                if (!executed) {
                    throw new Exception(parsed.get(i).get(0) + ": no such command");
                }
                if (rezultMessage != null) {
                    System.out.println(rezultMessage);
                }
            }
        } catch (Exception e) {
            if (e.getMessage() != null) {
                System.err.println(e.getMessage());
            } else {
                System.err.println("Undefined Exception: ");
                e.printStackTrace();
            }
            if (batchMode) {
                System.exit(1);
            }
        }
    }

    public static List<List<String>> parse(final String[] args) {
        List<List<String>> commands = new ArrayList<>();
        ArrayList<String> comAndArgs = new ArrayList<>();
        String[] arguments;

        if (args.length == 1) { // Interactive parse.
            arguments = args[0].split(";");
            for (String arg : arguments) {
                comAndArgs = new ArrayList<String>(Arrays.asList(arg.split(" ")));
                if (comAndArgs.size() != 0) {
                    commands.add(new ArrayList<String>(comAndArgs));
                }
                comAndArgs.clear();
            }
        } else {                // Batch.
            arguments = args;
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i].endsWith(";")) {
                    int start = 0;
                    int end = arguments[i].length() - 1;
                    char[] buf = new char[end];
                    arguments[i].getChars(start, end, buf, 0);
                    String lastArg = new String(buf);

                    comAndArgs.add(lastArg);
                    commands.add(new ArrayList<>(comAndArgs));
                    comAndArgs.clear();
                } else {
                    comAndArgs.add(arguments[i]);
                    if (i == arguments.length - 1) {
                        commands.add(comAndArgs);
                    }
                }
            }
        }
        return commands;
    }



}


