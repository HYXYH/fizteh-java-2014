package ru.fizteh.fivt.students.oscar_nasibullin.Junit.Tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.fizteh.fivt.students.oscar_nasibullin.Junit.TableProviderFactory;

import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TableProviderFactoryTest {
    TableProviderFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new TableProviderFactory();
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