package ru.fizteh.fivt.students.fedorov_andrew.databaselibrary.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ru.fizteh.fivt.storage.strings.Table;
import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.storage.strings.TableProviderFactory;
import ru.fizteh.fivt.students.fedorov_andrew.databaselibrary.test.support.TestUtils;

@RunWith(JUnit4.class)
public class TableTest extends TestBase {
    private static TableProviderFactory factory;
    private static TableProvider provider;
    private static Table table;

    @BeforeClass
    public static void globalPrepare() {
	factory = TestUtils.obtainFactory();
    }

    @Before
    public void prepare() {
	provider = factory.create(dbRoot.toString());
	table = provider.createTable("table");
    }

    @After
    public void cleanup() throws IOException {
	cleanDBRoot();
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void testGetExistent() {
	table.put("key", "value");
	String value = table.get("key");
	assertEquals("Existent key must return proper value", "value", value);
    }

    @Test
    public void testGetNotExistent() {
	String value = table.get("not_existent");
	assertNull("Not existent key must give null value", value);
    }

    @Test
    public void testGetNullKey() {
	exception.expect(IllegalArgumentException.class);
	exception.expectMessage("Key must not be null");
	table.get(null);
    }

    @Test
    public void testPutNew() {
	int sizeBefore = table.size();
	String oldValue = table.put("new", "value");
	int sizeAfter = table.size();

	assertNull("When inserting new (key,value), old value must be null",
		oldValue);
	assertEquals("WHen inserting new key, size must increase by 1",
		sizeBefore + 1, sizeAfter);
    }

    @Test
    public void testPutExistent() {
	table.put("existent", "value1");
	int sizeBefore = table.size();
	String oldValue = table.put("existent", "value2");
	int sizeAfter = table.size();

	assertEquals("Wrong old value when putting existent key", "value1",
		oldValue);
	assertEquals("When putting existent key, size must not change",
		sizeBefore, sizeAfter);
    }

    @Test
    public void testRemoveNotExistent() {
	int sizeBefore = table.size();
	String oldValue = table.remove("not existent");
	int sizeAfter = table.size();

	assertNull("When removing not existent key, old value must be null",
		oldValue);
	assertEquals("When removing not existent key, size must not change",
		sizeBefore, sizeAfter);
    }

    @Test
    public void testRemoveExistent() {
	table.put("key", "value1");
	int sizeBefore = table.size();
	String oldValue = table.remove("key");
	int sizeAfter = table.size();

	assertEquals("When removing existent key, old value must be returned",
		"value1", oldValue);
	assertEquals("When removing existent key, size must decrease by one",
		sizeBefore - 1, sizeAfter);
    }
}
