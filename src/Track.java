
public class Track {

	TrackState[][] pos;
	
	public Track() {
		pos = new TrackState[32][17];
		
		//everything on track
		for(int y = 0; y < pos.length; y++) {
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
		for(int y = 6; y < pos.length; y++) {
			for(int x = 9; x < pos[y].length; x++) {
				pos[y][x] = TrackState.OFFROAD;
			}
		}
		pos[6][9] = TrackState.TRACK;
		
		//off track left bottom
		for(int y = 14; y<pos.length; y++) {
			pos[y][0] = TrackState.OFFROAD;
		}
		for(int y = 22; y<pos.length; y++) {
			pos[y][1] = TrackState.OFFROAD;
		}
		for(int y = 29; y<pos.length; y++) {
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

	@Override
	public String toString() {
		
		StringBuilder str = new StringBuilder();
		
		for(int y = 0; y < pos.length; y++) {
			for(int x = 0; x < pos[y].length; x++) {
				TrackState ts = pos[y][x];
				str.append(ts.toString().charAt(0));
			}
			str.append("\n");
		}
		
		return str.toString();
	}
}
