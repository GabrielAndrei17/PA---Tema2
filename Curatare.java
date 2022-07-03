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
class PointOnMap {
    Point c;

    int[] dist; //distantele de la punct la fiecare spatiu murdar

    public PointOnMap(Point c){
        this.c = c;

        dist = new int[4];
        for(int i = 0; i < 4; i++)
            dist[i] = -1;
    }
}

public class Curatare {
	static class Task implements Cloneable  {
		public static final String INPUT_FILE = "curatare.in";
		public static final String OUTPUT_FILE = "curatare.out";

		int n;
		int m;
		char[][] labirint;

		Queue<Point> q = new LinkedList<>();
        PointOnMap[] robos = new PointOnMap[4];
        PointOnMap[] spaces = new PointOnMap[4];

        int nrRobo = 0;
        int nrspace = 0;

		int[] directions_x = {-1, 1, 0, 0};
	    int[] directions_y = {0, 0, 1, -1};

		private void readInput() {
			try {
				Scanner sc = new Scanner(new BufferedReader(new FileReader(
								INPUT_FILE)));
				n = sc.nextInt();
				m = sc.nextInt();

                labirint = new char[n][];
		        String line;

                //se citeste matricea
		        for (int i = 0; i < n; i++) {
			    
                    labirint[i] = new char[m];
			        line = sc.next();
			        int len = line.length();
			        for (int j = 0; j < len; j++) {
			    	    labirint[i][j] = line.charAt(j);

                        if(labirint[i][j] == 'R'){
                            nrRobo ++;
                            robos[nrRobo - 1] = new PointOnMap(new Point(i, j));

                        }
                        if(labirint[i][j] == 'S'){
                            nrspace ++;
                            spaces[nrspace - 1] = new PointOnMap(new Point(i, j));
                        }
			        }
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
		private int[][] bfs(Point c) {

            int[][] dist;
            int[][] visited;

		    visited = new int[n][];
            dist = new int[n][];
		    for (int i = 0; i < n; i++) {

			    visited[i] = new int[m];
                dist[i] = new int[m];
			    for (int j = 0; j < m; j++) {
				
                    visited[i][j] = 0;
                    dist[i][j] = -1;
			    }
		    }
		    
            q.add(c);
			visited[c.x][c.y] = 1;
            dist[c.x][c.y] = 0;

		    Point aux;
		    while (!q.isEmpty()) {
				aux = q.poll();
				
				int xn, yn;
				int x = aux.x, y = aux.y;

                visited[x][y] = 1;

				for (int i = 0; i < 4; i++) {

					xn = x + directions_x[i];
					yn = y + directions_y[i];
					if (xn >= n || xn < 0 || yn >= m || yn < 0) 
						continue;
	
					if (visited[xn][yn] == 0 && labirint[xn][yn] != 'X') {
												
						dist[xn][yn] = dist[x][y] + 1;
                        q.add(new Point(xn, yn));
			            visited[xn][yn] = 1;
					}
				}
		    }
			return dist;
		}
		
        int cleaned[]; // cleaned[i] = 1 => spatiul cu indicele 'i' este curat

		private int getResult() {
			int[][] dist;
            cleaned = new int[4];

            for(int i = nrspace; i < 4; i++)
                cleaned[i] = 1;
            for(int i = 0; i < nrspace; i++)
                cleaned[i] = 0;

            for(int i = 0; i < nrRobo; i++){
                dist = bfs(robos[i].c);

                //distanta de la un robot la spatiile murdare
                for(int j = 0; j < nrspace; j++){
                    robos[i].dist[j] = dist[spaces[j].c.x][spaces[j].c.y];
                }
            }
            for(int i = 0; i < nrspace; i++){
                dist = bfs(spaces[i].c);

                //distanta de la un spatiu la celelalte spatii murdare
                for(int j = 0; j < nrspace; j++){
                    spaces[i].dist[j] = dist[spaces[j].c.x][spaces[j].c.y];
                }
            }
            backtracking(0);
            
			return minim;
		}

        int minim = 1000000;
        int times[] = {0,0,0,0}; //times[i] => timpul pe care robotul 'i' il parcurge

        private void backtracking(int nr_cln){
           
            // daca numarul de spatii curatate este egal cu numarul de spatii
            if(nr_cln == nrspace){

                //se calculeaza timpul petrecut sa curete
                int maxim = Math.max(Math.max(times[0], times[1]), Math.max(times[2], times[3])); 

                //actualizam rezultatul
                if(maxim < minim){
                    minim = maxim;
                }
                return;
            }

            //pentru fiecare robot disponibil
            for (int i = 0; i < nrRobo; i++){

                //incercam sa curatam spatiile necuratate
                for (int j = 0; j < nrspace; j++)
                    if(cleaned[j] == 0){

                        times[i] += robos[i].dist[j];
                        cleaned[j] = 1;

                        PointOnMap aux_robo = robos[i];
                        //robotul 'i' se va deplasa pe pozitia spatiului 
                        robos[i] = spaces[j];
                        backtracking(nr_cln + 1);
                        robos[i] = aux_robo;

                        cleaned[j] = 0;
                        times[i] -= robos[i].dist[j];
                    }
            }

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
