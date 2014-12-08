package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableImpl;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdCreate extends Command {
    public CmdCreate() {
        setName("create");
        setArgs(2);
    }

    @Override
    public String run(List<String> args) {
        checkArgumentsAmount(args);
        String resultMessage = "";
        TableImpl createTable = tableProvider.getTable(args.get(1));

        if (createTable != null) {
            resultMessage = args.get(1) + " exists";
        } else {
            tableProvider.createTable(args.get(1));
            resultMessage = "created";
        }

        return resultMessage;
    }
}
