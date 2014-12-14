package ru.fizteh.fivt.students.oscar_nasibullin.Storeable.Comands;

import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.students.oscar_nasibullin.Storeable.TableImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oskar on 23.11.14.
 */
public class CmdCreate extends Command {
    public CmdCreate() {
        setName("create");
        //setArgs(); >= 3
    }

    @Override
    public String run(List<String> args) throws Exception {
        if (3 > args.size()) {
            throw new IllegalArgumentException("Not enough arguments for " + getName());
        }
        String resultMessage = "";
        Table createTable = tableProvider.getTable(args.get(1));

        if (createTable != null) {
            resultMessage = args.get(1) + " exists";
        } else {
            List<Class<?>> types = new ArrayList<>();
            for (String typeName : args.subList(2, args.size())) {
                typeName = typeName.replaceAll("[()]", "");
                if (!typeName.equals("")) {
                    String className = TableImpl.convertClassName(typeName);
                    try {
                        types.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        throw new Exception("wrong type (no type with name: " + typeName + ")", e);
                    }
                }
            }
            tableProvider.createTable(args.get(1), types);
            resultMessage = "created";
        }
        return resultMessage;
    }
}
