package ru.fizteh.fivt.students.oscar_nasibullin.Storeable.Comands;

import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdPut extends Command {
    public CmdPut() {
        setName("put");
        //setArgs(3); >= 3
    }

    @Override
    public String run(List<String> args) throws Exception {
        if (3 > args.size()) {
            throw new IllegalArgumentException("Not enough arguments for " + getName());
        }
        String rezultMessage = "";
        Table currTable = tableProvider.getTable(currentTableName);
        if (currTable == null) {
            throw new Exception("no table");
        }
        try {
            String arguments = String.join(" ", args.subList(2, args.size()));
            Storeable newValue = tableProvider.deserialize(currTable, arguments);
            Storeable changed = currTable.put(args.get(1), newValue);
            if (changed != null) {
                rezultMessage = "overwrite\n" + tableProvider.serialize(currTable, changed);
            } else {
                rezultMessage = "new";
            }
        } catch (Exception e)  {
            throw new Exception("wrong type (" + e.getMessage() + ")", e);
        }
        return rezultMessage;
    }
}
