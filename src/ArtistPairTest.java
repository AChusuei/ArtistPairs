import org.junit.Assert;
import org.junit.Test;

public class ArtistPairTest {
	
	@Test
	public void equals_OrderShouldNotMatter_ReturnsTrue() {
		ArtistPair first = new ArtistPair("The Wallflowers","Eric Clapton");
		ArtistPair second = new ArtistPair("Eric Clapton","The Wallflowers");
		Assert.assertTrue(first.equals(second));
		Assert.assertEquals(first.hashCode(), second.hashCode());
		Assert.assertEquals(0, first.compareTo(second));
	}
}
