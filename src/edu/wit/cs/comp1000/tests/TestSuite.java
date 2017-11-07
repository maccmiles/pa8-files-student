package edu.wit.cs.comp1000.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	PA8aTestCase.class,
	PA8bTestCase.class
})

public class TestSuite {
	
	static String stringOutput(String[] lines, Object[] values) {
		return String.format(String.join("", lines), values);
	}
	
	static String getBadPath(String name) {
		return new File(new File(TestSuite.class.getResource("empty.txt").getPath()).getParent(), name).getAbsolutePath();
	}
	
	static File getGoodFile() throws IOException {
		final File f = File.createTempFile("comp1000", ".txt");
		f.deleteOnExit();
//		System.err.println(f.getAbsolutePath());
		
		return f;
	}
	
	static void putInFile(File f, String contents) throws FileNotFoundException {
		final PrintWriter pw = new PrintWriter(f);
		pw.println(contents);
		pw.close();
	}
	
	static String getFileContents(File f) throws IOException {
		final List<String> lines = Files.readAllLines(f.toPath());
		return String.join(String.format("%n"), lines) + (lines.isEmpty()?"":String.format("%n"));
	}
	
}
