import java.io.*;
import java.util.*;

class Coords {

	int x;
	int y;

	public Coords(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "Coords{" + "x=" + x + ", y=" + y + '}';
	}

}

public class Labirint {

	int N;
	int M;

	int[] dx = {-1, 0, 1, 0};
	int[] dy = {0, 1, 0, -1};

	char[][] labirint;
	int[][] dist;
	List<Coords> iesiri;

	public void read() {
		MyScanner scan = new MyScanner();

		int i, j, len;
		N = scan.nextInt();
		M = scan.nextInt();
		labirint = new char[N][];
		dist = new int[N][];
		iesiri = new ArrayList<>();
		String line;
		for (i = 0; i < N; i++) {
			labirint[i] = new char[M];
			dist[i] = new int[M];
			line = scan.next();
			len = line.length();
			for (j = 0; j < len; j++) {
				labirint[i][j] = line.charAt(j);
				dist[i][j] = -1;
				if (labirint[i][j] == 'G') {
					iesiri.add(new Coords(i, j));
				}
			}
		}
	}

	public void bfs() {
		Queue<Coords> q = new LinkedList<>();
		int[][] selected;
		selected = new int[N][];
		for (int i = 0; i < N; i++) {
			selected[i] = new int[M];
			for (int j = 0; j < M; j++) {
				selected[i][j] = 0;
			}
		}
		for (Coords c : iesiri) {
//			System.out.println(c);
			q.add(c);
			selected[c.x][c.y] = 1;
			dist[c.x][c.y] = 0;
		}
		Coords aux;
		int xn, yn;
		while (!q.isEmpty()) {
			aux = q.poll();
			selected[aux.x][aux.y] = 1;

			for (int i = 0; i < 4; i++) {
				xn = aux.x + dx[i];
				if (xn >= N) {
					xn = 0;
				} else if (xn < 0) {
					xn = N - 1;
				}

				yn = aux.y + dy[i];
				if (yn >= M) {
					yn = 0;
				} else if (yn < 0) {
					yn = M - 1;
				}

				if (selected[xn][yn] == 0 && labirint[xn][yn] != 'X') {
					dist[xn][yn] = dist[aux.x][aux.y] + 1;
					selected[xn][yn] = 1;
					q.add(new Coords(xn, yn));
				}
			}
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				System.out.print(dist[i][j] + " ");
			}
			System.out.println("");
		}
	}

	public static void main(String[] args) {
		Labirint l = new Labirint();

		l.read();
		l.bfs();
	}
}


//Folositi clasa aceasta ca sa cititi inputul mai rapid
class MyScanner {
   BufferedReader br;
   StringTokenizer st;
   
   public MyScanner() {
      br = new BufferedReader(new InputStreamReader(System.in));
   }
   
   String next() {
      while (st == null || !st.hasMoreElements()) {
         try {
            st = new StringTokenizer(br.readLine());
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      return st.nextToken();
   }
   
   int nextInt() {
      return Integer.parseInt(next());
   }
   
   long nextLong() {
      return Long.parseLong(next());
   }
   
   double nextDouble() {
      return Double.parseDouble(next());
   }
   
   String nextLine(){
      String str = "";
      try {
         str = br.readLine();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return str;
   }
}