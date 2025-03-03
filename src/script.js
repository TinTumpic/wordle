const btnNewGame = document.querySelector(".new-game");
const maxGuesses = 6;
const wordLength = 5;
let currentGuessIndex = 0; // Which row are we filling right now

document.addEventListener("DOMContentLoaded", () => {
  createBoard();
});

function createBoard() {
  const board = document.getElementById("wordleBoard");
  for (let row = 0; row < maxGuesses; row++) {
    for (let col = 0; col < wordLength; col++) {
      const box = document.createElement("div");
      box.classList.add("letter-box");
      box.setAttribute("id", `box-${row}-${col}`);
      board.appendChild(box);
    }
  }
}

function submitGuess() {
  const input = document.getElementById("guessInput");
  const guess = input.value.trim().toLowerCase();

  if (guess.length !== wordLength) {
    showMessage("Word must be 5 letters!");
    return;
  }

  // Send guess to backend
  fetch("http://localhost:8080/api/wordle/guess", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ guess }),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Invalid word or server error.");
      }
      return response.json();
    })
    .then((data) => {
      updateGridWithFeedback(guess, data.feedback);
      input.value = "";
      currentGuessIndex++;

      if (data.feedback.every((color) => color === "green")) {
        showMessage("🎉 You got it!");
        btnNewGame.classList.remove("hidden");
        disableInput();
      } else if (currentGuessIndex >= maxGuesses) {
        showMessage("😢 Out of guesses!");
        disableInput();
      }
    })
    .catch((error) => {
      showMessage(error.message);
    });
}

function updateGridWithFeedback(guess, feedback) {
  for (let i = 0; i < wordLength; i++) {
    const box = document.getElementById(`box-${currentGuessIndex}-${i}`);
    box.textContent = guess[i].toUpperCase();
    box.classList.add(feedback[i]); // feedback = ["green", "gray", "yellow", ...]
  }
}

function showMessage(msg) {
  document.getElementById("message").textContent = msg;
}

function unshowMessage() {
  document.getElementById("message").textContent = "";
}

function disableInput() {
  document.getElementById("guessInput").disabled = true;
}

function newGame() {
  currentGuessIndex = 0;
  fetch("http://localhost:8080/api/wordle/new", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
  });
  for (let i = 0; i < wordLength; i++) {
    const box = document.getElementById(`box-${currentGuessIndex}-${i}`);
    box.textContent = "";
    box.style.backgroundColor = "white";
  }
  document.getElementById("guessInput").disabled = false;
  unshowMessage();
  btnNewGame.classList.add("hidden");
}
