@~/Documents/Projects/Minecraft/MinecraftDeveloperPortal/.claude/profiles/neoforge-moddev.md

# Choppers Delight - `neoforge/1.21`

**This file describes the `neoforge/1.21` line**: Minecraft 1.21.1 (the jar covers 1.21 and 1.21.1), NeoForge. A **baseline** line: new features start here.
Built with NeoForge ModDevGradle.

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
- Datagen: `runData`. Output: `src/generated/resources`, never hand-edited.
- Game tests in `src/test`, laid out as The Lead Age's (`GameTests`, `GameTestRegistration`, `TestCompat`,
  `verification.md`), run with `runGameTestServer`. The test source set joins the mod in `neoForge.mods`, and
  the `gameTestServer` run uses it.
- Publishes with `publishMods` to Modrinth and CurseForge, tagged NeoForge and 1.21, 1.21.1 (`publishing.md`).
- Hand-authored access transformer: `src/main/resources/META-INF/accesstransformer.cfg`. A first suspect when a port fails to load.
