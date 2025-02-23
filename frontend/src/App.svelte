<script lang="ts">
    import Game from './lib/Game.svelte'
    import svelteLogo from './assets/svelte.svg'
    import viteLogo from '/vite.svg'
</script>

<main>
    <h1>Increledger</h1>

    <div class="card">
        <Game/>
    </div>

    <div class="footer">
        <a href="https://vite.dev" target="_blank" rel="noreferrer">
            <img src={viteLogo} class="logo" alt="Vite Logo"/>
        </a>
        <a href="https://svelte.dev" target="_blank" rel="noreferrer">
            <img src={svelteLogo} class="logo svelte" alt="Svelte Logo"/>
        </a>
    </div>
</main>

<style>
    .footer {
        color: white;
        position: absolute;
        bottom: 1em;
        right: 1em;
        scale: 50%;
    }
</style>
