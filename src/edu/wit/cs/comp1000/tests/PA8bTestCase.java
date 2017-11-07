package edu.wit.cs.comp1000.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.security.Permission;

import edu.wit.cs.comp1000.PA8b;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

public class PA8bTestCase {
	
	@Rule
	public Timeout globalTimeout = Timeout.seconds(10);
	
	@SuppressWarnings("serial")
	private static class ExitException extends SecurityException {}
	
	private static class NoExitSecurityManager extends SecurityManager 
    {
        @Override
        public void checkPermission(Permission perm) {}
        
        @Override
        public void checkPermission(Permission perm, Object context) {}
        
        @Override
        public void checkExit(int status) { super.checkExit(status); throw new ExitException(); }
    }
	
	@Before
    public void setUp() throws Exception 
    {
        System.setSecurityManager(new NoExitSecurityManager());
    }
	
	@After
    public void tearDown() throws Exception 
    {
        System.setSecurityManager(null);
    }
	
	private void _test(String[] values, String result) {
		final String input = String.join(String.format("%n"), values);
		
		final String output = TestSuite.stringOutput(new String[] {
			"Enter the name of the input file: ",
			"File: %s%n" }, new Object[] { result });
		
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		System.setOut(new PrintStream(outContent));
		
		try {
			PA8b.main(new String[] { "foo" });
		} catch (ExitException e) {}
		assertEquals("Console Error!", output, outContent.toString());
		
		System.setIn(null);
		System.setOut(null);
	}
	
	private void _testInvalid(String path) {
		_test(new String[] {path}, "invalid");
	}
	
	private void _testValid(String path, String lower, String upper) {
		_test(new String[] {path}, String.format("[%s, %s]", lower, upper));
	}

	@Test
	public void testProgBadFile() {
		for (String f : new String[] {"test.txt", "integers.txt", "foo bar baz.qux"}) {
			_testInvalid(TestSuite.getBadPath(f));
		}
	}

	@Test
	public void testEmptyFile() throws IOException {
		_testInvalid(TestSuite.getGoodFile().getAbsolutePath());
	}
	
	private void _testGoodFile(String input) throws IOException {
		final File fIn = TestSuite.getGoodFile();
		
		TestSuite.putInFile(fIn, input);
		_testInvalid(fIn.getAbsolutePath());
	}
	
	private void _testGoodFile(String input, String expectedMin, String expectedMax) throws IOException {
		final File fIn = TestSuite.getGoodFile();
		
		TestSuite.putInFile(fIn, input);
		_testValid(fIn.getAbsolutePath(), expectedMin, expectedMax);
	}

	@Test
	public void testDoubles() throws IOException {
		_testGoodFile("1.0");
		_testGoodFile("1.1");
		
		_testGoodFile("1.1 2.2 3.3");
		
		_testGoodFile(String.format("3.14%n2.7"));
	}

	@Test
	public void testText() throws IOException {
		_testGoodFile(String.format("Hello World!%n:)%n"));
	}

	@Test
	public void testIntegers() throws IOException {
		_testGoodFile(String.format("1 2 3"), "1", "3");
		_testGoodFile(String.format("1%n2 3"), "1", "3");
		_testGoodFile(String.format("1%n2%n3"), "1", "3");
		
		_testGoodFile(String.format("3 2 1"), "1", "3");
		_testGoodFile(String.format("3%n2 1"), "1", "3");
		_testGoodFile(String.format("3%n2%n1"), "1", "3");
		
		_testGoodFile(String.format("3%n2%n1%n-1 100 -3"), "-3", "100");
	}

	@Test
	public void testMix() throws IOException {
		_testGoodFile(String.format("1 two 3%nfour 5 six"));
	}
	
}
