@~/Documents/Projects/Minecraft/MinecraftDeveloperPortal/.claude/profiles/fabric-loom.md

# Choppers Delight - `fabric/1.21.11`

**This file describes the `fabric/1.21.11` line**: Minecraft 1.21.11, Fabric. Migrated from `fabric/1.21.10` on 2026-11-01.
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

- Java 21, Mojang mappings with Parchment. Loom 1.17.21 on Gradle 9.5.1: Loom 1.17.0's alphas fail to remap Farmer's
  Delight Refabricated, whose class tweaker extends enums (1.21.1 builds against a patched jar for the same reason).
- Farmer's Delight Refabricated 1.21.11-3.6.16, from the Modrinth maven, a port of Farmer's Delight 1.3: its cutting
  board holds a stack again, as on 1.21.1. The decorated cutting board mirrors 3.6.16's
  `CuttingBoardBlock` and `CuttingBoardBlockEntity` (`DecoratedCuttingBoardBlock`, `DecoratedCuttingBoardBlockEntity`);
  check them against Farmer's Delight's own when it updates.
- Farmer's Delight Refabricated has JEI integration again: `JEIPlugin` adds the cutting boards as catalysts of its
  cutting category, and JEI runs in dev runs.
- Every Compat has no 1.21.11 build: no Every Compat module. Oh The Biomes We've Gone has one on Modrinth only; the BWG
  cutting boards are registered but nothing in a dev run loads BWG. Biomes O' Plenty runs in dev runs.
- Every item has an `items/` definition (1.21.4); the decorated cutting board's is a special model,
  `DecoratedCuttingBoardItemRenderer`, registered into `SpecialModelRenderers.ID_MAPPER`.
- Rendering goes through render states and `submit` (1.21.9): `DecoratedCuttingBoardRenderer` reads the board, the
  banner and a Farmer's Delight cutting board made from it in `extractRenderState`, and draws the content with Farmer's
  Delight's own renderer; the banner is a `Model.Simple` drawn by `BannerRenderer.submitPatterns` (`BannerUtils`).
- Farmer's Delight's own cutting board recipe is replaced in `RecipeManager#prepare` (`CuttingBoardRecipeMixin`).
- Datagen: `runDatagen`. No generated resources are committed on this line. Pinned to this mod with `modId = mod_id`.
- Game tests in `src/test`, laid out as The Lead Age's (`GameTests`, `GameTestRegistration`, `TestCompat`,
  `verification.md`), run with `runGameTest`: 19, and vanilla's own one. The test bodies and `GameTests` are the same
  to the byte as every other line's; only `TestCompat` and the registration differ. The test source set is a mod of
  its own, `choppersdelight_gametest`, whose `fabric.mod.json` names `GameTestRegistration` as its `main` entrypoint:
  from 1.21.5 it registers each test into vanilla's `test_function` and `test_instance` registries itself, as The
  Lead Age's Fabric registration does. Here `TestCompat` reads fuel from the server's `FuelValues` (the registration
  hands it the server), names a stack by its hover name's translation key, picks a block through
  `BlockState#getCloneItemStack`, gives `getAnalogOutputSignal` a side, and makes no mock player wait for the spawn
  area (1.21.9); `ResourceLocation` is `Identifier`.
- Scenarios in `scenarios/`, the same files as `fabric/1.21`'s, played by the portal's Play scenarios: cutting on a
  board, decorating in the crafting grid, Farmer's Delight's board's name, stacking, and a carved tool's look.
- Publishes the `remapJar` with `publishMods` to Modrinth and CurseForge, tagged Fabric and 1.21.11 (`publishing.md`).
- GitHub Actions: `build.yml`.
