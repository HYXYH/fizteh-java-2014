package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.Table;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdRemove extends Command {
    public CmdRemove() {
        setName("remove");
    }

    @Override
    public String run(List<String> args) throws Exception {
        if (args.size() != 2) {
            throw new IllegalArgumentException("Illegal arguments for remove");
        }
        String rezultMessage = "";
        Table currTable = tableProvider.getTable(currentTableName);
        if (currTable == null) {
            throw new Exception("no table");
        }

        if (currTable.remove(args.get(1)) != null) {
            rezultMessage = "removed";
        } else {
            rezultMessage = "not found";
        }
        return rezultMessage;
    }
}
