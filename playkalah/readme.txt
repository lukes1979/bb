Run the Java application via:
	mvn spring-boot:run
	
Service parameter information:
	gameid: 	the id of the game.  A game exists with id "1", using a different id will create a new game.
	username: 	player 1 is bill, player 2 is bob, these are hard coded for simplicity.
	pitnumber:	a number from 1 to 6 inclusive.
	
View the current status of the game via:
	http://localhost:8080/game/{gameid}
	
Play the game (and view results of the move) via POST (GET also enabled for simplicity in demonstration):
	http://localhost:8080/game/{gameid}/play/{username}/{pitnumber}