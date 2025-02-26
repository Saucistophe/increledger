<script lang="ts">
    import type {Tech} from "./shared.svelte";
    import {executeAction} from "./game-service.svelte";

    let {tech}: { tech: Tech } = $props();

    async function build(techName: string) {
        await executeAction({
            "type": ".Research",
            "tech": techName
        });
    }
</script>

<p>{tech.name}: {tech.count}
    {#if tech.cap >= 0} / {tech.cap}{/if}
    <button disabled="{ tech.cap >= 0 && tech.count >= tech.cap}"
            onclick={()=>build(tech.name)}>+
    </button>
</p>