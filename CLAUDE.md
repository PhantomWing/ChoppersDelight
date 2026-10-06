@~/Documents/Projects/Minecraft/MinecraftDeveloperPortal/.claude/profiles/legacy-forge.md

# Choppers Delight - `neoforge/1.20.1`

**This file describes the `neoforge/1.20.1` line**: Minecraft 1.20.1, Forge.
Built with ForgeGradle (legacy, maintenance only).

It belongs to whichever folder has this branch checked out - the main `ChoppersDelight` folder or a worktree
under `ChoppersDelight/.worktrees/`. A session started in a worktree also loads the main folder's CLAUDE.md,
which describes another line; for this folder, this file is the one that applies. Confirm with
`git branch --show-current`. `MinecraftDeveloperPortal/data/mods.json` lists every line of the mod.

An add-on for Farmer's Delight that adds cutting board variants for each wood type, decorated with
banners. Mod ID `choppersdelight`.

Released since 2025-09. Releases up to 1.2.0 (1.1.0 on Fabric) were uploaded by hand; every line now
publishes through `publishMods` (`publishing.md`), with the platform IDs in `gradle.properties`.

## This line

- **Forge line (ForgeGradle) despite the `neoforge/` prefix**, and named `1.20.1` where the ladder's suffix for this rung is `1.20`.
- Java 17, Mojang mappings with Parchment.
- Datagen: `runData`. Output: `src/generated/resources`, never hand-edited.
- No game tests yet. Writing the first one for whatever is ported next is the highest-value test available (`verification.md`).
- Publishes with `publishMods` to Modrinth and CurseForge, tagged Forge and NeoForge (NeoForge's 1.20.1 runs Forge
  jars) and 1.20.1 (`publishing.md`). The upload tasks depend on `reobfJar`, which reobfuscates the jar in place.
- Hand-authored access transformer: `src/main/resources/META-INF/accesstransformer.cfg`. A first suspect when a port fails to load.
