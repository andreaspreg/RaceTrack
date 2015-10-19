
import java.awt.Point;
import java.util.Collection;

public class QMethod {

	final static int VELOCITY_DIMENSION = 2 * RaceTrack.MAX_VELOCITY + 1;
	final static int ACC_DIMENSION = 3;
	
	//values[y][x][velY][velX][accY][accX]
	double values[][][][][][];
	
	public QMethod(Track track) {
		values = new double[Track.HEIGHT][Track.WIDTH][VELOCITY_DIMENSION][VELOCITY_DIMENSION][3][3];
		
		initValues(track);
	}
	
	private void initValues(Track track) {
		
		Collection<Point> finishLine = track.getFinishLine();
		
		//init values by distance
		for(int y=0; y<values.length; y++) {
			for(int x=0; x<values[0].length; x++) {
				for(int velY=-RaceTrack.MAX_VELOCITY; velY<=RaceTrack.MAX_VELOCITY; velY++) {
					for(int velX=-RaceTrack.MAX_VELOCITY; velX<=RaceTrack.MAX_VELOCITY; velX++) {
						for(int accY=0; accY<ACC_DIMENSION; accY++) {
							for(int accX=0; accX<ACC_DIMENSION; accX++) {
								initValue(finishLine, new State(y,x,velY,velX), new Action(accY - 1, accX -1), track);
							}
						}
					}
				}
			}
		}
	}

	private void initValue(Collection<Point> finishLine, State s, Action a, Track track) {
		/*double minDistance = Track.HEIGHT + Track.WIDTH;
		for(Point finish : finishLine) {
			double dx = finish.x - s.posX;
			double dy = finish.y - s.posY;
			double distance = Math.sqrt(dx*dx + dy*dy);
			minDistance = Math.min(distance, minDistance);
		}*/
		
		//minus distance, because the reward is minus
		//setValue(-minDistance, s);
		setValue(0,s,a);
	}
	
	public ActionValue getMaxValue(State s) {
		ActionValue av = null;
		for(int accY=0; accY<ACC_DIMENSION; accY++) {
			for(int accX=0; accX<ACC_DIMENSION; accX++) {
				Action a = new Action(accY-1, accX-1);
				double value = getValue(s, a);
				if(av == null) {
					av = new ActionValue(a, value);
				}
				else {
					if(value > av.value) {
						int newVelY = s.velY + a.accY;
						int newVelX = s.velX + a.accX;
						if(newVelY >= -RaceTrack.MAX_VELOCITY && newVelY <= RaceTrack.MAX_VELOCITY &&
								newVelX >= -RaceTrack.MAX_VELOCITY && newVelX <= RaceTrack.MAX_VELOCITY ) {
							av.value = value;
							av.action = a;
						}
					}
				}
			}
		}
		return av;
	}
	
	public void setValue(double value, State s, Action a) {
		if(s.posY < 0 || s.posY >= Track.HEIGHT || s.posX < 0 || s.posX >= Track.WIDTH) {
			return;
		}
		
		values[s.posY][s.posX][s.velY + RaceTrack.MAX_VELOCITY][s.velX + RaceTrack.MAX_VELOCITY][a.accY+1][a.accX+1] = value;
	}
	
	public double getValue(State s, Action a) {
		if(s.posY < 0 || s.posY >= Track.HEIGHT || s.posX < 0) {
			return -100;
		}

		//that's kind of a hack, because it's only doable, because we know that
		//the track has it's finish line on the right
		if(s.posX >= Track.WIDTH) {
			return 0;
		}
		
		return values[s.posY][s.posX][s.velY + RaceTrack.MAX_VELOCITY][s.velX + RaceTrack.MAX_VELOCITY][a.accY+1][a.accX+1];
	}
}

