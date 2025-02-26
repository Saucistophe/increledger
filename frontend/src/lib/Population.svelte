<script lang="ts">
    import type {Population} from "./shared.svelte";
    import {gameState, updateGame} from "./game-service.svelte";

    let {population}: { population: Population } = $props();

    async function assign(occupationName: string) {
        gameState.actions.push({
            "type": ".AssignOccupation",
            "occupation": occupationName,
            "numbersOfAssignees": 1
        });
        await updateGame();

    }

    async function unassign(occupationName: string) {
        gameState.actions.push({
            "type": ".UnassignOccupation",
            "occupation": occupationName,
            "numbersOfAssignees": 1
        });
        await updateGame();

    }

</script>

<h2>{population.name} ({population.count}/{population.cap})</h2>
{#each population.occupations as occupation}
    <p>{occupation.name}: {occupation.count}
        {#if occupation.cap >= 0} / {occupation.cap}{/if}
        <button disabled="{occupation.count <= 0}" onclick={()=>unassign(occupation.name)}>-</button>
        <button disabled="{population.freePopulation <= 0 || (population.cap >= 0 && occupation.count >= population.cap)}"
                onclick={()=>assign(occupation.name)}>+
        </button>
    </p>
{/each}