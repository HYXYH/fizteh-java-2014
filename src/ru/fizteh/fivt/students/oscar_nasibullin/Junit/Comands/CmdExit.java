package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableImpl;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdExit extends Command {
    public CmdExit() {
        setName("exit");
        setArgs(1);
    }

    @Override
    public String run(List<String> args) throws Exception {
        checkArgumentsAmount(args);
        TableImpl currTable = tableProvider.getTable(currentTableName);
        if (tableProvider.getTable(currentTableName) != null) {
            Integer unsaved = tableProvider.getTable(currentTableName).unsavedChangesNum();
            if (unsaved == 0) {
                tableProvider.getTable(currentTableName).close();
            } else {
                throw new Exception(unsaved.toString() + " unsaved changes");
            }
        }
        try {
            if (tableProvider.getTable(currentTableName) != null) {
                currTable.close();
            }
        } catch (Exception e) {
            if (e.getMessage() != null) {
                System.err.println("exit error: " + e.getMessage());
            } else {
                System.err.println("exit error: something went wrong when I tried to close current table");
                e.printStackTrace();
            }
            System.exit(1);
        }
        System.exit(0);
        return null;
    }
}
