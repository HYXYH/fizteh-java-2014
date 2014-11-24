package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.Table;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class cmdPut extends Command {
    public cmdPut() {
        setName("put");
    }

    @Override
    public String run(List<String> args) throws Exception {
        if (args.size() != 3) {
            throw new IllegalArgumentException("Illegal arguments for put");
        }
        String rezultMessage = "";
        Table currTable = tableProvider.getTable(currentTableName);
        if (currTable == null) {
            throw new Exception("no table");
        }

        String changed = currTable.put(args.get(1), args.get(2));
        if (changed != null) {
            rezultMessage = "overwrite\n" + changed;
        } else {
            rezultMessage = "new";
        }
        return rezultMessage;
    }
}
