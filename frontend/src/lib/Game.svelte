<script lang="ts">
    import Population from "./Population.svelte";
    import Resource from "./Resource.svelte";
    import {gameState, startAutoUpdate} from "./game-service.svelte";
    import {onMount} from 'svelte';
    import Tech from "./Tech.svelte";

    $inspect(gameState.initialized)

    onMount(() => {
        startAutoUpdate();
    });
</script>

<p>
    game is {JSON.stringify(gameState.gameDescription, null, 2)}
</p>

<div>
    {#if gameState.initialized && gameState.gameDescription}
        <h2>Populations</h2>
        {#each gameState.gameDescription.populations as population}
            <Population population={population}/>
        {/each}
        <h2>Resources</h2>
        {#each gameState.gameDescription.resources as resource}
            <Resource resource={resource}/>
        {/each}
        <h2>Techs</h2>
        {#each gameState.gameDescription.techs as tech}
            <Tech tech={tech}/>
        {/each}
    {/if}
</div>
