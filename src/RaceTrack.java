public class RaceTrack {
	
	public static void main(String[] args) {
		//Dynamic Programming
		DynamicProgramming dynamicProgramming = new DynamicProgramming();
		dynamicProgramming.computeValueFunction();
		Episode episode = dynamicProgramming.generateEpisode();

		System.out.println();
		System.out.println("Visualization of an example episode:");
		System.out.println(episode.toString());
		
		System.out.println();
		System.out.println("Example episode reward: " + episode.reward);
		
		System.out.println();
		System.out.println("Duration Value Function: " + dynamicProgramming.durationValueFunction + "ms");
		System.out.println("Duration Generation of Episode: " + dynamicProgramming.durationEpisodeGeneration + "ms");
		
		
		//multiple episodes
		int numberEpisodes = 1000;
		int sumRewards = 0;
		for(int i=0; i<numberEpisodes; i++) {
			episode = dynamicProgramming.generateEpisode();
			sumRewards += episode.reward;
		}
		System.out.println();
		System.out.println("Average episode reward: " + (sumRewards * 1.0 / numberEpisodes));
		
		
		//Sarsa
		Sarsa sarsa = new Sarsa();
		sarsa.computeQMethod();
		episode = sarsa.generateEpisode();

		System.out.println();
		System.out.println("Visualization of an example episode:");
		System.out.println(episode.toString());
		
		System.out.println();
		System.out.println("Example episode reward: " + episode.reward);
	}

	final static int MAX_VELOCITY = 5;
	final static int REWARD_ON_TRACK = -1;
	final static int REWARD_OFF_TRACK = -5;

}
