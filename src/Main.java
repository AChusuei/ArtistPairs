import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Algorithmic complexity and memory-space usage explanation:
 * 
 * Part one:
 * Given (n) lists in the file, with average (m) artists per list, we count how many time an artist appears in the file.
 * Also, for each list in the file, create a hash set containing all the artists for that list.
 * Complexity: both of these operations in tandem is O(n*m), as we go through each artist in every list.
 * Result: A HashMap of artists with (x) nodes, representing every artist found in the list.
 * Then, then iterate through the list, removing any artist that doesn't appear at least C times in the file.
 * Complexity: This is O(x), as we as iterate through every artist found in the file.
 * Result: A new list of artists with (p) nodes, each of which has count >= C.
 * Memory-wise:
 * 1) We store a HashMap of artist -> count while we parse the whole list, 
 *    but we pare this down only to artists that have at least than C occurrences once we've looked at all the lists.
 * 2) We also store the entire file as a list of HashSet<String>, which should be commensurate with the size of the file. 
 * 
 * Part two:                                                                     
 * With the new list of artists having (p) nodes, search each list for combinations of those artists using a double nested loop.
 * Finding an artist in a list is O(1), due to the translation of each list into a HashSet<String> in part one.
 * In the worst case for one list, every artist pair possible exists, which would be O((p^2)/2).
 * Thus overall for all (n) lists, complexity is O(n*p^2).
 * Result: A list of artist pair combinations with (q) nodes, some of which may have count >= C.
 * Memory-wise: We store another HashMap listing pairs found and their count.
 * 
 * Part three:
 * Just for cleanliness, we sort the last list of artist pairs by name. That's complexity O(log q).
 * Then we iterate through the list of artist pairs for find the ones with count >= C. That's complexity O(q).
 * 
 * Overall complexity: Depends on the average size of a list (m), and the value of C.
 * As C gets smaller, the number of artists that qualify for search increases the value of p, 
 * which could make O(n*p^2) > O(n*m), but only if m is small enough. With large enough lists,
 * O(n*m) > O(n*p^2) is possible, especially if C is large enough to limit the artist list used for search. 
 */
public class Main {

	public static final int NUMBER_OF_PAIRS = 50;
	
	public static void main(String[] args) {	
		try
		{
			/* 
			 * I just put the file in a folder on my c: drive, and ran this on Eclipse.
			 * To run this from the command line, just comment out the following line, and just run:
			 * java Main <file_location>
			 */
			args = new String[1]; args[0] = "c:\\Test\\Artist_lists_small.txt";
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
			String line; 
			
			/* 
			 * This holds a count per artist. If an artist appears no more than 50 times, then
			 * we shouldn't even check if this artist could pair up with some other artist, since it 
			 * will be impossible to make 50 unique pairs without at least 50 appearances in the file.
			 * (And that is presuming that there are no duplicate names per list!) 
			 */
			ConcurrentHashMap<String, Integer> artistcount = new ConcurrentHashMap<String, Integer>();
			
			/*
			 * This holds the representation of the input file as a list of HashSet of artists.
			 * A HashSet per list is done so that I can later check for the existence of an artist in a list in O(1) time.
			 */
			ArrayList<HashSet<String>> artistlists = new ArrayList<HashSet<String>>();
			
			while ((line = reader.readLine()) != null) 
			{
				if (!line.matches(".*\\w.*")) continue; // ignore empty lines.
				HashSet<String> artistlist = new HashSet<String>();
				String[] entries = line.split(",");
				if (entries.length < 2) continue; // ignore lists with only 1 entry, need at least two to form a pair.
				for (String artist : entries)
				{
					if (!(artistcount.containsKey(artist))) artistcount.put(artist, 0);
					artistcount.put(artist, artistcount.get(artist) + 1); // increment the count for the artist we found
					artistlist.add(artist); // add the artist found to the HashSet corresponding to this list of artists.
				}
				artistlists.add(artistlist);
			}
			
			/*
			 * Summary so far of what we've found.
			 */
			System.out.println("There are " + artistlists.size() + " valid artist lists.");
			System.out.println("Finding artists in at least " + NUMBER_OF_PAIRS + " lists...");
			for (String artist : artistcount.keySet()) 
			{
				if (artistcount.get(artist) >= NUMBER_OF_PAIRS) 
					System.out.println(artist + ": " + artistcount.get(artist));
				else 
				{
					/* 
					 * If an artist doesn't appear at least 50 times, then we don't need to look for them, so I'm removing them from the map.
					 * In order to do this, I needed to use a ConcurrentHashMap, so I can remove artists in linear time.
					 */
					// System.out.println("Removing " + artist + " from list, only has " + artistcount.get(artist) + " entries.");
					artistcount.remove(artist);
				}
			}
			System.out.println("There are " + artistcount.size() + " artists in more than " + NUMBER_OF_PAIRS + " lists...");
			
			/*
			 * This HashMap counts the combinations of artists we've found.
			 */
			HashMap<ArtistPair, Integer> artistpairs = new HashMap<ArtistPair, Integer>();
			/*
			 * Search each list with the artists that have at least 50 appearances.
			 */
			for (HashSet<String> artistlist : artistlists) 
			{
				/*
				 * We will be searching using a double for-loop, 
				 * since we need to look at all possible pairings of the artists that are in our list. 
				 */ 
				HashSet<String> alreadychecked = new HashSet<String>();
				for (String firstartist : artistcount.keySet()) 
				{
					if (artistlist.contains(firstartist))
					{
						for (String secondartist : artistcount.keySet())
						{
							if (alreadychecked.contains(secondartist) || firstartist.equalsIgnoreCase(secondartist)) continue;
							if (artistlist.contains(secondartist))
							{
								// We've found a pair of artists.
								ArtistPair pair = new ArtistPair(firstartist, secondartist);
								if (!artistpairs.containsKey(pair)) artistpairs.put(pair, 0);
								artistpairs.put(pair, artistpairs.get(pair) + 1);
							}
						}
					}
					/* 
					 * "alreadychecked" stores artists we've already looked at.
					 * Since order does not matter (combinations not permutations),
					 * we need not revisit an artist that we have searched for.
					 * If we have (p) artists in our list of artists, 
					 * this lowers the search time from having to do (p^2) searches to (p^2)/2.
					 */
					alreadychecked.add(firstartist);
				}
			}
			List<ArtistPair> sorted = new ArrayList<ArtistPair>(artistpairs.keySet());
			Collections.sort(sorted); 
			for (ArtistPair combo : sorted) 
			{
				if (artistpairs.get(combo) >= NUMBER_OF_PAIRS) System.out.println(combo + ": " + artistpairs.get(combo));	
			}
		}
		catch (IOException ioe)
		{
			System.out.println("ERROR processing file: " + ioe.getMessage());	
		}
	}
}
