import java.awt.Point;
import java.util.Collection;

public class ValueFunction {
	
	Track track;
	
	//values[y][x][velY][velX]
	double values[][][][];
	
	public ValueFunction(Track track) {
		this.track = track;
		int maxVelocity = Math.max(track.height, track.width);
		values = new double[track.height][track.width][maxVelocity][maxVelocity];
		
		initValues();
	}
	
	private void initValues() {
		
		Collection<Point> finishLine = track.getFinishLine();
		
		//init values by distance
		for(int y=0; y<values.length; y++) {
			for(int x=0; x<values[0].length; x++) {
				for(int velY=0; velY<values[0][0].length; velY++) {
					for(int velX=0; velX<values[0][0][0].length; velX++) {
						initValue(finishLine, y,x,velY,velX);
					}
				}
			}
		}
	}

	private void initValue(Collection<Point> finishLine, int y, int x, int velY, int velX) {
		double minDistance = track.height + track.width;
		for(Point finish : finishLine) {
			double dx = finish.x - x;
			double dy = finish.y - y;
			double distance = Math.sqrt(dx*dx + dy*dy);
			minDistance = Math.min(distance, minDistance);
		}
		values[y][x][velY][velX] = minDistance;
	}
}
