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

Released since 2025-09, uploaded by hand: no line has `publishMods` or platform IDs in
`gradle.properties`. The Modrinth and CurseForge IDs are in `mods.json`; add the block and copy them
before publishing a line through the plugin (`publishing.md`).

## This line

- Java 21, Mojang mappings with Parchment.
- Datagen: `runData`. Output: `src/generated/resources`, never hand-edited.
- No game tests yet. Writing the first one for whatever is ported next is the highest-value test available (`verification.md`).
- No `publishMods` on this line: add the block before publishing it through the plugin (`publishing.md`).
- Hand-authored access transformer: `src/main/resources/META-INF/accesstransformer.cfg`. A first suspect when a port fails to load.
