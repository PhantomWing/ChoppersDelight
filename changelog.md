# 1.1.0
### Additions
- Added Every Compat (Wood Good) support — cutting boards now generate for every wood type registered through Every Compat.

### Changes
- Updated for Farmer's Delight Refabricated 3.3.2.
- Restored the original Farmer's Delight cutting board appearance to match Chopper's Delight textures.

### Fixes
- Decorated cutting boards no longer drop themselves when destroyed by a player in creative mode.
- Sneak-carving a tool now works on decorated cutting boards (the carving event handler was previously not registered).

# 1.0.2
### Fixes
- Fix decorated cutting boards's internal Cutting Board stack size not resetting to `1` when crafted.
- Fix cutting boards not being broken faster with an Axe

# 1.0.1
### Fixes
- Fixed Farmer's Delight's Cutting Board recipe not being overridden (it still allowed any wood type), causing some other cutting boards to be unobtainable
- Fixed Farmer's Delight's Cutting Board not being renamed to Spruce Cutting Board
- Fixed recipes, advancements & loot tables of cutting boards not being loaded conditionally (now they only load in if their wood type is present)
- Fixed decorated cutting boards dropping the wrong item when broken
- Fixed decorated cutting boards missing a particle texture when breaking

# 1.0.0
First Fabric version.

### Additions
- Added cutting boards for vanilla Minecraft wood types
- Added compatibility with Biomes O' Plenty wood types
- Added compatibility with Oh The Biomes We've Gone wood types
- Added decorated cutting boards
  - Decorate a cutting board by combining it with a Banner

### Changes
- Farmer's Delight's `Cutting Board` is renamed to `Spruce Cutting Board
- Farmer's Delight's `Cutting Board` is now crafted using `Spruce Planks`