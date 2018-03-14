# Test Plan

**Author**: Team 74

## 1 Testing Strategy

### 1.1 Overall strategy

|Activity:|To Be Performed By:|
|------   |:---------------   |
|Derive appropriate test cases from requirements|Testing Team|
|Validate test cases|Testing Lead|
|Perform system-level tests|Testing Team|
|Analyze code coverage of system-level tests|Testing Team|
|Validate results of system-level tests and code coverage|Testing Lead|
|Perform integration-level tests|Testing Team|
|Analyze code coverage of integration-level tests|Testing Team|
|Validate results of integration-level tests and code coverage|Testing Lead|
Perform unit-level tests|Testing Team|
|Analyze code coverage of unit-level tests|Testing Team|
|Validate results of unit-level tests and code coverage|Testing Lead|
|Report software bugs during testing|Testing Team|
|Validate software bugs|Testing Lead|
|Implement and report software fixes|Development Team|
|Validate software fixes|Development Lead|
|Repeat tests after software fixes|Testing Team|
|Validate test results after software fix|Testing Lead|


### 1.2 Test Selection

To select appropriate test cases for this application, we will first determine the independently testable features. With those testable features established, test specifications will be made using black-box testing techniques such as partition testing. These partitions will be used in determining test cases on the unit, integration, and system levels to meet requirements. In addition to requirement coverage, code coverage is another metric we will use for test quality. To reach our code coverage testing goals, white-box testing techniques such as statement coverage, branch coverage, and condition coverage will be used to make appropriate test cases. 

### 1.3 Adequacy Criterion
   **In order to assess the quality of the test cases:**
1. Requirement traceability will be emphasized

    There will be atleast one test case per project requirement and/or sub-requirement until 
    100% requirement coverage is met
  
2. Code coverage will be emphasized

    The number of lines of code encapsulated by the test cases will be assessed and compared to the total number
    of lines of code in order to determine percentage of code coverage


### 1.4 Bug Tracking
YouTrack will be used to log software bugs and fixes as it is an intiuitive, open-source tracking software that can easily integrate with the IntelliJ IDE of choice. 

-Software bugs will be reported on YouTrack by the software testing team and validated by the software testing lead

-The software development lead will then cascade the software issues to the rest of the development team 

-Software bugs that have been reported as fixed are then re-tested by the testing team and validated by the testing lead

### 1.5 Technology
JUnit will be used to perform unit testing on the android application as it allows for an easy setup with batch testing, integrates well with the IDE of choice, and was designed for java-based applications.

## 2.0 Test Cases

[TestCases](resources/TestCases.pdf)

## 3.0 Bug Reports

|    Bug | Expected Result | Being worked on by | Result |
|-----------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------|--------|
| Create a new player and input   a previously made username | Error message stating that username has already   been taken should appear | Tim | Fixed |
| Click 'Create a New Player'   and then click submit without providing any information | Error message prompting user to enter valid   field information | Tim | Fixed |
| Click 'Create a Puzzle',   leave the phrase blank, enter the number of wrong guesses allowed, and click   'Save' | Error message that prompts user to enter a   puzzle phrase will appear | David | Fixed |
| Create tournament shows   Puzzle object’s toString | Create tournament should display the object’s   identifier | Tim | Fixed |
| When solving a puzzle, the   random prize value isn’t displayed to the user | The random amount for the letter needs to be   displayed to the user | Andrew | Fixed |
| After solving a puzzle the   user should get $1000 for each letter not revealed yet | There is no additional prize for solving the   puzzle | Andrew | Fixed |
| Solving a random puzzle   displays the puzzle with ID = 1 | Solving a random puzzle should randomly choose a   puzzle the player has not created/played | Tim | Fixed |
| Puzzle statistics show every   game that has been played | Puzzle statistics should only show the top   score, scorer, and number of players that played it | Tim | Fixed |
| Tournament statistics show   every tournament that has been played | Tournament statistics should only show the top   score, scorer, and number of players that played it | Tanner | Fixed |
| When creating a puzzle, blank   puzzles are still saved to database even if error message appears | Error that prompts user to enter a puzzle phrase   appears and nothing is saved to database | David | Fixed |
| When solving a puzzle, if   user doesn’t enter a character then clicks buy vowel, the app crashes | Error message that prompts user to enter a vowel | Andrew | Fixed |
| When solving a puzzle, user   has to backspace their previous guess and enter a new one  | UI deletes previously guessed character after a   guess/buy has been done | Andrew | Fixed |
| When solving a puzzle, after   correctly guessing all the letters/consonants (without   clicking Solve the Puzzle) app goes back to the   main menu | Prompt user to Solve the Puzzle | Andrew | Fixed |
| When solving a puzzle, user   shouldn’t be able to type in the Puzzle Phrase field | Prohibit text editing | Andrew | Fixed |
| When creating a puzzle, no   uniqueID is returned | Return a uniqueID | David | Fixed |
| When creating a puzzle,   nothing happens after the user clicks Save | Return a uniqueID, “Puzzle Created” message   appears, and screen changes to main screen | David | Fixed |
| When attempting to join a   tournament, app crashes if no puzzles are created | Throw warning and return back to main menu | Tanner | Fixed |
| No way to get back to the   login screen to change users. | Button to allow user to logout and login/create   a different user. | Tim | Fixed |
| Login with username that   doesn’t already exist gives no indicator | Clicking login with a username that doesn’t   exist should pop up an error. | Tim | Fixed |
| No way to get back to main   menu from create user. | Should be a button to take you back to the title   screen | Tim | Fixed |
| Title Screen Img broken | There should be a Title screen image | Tim | Fixed |
| Back button will let you go   back to failed puzzles | Back button should go back to main menu | Tanner | Fixed |
| When playing a puzzle, Back   button and back to main menu button should confirm that user will forfeit   puzzle | Should pop up confirm dialog that user agrees to   fail a puzzle. | David | Fixed |
| When creating a tournament   app crashes when no puzzles are selected | Throw error message prompting user to choose   puzzles | Tanner | Fixed |
| When creating a tournament,   puzzleID isn’t added to database | When creating a tournament, selected puzzles   should be added to database | Tanner | Fixed |
| When solving a puzzle number   of guesses is off by one | With 1 guess they should only get 1 letter.    Currently allows two. | Andrew | Fixed |
| When limit of guesses is met,   game data isn’t saved | Should save the game with a score of 0 and send   user by to main screen telling them they lost the game. | Andrew | Fixed |
| When creating new user,   successful submission does not give any feedback. | Message alerting user that information is saved. | David | Fixed |
| Tournament stats include   tournaments that have not been completed | Tournament stats should only be shown for   completed tournaments | Tim | Fixed |
| Total prize won in Tournament   Stats displaying as 0 | Should display correct score / total prize | Tanner | Fixed |
| Tournament stats and Player’s   Tournament stats pull TournamentPlayThrough’s TotalPrize. TotalPrize in play   through is not being set.  | The total prize for all of the puzzles in that   tournament should be added up as the total score. It should not matter if   they game was played as part of random puzzle play or part of playing another   tournament. | Tanner | Fixed |
| Quitting a puzzle does not store a game | A game should be stored with a prize of 0 | David | Fixed |
| A Player can join a   tournament they’ve already joined | Players should not be given to join a tournament   they’ve already joined | Tanner | Fixed |
