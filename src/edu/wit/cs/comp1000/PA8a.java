package edu.wit.cs.comp1000;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

/**
 * PA8a
 * @author Miles Macchiaroli
 *
 */
public class PA8a {
	
	/**
	 * Error to output when a file cannot be opened.
	 */
	static final String E_NOT_FOUND = "Error! File not found!";
	
	/**
	 * Reads all integers in input scanner,
	 * outputs positive ones to output each on
	 * its own line
	 * 
	 * @param input input source
	 * @param output output destination 
	 */
	public static void process(Scanner input, PrintWriter output) {
		int current; 
			while(true) { //continuous loop
				try {//try to take next int
					current = input.nextInt();
					if(current > 0) {//if int > 0
						output.printf("%d%n", current);//print int to file
					}else {}
		}
		catch(NoSuchElementException e){break;}}//catch no more ints
	}

	/**
	 * Program execution point:
	 * input an input file name and an output file name,
	 * for each positive number in the input file 
	 * print on its own line to the output file
	 * 
	 * @param args command-line arguments (ignored)
	 */
	public static void main(String[] args) {
		String fileIn = "", fileOut = "";//create a string for in and out file names
		Scanner input = new Scanner(System.in);//Call Scanner object
		System.out.printf("Enter the name of the input file: ");
		fileIn = input.nextLine();//Set location of input file
		System.out.printf("Enter the name of the output file: ");
		fileOut = input.nextLine();//Set desired output location
	//TEST FILE
		try (Scanner fin = new Scanner(new File(fileIn)))  {//Attempt input file
			
			try (PrintWriter fout = new PrintWriter(new File(fileOut))){ //Attempt Output File
				process(fin, fout);
			} catch (FileNotFoundException ex) {//If output cannot be established
			System.out.printf("Error! File not found!%n");
			System.exit(0);
			}
		} catch (FileNotFoundException ex) {//If fin cannot be found
			System.out.printf("Error! File not found!%n");
			System.exit(0);
		}
		
	}
	

}
