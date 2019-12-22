import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StreamTokenizer;
import java.util.ArrayList;

public class Main {
	
	StreamTokenizer stk;
	double readDouble() throws Exception {
		stk.nextToken();
		return stk.nval;
	}

	String currency[] = {"CNY.txt", "EUR.txt", "SGD.txt", "USD.txt"};
	ArrayList<ArrayList<Double>> sellRate;
	ArrayList<ArrayList<Double>> buyRate;
	double THB;
	public static void main(String args[]) throws Exception {
//		System.setIn(new FileInputStream("in.txt"));
		System.setOut(new PrintStream(new FileOutputStream("out.txt")));
		new Main().run();
	}

	void run() throws Exception {
		stk = new StreamTokenizer(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
	
		THB = 10000;
		sellRate = new ArrayList<ArrayList<Double>>();
		buyRate = new ArrayList<ArrayList<Double>>();
		for (int i=0;i<currency.length;i++) buyRate.add(new ArrayList<Double>());
		for (int i=0;i<currency.length;i++) sellRate.add(new ArrayList<Double>());
		
		
//		Read exchange rate from each currency
		for (int i=0;i<currency.length;i++) {
			System.setIn(new FileInputStream(currency[i]));
			stk = new StreamTokenizer(new InputStreamReader(System.in));
			while (stk.nextToken()!= StreamTokenizer.TT_EOF) {
				stk.nextToken();
				stk.nextToken();
				stk.nextToken();
				buyRate.get(i).add(stk.nval);
				stk.nextToken();
				stk.nextToken();
				sellRate.get(i).add(stk.nval);
			}
//			System.out.println(exchangeRate.get(i).size());
		}

		/*
		 * Exchange THB to other step:
		 * 1. Select currency
		 * 2. Exchange THB to selected one
		 * 3. The result would be the selected currency and the rest of THB
		 *
		 * Calculate Final Outcome = Sum of each currency * exchange rate of last date
		 * 
		 * */
		
		long startTime = System.nanoTime();
		
		/*
		 * Greedy algorithm
		 * Greedy criteria: Maximum Outcome for Each days
		 * */
		//int nDays = sellRate.get(0).size();
		int nDays= 1000;
		// Rate of the last day that exchange currency
		double best[] = {sellRate.get(0).get(0), sellRate.get(1).get(0), sellRate.get(2).get(0), sellRate.get(3).get(0)};
		
		for(int d=1;d<nDays;d++) {
			double temp = THB;
			double profit = 0;
			boolean trade = false;
			for(int i=0;i<currency.length;i++) {
				if (best[i] > sellRate.get(i).get(d)) best[i] = sellRate.get(i).get(d); 
				double cProfit = (buyRate.get(i).get(d)-best[i]) * THB/best[i];
				if (cProfit > profit) {
					trade = true;
					temp = cProfit+THB;
				}
			}
			if (trade) {
				for(int a=0;a<currency.length;a++) {
					best[a] = sellRate.get(a).get(d);
				}
			}
			THB = temp;
		}
		System.out.println("Final outcome: "+THB);
		long elapsedTime = (System.nanoTime() - startTime);
		System.out.println("Algorithm execution time in nanosec: " + elapsedTime);
	}
}

