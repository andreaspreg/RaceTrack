import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Episode {
	
	Track track;
	
	List<Action> actions = new ArrayList<Action>();
	final Point start;
	
	final int[][] path;
	
	public Episode(Track track, Point start) {
		this.track = track;
		this.start = start;
		
		path = new int[track.height][track.width];
	}
	
	public void addAction(Action action) {
		actions.add(action);
	}
	
	public void generatePath() {
		
		//reset path
		for(int y = 0; y < track.height; y++) {
			for(int x = 0; x < track.width; x++) {
				path[y][x] = 0;
			}
		}
		
		//set start point
		path[start.y][start.x] = 1;
		
		//iterate through actions
		Point currentPos = (Point) start.clone();
		Point currentSpeed = new Point(0,0);
		int i = 2;
		
		for(Action action : actions) {
			currentSpeed.x += action.accX;
			currentSpeed.y += action.accY;
			currentPos.x += currentSpeed.x;
			currentPos.y += currentSpeed.y;
			
			if(currentPos.x < 0 || currentPos.x >= track.width ||
					currentPos.y < 0 || currentPos.y >= track.height) {
				continue;
			}
			
			path[currentPos.y][currentPos.x] = i++;
		}
	}

	@Override
	public String toString() {
		generatePath();
		
		StringBuilder str = new StringBuilder();
		
		for(int y = 0; y < track.pos.length; y++) {
			for(int x = 0; x < track.pos[y].length; x++) {
				TrackState ts = track.pos[y][x];
				if(path[y][x] > 0) {
					if(ts == TrackState.OFFROAD) {
						str.append("X");
					}
					else {
						str.append(Integer.toString(path[y][x]).charAt(0));
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
