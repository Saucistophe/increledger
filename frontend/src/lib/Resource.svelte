<script lang="ts">
    import type {Resource} from "./shared.svelte";

    let {resource}: { resource: Resource } = $props();
</script>

<div class="line">
    <span class="name">{resource.emoji} {resource.name}</span>
    <span class="amount">{resource.amount.toFixed(1)} / {resource.cap}</span>
    <span class="production"> {resource.production || ''}</span>
</div>

<style>
    .line {
        font-size: 16px;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        width: 30em;
    }


    .name {
        text-align: left;
        text-transform:  capitalize;
        flex: 2;
    }

    .amount {
        text-align: center;
        flex: 1;
    }

    .production {
        text-align: right;
        flex: 1;
    }
</style>