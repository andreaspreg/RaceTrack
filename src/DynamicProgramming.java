import java.awt.Point;

public class DynamicProgramming {
	
	final static double MIN_DELTA = 0.01;
	final static double GAMMA = 1;
	
	long durationValueFunction;
	long durationEpisodeGeneration;
	
	Track track;
	private ValueFunction valueFunction;
	
	public DynamicProgramming() {
		
		track = new Track();
		valueFunction = new ValueFunction(track);
	}
	
	public Episode generateEpisode() {
		durationEpisodeGeneration = System.currentTimeMillis();
		
		//start point
		Point startPoint = track.selectRandomStartPoint();
		Episode episode = new Episode(track, startPoint);
		
		State state = new State(startPoint.y, startPoint.x, 0, 0);
		Action action = null;
		
		do {
			action = computeActionValue(state).action;
			state = state.actWithStochastic(action);
			episode.add(action, state);
			episode.reward += track.getReward(state);
		} while(state.posX < Track.WIDTH-1);
		
		durationEpisodeGeneration = System.currentTimeMillis() - durationEpisodeGeneration;
		
		return episode;
	}

	public void computeValueFunction() {
		
		durationValueFunction = System.currentTimeMillis();
		
		System.out.println("Computation of the value function:");
		
		double delta;
		int i=0;
		
		do {
			delta = 0;
			i++;
			
			for(int y=0; y<Track.HEIGHT; y++) {
				for(int x=0; x<Track.WIDTH; x++) {
					for(int velY=-RaceTrack.MAX_VELOCITY; velY<=RaceTrack.MAX_VELOCITY; velY++) {
						for(int velX=-RaceTrack.MAX_VELOCITY; velX<=RaceTrack.MAX_VELOCITY; velX++) {
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
		
		durationValueFunction = System.currentTimeMillis() - durationValueFunction;
	}
	
	public ActionValue computeActionValue(State s) {
		
		ActionValue max = null;
		
		double value = valueFunction.getValue(s);
		
		for(Action action : track.getPossibleActions(this, s)) {
			
			//generate states (s2 and s3 handle the sliding)
			State s1 = s.act(action);
			State s2 = s1.clone();
			s2.posY += -1;
			State s3 = s1.clone();
			s3.posX += 1;
			
			double sum = 0;
			sum += 0.5 * (track.getReward(s1) + GAMMA * valueFunction.getValue(s1));
			sum += 0.25 * (track.getReward(s2) + GAMMA * valueFunction.getValue(s2));
			sum += 0.25 * (track.getReward(s3) + GAMMA * valueFunction.getValue(s3));
			
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
}
