package ru.fizteh.fivt.students.oscar_nasibullin.Storeable.Comands;

import ru.fizteh.fivt.storage.structured.Table;
import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdRollback extends Command {
    public CmdRollback() {
        setName("rollback");
        setArgs(1);
    }

    @Override
    public String run(List<String> args) throws Exception {
        checkArgumentsAmount(args);
        String resultMessage;
        Table currTable = tableProvider.getTable(currentTableName);
        if (currTable == null) {
            throw new Exception("no table");
        }
        resultMessage = new Integer(currTable.rollback()).toString();
        return resultMessage;
    }
}
