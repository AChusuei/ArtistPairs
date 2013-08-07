public class ArtistCombo implements Comparable<ArtistCombo> {

	private String artistOne;
	private String artistTwo;
	
	ArtistCombo(String artistOne, String artistTwo) {
		if (artistOne.compareTo(artistTwo) < 0) 
		{
			this.artistOne = artistOne;
			this.artistTwo = artistTwo;
		}
		else
		{
			this.artistOne = artistTwo;
			this.artistTwo = artistOne;
		}
	}
	
	@Override
	public boolean equals(Object arg0) {
		if (this == arg0) return true;
		if (arg0 == null) return false;
		if (getClass() != arg0.getClass()) return false;
		ArtistCombo that = (ArtistCombo) arg0;
		if (this.artistOne.equalsIgnoreCase(that.artistOne) && this.artistTwo.equalsIgnoreCase(that.artistTwo)) return true;
		if (this.artistOne.equalsIgnoreCase(that.artistTwo) && this.artistTwo.equalsIgnoreCase(that.artistOne)) return true;
		return false;
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artistOne == null) ? 0 : artistOne.hashCode());
		result = prime * result + ((artistTwo == null) ? 0 : artistTwo.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "(" + artistOne + "|" + artistTwo + ")";
	}

	@Override
	public int compareTo(ArtistCombo that) {
		if (this.artistOne.compareTo(that.artistOne) == 0) 
		{
			return this.artistTwo.compareTo(that.artistTwo);
		}
		else
		{
			return this.artistOne.compareTo(that.artistOne);
		}
	}
}
