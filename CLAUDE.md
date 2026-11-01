@~/Documents/Projects/Minecraft/MinecraftDeveloperPortal/.claude/profiles/fabric-loom.md

# Choppers Delight - `fabric/26.1`

**This file describes the `fabric/26.1` line**: Minecraft 26.1.2 (the jar covers 26.1, 26.1.1 and 26.1.2), Fabric. Migrated from `fabric/1.21.11` on 2026-11-01.
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
  Fabric Loader 0.19.5, above the ladder's for 26.1.2, so `loader_version` and the manifest's floor are 0.19.5. The
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
- No game tests yet. Writing the first one for whatever is ported next is the highest-value test available (`verification.md`).
- Publishes the `jar` with `publishMods` to Modrinth and CurseForge, tagged Fabric and 26.1, 26.1.1, 26.1.2 (`publishing.md`).
- GitHub Actions: `build.yml`.
