package com.tintumpic.wordle.controller;

import com.tintumpic.wordle.model.GuessRequest;
import com.tintumpic.wordle.model.GuessResponse;
import com.tintumpic.wordle.service.WordleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/wordle")
@CrossOrigin(origins = "http://127.0.0.1:8080")
public class WordleController {
    private final WordleService wordleService;

    public WordleController(WordleService wordleService) {
        this.wordleService = wordleService;
    }

    @PostMapping(path = "{guess}")
    public ResponseEntity<GuessResponse> makeGuess(@RequestBody GuessRequest request) {
        List<String> feedback = wordleService.evaluateGuess(request.getGuess());
        return ResponseEntity.ok(new GuessResponse(feedback));
    }

    @PostMapping(path = "new")
    public ResponseEntity<String> startNewGame() {
        wordleService.pickNewWord();
        return ResponseEntity.ok("New word selected");
    }
}
