package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.Table;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class cmdGet extends Command {
    public cmdGet() {
        setName("get");
    }

    @Override
    public String run(List<String> args) throws Exception {
        if (args.size() != 2) {
            throw new IllegalArgumentException("Illegal arguments for get");
        }
        String resultMessage = "";
        Table currTable = tableProvider.getTable(currentTableName);
        if (currTable == null) {
            throw new Exception("no table");
        }

        String value = currTable.get(args.get(1));
        if (value != null) {
            resultMessage = "found\n" + value;
        } else {
            resultMessage = "not found";
        }
        return resultMessage;
    }
}
