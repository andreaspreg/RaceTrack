import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Track {

	final static int WIDTH = 17;
	final static int HEIGHT = 32;
	
	TrackState[][] pos;
	
	public Track() {
		pos = new TrackState[HEIGHT][WIDTH];
		
		//everything on track
		for(int y = 0; y < HEIGHT; y++) {
			for(int x = 0; x < pos[y].length; x++) {
				pos[y][x] = TrackState.TRACK;
			}
		}
		
		//create finish line
		for(int y = 0; y<6; y++) {
			pos[y][16] = TrackState.FINISH;
		}
		
		//create finish line
		for(int x = 3; x<9; x++) {
			pos[31][x] = TrackState.START;
		}
		
		//off track right bottom
		for(int y = 6; y < HEIGHT; y++) {
			for(int x = 9; x < pos[y].length; x++) {
				pos[y][x] = TrackState.OFFROAD;
			}
		}
		pos[6][9] = TrackState.TRACK;
		
		//off track left bottom
		for(int y = 14; y<HEIGHT; y++) {
			pos[y][0] = TrackState.OFFROAD;
		}
		for(int y = 22; y<HEIGHT; y++) {
			pos[y][1] = TrackState.OFFROAD;
		}
		for(int y = 29; y<HEIGHT; y++) {
			pos[y][2] = TrackState.OFFROAD;
		}
		
		//off track left upper
		for(int y = 0; y < 4; y++) {
			for(int x = 0; x < 2; x++) {
				pos[y][x] = TrackState.OFFROAD;
			}
		}
		pos[3][1] = TrackState.TRACK;
		pos[0][2] = TrackState.OFFROAD;
	}
	
	public TrackState getTrackState(int y, int x) {
		if(y < 0 || y >= HEIGHT || x < 0 || x >= WIDTH) {
			return TrackState.OFFROAD;
		}
		
		return pos[y][x];
	}
	
	public Collection<Point> getFinishLine() {
		Collection<Point> line = new ArrayList<Point>();
		
		for(int y = 0; y < HEIGHT; y++) {
			for(int x = 0; x < WIDTH; x++) {
				if(pos[y][x] == TrackState.FINISH) {
					line.add(new Point(x,y));
				}
			}
		}
		
		return line;
	}

	public Point selectRandomStartPoint() {
		List<Point> startLine = new ArrayList<Point>();
		
		for(int y = 0; y < HEIGHT; y++) {
			for(int x = 0; x < WIDTH; x++) {
				if(pos[y][x] == TrackState.START) {
					startLine.add(new Point(x,y));
				}
			}
		}
		
		//select a random
		Random random = new Random();
		return startLine.get(random.nextInt(startLine.size()));
	}

	@Override
	public String toString() {
		
		StringBuilder str = new StringBuilder();
		
		for(int y = 0; y < HEIGHT; y++) {
			for(int x = 0; x < WIDTH; x++) {
				TrackState ts = pos[y][x];
				str.append(ts.toString().charAt(0));
			}
			str.append("\n");
		}
		
		return str.toString();
	}
}
