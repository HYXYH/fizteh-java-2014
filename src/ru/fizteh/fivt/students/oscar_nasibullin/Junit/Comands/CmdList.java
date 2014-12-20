package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableImpl;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdList extends DataBaseCommand {
    public CmdList() {
        super("list", 1);
    }

    @Override
    public String execute(List<String> args) throws Exception {
        TableImpl currTable = tableProvider.getTable(currentTableName);
        if (currTable == null) {
            throw new Exception("no table");
        }

        List<String> keys = currTable.list();
        return String.join(", ", keys);
    }
}
