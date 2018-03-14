# Guess It

Author(s): Andrew Wung in collaboration with Tanner Mapes, David DeKime, and Tim Hays

## 1.0 General Information

Guess It is an application for Android based on a variation of the Wheel of Fortune game. From the beginning, the user must create an account. After creating an account and logging in, the user will be presented with the main menu. 

The main menu has the following options:
1. Create a Puzzle
2. Solve a Puzzle
3. Create a Tournament
4. Join/Continue Tournament
5. View Statistics
6. Clear Application Data

Creating a puzzle takes in a phrase input and number of guesses allowed input. This data persists through to all others trying to solve the puzzle. In solving a puzzle, the user can only solve puzzles he/she has not created. The user will see Total Prize, Wrong Guesses Remaining, and Puzzle Phrase. The Puzzle Phrase has underscores in place of the characters that must be guessed correctly to solve the puzzle. Total Prize indicates how much money the user will get when he/she correctly solves the puzzle. Whenever total prize or wrong guesses remaining = 0, the game is exited and the player loses. The player has 3 options- Guess a Consonant, Buy a Vowel, or Solve the Puzzle. Buying a vowel will cost the player $300. The game will validate that the text entered is only a vowel, and not anything else. To win, the player must either solve the puzzle or guess the remaining characters. The game ending conditions are 1) Remaining Guesses = 0 2) Solved incorrectly 3) Solved correctly 4) Guessed all characters correctly.

A tournament is a collection of puzzles. A user can create or join/continue a tournament. A tournament has 1 to 5 puzzles that a user must solve, but the player that created the puzzles cannot solve their own tournament. The Total Prize is recorded for the player, and the player can view it in the View Statistics option. The View Statistics option records 4 pieces of key information: My Puzzles Stats, My Tournament Stats, Puzzle Stats, and Tournament Stats. My Puzzles Stats has the list of puzzles completed by that player with, for each puzzle, the prize the player won (including $0 for puzzles he/she quit or did not successfully solve). My Tournament Stats has the list of tournaments completed by that player with, for each tournament, the prize the player won. Puzzle Stats and Tournament Stats serve as a scoreboard for every player. Puzzle Stats has the complete list of puzzles with, for each puzzle, (1) the number of players who played it and (2) the top prize won by a player for that puzzle, together with the username of that player. Tournament Stats has the complete list of tournaments with, for each tournament, (1) the number of players who completed the tournament and (2) the top prize won by a player for that tournament, together with the username of that player.

Finally, Clear Application Data will simply destroy the instance of the player- essentially wiping out all information in the database and erasing that player's information including statistics, puzzles, and tournaments.

## 2.0 Step-by-step instructions on how to run the Guess It Android application

1. Clone the repository: git clone https://github.gatech.edu/gt-omscs-se-2018spring/6300Spring18Team74.git
2. Get Android Studio and the applicable plugins, see instructions here: https://developer.android.com/studio/index.html
3. Open Android Studio
4. Open Android Project from Existing Code
5. Select the Guess It application from the appropriate folder
6. Run the application on your mobile device or set up a virtual android device and run it there.

## 3.0 Features
1. Underscores as placeholders for letters not yet guessed that shows the word’s length.
2. Displays how many guesses left until a player loses
3. Displays all letters already guessed
5. App only accepts single alphabetical characters as valid input (case-insensitively).
6. Congratulation message after you win or "Sorry, you lost" message after you lose
7. Puzzle has customize-able settings for number of words and maximum number of incorrect guesses allowed
8. Tournament creation has customizable amount of puzzles to play
9. Maintains a history of high scores that’s displayed anytime a game is won or lost that is persisted across all players and instances.

## 4.0 Assumptions

1. It is assumed the the application will not be added to the Google playstore and will be copied to any device directly.
2. It is assumed that there will not be more data stored using the applications than the SqlLite database can support.
3. The application will depend on the Android API version 23.
4. The application will depend on the Android 6.0's local SqlLite database.
5. When guesses = 0, the game ends, and the player loses. Number of guesses is not the same as Number of Wrong guesses remaining.

## 5.0 Technical Information 

1. The database queries in the UI thread
