# zSpawner performance review

> Note: the provided Spark profile link could not be fetched in this offline environment. The hotspots below are derived from the code paths that typically dominate CPU in live profiles of this plugin (periodic spawner ticking and nearby-player lookups). Please re-run Spark in your environment to confirm the baseline percentages and validate the impact of the changes.

## Top hotspots (from profiling focus)
1) **SpawnerManager.run → World#getNearbyEntities** – executed every second for each virtual spawner to count nearby players; performs spatial queries per spawner and allocates entity collections.
2) **Spawner.tick/autokill scheduling** – per-spawner tick invoked once players are detected; repeated entity state checks and random delay generation.
3) **Spawner.spawnEntity clean-up loop** – nearby-entity sweep on each spawn to remove duplicates.
4) **ServerProfile lookups** – repeated map traversals and temporary lists when iterating spawners by type.
5) **Inventory updates on item injection** – updates every online player inventory when drops are added.

## Changes implemented
- Batched nearby-player detection in `SpawnerManager#run`, iterating players once per tick and using squared-distance checks to decide whether to tick a spawner. This removes per-spawner world queries and associated allocations.

## Risks / notes
- Behaviour is preserved: spawners still tick only when at least one player is within the configured distance. Auto-kill logic still runs each tick cycle. The change depends on player locations at the start of the task; highly mobile players might introduce up to one-second latency, matching the existing task period.

## Validation checklist
- Capture a Spark profile before and after deployment; compare time spent under `SpawnerManager.run` and any `World#getNearbyEntities` children.
- Load-test with hundreds of spawners and multiple players moving in/out of range to ensure spawning still triggers as expected.
- Verify no console warnings during the periodic task.
- Confirm no change in drop/auto-kill behaviour for virtual spawners.
