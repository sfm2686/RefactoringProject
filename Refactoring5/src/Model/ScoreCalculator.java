package Model;

/**
 * NEW CLASS FOR REFACTORING
 * 
 * @author Hunter Caskey & Sultan Mira
 *
 */
public class ScoreCalculator {

	private int[][] cumulScores; 
	
	public ScoreCalculator(int[][] scores){
		this.cumulScores = scores;  // Operate on Lane's reference 
	}

	protected void calculateGame(int[] bowlersScores, int bowlerIndex, int frameNumber, int roll, int pinsDown ){
		int currentRoll = (2 * frameNumber) + roll;
		
		
		// Reset the previous record of scores
		for(int i = 0; i < cumulScores[bowlerIndex].length; i++){
			cumulScores[bowlerIndex][i] = 0;
		}

		int frameIndex;
		int prevScore = 0;
		
		for(int i = 0; i < currentRoll; i++){
			boolean even = i % 2 == 0;
			
			// Check Strike
			if(even && bowlersScores[i] == 10 && bowlersScores[i + 1] == -1){ // If all ten are down, and the roll we are on is even (starting at zero) 
				prevScore += strike(i, bowlersScores);
			}
			// Check Spare
			else if( !even && (bowlersScores[i - 1] + bowlersScores[i] == 10) ){ // If the roll we are on an odd roll, and the past two rolls add to ten
				prevScore += spare(i, bowlersScores);
			}
			// Check if Normal
			else if(bowlersScores[i] >= 0){ // Only display after the second throw of the frame
				if (!even || i == 20){
					prevScore += normal(i, bowlersScores);
				}
			}
			
			frameIndex = i / 2 ;
			if(i > 19){ // Last frame could have three throws 
				frameIndex = 9; 
			}
			this.cumulScores[bowlerIndex][frameIndex] = prevScore;

		}
	}
	
	private int strike(int index, int[] currentScores){
		if(index > 17){
			return (10);
		}
		int count = 0;
		int scoreSum = 10; // Start at ten since it was a strike
		for(int i = 1; i <= 4; i++){ // If a strike is thrown, then also add the next two rolls
			if(currentScores[index + i] >= 0 && count < 2){ // Add only the next two valid scores
				scoreSum += currentScores[index + i];
				count++;
			}
		}
		return scoreSum;
	}
	
	private int spare(int index, int[] currentScores){
		if(index > 17){
			return (10);
		}
		int count = 0;
		int scoreSum = 10; // Start at ten since it was a strike
		for(int i = 1; i <= 4; i++){ // If a strike is thrown, then also add the next two rolls
			if(currentScores[index + i] >= 0 && count < 1){ // Add only the next two valid scores
				scoreSum += currentScores[index + i];
				count++;
			}
		}
		return scoreSum;
	}
	
	private int normal(int index, int[] currentScores){
		if(index % 2 == 1 && currentScores[index - 1] >= 0){
			return(currentScores[index] + currentScores[index - 1]);
		}
		return(currentScores[index]);
	}
}