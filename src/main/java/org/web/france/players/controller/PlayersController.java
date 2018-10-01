package org.web.france.players.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PlayersController {

	@RequestMapping("/")
	String players(ModelMap modal) {
		modal.addAttribute("title", "Equipe de France");
		modal.addAttribute("message", "Liste des joueurs");
		return "players";
	}
}