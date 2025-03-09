import type {GameDescription} from "./shared.svelte";

export type Settings = {
    supportedLanguages: string[];
    selectedLanguage: string;
}

type GameState = {
    initialized: boolean;
    game: object; // The goal is not to care about this object.
    gameDescription: GameDescription;
    actions: any[];
    signature: string;
    settings: Settings;
}

export const gameState: GameState = $state({initialized: false} as any as GameState);

// Attempt to load from local storage
const storage = window.localStorage.getItem('gameState');
if (storage) {
    const storedGameState = JSON.parse(storage) as GameState;
    Object.assign(gameState, storedGameState);
}

let updating = false;

export async function updateGame() {
    updating = true;
    if (!gameState.initialized) {
        const response = await fetch('/api/game');
        const newGame: GameState = await response.json();

        Object.assign(gameState, newGame);
        gameState.actions = [];
        gameState.initialized = true;
    } else {
        const response = await fetch('/api/game', {
            method: "POST", body: JSON.stringify(gameState), headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            console.error(`Error received: ${response.status}`);
        } else {
            const newGame: GameState = await response.json();
            Object.assign(gameState, newGame);
            gameState.actions = [];
            gameState.initialized = true;
        }
    }
    updating = false;
    window.localStorage.setItem('gameState', JSON.stringify(gameState));
}


export async function resetGame() {
    updating = true;

    const response = await fetch('/api/game');
    const newGame: GameState = await response.json();

    Object.assign(gameState, newGame);
    gameState.actions = [];
    gameState.initialized = true;

    updating = false;
    window.localStorage.setItem('gameState', JSON.stringify(gameState));
}


let autoUpdateEnabled: boolean;

function autoUpdate() {
    if (!autoUpdateEnabled) return;
    updateGame().then(() => console.debug("Game auto-updated"));
    setTimeout(autoUpdate, 2000);
}

export function startAutoUpdate() {
    autoUpdateEnabled = true;
    autoUpdate();
}

export function stopAutoUpdate() {
    autoUpdateEnabled = false;
}

export async function executeAction(action: any) {
    if (updating) {
        setTimeout(executeAction, 100, action);
        return;
    }

    gameState.actions = [action];
    await updateGame();
}