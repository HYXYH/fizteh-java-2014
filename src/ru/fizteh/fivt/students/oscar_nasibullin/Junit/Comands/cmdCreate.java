package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.Table;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdCreate extends Command {
    public CmdCreate() {
        setName("create");
    }

    @Override
    public String run(List<String> args) {
        if (args.size() != 2) {
            throw new IllegalArgumentException("Illegal arguments for create");
        }
        String resultMessage = "";
        Table createTable = tableProvider.getTable(args.get(1));

        if (createTable != null) {
            resultMessage = args.get(1) + " exists";
        } else {
            tableProvider.createTable(args.get(1));
            resultMessage = "created";
        }

        return resultMessage;
    }
}
