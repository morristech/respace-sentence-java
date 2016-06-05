package ai;

import java.io.*;
import java.util.*;

public class SentenceParser { // Re-adds spaces to a text which has had all its whitespace removed

	private final String initialText;
	private final String noSpaceText;
	private final HashMap<String, Long> dataMap;
	private static final String SENTENCE_SPLITS = "!\"/().,:;?*<>";
	private static final String WORD_FREQUENCY = "/Users/platelminto/Documents/frequencies.txt"; // Location of file with word frequency information
	private static final String WORD_FREQUENCY_DELIMITER = " - "; // What to use to separate the word from its frequency in the WORD_FREQUENCY file

	public static void main(String...args) {

		final SentenceParser parser = new SentenceParser("épork does this work");
		parser.parse();
	}

	SentenceParser() {

		this(askForText());
	}

	SentenceParser(String text) {

		initialText = text;
		noSpaceText = removeWhitespace(initialText);

		dataMap = getDataMap(WORD_FREQUENCY);
	}

	// TODO: Re-join sentences with correct punctuation
	public String parse() { // Returns the text with appropriate spacing

		final String[] separatedSentences = noSpaceText.split("[" + SENTENCE_SPLITS + "]"); // Splits the whole text into smaller chunks, delimited by specific characters

		final long initTime = System.currentTimeMillis();

		for(String sentence : separatedSentences)

			System.out.println(respace(sentence));

		System.out.println("time taken: " + (System.currentTimeMillis() - initTime) + "ms");

		return "";
	}

	// TODO: Use an average frequency of all the nodes in a specific path, as the currently used method prefers shorter (single letter) words
	private String respace(String sentence) { // Respaces the sentence 

		Node<String> root = new Node<>(null);
		root = generateTree(0, sentence, root); // Generate tree, with an empty node as its root, using the full phrase

		final StringBuilder spacedSentence = new StringBuilder();

		while(!root.isLeaf()) {

			int frequentNode = 0;

			for(int i = 0, children = root.childAmount(); i < children; i++) { // Find node with the most frequent word, adding the word to the StringBuilder

				if(getFrequency(root.getChild(i).data) > getFrequency(root.getChild(frequentNode).data))
					
					frequentNode = i;
			}
			
			spacedSentence.append(root.getChild(frequentNode).data + " ");
			root = root.getChild(frequentNode); // Go down the tree using the node with highest frequency 
		}

		return spacedSentence.toString();
	}

	// TODO: Handle cases where, initially, words can't be found
	private Node<String> generateTree(int startFrom, final String sentence, Node<String> parent) { // Generates a tree, each node being a possible word with children being possible words following the parent

		for(int i = startFrom + 1, length = sentence.length(); i <= length; i++) {

			final String word = sentence.substring(startFrom, i); // Try every string of characters starting from the specified index

			if(isAWord(word)) {

				final Node<String> node = new Node<String>(word, parent);

				parent.addChild(node);
				
				generateTree(i, sentence, node); // Generate a tree with the new node as its root, and the rest of the phrase as the sentence

			} else if(word == sentence) { // The end of the sentence has been reached, and the final few characters aren't a valid word

				Node<String> leaf = parent;

				while(leaf.isLeaf()) { // Go up the tree, deleting each node if its a leaf (as this path lead to an invalid phrasew)

					parent.removeChild(leaf);
					leaf = leaf.parent;
				}

				break;
			}
		}

		return parent;
	}

	public static String removeWhitespace(String text) {

		return text.replaceAll(" ", "");
	}

	public boolean isAWord(String s) {

		return dataMap.containsKey(s.toLowerCase()); // Capitalision does not affect frequency
	}

	private static HashMap<String, Long> getDataMap(String frequencyFile) { // Returns a list of every word present in the frequency file

		HashMap<String, Long> dataMap = new HashMap<>();

		try (final BufferedReader reader = new BufferedReader(new FileReader(frequencyFile))) {

			String line;

			while((line = reader.readLine()) != null) {

				final String[] lineData = line.split(WORD_FREQUENCY_DELIMITER); // The first index will contain the word; the second, its frequency

				final String word = lineData[0];
				final long frequency = Long.parseLong(lineData[1]);
				dataMap.put(word, frequency);
			}

		} catch (IOException e) {

			e.printStackTrace();
		}

		return dataMap;
	}

	private long getFrequency(String word) {

		return dataMap.get(word);
	}

	private static String askForText() {

		final Scanner scan = new Scanner(System.in);

		System.out.println("Enter normal text: ");
		final String text = scan.nextLine();

		scan.close();

		return text;
	}
}