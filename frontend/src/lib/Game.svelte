<script lang="ts">
    import Population from "./Population.svelte";
    import Resource from "./Resource.svelte";
    import {gameState, startAutoUpdate} from "./game-service.svelte";
    import {onMount} from 'svelte';
    import Tech from "./Tech.svelte";
    import Dialog from "./Dialog.svelte";
    import Settings from "./Settings.svelte";

    $inspect(gameState.initialized)

    onMount(() => {
        startAutoUpdate();
    });
</script>

<!--p>
    game is {JSON.stringify(gameState.gameDescription, null, 2)}
</p-->

<div>
    {#if gameState.initialized && gameState.gameDescription}
        {#if gameState.gameDescription.dialogs?.length}
            <Dialog dialog={gameState.gameDescription.dialogs[0]}/>
        {/if}

        <h2>Populations</h2>
        {#each gameState.gameDescription.populations as population}
            <Population population={population}/>
        {/each}
        <div class="resources">
        {#each gameState.gameDescription.resources as resource}
            <Resource resource={resource}/>
        {/each}
        </div>
        <h2>Techs</h2>
        {#each gameState.gameDescription.techs as tech}
            <Tech tech={tech}/>
        {/each}
        <Settings settings={gameState.settings}/>
    {/if}
</div>


<style>
    .resources {
        position: absolute;
        left: 0;
        top: 0;
    }
</style>
