@~/Documents/Projects/Minecraft/MinecraftDeveloperPortal/.claude/profiles/fabric-loom.md

# Choppers Delight - `fabric/1.21.5`

**This file describes the `fabric/1.21.5` line**: Minecraft 1.21.5, Fabric. Migrated from `fabric/1.21` on 2026-11-01.
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
- Farmer's Delight Refabricated 1.21.5-3.2.5, from the Greenhouse maven: the only build for 1.21.5, a port of Farmer's
  Delight 1.2, so its cutting board holds one item at a time. The decorated cutting board mirrors 3.2.5's
  `CuttingBoardBlock` and `CuttingBoardBlockEntity` (`DecoratedCuttingBoardBlock`, `DecoratedCuttingBoardBlockEntity`);
  check them against Farmer's Delight's own when it updates.
- Farmer's Delight Refabricated has no JEI integration on 1.21.5 (it integrates REI), so there is no JEI plugin here.
- Every Compat and Oh The Biomes We've Gone have no 1.21.5 build: no Every Compat module, and the BWG cutting boards
  are registered but nothing in a dev run loads BWG. Biomes O' Plenty runs in dev runs.
- Every item has an `items/` definition (1.21.4); the decorated cutting board's is a special model,
  `DecoratedCuttingBoardItemRenderer`, registered into `SpecialModelRenderers.ID_MAPPER`.
- Farmer's Delight's own cutting board recipe is replaced in `RecipeManager#prepare` (`CuttingBoardRecipeMixin`).
- Datagen: `runDatagen`. No generated resources are committed on this line. Pinned to this mod with `modId = mod_id`.
- No game tests yet. Writing the first one for whatever is ported next is the highest-value test available (`verification.md`).
- Publishes the `remapJar` with `publishMods` to Modrinth and CurseForge, tagged Fabric and 1.21.5 (`publishing.md`).
- GitHub Actions: `build.yml`.
