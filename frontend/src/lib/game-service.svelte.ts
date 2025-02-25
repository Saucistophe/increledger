import type {GameDescription} from "./shared.svelte";

type GameState = {
    initialized: boolean;
    game: object; // The goal is not to care about this object.
    gameDescription: GameDescription;
    actions: any[];
    signature: string;
}

export const gameState: GameState = $state( {initialized: false} as any as GameState);

export async function updateGame() {
    if (!gameState.initialized) {
        const response = await fetch('/api/game');
        const newGame: GameState = await response.json();

        gameState.gameDescription = newGame.gameDescription;
        gameState.signature = newGame.signature;
        gameState.game = newGame.game;
        gameState.actions = [];
        gameState.initialized=true;
    } else {
        const response = await fetch('/api/game', {
            method: "POST", body: JSON.stringify(gameState), headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        });
        const newGame: GameState = await response.json();
        gameState.gameDescription = newGame.gameDescription;
        gameState.signature = newGame.signature;
        gameState.game = newGame.game;
        gameState.actions = [];
    }
}