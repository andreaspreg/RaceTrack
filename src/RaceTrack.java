
public class RaceTrack {

	private Track track;
	
	public RaceTrack() {
		track = new Track();
		System.out.println(track.toString());
	}
	
	public static void main(String[] args) {
		new RaceTrack();
	}

}
