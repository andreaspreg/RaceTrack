import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Episode {
	
	Track track;
	
	int reward = 0;

	List<Action> actions = new ArrayList<Action>();
	List<State> states = new ArrayList<State>();
	final Point start;
	
	final int[][] path;
	int pathNumber = 1;
	
	public Episode(Track track, Point start) {
		this.track = track;
		this.start = start;
		
		//create and initialize path array
		path = new int[Track.HEIGHT][Track.WIDTH];
		for(int y = 0; y < Track.HEIGHT; y++) {
			for(int x = 0; x < Track.WIDTH; x++) {
				path[y][x] = 0;
			}
		}
		path[start.y][start.x] = pathNumber++;
	}
	
	public void add(Action action, State state) {
		actions.add(action);
		states.add(state);
		
		addPath(state);
	}
	
	private void addPath(State state) {
		if(state.posX >= 0 && state.posX < Track.WIDTH &&
				state.posY >= 0 && state.posY < Track.HEIGHT) {
			path[state.posY][state.posX] = pathNumber++;
		}
	}

	@Override
	public String toString() {		
		StringBuilder str = new StringBuilder();
		
		for(int y = 0; y < track.pos.length; y++) {
			for(int x = 0; x < track.pos[y].length; x++) {
				TrackState ts = track.pos[y][x];
				if(path[y][x] > 0) {
					if(ts == TrackState.OFFROAD) {
						str.append("X");
					}
					else {
						String s = Integer.toString(path[y][x]);
						str.append(s.charAt(s.length()-1));
					}
				}
				else {
					
					str.append(ts.toString().charAt(0));
				}
			}
			str.append("\n");
		}
		
		return str.toString();
	}
}
