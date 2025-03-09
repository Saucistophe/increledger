<script lang="ts">

    import {resetGame, type Settings} from "./game-service.svelte";

    let expanded = $state(false);
    let {settings}: { settings: Settings } = $props();

    const getFlagEmoji = (countryCode: string) => {
        const codePoints = countryCode
            .toUpperCase()
            .split('')
            .map((char) => 127397 + char.charCodeAt(0))
        return String.fromCodePoint(...codePoints)
    }

    function resetGameClicked() {
        // TODO: Confirmation dialog
        resetGame();
    }
</script>

<div class="settings">
    <button onclick="{() => expanded = !expanded}">⚙️</button>
    <div hidden="{!expanded}">
        <label for="language-select">🌐💬</label>
        <select name="language"
                bind:value={settings.selectedLanguage}
                id="language-select">
            {#each settings?.supportedLanguages as language}
                <option value={language}
                       >{getFlagEmoji(language)}{language}</option>
            {/each}
        </select>
        <button onclick="{() => resetGame()}">Reset</button>
    </div>
</div>

<style>
</style>