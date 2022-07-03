import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class Point {

    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class Beamdrone {
	static class Task {
		public static final String INPUT_FILE = "beamdrone.in";
		public static final String OUTPUT_FILE = "beamdrone.out";

		int n;
		int m;
		char[][] labirint;
	    int[][] dist;
        int sx, sy, dx, dy;

		Queue<Point> q = new LinkedList<>();
		Queue<Integer> q_dir = new LinkedList<>();
		Queue<Integer> q_val = new LinkedList<>();

		//coada cu prioritate mai mare
		Queue<Point> Hq = new LinkedList<>();
		Queue<Integer> Hq_dir = new LinkedList<>();
		Queue<Integer> Hq_val = new LinkedList<>();
		int[][] visited;

		int[] directions_x = {-1, 1, 0, 0};
	    int[] directions_y = {0, 0, 1, -1};

		private void readInput() {
			try {
				Scanner sc = new Scanner(new BufferedReader(new FileReader(
								INPUT_FILE)));
				n = sc.nextInt();
				m = sc.nextInt();
                sx = sc.nextInt();
				sy = sc.nextInt();

                dx = sc.nextInt();
                dy = sc.nextInt();

                labirint = new char[n][];
		        dist = new int[n][];
		        String line;

				//se citeste matricea
		        for (int i = 0; i < n; i++) {

			        labirint[i] = new char[m];
			        dist[i] = new int[m];
			        line = sc.next();
			        int len = line.length();
			        for (int j = 0; j < len; j++) {

			    	    labirint[i][j] = line.charAt(j);
				        dist[i][j] = -1;
			        }
		        }
                
                dist[sx][sy] = 0;
				
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
		private int bfs() {

		    visited = new int[n][];
		    for (int i = 0; i < n; i++) {
			    visited[i] = new int[m];
			    for (int j = 0; j < m; j++) {
				    visited[i][j] = 0;
			    }
		    }
		    
            q.add(new Point(sx, sy));
			q_dir.add(4);
			q_val.add(0); //directia 4 : numai in cazul sursei care poate merge in orice sens
			visited[sx][sy] = 1;

		    Point aux;
			int aux_dir, val;
			
		    while (!q.isEmpty() || !Hq.isEmpty()) {

				if(!Hq.isEmpty()){

					//se trateaza mai intai elementele din coada Hq
					aux = Hq.poll();
					aux_dir = Hq_dir.poll();
					val = Hq_val.poll();
				}
				else{
					aux = q.poll();
					aux_dir = q_dir.poll();
					val = q_val.poll();
				}

				if (visited[aux.x][aux.y] == 1 && dist[aux.x][aux.y] < val){
					continue;
				}
				dist[aux.x][aux.y] = val;
			    visited[aux.x][aux.y] = 1;
				
				int xn, yn;
				int x = aux.x, y = aux.y;

				//pentru toate directiile
				for (int i = 0; i < 4; i++) {

					xn = x + directions_x[i];
					yn = y + directions_y[i];
					if (xn >= n || xn < 0 || yn >= m || yn < 0) 
						continue;
	
					if (labirint[xn][yn] != 'W') {
						
						if (aux_dir == 4 || aux_dir == i){
							//vecinul are aceasi directie

							Hq_val.add(dist[x][y]);
							Hq_dir.add(i);
							Hq.add(new Point(xn, yn));
							
						}
						else if (visited[xn][yn] != 1){

							q_val.add(dist[x][y] + 1);
							q_dir.add(i);
							q.add(new Point(xn, yn));

							visited[xn][yn] = 2; //in curs de verificare
						}
					}
				}
		    }
			return dist[dx][dy];
		}
		
		private int getResult() {
				
			return bfs();
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
