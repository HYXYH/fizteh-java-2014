package ru.fizteh.fivt.students.oscar_nasibullin.Storeable.Comands;

import ru.fizteh.fivt.storage.structured.Table;
import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdGet extends Command {
    public CmdGet() {
        setName("get");
        setArgs(2);
    }

    @Override
    public String run(List<String> args) throws Exception {
        checkArgumentsAmount(args);
        String resultMessage = "";
        Table currTable = tableProvider.getTable(currentTableName);
        if (currTable == null) {
            throw new Exception("no table");
        }

        String value = tableProvider.serialize(currTable, currTable.get(args.get(1)));
        if (value != null) {
            resultMessage = "found\n" + value;
        } else {
            resultMessage = "not found";
        }
        return resultMessage;
    }
}
