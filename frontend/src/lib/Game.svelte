<script lang="ts">
    let game: object = $state({})
    async function call() {
        const response = await fetch('/api/game');
        game = await response.json();
    }
</script>

<button onclick={call}>
    game is {JSON.stringify(game)}
</button>
