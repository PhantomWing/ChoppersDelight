@~/Documents/Projects/Minecraft/MinecraftDeveloperPortal/.claude/profiles/fabric-loom.md

# Choppers Delight - `fabric/26.2`

**This file describes the `fabric/26.2` line**: Minecraft 26.2, Fabric. Migrated from `fabric/26.1` on 2026-11-01.
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

- Java 25. 26.x is unobfuscated: no mappings, no Parchment and no remap step; Loom's no-remap plugin
  (`net.fabricmc.fabric-loom` 1.17.21 on Gradle 9.5.1), plain `implementation` / `compileOnly` / `localRuntime`, and the
  `jar` is what `publishMods` uploads.
- Farmer's Delight Refabricated 3.6.30 from the Modrinth maven, one build per Minecraft version family
  (`farmersdelight_minecraft_version`), a port of Farmer's Delight 1.3: its cutting board holds a stack. It asks for
  Fabric Loader 0.19.5, so `loader_version` and the manifest's floor are 0.19.5. The
  decorated cutting board mirrors 3.6.30's `CuttingBoardBlock` and `CuttingBoardBlockEntity` (`DecoratedCuttingBoardBlock`,
  `DecoratedCuttingBoardBlockEntity`); check them against Farmer's Delight's own when it updates.
- `JEIPlugin` adds the cutting boards as crafting stations of Farmer's Delight's JEI cutting category; JEI runs in dev runs.
- Every Compat has no 26.x build: no Every Compat module. The Oh The Biomes We've Gone cutting boards are registered but
  nothing in a dev run loads BWG. Biomes O' Plenty runs in dev runs.
- Every item has an `items/` definition; the decorated cutting board's is a special model,
  `DecoratedCuttingBoardItemRenderer`, registered into `SpecialModelRenderers.ID_MAPPER`.
- Rendering goes through render states and `submit`: `DecoratedCuttingBoardRenderer` reads the board, the banner and a
  Farmer's Delight cutting board made from it in `extractRenderState` (with no level, so filling it marks nothing dirty),
  and draws the content with Farmer's Delight's own renderer; the banner is a `Model.Simple` drawn as vanilla's banner
  draws its flag (`BannerUtils`).
- The add and remove pattern recipes hold nothing, so each is one instance its serializer decodes every recipe file to.
- Farmer's Delight's own cutting board recipe is replaced in `RecipeManager#prepare` (`CuttingBoardRecipeMixin`).
- Datagen: `runDatagen`. No generated resources are committed on this line. Pinned to this mod with `modId = mod_id`.
- Game tests in `src/test`, laid out as The Lead Age's (`GameTests`, `GameTestRegistration`, `TestCompat`,
  `verification.md`), run with `runGameTest`. The test bodies are the same to the byte as every other line's; only
  `TestCompat` and the registration differ. Here `TestCompat` gives the white banner as `Items.BANNER.pick(DyeColor.WHITE)`
  (26.2 made the coloured items one collection), takes Components in `fail` and `assertTrue`, looks items up by
  `Identifier` with `getValue`, crafts through `level.recipeAccess()` with no registries to `assemble`, reads fuel from the server's `fuelValues()` (Fabric's `FuelRegistry` is gone; the registration keeps the server as
  it starts),
  names a stack by its item name's translation key, gives the comparator a side, and doesn't wait for the spawn's
  entities before a mock player. The test source set is a mod of its own, `choppersdelight_gametest`, whose
  `fabric.mod.json` names the registration as its `main` entrypoint: from 1.21.5 it registers the tests into vanilla's
  test registries itself. It also registers the mod's creative tab, which the mod registers from its client
  entrypoint alone (on 1.21.1 Every Compat's module registered it on a server too; 26.x has no Every Compat). The
  JUnit `test` task is told not to fail on finding no tests (Gradle 9).
- Scenarios in `scenarios/`, played by the portal's Play scenarios: cutting on a board, decorating in the crafting
  grid, Farmer's Delight's board's name, stacking, and a carved tool's look. A `give` names an item in SNBT
  (`custom_name="Grandma's Board"`), as from 1.21.5.
- Publishes the `jar` with `publishMods` to Modrinth and CurseForge, tagged Fabric and 26.2 (`publishing.md`).
- GitHub Actions: `build.yml`.
