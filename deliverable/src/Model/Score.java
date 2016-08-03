package Model;

public class Score {

    private String nick;
    private String date;
    private String score;

    public Score( String nick, String date, String score ) {
		this.nick=nick;
		this.date=date;
		this.score=score;
    }

    public String getNickName() {
        return nick;  
    }

	public String getDate() {
		return date;
	}
	
	public String getScore() {
		return score;
	}

	public String toString() {
		return nick + "\t" + date + "\t" + score;
	}

}
