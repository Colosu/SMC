package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class Main {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws InterruptedException {
		
		Random rand = new Random();
		
		File folder = new File("./TestsSM");
		File[] files_list = folder.listFiles();
		Arrays.sort(files_list);
		
		Mutant m;
		LinkedList<Test> t;
		boolean[] results;
		int[] idx;
		double ops = 0.0;
		double q_5 = 0.0;
		double q_25 = 0.0;
		double q_set = 0.0;
		double[] q_vals;
		double BF_ops = 0.0;
		double cap_ops = 0.0;
		double cap_q_5 = 0.0;
		double cap_q_25 = 0.0;
		double cap_q_set = 0.0;
		double[] cap_vals;
		double rand_ops = 0.0;
		double rand_q_5 = 0.0;
		double rand_q_25 = 0.0;
		double rand_q_set = 0.0;
		double[] rand_vals;
		double saving = 0.0;
		double cap_saving = 0.0;
		double rand_saving = 0.0;
		int lim = 0;

		String Ofile;
		FileWriter OFile;
		String Ofile2;
		FileWriter OFile2;
		try {
			Ofile = "Ops.txt";
			OFile = new FileWriter(Ofile);
			OFile.write("| #Test | Brute Force | SMC | Cap | Random | SMC Saving Percentage | Cap Saving Percentage | Random Saving Percentage |\n");
			OFile.flush();
			Ofile2 = "Quality.txt";
			OFile2 = new FileWriter(Ofile2);
			OFile2.write("| #Test | SMC < 5% | Cap < 5% | Random < 5% | SMC > 25% | Cap > 25% | Random > 25% | SMC in set | Cap in set | Random in set |\n");
			OFile2.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		int I = 0;
		for (File Ifile : files_list) {

			System.out.println("===================================");
			System.out.println(Ifile.toString());
			
			load_matrix(Ifile.listFiles()[0]);
			
			mutants = new Mutant[num_mutants];
			tests = new LinkedList<Test>();
			agents = new Agent[num_agents];
			
//			print_matrix("Original", simul_matrix);
	
			load_tests();
			load_mutants();
			
			hard_mutants = new LinkedList<Mutant>();
			final_hard_mutants = new LinkedList<Mutant>();
			cap_hard_mutants = new LinkedList<Mutant>();
			rand_hard_mutants = new LinkedList<Mutant>();
			
			for (Mutant mu : mutants) {
				hard_mutants.add(mu);
			}
			
			ops = 0;
			lim = 0;
			while (!hard_mutants.isEmpty()) {
				
				for (int j = 0; j < num_agents; j++) {
					if (!hard_mutants.isEmpty()) {
						m = hard_mutants.pop();
						t = new LinkedList<Test>();
						if (m.getKilled() + m.getTests().size() > lim) {
							for (int k = 0; k < test_per_agent; k++) {
								if (!m.getTests().isEmpty()) {
									t.add(m.getTests().pop());
									ops++;
								}
							}
							agents[j] = new Agent(m, t);
						} else {
							final_hard_mutants.add(m);
							agents[j] = null;
						}
					} else {
						agents[j] = null;
					}
				}
				
				for (Agent a : agents) {
					if (a != null) {
						a.start();
					}
				}
				
				for (Agent a : agents) {
					if (a != null) {
						a.join();
					}
				}
				
				for (Agent a : agents) {
					if (a != null) {
						results = a.getResults();
						idx = a.getIdx();
						for (int j = 0; j < results.length; j++) {
							matrix[a.getMutant().getIdx()][idx[j]] = results[j] ? 1 : -1;
						}
					}
				}
				
				lim = update_hard();
			}
			
			q_vals = comp_hard_quality(final_hard_mutants);
			q_5 = q_vals[0];
			q_25 = q_vals[1];
			q_set = q_vals[2];

			BF_ops = num_mutants*num_tests;
			
			cap_ops = comp_cap_ops();
			cap_vals = comp_hard_quality(cap_hard_mutants);
			cap_q_5 = cap_vals[0];
			cap_q_25 = cap_vals[1];
			cap_q_set = cap_vals[2];
			

			rand_ops = ops;
			comp_rand_ops(rand, (int)ops);
			rand_vals = comp_hard_quality(rand_hard_mutants);
			rand_q_5 = rand_vals[0];
			rand_q_25 = rand_vals[1];
			rand_q_set = rand_vals[2];
			
//			print_matrix("Created", matrix);
//			print_difference(simul_matrix, matrix);
			saving = 1 - ops/BF_ops;
			cap_saving = 1 - cap_ops/BF_ops;
			rand_saving = 1 - rand_ops/BF_ops;
			System.out.println("===================================");
			System.out.println("Operations: " + Double.toString(ops) + " of " + Double.toString(BF_ops));
			System.out.println("Saving: " + String.format("%.2f", saving*100).replace(',', '.'));
			System.out.println("===================================");
			
			try {
				OFile.write(Integer.toString(I) + " & " + String.format("%d", (int)BF_ops).replace(',', '.')
						+ " & " + String.format("%d", (int)ops).replace(',', '.')
						+ " & " + String.format("%d", (int)cap_ops).replace(',', '.')
						+ " & " + String.format("%d", (int)rand_ops).replace(',', '.')
						+ " & " + String.format("%.2f", saving*100).replace(',', '.')
						+ " & " + String.format("%.2f", cap_saving*100).replace(',', '.')
						+ " & " + String.format("%.2f", rand_saving*100).replace(',', '.') + "\\\\\n");
				OFile.write("\\hline\n");
				OFile.flush();
				OFile2.write(Integer.toString(I) + " & " + String.format("%d", (int)q_5).replace(',', '.')
						+ " & " + String.format("%d", (int)cap_q_5).replace(',', '.')
						+ " & " + String.format("%d", (int)rand_q_5).replace(',', '.')
						+ " & " + String.format("%d", (int)q_25).replace(',', '.')
						+ " & " + String.format("%d", (int)cap_q_25).replace(',', '.')
						+ " & " + String.format("%d", (int)rand_q_25).replace(',', '.')
						+ " & " + String.format("%d", (int)q_set).replace(',', '.')
						+ " & " + String.format("%d", (int)cap_q_set).replace(',', '.')
						+ " & " + String.format("%d", (int)rand_q_set).replace(',', '.') + "\\\\\n");
				OFile2.write("\\hline\n");
				OFile2.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			
			I++;
		}
		
		System.out.println("===================================");
		System.out.println("Finished");
		System.out.println("===================================");
		
		try {
			OFile.close();
			OFile2.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private static double[] comp_hard_quality(LinkedList<Mutant> set) {
		double q_5 = 0.0;
		double q_25 = 0.0;
		double q_set = 0.0;
		double[] results = new double[3];
		double lim = 0.0;
		int val = 0;
		for (Mutant m : set) {
			val = Arrays.stream(simul_matrix[m.getIdx()]).filter(a -> a > 0).sum();
			if (val > lim) {
				lim = val;
			}
		}
		val = 0;
		for (int i = 0; i < num_mutants; i++) {
			val = Arrays.stream(simul_matrix[i]).filter(a -> a > 0).sum();
			if (val < num_tests*q_inf) {
				if (!set.contains(mutants[i])) {
					q_5++;
				}
			}
			if (val > num_tests*q_sup) {
				if (set.contains(mutants[i])) {
					q_25++;
				}
			}
			if (val < lim) {
				if (!set.contains(mutants[i])) {
					q_set++;
				}
			}
		}
		results[0] = q_5;
		results[1] = q_25;
		results[2] = q_set;
		return results;
	}
	
	private static double comp_cap_ops() {
		double ops = 0.0;
		int j = 0;
		double count = 0.0;
		double lim = num_tests*0.05;
		
		for (int i = 0; i < num_mutants; i++) {
			j = 0;
			count = 0;
			while (j < num_tests && count <= lim) {
				if (simul_matrix[i][j] == 1) {
					count++;
				}
				j++;
			}
			if (j >= num_tests) {
				cap_hard_mutants.add(mutants[i]);
			}
			ops += j;
		}
		return ops;
	}
	
	private static void comp_rand_ops(Random rand, int ops) {
		int[][] matr = new int[num_mutants][num_tests];
		int mut = 0;
		int tes = 0;
		double val = 0.0;
		
		for (int i = 0; i < num_mutants; i++) {
			for (int j = 0; j < num_tests; j++) {
				matr[i][j] = 0;
			}
		}
		
		for (int i = 0; i < ops; i++) {
			mut = rand.nextInt(num_mutants);
			tes = rand.nextInt(num_tests);
			matr[mut][tes] = simul_matrix[mut][tes];
		}
		
		for (int i = 0; i < num_mutants; i++) {
			val = Arrays.stream(matr[i]).filter(a -> a > 0).sum();
			if (val <= num_tests*0.05) {
				rand_hard_mutants.add(mutants[i]);
			}
		}
	}
	
	private static int update_hard() {
		
		int min = num_tests;
		int max = 0;
		int value = 0;
		int lim = 0;
		
		hard_mutants = new LinkedList<Mutant>();
		
		for (int i = 0; i < num_mutants; i++) {
			value = Arrays.stream(matrix[i]).filter(a -> a > 0).sum();
			mutants[i].setKilled(value);
			if (value > max) {
				max = value;
			}
			if (value < min && value > 0) {
				min = value;
			}
		}
		
		if (min > max) {
			min = 0;
		}
		
		lim = min + (max - min)/4;
		
		for (int i = 0; i < num_mutants; i++) { 
			value = Arrays.stream(matrix[i]).filter(a -> a > 0).sum();
			if (value <= lim && !mutants[i].getTests().isEmpty() && !final_hard_mutants.contains(mutants[i])) { //TODO: chose a good comparison.
//			if (value <= num_tests*0.05 && !mutants[i].getTests().isEmpty()) { //TODO: chose a good comparison.
				hard_mutants.add(mutants[i]);
			} else if (value <= lim && mutants[i].getTests().isEmpty() && !final_hard_mutants.contains(mutants[i])) {
				final_hard_mutants.add(mutants[i]);
			}
		}
		
		return lim;
	}
	
	private static void load_matrix(File file) {

		num_mutants = 0;
		num_tests = 0;
		BufferedReader FR;
		String line;
		String[] vals;
		
		try {
			FR = new BufferedReader(new FileReader(file));
			line = FR.readLine();
			num_mutants = (int) Files.lines(file.toPath()).count() - 1;
			num_tests = line.split(" ").length - 1;
			
			matrix = new int[num_mutants][num_tests];
			simul_matrix = new int[num_mutants][num_tests];
			
			for (int i = 0; i < num_mutants; i++) {
				line = FR.readLine();
				vals = line.split(" ");
				for (int j = 0; j < num_tests; j++) {
					matrix[i][j] = 0;
					simul_matrix[i][j] = Integer.valueOf(vals[1+j]) > 0 ? 1 : -1;
				}
			}
			
			FR.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private static void load_tests() {
		//TODO: load tests
		for (int i = 0; i < num_tests; i++) {
			tests.add(new Test(i));
		}
	}
	
	private static void load_mutants() {
		//TODO: load mutants
		for (int i = 0; i < num_mutants; i++) {
			mutants[i] = new Mutant(tests, i, simul_matrix[i]);
			
		}
	}
	
	@SuppressWarnings("unused")
	private static void print_matrix(String name, int[][] mat) {
		int len_1 = mat.length;
		int len_2 = mat[0].length;
		String line;
		int suma;
		
		System.out.println("===================================");
		System.out.println("Matrix: " + name);
		System.out.println("===================================");
		
		for (int i = 0; i < len_1; i++) {
			line = "[";
			for (int j = 0; j < len_2; j++) {
				line = line + Integer.toString(mat[i][j]) + ", ";
			}
			line = line + "]";
			System.out.println(line);
		}
		System.out.println("===================================");
		line = "[";
		for (int i = 0; i < len_1; i++) {
			suma = Arrays.stream(mat[i]).filter(a -> a > 0).sum();
			line = line + "[" + Integer.toString(suma) + "]";
		}
		line = line + "]";
		System.out.println(line);
		System.out.println("===================================");
	}
	
	@SuppressWarnings("unused")
	private static void print_difference(int[][] mat_1, int[][] mat_2) {
		int len = mat_1.length;
		String line;
		int suma_1;
		int suma_2;

		System.out.println("===================================");
		line = "[";
		for (int i = 0; i < len; i++) {
			suma_1 = Arrays.stream(mat_1[i]).filter(a -> a > 0).sum();
			suma_2 = Arrays.stream(mat_2[i]).filter(a -> a > 0).sum();
			line = line + "[" + Integer.toString(suma_1 - suma_2) + "]";
		}
		line = line + "]";
		System.out.println(line);
		System.out.println("===================================");
	}
	
	private static int num_mutants = 50;
	private static int num_tests = 50;
	private static int num_agents = num_mutants/10;
	private static int test_per_agent = num_tests/10;
	private static Mutant[] mutants;
	private static LinkedList<Mutant> hard_mutants;
	private static LinkedList<Mutant> final_hard_mutants;
	private static LinkedList<Mutant> cap_hard_mutants;
	private static LinkedList<Mutant> rand_hard_mutants;
	private static LinkedList<Test> tests;
	private static Agent[] agents;
	private static int[][] matrix;
	private static int[][] simul_matrix;
	private static double q_inf = 0.05;
	private static double q_sup = 0.25;
}
