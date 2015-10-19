import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Sarsa {

	public final static int NUMBER_EPISODES = 1000;
	public final static int MAX_EXPLORATIONS_WITHIN_ONE_EPISODE = 10000;
	
	public final static double ALPHA = 0.9;
	final static double GAMMA = 1;
	
	private Track track;
	private QMethod qMethod;

	long durationQMethod;
	long durationEpisodeGeneration;
	
	public Sarsa() {
		track = new Track();
		qMethod = new QMethod(track);
	}
	
	public void computeQMethod() {
		
		//iterate through all episodes
		for(int i=0; i<NUMBER_EPISODES; i++) {
			//randomly initialize state
			State s = getRandomState();
			Action a = null;
			//limit explorations within one episode
			int numberExplorations = 0;
			
			while(true) {
				
				//limit explorations within one episode
				if(numberExplorations++ >= MAX_EXPLORATIONS_WITHIN_ONE_EPISODE) {
					System.out.println("Number max explorations within one episode reached.");
					break;
				}
				
				List<Action> possibleActions = getPossibleActions(s);
				if(possibleActions.size() <= 0) {
					int k=0;
				}
				a = possibleActions.get(new Random().nextInt(possibleActions.size()));
				
				//select the next state with probability
				State s2 = s.actWithStochastic(a);
				s2.velY = Math.max(-RaceTrack.MAX_VELOCITY, Math.min(RaceTrack.MAX_VELOCITY, s2.velY));
				s2.velX = Math.max(-RaceTrack.MAX_VELOCITY, Math.min(RaceTrack.MAX_VELOCITY, s2.velX));

				double q = qMethod.getValue(s, a);
				int reward = track.getReward(s2);
				ActionValue maxQ = qMethod.getMaxValue(s2);
				Action maxAction = maxQ.action;
				double maxValue = maxQ.value;
				
				double value = q + ALPHA * ( reward + GAMMA * maxValue - q);
				qMethod.setValue(value, s2, maxAction);
				
				//if states are equal, terminate
				if(s.equals(s2)) {
					break;
				}
				
				//check if we reached a goal state
				if(isGoalState(s2)) {
					break;
				}
				
				s = s2;
				a = maxAction;
			}
		}
	}
	
	private boolean isGoalState(State s) {
		if(s.posX >= Track.WIDTH || track.getTrackState(s.posY, s.posX) == TrackState.FINISH) {
			return true;
		}
		return false;
	}

	public List<Action> getPossibleActions(State s) {
		List<Action> actions = new ArrayList<>();
		
		for(int y=-1; y<=1; y++) {
			for(int x=-1; x<=1; x++) {
				Action action = new Action(y, x);
				
				//check velocity boundaries (velocity in between -5 and 5)
				int newVelY = s.velY + action.accY;
				int newVelX = s.velX + action.accX;
				if(newVelY >= -RaceTrack.MAX_VELOCITY && newVelY <= RaceTrack.MAX_VELOCITY &&
						newVelX >= -RaceTrack.MAX_VELOCITY && newVelX <= RaceTrack.MAX_VELOCITY ) {
					actions.add(action);
				}
			}
		}
		
		return actions;
	}
	
	public Episode generateEpisode() {
		durationEpisodeGeneration = System.currentTimeMillis();
		
		//start point
		Point startPoint = track.selectRandomStartPoint();
		Episode episode = new Episode(track, startPoint);
		
		State state = new State(startPoint.y, startPoint.x, 0, 0);
		Action action = null;
		
		do {
			action = qMethod.getMaxValue(state).action;
			state = state.actWithStochastic(action);
			episode.add(action, state);
			episode.reward += track.getReward(state);
		} while(state.posX < Track.WIDTH-1);
		
		durationEpisodeGeneration = System.currentTimeMillis() - durationEpisodeGeneration;
		
		return episode;
	}

	private State getRandomState() {
		Random r = new Random();
		int posY = r.nextInt(Track.HEIGHT);
		int posX = r.nextInt(Track.WIDTH);
		int velY = r.nextInt(QMethod.VELOCITY_DIMENSION) - RaceTrack.MAX_VELOCITY;
		int velX = r.nextInt(QMethod.VELOCITY_DIMENSION) - RaceTrack.MAX_VELOCITY;
		return new State(posY, posX, velY, velX);
	}
}
