package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableImpl;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdRemove extends DataBaseCommand {
    public CmdRemove() {
        super("remove", 2);
    }

    @Override
    public String execute(List<String> args) throws Exception {
        TableImpl currTable = tableProvider.getTable(currentTableName);
        if (currTable == null) {
            throw new Exception("no table");
        }

        if (currTable.remove(args.get(1)) != null) {
            return "removed";
        } else {
            return "not found";
        }
    }
}
