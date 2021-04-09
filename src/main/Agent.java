package main;

import java.util.LinkedList;

public class Agent extends Thread {

	public Agent(Mutant m, LinkedList<Test> ts) {
		mutant = m;
		tests = ts;
	}
	
	@Override
	public void run() {
		int len = tests.size();
		results = new boolean[len];
		idx = new int[len];
		
		Test t;
		for (int i = 0; i < len; i++) {
			t = tests.pop();
			idx[i] = t.getIdx();
			results[i] = detected(mutant, t);
		}
		
	}

	public boolean[] getResults() {
		return results;
	}

	public int[] getIdx() {
		return idx;
	}

	public Mutant getMutant() {
		return mutant;
	}
	
	private boolean detected(Mutant m, Test t) {
		return m.detected(t); //We assume all the tests have not been executed previously in this mutant?
	}
	
	private int[] idx;
	private boolean[] results;
	private Mutant mutant;
	private LinkedList<Test> tests;
}
