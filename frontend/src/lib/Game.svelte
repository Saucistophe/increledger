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

{#if gameState.initialized && gameState.gameDescription}
    {#if gameState.gameDescription.dialogs?.length}
        <Dialog dialog={gameState.gameDescription.dialogs[0]}/>
    {/if}
    <div class="top-bar">
        <span>{gameState.gameDescription.timeSpent}</span>
        <h1>{gameState.gameDescription.title}</h1>
        <Settings settings={gameState.settings}/>
    </div>

    <div class="main-panel">
        <div class="resources">
            {#each gameState.gameDescription.resources as resource}
                <Resource resource={resource}/>
            {/each}
        </div>
        <div>
            <h2>Populations</h2>
            {#each gameState.gameDescription.populations as population}
                <Population population={population}/>
            {/each}
        </div>

        <div>
            <h2>Techs</h2>
            {#each gameState.gameDescription.techs as tech}
                <Tech tech={tech}/>
            {/each}
        </div>
    </div>
{:else}
    Loading game from server...
{/if}


<style>
    .top-bar {
        width: 100%;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
    }

    h1 {
        font-size: 17px;
    }

    .main-panel {
        width: 100%;
        height: 100%;

        display: flex;
        flex-direction: row;
        align-items: stretch;
        justify-content: space-between;
    }
</style>
