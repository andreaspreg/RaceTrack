import java.awt.Point;

public class RaceTrack {

	private Track track;
	
	
	public RaceTrack() {
		track = new Track();
		
		Episode episode = new Episode(track, new Point(4,31));
		episode.addAction(new Action(-1, -1));
		episode.addAction(new Action(-1, 0));
		episode.addAction(new Action(-1, 1));
		episode.addAction(new Action(-1, 0));
		episode.addAction(new Action(-1, 1));
		episode.addAction(new Action(0, 1));
		episode.addAction(new Action(1, 1));
		episode.addAction(new Action(1, 1));
		episode.addAction(new Action(1, 1));
		episode.addAction(new Action(1, 1));
		
		System.out.println(episode.toString());
		
		ValueFunction valueFunction = new ValueFunction(track);
	}
	
	public static void main(String[] args) {
		new RaceTrack();
	}

}
