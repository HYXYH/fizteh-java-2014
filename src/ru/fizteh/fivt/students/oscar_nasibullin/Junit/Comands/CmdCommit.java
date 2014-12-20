package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableImpl;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdCommit extends DataBaseCommand {
    public CmdCommit() {
        super("commit", 1);
    }

    @Override
    public String execute(List<String> args) throws Exception {
        TableImpl currTable = tableProvider.getTable(currentTableName);
        if (currTable == null) {
            throw new Exception("no table");
        }
        return String.valueOf(currTable.commit());

    }
}
