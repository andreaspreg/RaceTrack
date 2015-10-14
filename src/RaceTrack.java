public class RaceTrack {
	
	public static void main(String[] args) {
		DynamicProgramming dynamicProgramming = new DynamicProgramming();
		dynamicProgramming.computeValueFunction();
		Episode episode = dynamicProgramming.generateEpisode();

		System.out.println();
		System.out.println("Visualization of the episode:");
		System.out.println(episode.toString());
	}

}
