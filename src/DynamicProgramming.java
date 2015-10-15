import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

public class DynamicProgramming {
	
	final static int REWARD_ON_TRACK = -1;
	final static int REWARD_OFF_TRACK = -5;

	final static int MAX_VELOCITY = 5;
	final static double MIN_DELTA = 0.0001;
	final static double GAMMA = 1;
	
	private Track track;
	private ValueFunction valueFunction;
	
	public DynamicProgramming() {
		
		track = new Track();
		valueFunction = new ValueFunction(track);
	}
	
	public Episode generateEpisode() {
		//start point
		Point startPoint = track.selectRandomStartPoint();
		Episode episode = new Episode(track, startPoint);
		
		State state = new State(startPoint.y, startPoint.x, 0, 0);
		Action action = null;
		int totalReward = 0;
		
		do {
			action = computeActionValue(state).action;
			state = state.actWithStochastic(action);
			episode.add(action, state);
			totalReward += getReward(state);
		} while(state.posX < Track.WIDTH-1);
		
		System.out.println();
		System.out.println("Total episode reward: " + totalReward);
		
		return episode;
	}

	public void computeValueFunction() {
		
		System.out.println("Computation of the value function:");
		
		double delta;
		int i=0;
		
		do {
			delta = 0;
			i++;
			
			for(int y=0; y<Track.HEIGHT; y++) {
				for(int x=0; x<Track.WIDTH; x++) {
					for(int velY=-MAX_VELOCITY; velY<=MAX_VELOCITY; velY++) {
						for(int velX=-MAX_VELOCITY; velX<=MAX_VELOCITY; velX++) {
							State s = new State(y,x,velY,velX);
							ActionValue actionValue = computeActionValue(s);
							valueFunction.setValue(actionValue.value, s);
							delta = Math.max(delta, actionValue.delta);
						}
					}
				}
			}
			
			System.out.println("Step " + i + " delta: " + delta);
			
		} while (delta > MIN_DELTA);
		
	}
	
	public ActionValue computeActionValue(State s) {
		
		ActionValue max = null;
		
		double value = valueFunction.getValue(s);
		
		for(Action action : getPossibleActions(s)) {
			
			//generate states (s2 and s3 handle the sliding)
			State s1 = s.act(action);
			State s2 = s1.clone();
			s2.posY += -1;
			State s3 = s1.clone();
			s3.posX += 1;
			
			double sum = 0;
			sum += 0.5 * (getReward(s1) + GAMMA * valueFunction.getValue(s1));
			sum += 0.25 * (getReward(s2) + GAMMA * valueFunction.getValue(s2));
			sum += 0.25 * (getReward(s3) + GAMMA * valueFunction.getValue(s3));
			
			if(max == null) {
				max = new ActionValue(action, sum);
			}
			else if(sum > max.value) {
				max.value = sum;
				max.action = action;
			}
		}
		
		max.delta = Math.abs(value - max.value);
		
		return max;
	}
	
	public Collection<Action> getPossibleActions(State s) {
		Collection<Action> actions = new ArrayList<>();
		
		for(int y=-1; y<=1; y++) {
			for(int x=-1; x<=1; x++) {
				Action action = new Action(y, x);
				
				//check velocity boundaries (velocity in between -5 and 5)
				int newVelY = s.velY + action.accY;
				int newVelX = s.velX + action.accX;
				if(newVelY >= -MAX_VELOCITY && newVelY <= MAX_VELOCITY &&
						newVelX >= -MAX_VELOCITY && newVelX <= MAX_VELOCITY ) {
					actions.add(action);
				}
			}
		}
		
		return actions;
	}
	
	public int getReward(State s) {
		return getReward(s.posY, s.posX);
	}
	
	public int getReward(int y, int x) {
		TrackState trackState = track.getTrackState(y, x);
		
		if(trackState == TrackState.OFFROAD) {
			return REWARD_OFF_TRACK;
		}
		else {
			return REWARD_ON_TRACK;
		}
	}
	
	class ActionValue {
		Action action;
		double value;
		double delta;
		
		public ActionValue(Action action, double value) {
			this.action = action;
			this.value = value;
		}
	}
}
