package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableImpl;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdRollback extends Command {
    public CmdRollback() {
        setName("rollback");
    }

    @Override
    public String run(List<String> args) throws Exception {
        if (args.size() != 1) {
            throw new IllegalArgumentException("Illegal arguments for rollback");
        }
        String resultMessage;
        TableImpl currTable = tableProvider.getTable(currentTableName);
        if (currTable == null) {
            throw new Exception("no table");
        }
        resultMessage = new Integer(currTable.rollback()).toString();
        return resultMessage;
    }
}
