package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableProviderImpl;

import java.util.List;

/**
 * Created by Oskar on 19.11.14.
 */
public abstract class Command {

    private String name;
    private int argumentsAmount = 0;
    public static TableProviderImpl tableProvider;
    public static String currentTableName; // or even whole Table ?

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }
    public void setArgs(int amount) {
        argumentsAmount = amount;
    }

    public void checkArgumentsAmount(List<String> args) throws IllegalArgumentException {
        if (argumentsAmount != args.size()) {
            throw new IllegalArgumentException("Illegal arguments for " + name);
        }
    }

    /**
     * Выполняет что-то.
     *
     * @param args Список аргументов.
     * @return Сообщение с результатом работы. Если возвращает null, то на экран ничего не печатается.
     *
     * @throws Exception проброс наверх любого исключения.
     */
    public String run(List<String> args) throws Exception {
        return null;
    }
}
