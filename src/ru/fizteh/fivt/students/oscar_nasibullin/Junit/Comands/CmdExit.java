package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableImpl;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdExit extends Command {
    public CmdExit() {
        setName("exit");
    }

    @Override
    public String run(List<String> args) {
        TableImpl currTable = tableProvider.getTable(currentTableName);
        try {
            currTable.close();
        } catch (Exception e) {
            System.err.println("exit error: " + e.getMessage());
            System.exit(1);
        }
        System.exit(0);
        return null;
    }
}
