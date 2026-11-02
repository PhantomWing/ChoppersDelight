@~/Documents/Projects/Minecraft/MinecraftDeveloperPortal/.claude/profiles/fabric-loom.md

# Choppers Delight - `fabric/1.21`

**This file describes the `fabric/1.21` line**: Minecraft 1.21.1 (the jar covers 1.21 and 1.21.1), Fabric. A **baseline** line: new features start here.
Built with Fabric Loom.

It belongs to whichever folder has this branch checked out - the main `ChoppersDelight` folder or a worktree
under `ChoppersDelight/.worktrees/`. A session started in a worktree also loads the main folder's CLAUDE.md,
which describes another line; for this folder, this file is the one that applies. Confirm with
`git branch --show-current`. `MinecraftDeveloperPortal/data/mods.json` lists every line of the mod.

An add-on for Farmer's Delight that adds cutting board variants for each wood type, decorated with
banners. Mod ID `choppersdelight`.

Released since 2025-09. Releases up to 1.2.0 (1.1.0 on Fabric) were uploaded by hand; every line now
publishes through `publishMods` (`publishing.md`), with the platform IDs in `gradle.properties`.

## This line

- Java 21, Mojang mappings with Parchment.
- Datagen: `runDatagen`. No generated resources are committed on this line. Pinned to this mod with `modId = mod_id`.
- Game tests in `src/test`, laid out as The Lead Age's (`GameTests`, `GameTestRegistration`, `TestCompat`,
  `verification.md`), run with `runGameTest`. The test bodies are the same to the byte as `neoforge/1.21`'s; only
  `TestCompat` (the creative tab, fuel) and the registration differ. The test source set is a mod of its own,
  `choppersdelight_gametest`, whose `fabric.mod.json` names the `fabric-gametest` entrypoint.
- Scenarios in `scenarios/`, played by the portal's Play scenarios: cutting on a board, decorating in the crafting
  grid, Farmer's Delight's board's name, stacking, and a carved tool's look.
- Publishes the `remapJar` with `publishMods` to Modrinth and CurseForge, tagged Fabric and 1.21, 1.21.1 (`publishing.md`).
- GitHub Actions: `build.yml`.
