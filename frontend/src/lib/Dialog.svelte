<script lang="ts">
    import type {Dialog} from "./shared.svelte";
    import {executeAction} from "./game-service.svelte";

    let {dialog}: { dialog: Dialog } = $props();

    async function respond(choice: string) {
        await executeAction({
            "type": ".RespondToDialog",
            "dialog": dialog.name,
            "choice": choice
        });
    }
</script>

<div class="modal">
    <div class="modal-content">
        <span class="name"> {dialog.translation || dialog.name}</span>
        {#each dialog.choices as choice}
            <button onclick="{()=>respond(choice.name)}"> { choice.translation || choice.name }</button>
        {/each}
    </div>
</div>


<style>
    .modal {
        position: fixed;
        z-index: 1;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        backdrop-filter: blur(2.5px);
        background-color: #000A;

        display: flex;
        justify-content: center;
        align-items: center;
    }

    .modal-content {
        border-radius: 1em;
        border: 2px solid grey;
        min-height: 5em;
    }
</style>