import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

	public static final int NUMBER_OF_PAIRS = 100;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		try
		{
			args = new String[2]; args[0] = "c:\\Knewton\\Artist_lists_small.txt"; args[1] = "c:\\Knewton\\pairs.csv";
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
			String line; 
			ConcurrentHashMap<String, Integer> artistcount = new ConcurrentHashMap<String, Integer>();
			ArrayList<HashSet<String>> artistlists = new ArrayList<HashSet<String>>();
			int linecount = 1;
			while ((line = reader.readLine()) != null) 
			{
				// System.out.println("processing line " + linecount++);
				if (!line.matches(".*\\w.*")) continue;
				// System.out.println("parsing line " + linecount);
				HashSet<String> artistlist = new HashSet<String>();
				String[] entries = line.split(",");
				if (entries.length < 2) continue;
				for (String artist : entries)
				{
					if (!(artistcount.containsKey(artist)))
					{
						artistcount.put(artist, 0);
					}
					artistcount.put(artist, artistcount.get(artist) + 1);
					artistlist.add(artist);
				}
				artistlists.add(artistlist);
			}
			System.out.println("There are " + artistlists.size() + " valid artist lists.");
			System.out.println("Finding artists in more than " + NUMBER_OF_PAIRS + " lists...");
			for (String artist : artistcount.keySet()) 
			{
				if (artistcount.get(artist) >= NUMBER_OF_PAIRS) 
					System.out.println(artist + ": " + artistcount.get(artist));
				else
					artistcount.remove(artist);	
			}
			System.out.println("There are " + artistcount.size() + " artists in more than " + NUMBER_OF_PAIRS + " lists...");
			HashMap<ArtistCombo, Integer> artistcombos = new HashMap<ArtistCombo, Integer>();
			int listcount = 1;
			for (HashSet<String> artistlist : artistlists) 
			{
				// System.out.println("Checking possible combos in list #" + listcount++);
				HashSet<String> alreadychecked = new HashSet<String>();
				for (String firstartist : artistcount.keySet()) 
				{
					if (artistlist.contains(firstartist))
					{
						// System.out.println("Found possible first artist: " + firstartist);
						for (String secondartist : artistcount.keySet())
						{
							if (alreadychecked.contains(secondartist) || firstartist.equalsIgnoreCase(secondartist)) continue;
							if (artistlist.contains(secondartist))
							{
								ArtistCombo combo = new ArtistCombo(firstartist, secondartist);
								// System.out.println("Found combo: " + combo);
								if (!artistcombos.containsKey(combo)) {
									artistcombos.put(combo, 0);
								}
								artistcombos.put(combo, artistcombos.get(combo) + 1);
							}
						}
					}
					alreadychecked.add(firstartist);
				}
			}
			List<ArtistCombo> sorted = new ArrayList<ArtistCombo>(artistcombos.keySet());
			Collections.sort(sorted);
			for (ArtistCombo combo : sorted) 
			{
				if (artistcombos.get(combo) >= NUMBER_OF_PAIRS) System.out.println(combo + ": " + artistcombos.get(combo));	
			}
		}
		catch (IOException ioe)
		{
			
		}
	}
}
