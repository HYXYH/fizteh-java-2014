package ru.fizteh.fivt.students.oscar_nasibullin.Storeable;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oskar on 03.12.14.
 */
public class StoreableImpl implements Storeable {

    private List<Object> storage;
    private List<Class<?>> types;

    public StoreableImpl(List<Object> values, List<Class<?>> valuesTypes) {
        storage = values;
        types = valuesTypes;
    }

    @Override
    public void setColumnAt(int columnIndex, Object value) throws ColumnFormatException, IndexOutOfBoundsException {
        if (value.getClass() != types.get(columnIndex)) {
            throw new ColumnFormatException(
                    "Storable: expected " + types.get(columnIndex).getName()
                            + ", found " + value.getClass().getName());
        }
        storage.set(columnIndex, types.get(columnIndex).cast(value)); // todo: check IndexOutOfBounds
    }

    @Override
    public Object getColumnAt(int columnIndex) throws IndexOutOfBoundsException {
        return storage.get(columnIndex); // todo: check IndexOutOfBounds
    }

    @Override
    public Integer getIntAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        if (types.get(columnIndex) != Integer.class) {
            throw new ColumnFormatException(
                    "Storable: reqested Integer, but found " + types.get(columnIndex).getName());
        }
        return (Integer) storage.get(columnIndex); //todo: check for null
    }

    @Override
    public Long getLongAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        if (types.get(columnIndex) != Long.class) {
            throw new ColumnFormatException(
                    "Storable: reqested Long, but found " + types.get(columnIndex).getName());
        }
        return (Long) storage.get(columnIndex); //todo: check for null
    }

    @Override
    public Byte getByteAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        if (types.get(columnIndex) != Byte.class) {
            throw new ColumnFormatException(
                    "Storable: reqested Byte, but found " + types.get(columnIndex).getName());
        }
        return (Byte) storage.get(columnIndex); //todo: check for null
    }

    @Override
    public Float getFloatAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        if (types.get(columnIndex) != Float.class) {
            throw new ColumnFormatException(
                    "Storable: reqested Float, but found " + types.get(columnIndex).getName());
        }
        return (Float) storage.get(columnIndex); //todo: check for null
    }

    @Override
    public Double getDoubleAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        if (types.get(columnIndex) != Double.class) {
            throw new ColumnFormatException(
                    "Storable: reqested Double, but found " + types.get(columnIndex).getName());
        }
        return (Double) storage.get(columnIndex); //todo: check for null
    }

    @Override
    public Boolean getBooleanAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        if (types.get(columnIndex) != Boolean.class) {
            throw new ColumnFormatException(
                    "Storable: reqested Boolean, but found " + types.get(columnIndex).getName());
        }
        return (Boolean) storage.get(columnIndex); //todo: check for null
    }

    @Override
    public String getStringAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        if (types.get(columnIndex) != String.class) {
            throw new ColumnFormatException(
                    "Storable: reqested String, but found " + types.get(columnIndex).getName());
        }
        return (String) storage.get(columnIndex); //todo: check for null
    }

    public List<Class<?>> getTypes() {
        return new ArrayList<>(types);
    }
}
