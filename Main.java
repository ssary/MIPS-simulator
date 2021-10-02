import java.io.*;
import java.util.*;

public class Main {
	static String JumpToIt;
	int memory[] = new int[1000];
	int register[] = new int[32];
	String path;
	ArrayList<String> lines = new ArrayList<String>() ;
	HashMap<String , Integer > map = new HashMap < String , Integer>();
	int idxProgram =0 ;
	
	
	public Main(String path) {
		this.path = path;
		initializeEmpty(memory);
		initializeEmpty(register);
		register[ getRegisterIndex("$zero") ] =0;
	}

	public void initializeEmpty(int[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = -100;
		}
	}
// ADD $t0 , $t1 , $t2
// part0 : ADD 
// part1 : $t0 , $t1 , $t2	
	public void printNonEmpty(int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] != -100)
				System.out.println("At index " + i + " Value: " + array[i]);
		}
	}

	public int getRegisterIndex(String registerName) {
		int result = 0;

		if (registerName .equals( "$zero"))
			return 0;
		if (registerName .equals( "$at"))
			return 1;

		if (registerName .equals( "$v0"))
			return 2;

		if (registerName .equals( "$v1"))
			return 3;

		if (registerName .equals( "$a0"))
			return 4;

		if (registerName .equals( "$a1"))
			return 5;

		if (registerName .equals( "$a2"))
			return 6;

		if (registerName .equals( "$a3"))
			return 7;

		if (registerName .equals( "$t0"))
			return 8;

		if (registerName .equals( "$t1"))
			return 9;

		if (registerName.equals("$t2"))
			return 10;

		if (registerName .equals( "$t3"))
			return 11;

		if (registerName .equals( "$t4"))
			return 12;

		if (registerName .equals( "$t5"))
			return 13;

		if (registerName .equals( "$t6"))
			return 14;

		if (registerName .equals( "$t7"))
			return 15;

		if (registerName .equals( "$s0"))
			return 16;

		if (registerName .equals( "$s1"))
			return 17;

		if (registerName .equals( "$s2"))
			return 18;

		if (registerName .equals( "$s3"))
			return 19;

		if (registerName .equals( "$s4"))
			return 20;

		if (registerName .equals( "$s5"))
			return 21;

		if (registerName .equals( "$s6"))
			return 22;

		if (registerName .equals( "$s7"))
			return 23;

		if (registerName .equals( "$t8"))
			return 24;

		if (registerName .equals( "$t9"))
			return 25;

		return result;
	}

	public void runInstruction() {
		String instruction = lines.get(idxProgram);
		String[] parts = instruction.split(" ");
			if(!parts[0].equals("")){
			System.out.println(parts[0]);
			
			if(parts[0].charAt(0)=='J') {
				idxProgram = map.get(parts[1]) - 1;
				}
			
			else if (parts[0].equals("ADD")) {
				String[] operands = parts[1].split(",");
				int indexOutput = getRegisterIndex(operands[0]);
				int indexOp1 = getRegisterIndex(operands[1]);
				int indexOp2 = getRegisterIndex(operands[2]);
				if(register[indexOutput] == -100)
					register[indexOutput] = 0;
				register[indexOutput] = register[indexOp1] + register[indexOp2];
				printNonEmpty(register);
				System.out.println("--------Instruction Done---------");
			} 
			
			else if (parts[0].equals("ADDI")) {

				String[] operands = parts[1].split(",");
				int indexOutput = getRegisterIndex(operands[0]);
				int indexOp1 = getRegisterIndex(operands[1]);
				int immediate = Integer.parseInt(operands[2]);
				if(register[indexOutput] == -100)
					register[indexOutput] = 0;
				register[indexOutput] = register[indexOp1] + immediate;
				printNonEmpty(register);
				System.out.println("--------Instruction Done---------");
			}
			
			else if(parts[0].equals("SUB")) {
				String[] operands = parts[1].split(",");
				int indexOutput = getRegisterIndex(operands[0]);
				int indexOp1 = getRegisterIndex(operands[1]);
				int indexOp2 = getRegisterIndex(operands[2]);		
				if(register[indexOutput] == -100)
					register[indexOutput] = 0;
				register[indexOutput] = register[indexOp1] - register[indexOp2];
				printNonEmpty(register);
				System.out.println("--------Instruction Done---------");
			}
			
			else if(parts[0].equals("LW")) { //Convention:lw $t0,8($s0)
				String[] operands = parts[1].split(",");
				
				int indexOutput = getRegisterIndex(operands[0]);
				
				String[] op = operands[1].split("\\("); // split el number 3n el register
				int idx = Integer.parseInt(op[0]) * 4; // number
				String base ="" + op[1].charAt(0) + op[1].charAt(1) + op[1].charAt(2) ;//base address
				
				
				int target = idx  + getRegisterIndex(base) ;
				
				register[indexOutput] = memory[target % memory.length];
				System.out.println(base + " " +idx + " " + indexOutput + " " + target);
				printNonEmpty(register);
				System.out.println("--------Instruction Done---------");
			}
			
			else if(parts[0].equals("SW")) { // convention : sw $t0,8($s0)
				String[] operands = parts[1].split(",");
				
				int indexOutput = getRegisterIndex(operands[0]);
				
				String[] op = operands[1].split("\\("); // split el number 3n el register
				int idx = Integer.parseInt(op[0])*4 ; // number
				String base ="" + op[1].charAt(0) + op[1].charAt(1) + op[1].charAt(2) ;//base address
				
				
				int target = idx * 4 + getRegisterIndex(base) ;
				
				memory[target%memory.length] = register[indexOutput] ;
				System.out.println(base + " " +idx + " " + indexOutput + " " + target);
				printNonEmpty(register);
				System.out.println("--------Instruction Done---------");
			}
			
			else if(parts[0].equals("BEQ")) { // beq $s3,$s4,Else
				String[] operands = parts[1].split(",");
				int indexOutput = getRegisterIndex(operands[0]);
				int indexOp1 = getRegisterIndex(operands[1]);
				String Line = operands[2];
				
				if(register[indexOutput] == register[indexOp1]) {
					idxProgram = map.get(Line) - 1;
				}
			}
			}
	
	}
	public void runProgram() {
		
		
		try {
			File myObj = new File(path);
			Scanner myReader = new Scanner(myObj);
			idxProgram =0 ;
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				//runInstruction(data);
				lines.add(data);
				if(data.contains(":")) {
					
					String op[] = data.split(" ");
					map.put(op[0],idxProgram);
					System.out.println(op[0] + "  " + map.get(op[0]));
				}
				idxProgram ++ ;
			}
			
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		idxProgram =0 ;
		while(idxProgram < lines.size()) {
			runInstruction();
			idxProgram ++ ;
		}
	}

	public static void main(String[] args) {
		Main run = new Main("program.txt");
		
		run.runProgram();

	}
}
