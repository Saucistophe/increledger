<script lang="ts">
    import type {Population} from "./shared.svelte";
    import {gameState} from "./game-service.svelte";

    let {population}: { population: Population } = $props();

    async function assign(occupationName: string) {
        console.log("assign", occupationName);
        gameState.actions.push(    {
            "type": ".AssignOccupation",
            "occupation": "woodcutter",
            "numbersOfAssignees": 1
        });

    }

</script>

<h2>{population.name}</h2>
{#each population.occupations as occupation}
    <p>{occupation.name}: {occupation.count}
        {#if occupation.cap >= 0} / {occupation.cap}{/if}
        <button onclick={()=>assign(occupation.name)}>+</button>
    </p>
{/each}