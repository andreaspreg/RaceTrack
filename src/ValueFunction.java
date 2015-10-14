import java.awt.Point;
import java.util.Collection;

public class ValueFunction {

	final static int VELOCITY_DIMENSION = 2 * DynamicProgramming.MAX_VELOCITY + 1;
	
	//values[y][x][velY][velX]
	double values[][][][];
	
	public ValueFunction(Track track) {
		values = new double[Track.HEIGHT][Track.WIDTH][VELOCITY_DIMENSION][VELOCITY_DIMENSION];
		
		initValues(track);
	}
	
	private void initValues(Track track) {
		
		Collection<Point> finishLine = track.getFinishLine();
		
		//init values by distance
		for(int y=0; y<values.length; y++) {
			for(int x=0; x<values[0].length; x++) {
				for(int velY=-DynamicProgramming.MAX_VELOCITY; velY<=DynamicProgramming.MAX_VELOCITY; velY++) {
					for(int velX=-DynamicProgramming.MAX_VELOCITY; velX<=DynamicProgramming.MAX_VELOCITY; velX++) {
						initValue(finishLine, new State(y,x,velY,velX), track);
					}
				}
			}
		}
	}

	private void initValue(Collection<Point> finishLine, State s, Track track) {
		double minDistance = Track.HEIGHT + Track.WIDTH;
		for(Point finish : finishLine) {
			double dx = finish.x - s.posX;
			double dy = finish.y - s.posY;
			double distance = Math.sqrt(dx*dx + dy*dy);
			minDistance = Math.min(distance, minDistance);
		}
		
		//minus distance, because the reward is minus
		setValue(-minDistance, s);
	}
	
	public void setValue(double value, State s) {
		values[s.posY][s.posX][s.velY + DynamicProgramming.MAX_VELOCITY][s.velX + DynamicProgramming.MAX_VELOCITY] = value;
	}
	
	public double getValue(State s) {
		if(s.posY < 0 || s.posY >= Track.HEIGHT || s.posX < 0) {
			return -100;
		}

		//that's kind of a hack, because it's only doable, because we know that
		//the track has it's finish line on the right
		if(s.posX >= Track.WIDTH) {
			return 0;
		}
		
		return values[s.posY][s.posX][s.velY + DynamicProgramming.MAX_VELOCITY][s.velX + DynamicProgramming.MAX_VELOCITY];
	}
}
