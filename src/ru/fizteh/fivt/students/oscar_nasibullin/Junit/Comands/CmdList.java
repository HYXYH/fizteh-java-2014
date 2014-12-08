package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableImpl;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdList extends Command {
    public CmdList() {
        setName("list");
        setArgs(1);
    }

    @Override
    public String run(List<String> args) throws Exception {
        checkArgumentsAmount(args);
        String resultMessage = "";
        TableImpl currTable = tableProvider.getTable(currentTableName);
        if (currTable == null) {
            throw new Exception("no table");
        }

        boolean firstWord = true;
        List<String> keys = currTable.list();
        resultMessage = String.join(", ", keys);
        return resultMessage;
    }
}
