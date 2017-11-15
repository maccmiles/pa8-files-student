package edu.wit.cs.comp1000;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * PA8b
 * @author Miles Macchiaroli
 *
 */
public class PA8b {

	/**
	 * Program execution point:
	 * input an input file name,
	 * output the smallest and largest
	 * integers found in the file (or invalid
	 * if not only integers in the file)
	 * 
	 * @param args command-line arguments (ignored)
	 */
	public static void main(String[] args) {
		int largest = 0, smallest = 0, current, l=0;
			String fileIn = "";//create a string for in and out file names
			Scanner input = new Scanner(System.in);//Call Scanner object
			System.out.printf("Enter the name of the input file: ");
			fileIn = input.nextLine();//Set location of input file
		//TEST FILE
			try (Scanner fin = new Scanner(new File(fileIn)))  {//Attempt input file
					while(fin.hasNextInt()) { //while file has int
						current = fin.nextInt();//try to take next int
						//
						if (l == 0){                // Avoid lower number > 0
							smallest = current;     // By initializing smallest and largest
							largest = current; l++;}// to be the first number in the file     
						//
						if(current > largest) {//if int > largest
							largest = current;//set current to largest
						}else if(current < smallest) {//if int < smallest
							smallest = current;}}// set current to smallest
					if((fin.hasNext())||((smallest == 0)&&(largest == 0))) {//If more data in document, print error
			System.out.printf("File: invalid%n");
			}else {//if no remaining lines, print solution
					System.out.printf("File: [%d, %d]%n", smallest, largest);}
					
				}catch (FileNotFoundException ex) {//If input file cannot be found
				System.out.printf("File: invalid%n");
				System.exit(0);
			}
			
		}
	}
