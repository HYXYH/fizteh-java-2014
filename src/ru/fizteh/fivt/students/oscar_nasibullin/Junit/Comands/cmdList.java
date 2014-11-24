package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.Table;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdList extends Command {
    public CmdList() {
        setName("list");
    }

    @Override
    public String run(List<String> args) throws Exception {
        if (args.size() != 1) {
            throw new IllegalArgumentException("Illegal arguments for list");
        }
        String resultMessage = "";
        Table currTable = tableProvider.getTable(currentTableName);
        if (currTable == null) {
            throw new Exception("no table");
        }

        boolean firstWord = true;
        List<String> keys = currTable.list();

        for (String key : keys) {
            if (firstWord) {
                resultMessage = key;
                firstWord = false;
            } else {
                resultMessage += ", " + key;
            }
        }

        return resultMessage;
    }
}
