package main;

import java.util.LinkedList;

public class Mutant {

	@SuppressWarnings("unchecked")
	public Mutant(LinkedList<Test> t, int i, int[] simul) {
		load_mutant();
		tests = (LinkedList<Test>) t.clone();
		idx = i;
		simul_vector = simul;
		killed = 0;
	}
	
	public boolean detected(Test t) {
		//If detected true, if not detected false.
		return simul_vector[t.getIdx()] > 0; //TODO: execute test on the mutant.
	}
	
	public LinkedList<Test> getTests() {
		return tests;
	}
	
	public int getIdx() {
		return idx;
	}
	
	public int getKilled() {
		return killed;
	}

	public void setKilled(int killed) {
		this.killed = killed;
	}
	
	private void load_mutant() { //TODO: load mutant
		
	}
	
	private LinkedList<Test> tests;
	private int idx;
	private int[] simul_vector;
	private int killed;
}
