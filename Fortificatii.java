import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

class Nod {

    String type = "P";
    ArrayList<Integer> cost;
    ArrayList<Integer> edges;

    public Nod(){

        cost = new ArrayList<>();
        edges = new ArrayList<>();
    }

    @Override
    public String toString(){

        String line = type + " - Edges:";

        for (int i = 0; i < edges.size(); i++){
            line += (edges.get(i)+1) + " - c =" + cost.get(i) + "; ";
        }
        return line;
    }
}

public class Fortificatii {
	static class Task implements Cloneable  {
		public static final String INPUT_FILE = "fortificatii.in";
		public static final String OUTPUT_FILE = "fortificatii.out";

		int n;
		int m;
		int k;
        int b;

		Nod [] fortificatii;

		boolean [] block;
		boolean [] visited;

		private void readInput() {
			try {
				Scanner sc = new Scanner(new BufferedReader(new FileReader(
								INPUT_FILE)));
				n = sc.nextInt();
				m = sc.nextInt();
				k = sc.nextInt();

                b = sc.nextInt();

                fortificatii = new Nod[n];
                for (int i = 0; i < n; i++){

                    fortificatii[i] = new Nod();
                }

                //orase in care se afla barbarii
                for (int i = 0; i < b; i++){

                    int barbs = sc.nextInt() - 1;
                    fortificatii[barbs].type = "B";
                }
				
                //citire muchii
				for (int i = 0; i < m; i++) {
                    
                    int n1 = sc.nextInt() - 1, n2 = sc.nextInt() - 1, c = sc.nextInt();

                    // daca 2 orase barbare sunt conectate, nu adaug muchia
                    if(fortificatii[n1].type == "B" && fortificatii[n2].type == "B"){

                        continue;
                    }
                    fortificatii[n1].edges.add(n2);
                    fortificatii[n2].edges.add(n1);

                    fortificatii[n1].cost.add(c);
                    fortificatii[n2].cost.add(c);
				}
				
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(int result) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
								OUTPUT_FILE)));
				pw.printf("%d",result);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
        }
		
        private int bfs(int nod) {

            int[] dist;
            int[] visited;

            Queue<Integer> q = new LinkedList<>();

		    visited = new int[n];
            dist = new int[n];
		    for (int i = 0; i < n; i++) {
            
                visited[i] = 0;
                dist[i] = -1;
		    }
		    
            q.add(nod);
			visited[nod] = 1;
            dist[nod] = 0;

		    int x;
		    while (!q.isEmpty()) {
				
                x = q.poll();
                
                int size = fortificatii[x].edges.size();
                for(int i = 0; i < size; i++){

                    int nxt = fortificatii[x].edges.get(i);
                    if(fortificatii[nxt].type == "B"){
                        continue;
                    }

                    if(visited[nxt] == 0){

                        visited[nxt] = 2;
                        dist[nxt] = dist[x] + fortificatii[x].cost.get(i);

                        //destinatia este orasul 0 (capitala)
                        if(nxt != 0){
                            
                            q.add(nxt);
                        }
                    }
                    else{

                        if(dist[nxt] > dist[x] + fortificatii[x].cost.get(i)){

                            dist[nxt] = dist[x] + fortificatii[x].cost.get(i);
                            if(nxt != 0){
                            
                                q.add(nxt);
                            }
                        }
                    }
                }
		    }
			return dist[0];
		}

		private int getResult() {

            List<Integer> ways = new ArrayList<>(); // costurile drumurilor pe care barbarii o pot lua
            int nrW = 0;

            for(int i = 0; i < n; i++){

                if(fortificatii[i].type == "B"){
                    //Pentru fiecare oras invecinat cu unul barbar, se calculeaza
                    // drumul minim spre capitala

                    int size = fortificatii[i].edges.size();
                    for(int j = 0; j < size; j++){
                        
                        ways.add(bfs(fortificatii[i].edges.get(j)) + fortificatii[i].cost.get(j)) ;
                        nrW ++;
                    }
                }
            }

            Collections.sort(ways);

            Integer[] w = new Integer[nrW];
            w = ways.toArray(w); 

            int index = 0;
            while(k != 0){

                if(index == nrW - 1){

                    w[index] = w[index] + k / (index + 1);
                    k = 0;
                    continue;
                }

                int act = w[index], nxt = w[index + 1];
                
                if(act == nxt){
                    index ++;
                    continue;
                }

                int coverS = (nxt - act) * (index + 1);

                if(coverS <= k){

                    k -= coverS;
                    w[index] = nxt;
                }
                else{

                    w[index] = w[index] + k / (index + 1);
                    k = 0;
                }
            }
            
			return w[index];
		}


        public void solve() {
			readInput();
			writeOutput(getResult());
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}