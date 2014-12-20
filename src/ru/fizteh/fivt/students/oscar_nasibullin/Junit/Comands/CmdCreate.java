package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableImpl;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdCreate extends DataBaseCommand {
    public CmdCreate() {
        super("create", 2);
    }

    @Override
    public String execute(List<String> args) {
        TableImpl createTable = tableProvider.getTable(args.get(1));

        if (createTable != null) {
            return args.get(1) + " exists";
        } else {
            tableProvider.createTable(args.get(1));
            return "created";
        }
    }
}
