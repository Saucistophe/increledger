<script lang="ts">

    import type {Settings} from "./game-service.svelte";

    let expanded = $state(false);
    let {settings}: { settings: Settings } = $props();

    const getFlagEmoji = (countryCode: string) => {
        const codePoints = countryCode
            .toUpperCase()
            .split('')
            .map((char) => 127397 + char.charCodeAt(0))
        return String.fromCodePoint(...codePoints)
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
    </div>
</div>

<style>.settings {
    position: fixed;
    top: 0;
    right: 0;
}
</style>