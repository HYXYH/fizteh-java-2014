package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableImpl;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdPut extends DataBaseCommand {
    public CmdPut() {
        super("put", 3);
    }

    @Override
    public String execute(List<String> args) throws Exception {
        TableImpl currTable = tableProvider.getTable(currentTableName);
        if (currTable == null) {
            throw new Exception("no table");
        }

        String changed = currTable.put(args.get(1), args.get(2));
        if (changed != null) {
            return "overwrite\n" + changed;
        } else {
            return "new";
        }
    }
}
