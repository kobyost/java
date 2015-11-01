package presenter;


import java.io.Serializable;

@SuppressWarnings("serial")
public class Properties implements Serializable {
//כמה ת'רדים צריך לאפשר ב Thread Pool ? באיזה אלגוריתם לפתור את המבוכים? ובאיזה ליצור אותם
	//Data memebers
	String genrateAlgo;
	String solveAlgo;
	int numOfRows;
	int numOfColums;
	int numOfLevels;
	

	public Properties() {
		this.genrateAlgo="Dfs";
		this.solveAlgo="Astar AirDistance";	
		this.numOfRows=8;
		this.numOfColums=8;
		this.numOfLevels=3;
	}
	
	public Properties(Properties p) {
		this.genrateAlgo=p.genrateAlgo;
		this.solveAlgo=p.solveAlgo;	
		this.numOfRows=p.numOfRows;
		this.numOfColums=p.numOfColums;
		this.numOfLevels=p.numOfLevels;
	}
	
	public int getNumOfRows() {
		return numOfRows;
	}

	public void setNumOfRows(int numOfRows) {
		this.numOfRows = numOfRows;
	}

	public int getNumOfColums() {
		return numOfColums;
	}

	public void setNumOfColums(int numOfColums) {
		this.numOfColums = numOfColums;
	}

	public int getNumOfLevels() {
		return numOfLevels;
	}

	public void setNumOfLevels(int numOfLevels) {
		this.numOfLevels = numOfLevels;
	}

	public String getGenrateAlgo() {
		return genrateAlgo;
	}
	public void setGenrateAlgo(String genrateAlgo) {
		this.genrateAlgo = genrateAlgo;
	}
	public String getSolveAlgo() {
		return solveAlgo;
	}
	public void setSolveAlgo(String solveAlgo) {
		this.solveAlgo = solveAlgo;
	}
	

	

}
