package com.tintumpic.wordle.model;

import java.util.List;

public class GuessResponse {
    private List<String> feedback;

    public GuessResponse(List<String> feedback) {
        this.feedback = feedback;
    }

    public List<String> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<String> feedback) {
        this.feedback = feedback;
    }
}
