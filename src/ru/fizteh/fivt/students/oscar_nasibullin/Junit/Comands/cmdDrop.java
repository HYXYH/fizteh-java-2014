package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.Table;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class cmdDrop extends Command {
    public cmdDrop() {
        setName("drop");
    }

    @Override
    public String run(List<String> args) throws Exception {
        if (args.size() != 2) {
            throw new IllegalArgumentException("Illegal arguments for drop");
        }
        String resultMessage = "";
        Table dropTable = tableProvider.getTable(args.get(1));

        if (dropTable == null) {
            resultMessage = args.get(1) + " not exists";
        } else {
            tableProvider.removeTable(args.get(1));
            resultMessage = "dropped";
        }
        return resultMessage;
    }
}
