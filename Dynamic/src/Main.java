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

	String currency[] = {"CNY.txt", "EUR.txt" ,"JPY.txt", "SGD.txt", "USD.txt"};
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
//		StringBuilder sb = new StringBuilder();
	
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

		//int nDays = sellRate.get(0).size();
		int nDays = 10;

		System.out.println(nDays);
		System.out.println(currency.length);
		
		long startTime = System.nanoTime();
		
		double dp[][] = new double[currency.length+1][nDays]; 
		double fundingTHB = 10000;
		
		// Base cases
		for(int d=0;d<nDays;d++) dp[0][d] = 0;
		for(int c=1;c<currency.length+1;c++) {
			dp[c][0] = Math.floor(fundingTHB/sellRate.get(c-1).get(0));
			//System.out.println(dp[c][0]);
		}
		dp[0][0] = fundingTHB; // THB Funding
		
		
		System.out.println(dp.length+" "+dp[0].length);
		for(int d=1;d<nDays;d++) {
			for(int c=0;c<currency.length+1;c++) {
				if (c==0) {
					for (int j=0;j<currency.length+1;j++) {
						if (j==0) dp[c][d] = Math.max(dp[j][d], dp[c][d-1]);
						else {
							dp[c][d] = Math.max(dp[c][d], dp[j][d-1]*buyRate.get(j-1).get(d));
						}
					}
				} else {
					dp[c][d] = Math.max(dp[c][d-1], Math.floor(dp[0][d] / sellRate.get(c-1).get(d)));
					//System.out.println(d+" "+c);
				}
			}
		}
		
		long elapsedTime = (System.nanoTime() - startTime);
		System.out.println("Algorithm execution time in nanosec: " + elapsedTime);
		System.out.println("Final outocome: "+dp[0][nDays-1]);

	}

}
