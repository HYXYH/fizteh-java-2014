package ru.fizteh.fivt.students.oscar_nasibullin.Storeable.Tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.fizteh.fivt.students.oscar_nasibullin.Storeable.TableProviderFactoryImpl;

import java.nio.file.Paths;

public class TableProviderFactoryTest {
    TableProviderFactoryImpl factory;

    @Before
    public void setUp() throws Exception {
        factory = new TableProviderFactoryImpl();
    }

    @After
    public void tearDown() throws Exception {
        Paths.get("./factoryTest").toFile().delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateThrowsIllegalArgumentExceptionOnNull() throws Exception {
        factory.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateThrowsIllegalArgumentException() throws Exception {
        factory.create("abc/def\\gh*./-=09()'///../");
    }

    @Test
    public void testCreate() throws Exception {
        factory.create("./factoryTest");
    }

}
