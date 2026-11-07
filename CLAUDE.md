@~/Documents/Projects/Minecraft/MinecraftDeveloperPortal/.claude/profiles/fabric-loom.md

# Choppers Delight - `fabric/1.21.6`

**This file describes the `fabric/1.21.6` line**: Minecraft 1.21.6, Fabric. Migrated from `fabric/1.21.5` on 2026-11-01.
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
- Farmer's Delight Refabricated 1.21.8-3.3.3, the newest for 1.21.6, from the Greenhouse maven, named by
  `farmersdelight_minecraft_version` since Refabricated names a build after the newest Minecraft it covers (1.21.6 to
  1.21.8 here); a port of Farmer's Delight 1.2, so its cutting board holds one item at a time. The manifest still takes
  1.21.6-3.2.3, whose cutting board is the same. The decorated cutting board mirrors 3.3.3's
  `CuttingBoardBlock` and `CuttingBoardBlockEntity` (`DecoratedCuttingBoardBlock`, `DecoratedCuttingBoardBlockEntity`);
  check them against Farmer's Delight's own when it updates.
- Farmer's Delight Refabricated has no JEI integration on 1.21.6 (it integrates REI), and JEI has no 1.21.6 build, so there is no JEI plugin here.
- Every Compat and Oh The Biomes We've Gone have no 1.21.6 build: no Every Compat module, and the BWG cutting boards
  are registered but nothing in a dev run loads BWG. Biomes O' Plenty runs in dev runs, on GlitchCore 2.6.0.5 and
  TerraBlender 6.0.0.1: BoP 21.4.0.62 needs at least 2.6.0.1 and 6.0.0.1, and GlitchCore 2.6.0.1's tooltip mixin crashes
  the client when an inventory opens with the cursor on no slot (`Optional.of` of a null hovered slot), fixed by 2.6.0.5.
- Every item has an `items/` definition (1.21.4); the decorated cutting board's is a special model,
  `DecoratedCuttingBoardItemRenderer`, registered into `SpecialModelRenderers.ID_MAPPER`.
- Farmer's Delight's own cutting board recipe is replaced in `RecipeManager#prepare` (`CuttingBoardRecipeMixin`).
- Datagen: `runDatagen`. No generated resources are committed on this line. Pinned to this mod with `modId = mod_id`.
- Game tests in `src/test`, laid out as The Lead Age's (`GameTests`, `GameTestRegistration`, `TestCompat`,
  `verification.md`), run with `runGameTest`. The test bodies and `GameTests` are the same to the byte as every other
  line's; only `TestCompat` and the registration differ. The test source set is a mod of its own,
  `choppersdelight_gametest`, and from 1.21.5 its `fabric.mod.json` names `GameTestRegistration` as a `main`
  entrypoint, which registers the bodies into vanilla's `test_function` registry and the tests into `test_instance`
  from `DynamicRegistrySetupCallback`, as The Lead Age's Fabric registration does (`kb/mc-deltas/1.21.4-to-1.21.5.md`).
  The run counts vanilla's own `always_pass` too: 20 tests, 19 of them the mod's.
- The registration keeps the server for `TestCompat.burnTime`, since the fuel values are the server's from 1.21.2 and
  the game-test server isn't Fabric's game instance.
- Scenarios in `scenarios/`, `fabric/1.21`'s, played by the portal's Play scenarios: cutting on a board, decorating in
  the crafting grid, Farmer's Delight's board's name, stacking, and a carved tool's look. The renamed board's `give`
  names it with an SNBT string, `custom_name="Grandma's Board"`, since text components in a command are SNBT from
  1.21.5; 1.21.1's JSON string inside quotes names it `"Grandma's Board"`, quotes and all.
- Publishes the `remapJar` with `publishMods` to Modrinth and CurseForge, tagged Fabric and 1.21.6 (`publishing.md`).
- GitHub Actions: `build.yml`.
