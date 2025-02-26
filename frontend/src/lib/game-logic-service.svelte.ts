import {gameState} from "./game-service.svelte";

export function canAfford(cost: Map<string, number>): boolean {

    const costClone = new Map(Object.entries(cost));

    for (const resource of gameState.gameDescription.resources) {
        if (costClone.has(resource.name)) {
            const neededAmount = costClone.get(resource.name);
            const currentAmount = resource.amount;
            if (neededAmount ! > currentAmount) return false;
            costClone.delete(resource.name);
        }
    }

    return costClone.size == 0;
}