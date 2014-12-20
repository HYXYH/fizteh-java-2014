package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableImpl;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdDrop extends DataBaseCommand {
    public CmdDrop() {
        super("drop", 2);
    }

    @Override
    public String execute(List<String> args) throws Exception {
        TableImpl dropTable = tableProvider.getTable(args.get(1));

        if (dropTable == null) {
            return args.get(1) + " not exists";
        } else {
            tableProvider.removeTable(args.get(1));
            return "dropped";
        }
    }
}
