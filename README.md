# EvilHangman
A project I did for a class freshmen year that has always stuck with me. Rigs a game against the player to make it almost impossible to win.

Based off of Treemaps and families of words, the user will guess letters and the program will wittle down the possible families of answers
and make the game almost impossible to win. It does this by assigning a pattern to every word. The system uses a dictionary of length n words
that you input at the beginning of the program and then reduces the possible dictionary of words by taking out possibilities with a letter 
guess that the player guesses. Once the program is forced to take a guess, it will then begin to create a pattern with all words that have 
the letter guessed at that exact spot. Say we have guessed a, e, i , o, u. All words with the first couple of vowels will be taken out of the
possible dictionary of words until it is forced to display one of those vowels in the pattern. Then all the words left will have the pattern
composition. Say you guessed these vowels and your pattern looks like this -ou-. Your dictionary of words will comprise of words like Foul, Rout,
Lout, Pout. Every time you guess one of these words, they will be taken from the dictionary until the letter guessed is common among all of the words.
In the end it will be very hard to widdle it down to the right word because of this algorithm.
