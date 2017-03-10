package com.starkie.playkalah.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.starkie.playkalah.model.Game;
import com.starkie.playkalah.service.KalahService;

/**
 * Test class for the REST controller
 * @author luke.starkie
 */
@RunWith(MockitoJUnitRunner.class)
public class KalahRestControllerTest {

	private MockMvc mockMvc;
	
	@Mock
	private KalahService kalahService;	
	
	@InjectMocks
	private KalahRestController kalahRestController;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(kalahRestController).build();
	}
	
	/**
	 * Test the findGame REST API
	 * @throws Exception
	 */
	@Test
	public void testGetGame() throws Exception {
		when(kalahService.findGame("1"))
			.thenReturn(new Game("bill", "bob", "1"));
		mockMvc.perform(get("/game/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$.id").value("1"));
	}
	
	/**
	 * Test the playGame REST API for a valid move.  Should return a valid 200 HTTP response and JSON body with a valid ID.
	 * @throws Exception
	 */
	@Test
	public void testPlayGame() throws Exception {
		when(kalahService.playGame("1", "bill", 2))
			.thenReturn(new Game("bill", "bob", "1"));
		mockMvc.perform(post("/game/1/play/bill/2"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$.id").value("1"));
	}
	
	/**
	 * Test the playGame REST API with an invalid pit.  Should receive a 400 HTTP bad request with an appropriate body message.
	 * @throws Exception
	 */
	@Test
	public void testPlayGameError() throws Exception {
		when(kalahService.playGame("1", "bill", 12))
			.thenThrow(new IllegalArgumentException("Not a valid pit number"));
		mockMvc.perform(post("/game/1/play/bill/12"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Not a valid pit number"));
	}
}