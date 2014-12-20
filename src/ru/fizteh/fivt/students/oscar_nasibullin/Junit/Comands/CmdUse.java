package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableImpl;

import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdUse extends DataBaseCommand {

    public CmdUse() {
        super("use", 2);
    }

    @Override
    public String execute(List<String> args) throws Exception {
        TableImpl newTable = tableProvider.getTable(args.get(1));
        if (newTable != null) {
            if (tableProvider.getTable(currentTableName) != null) {
                Integer unsaved = tableProvider.getTable(currentTableName).unsavedChangesNum();
                if (unsaved == 0) {
                    tableProvider.getTable(currentTableName).close();
                } else {
                    throw new Exception(unsaved.toString() + " unsaved changes");
                }
            }
            currentTableName = args.get(1);
            tableProvider.getTable(currentTableName).open();
            return "using " + currentTableName;
        } else {
            return args.get(1) + " not exists";
        }
    }
}
