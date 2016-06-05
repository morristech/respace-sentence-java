# respace-sentence-java
First AI project of mine - not even sure it counts as AI. Enter a text and the program will remove all whitespace, and then try to re-add it to make a decent phrsae. 

### Classes
There are 2 main classes right now:
- **CollectData.java**: Change the ```FEED_FILE``` constant to the location of a text file; the program will read it and record frequencies of each word, adding info to the ```WORD_FREQUENCY``` file. 
- **SentenceParser.java**: Enter a text when prompted; the program will remove the whitespace and return a sentence with added spaces.

### To-do

Right now, the parser ignores punctuation, returning a mess of a text, and the sentence it does return prefers very short words, leading to a high number of cases where words are split into single letters. This sucks. What I want to end up with should:
- Consider punctuation in the original text, adding it back to the final result.
- Not suck at re-building the phrase - currently, it goes down a tree with all the possible words and chooses the next child by seeking the one with the most frequent word. This doesn't work well, as the single letter 'a' is often chosen, and the rest of the word it may have been attached to is now split into meaningless, single-character words. A plan to fix this is to find the path from root to final word with the highest average frequency (which will make the program prefer longer words, accurately portraying real sentences).
- Handle cases where real words cannot be found, and try to make sense of the rest of the sentence anyway.
- Also have a text file that considers the frequency of one word after the other (this would require a lot of data)
- Feed the english wikipedia to the ```CollectData.java``` file.

Alternatively, I could: 
- Make a completely new algorithm which imitates how a human would accomplish the task. Humans don't find every possible word, and then pick the sentence that makes the most sense (a more computational way of doing it, and what is being done currently). An attempt to do this would be to find the longest word with a relatively high frequency (humans usually do this, as words like 'because' stand out from shorter words like 'at'), and then keep doing this with the rest of the sentence, which is now going to be split from the word found. If the rest of the sentence makes no sense whatsoever (or nearly), the program will try again with a different word.
