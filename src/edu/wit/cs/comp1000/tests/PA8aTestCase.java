package edu.wit.cs.comp1000.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.security.Permission;
import java.util.Scanner;

import edu.wit.cs.comp1000.PA8a;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

public class PA8aTestCase {
	
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
	
	private void _testProcess(String input, String expectedOutput) {
		final Scanner sIn = new Scanner(input);
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final PrintWriter pwOut = new PrintWriter(os, true);
		
		try {
			PA8a.process(sIn, pwOut);
		} catch (ExitException e) {}
		System.setOut(null);
		
		assertEquals("Output Error!", expectedOutput, os.toString());
	}

	@Test
	public void testProcess() {
		_testProcess(String.format(""), String.format(""));
		
		_testProcess(String.format("-1"), String.format(""));
		_testProcess(String.format("-1 -2"), String.format(""));
		_testProcess(String.format("-1 -2 -3"), String.format(""));
		
		_testProcess(String.format("1"), String.format("1%n"));
		_testProcess(String.format("1 2"), String.format("1%n2%n"));
		_testProcess(String.format("1 2 3"), String.format("1%n2%n3%n"));
		_testProcess(String.format("1%n2 3"), String.format("1%n2%n3%n"));
	}
	
	private static final String E_NOT_FOUND = "Error! File not found!";
	
	private void _test(String[] values, String result) {
		final String input = String.join(String.format("%n"), values);
		
		final String output = TestSuite.stringOutput(new String[] {
			"Enter the name of the input file: ",
			"Enter the name of the output file: ",
			"%s" }, new Object[] { result });
		
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		System.setOut(new PrintStream(outContent));
		
		try {
			PA8a.main(new String[] { "foo" });
		} catch (ExitException e) {}
		assertEquals("Console Error!", output, outContent.toString());
		
		System.setIn(null);
		System.setOut(null);
	}

	@Test
	public void testProgBadFile() {
		for (String f : new String[] {"test.txt", "integers.txt", "foo bar baz.qux"}) {
			final String path = TestSuite.getBadPath(f);
			_test(new String[] {path, "output.txt"}, String.format(E_NOT_FOUND + "%n"));
		}
	}
	
	private void _testGood(String input, String expectedOutput) throws IOException {
		final File fIn = TestSuite.getGoodFile();
		final File fOut = TestSuite.getGoodFile();
		
		TestSuite.putInFile(fIn, input);
		_test(new String[] {fIn.getAbsolutePath(), fOut.getAbsolutePath()}, "");
		assertEquals("File Contents Error!", expectedOutput, TestSuite.getFileContents(fOut));
	}

	@Test
	public void testProgGoodFile() throws IOException {
		_testGood(String.format(""), String.format(""));
		
		_testGood(String.format("-1"), String.format(""));
		_testGood(String.format("-1 -2"), String.format(""));
		_testGood(String.format("-1 -2 -3"), String.format(""));
		
		_testGood(String.format("1"), String.format("1%n"));
		_testGood(String.format("1 2"), String.format("1%n2%n"));
		_testGood(String.format("1 2 3"), String.format("1%n2%n3%n"));
		_testGood(String.format("1%n2 3"), String.format("1%n2%n3%n"));
	}
	
}
