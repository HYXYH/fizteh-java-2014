package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Comands;

import java.util.List;

/**
 * Created by Oskar on 19.11.14.
 */
public abstract class Command {

    private String name;
    private int argumentsAmount = 0;
    private boolean checkArguments = false;

    public Command(String name) {
        this.name = name;
        checkArguments = false;
    }

    public Command(String name, int numOfArgs) {
        this.name = name;
        argumentsAmount = numOfArgs;
        checkArguments = true;
    }

    public String getName() {
        return name;
    }


    /**
     * Выполняет проверку аргументов (если нужно), после чего запускает execute.
     *
     * @param args Список аргументов.
     * @return Сообщение с результатом работы execute.
     *
     * @throws Exception проброс наверх любого исключения.
     */
    public String run(List<String> args) throws Exception {
        if (checkArguments) {
            if (argumentsAmount != args.size()) {
                throw new IllegalArgumentException("Illegal arguments for " + name);
            }
        }
        return execute(args);
    }

    /**
     * Выполняет что-то.
     *
     * @param args Список аргументов.
     * @return Сообщение с результатом работы. Если возвращает null, то на экран ничего не печатается.
     *
     * @throws Exception проброс наверх любого исключения.
     */
    public abstract String execute(List<String> args) throws Exception;
}
