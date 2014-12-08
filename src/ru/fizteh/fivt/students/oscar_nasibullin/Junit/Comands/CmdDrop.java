package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableImpl;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdDrop extends Command {
    public CmdDrop() {
        setName("drop");
        setArgs(2);
    }

    @Override
    public String run(List<String> args) throws Exception {
        checkArgumentsAmount(args);
        String resultMessage = "";
        TableImpl dropTable = tableProvider.getTable(args.get(1));

        if (dropTable == null) {
            resultMessage = args.get(1) + " not exists";
        } else {
            tableProvider.removeTable(args.get(1));
            resultMessage = "dropped";
        }
        return resultMessage;
    }
}
