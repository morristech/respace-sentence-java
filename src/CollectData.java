package ai;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class CollectData { // Adds data to the word frequency file by reading in text

	private static final File FEED_FILE = new File(""); // Location of text file to be fed
	private static final String WORD_FREQUENCY = "/Users/platelminto/Documents/frequencies.txt"; // Location of file with word frequency information
	private static final String SPLIT_WORDS_AT_CHARS = " !\"/().,:;?*<>";
	private static final String WORD_FREQUENCY_DELIMITER = " - "; // What to use to separate the word from its frequency in the WORD_FREQUENCY file

	public static void main(String...args) {

		try {

			List<String> wordList = getWordList(FEED_FILE);

			final File frequencyFile = new File(WORD_FREQUENCY);
			frequencyFile.createNewFile(); // If the file already exists, this will not overwrite it

			final HashMap<String, Long> dataMap = new HashMap<>(); // HashMap that will have each word as a key and its frequency as a value

			fillDataMapFromFrequencyFile(frequencyFile, dataMap);
			feedMapWithWordList(wordList, dataMap);

			updateFrequencyFile(frequencyFile, dataMap);

		} catch (IOException e) {

			e.printStackTrace();
		} 
	}

	private static List<String> getWordList(File feedFile) throws IOException { // Gets a list of lowercase, non-empty words from the text file it is being fed

		final List<String> words = new ArrayList<>();

		final BufferedReader textReader = new BufferedReader(new FileReader(feedFile));

		textReader.lines()
		.forEach(lines -> words.addAll(Arrays.asList(lines.split("[" + SPLIT_WORDS_AT_CHARS + "]")))); // Adds every word of every line to the words list

		textReader.close();

		return words.stream()
				.map(String::toLowerCase)
				.filter(word -> !word.equals("")) // Filters out empty strings
				.map(String::trim)
				.collect(Collectors.toList());
	}

	private static void fillDataMapFromFrequencyFile(File frequencyFile, HashMap<String, Long> dataMap) throws IOException { // Reads in current data from the frequency file and fills the data map with it

		final BufferedReader frequencyReader = new BufferedReader(new FileReader(frequencyFile));

		List<String> lines = frequencyReader.lines()
				.collect(Collectors.toList());

		frequencyReader.close();

		for(int i = 0, size = lines.size(); i < size; i++) { // Split each line at the delimiter, into word & frequency - put them in the data map

			final String[] dataSplit = lines.get(i).split(WORD_FREQUENCY_DELIMITER);
			final String word = dataSplit[0];
			final long frequency = Long.parseLong(dataSplit[1]);

			dataMap.put(word, frequency);
		}
	}

	private static void feedMapWithWordList(List<String> wordList, HashMap<String, Long> dataMap) { // Add to values in the current data map using the provided text file

		for(int i = 0, size = wordList.size(); i < size; i++) { // Go through the list of words being fed - if a word already exists, add 1 to its frequency; otherwise, create a new key and give it a value of 1

			final String word = wordList.get(i);

			if(dataMap.containsKey(word))

				dataMap.replace(word, dataMap.get(word) + 1);

			else

				dataMap.put(word, 1L);
		}
	}

	private static void updateFrequencyFile(File frequencyFile, HashMap<String, Long> dataMap) throws IOException { // Re-write frequency file with the data map

		final BufferedWriter frequencyWriter = new BufferedWriter(new FileWriter(frequencyFile));

		for(Entry<String, Long> entry : dataMap.entrySet()) { // Write every entry to the frequency file

			frequencyWriter.write(generateStringFromEntry(entry));
			frequencyWriter.newLine();
		}

		frequencyWriter.close();
	}

	private static String generateStringFromEntry(Entry<String, Long> entry) { // Returns a String representing an entry, using the frequency file delimiter

		return entry.getKey() + WORD_FREQUENCY_DELIMITER + entry.getValue();
	}
}
