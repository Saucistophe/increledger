<script lang="ts">
    import type {Resource} from "./shared.svelte";

    let {resource}: { resource: Resource } = $props();
</script>

<p>{resource.name} : {resource.amount.toFixed(1)} / {resource.cap}</p>