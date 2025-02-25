<script lang="ts">
    import Population from "./Population.svelte";
    import Resource from "./Resource.svelte";
    import {gameState, updateGame} from "./game-service.svelte";

    $inspect(gameState.initialized)
</script>

<button onclick={updateGame}>
    game is {JSON.stringify(gameState.gameDescription, null, 2)}
</button>

<div>
    {#if gameState.initialized && gameState.gameDescription}
        {#each gameState.gameDescription.populations as population}
            <Population population={population}/>
        {/each}
        {#each gameState.gameDescription.resources as resource}
            <Resource resource={resource}/>
        {/each}
    {/if}
</div>
