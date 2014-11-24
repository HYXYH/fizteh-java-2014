package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.Table;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdCommit extends Command {
    public CmdCommit() {
        setName("commit");
    }

    @Override
    public String run(List<String> args) throws Exception {
        if (args.size() != 1) {
            throw new IllegalArgumentException("Illegal arguments for commit");
        }
        String resultMessage;
        Table currTable = tableProvider.getTable(currentTableName);
        if (currTable == null) {
            throw new Exception("no table");
        }
        resultMessage = new Integer(currTable.commit()).toString();
        return resultMessage;
    }
}
