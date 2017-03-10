package com.starkie.playkalah.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.starkie.playkalah.model.Game;
import com.starkie.playkalah.service.KalahService;

/**
 * Rest controller to support operation to play the game of kalah
 * @author luke.starkie
 */
@RestController
public class KalahRestController {
	
	@Autowired
	private KalahService kalahService;

	/**
	 * GET mapping to find a game.
	 * For demo purposes this will create a game if one doesn't exist, typically this would not alter state.
	 * @param id
	 * @return
	 */
	@RequestMapping(path="/game/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Game getGame(@PathVariable("id") String id) {
		return kalahService.findGame(id);
	}
	
	/**
	 * GET / POST mapping to allow the game to be played by a player.
	 * For demo purposes the GET is enabled to allow easy calling from a browser, typically this method would only support POST.
	 * @param id
	 * @param user
	 * @param pit
	 * @return
	 */
	@RequestMapping(path="/game/{id}/play/{user}/{pit}", method={RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public Game play(@PathVariable("id") String id, @PathVariable("user") String user, @PathVariable("pit") int pit) {
		return kalahService.playGame(id, user, pit);
	}
	
	/**
	 * Exception handler for exceptions that should result in a 400 error (resulting from a bad request).
	 * @param ex
	 * @return
	 */
	@ExceptionHandler({IllegalArgumentException.class,IllegalStateException.class})
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody String handleError(Exception ex) {
		return ex.getMessage();
	}
}