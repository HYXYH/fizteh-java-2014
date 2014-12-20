package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableImpl;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdGet extends DataBaseCommand {
    public CmdGet() {
        super("get", 2);
    }

    @Override
    public String execute(List<String> args) throws Exception {
        TableImpl currTable = tableProvider.getTable(currentTableName);
        if (currTable == null) {
            throw new Exception("no table");
        }

        String value = currTable.get(args.get(1));
        if (value != null) {
            return "found\n" + value;
        } else {
            return "not found";
        }
    }
}
