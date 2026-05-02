# 1.2.0
### Changes
- Updated for Farmer's Delight 1.3.1
- For now, restored the original Farmer's Delight cutting board appearance to match Chopper's Delight textures

### Fixes
- Decorated cutting boards no longer drop themselves when destroyed by a player in creative mode

# 1.1.2
### Fixes
- Fix game crashing when opening Creative Inventory with Every Compat installed in a production environment

# 1.1.1
### Fixes
- Fix incorrect tab key passed to Every Compat

# 1.1.0
### Additions
- Added Every Compat support for cutting boards
- Added cutting boards to the `#create:brittle` block tag

# 1.0.2
### Fixes
- Fix decorated cutting boards's internal Cutting Board stack size not resetting to `1` when crafted.
- Fix cutting boards not being broken faster with an Axe

# 1.0.1
### Fixes
- Fix crash on startup when running the mod on a server
- Fix decorated cutting boards not being craftable
- Fix crafting recipes for cutting boards of compatible mods always loading, even if the mod is not present
- Fix block loot tables for cutting boards of compatible mods always loading, even if the mod is not present
- Fix JEIPlugin trying to load cutting boards of compatible mods, even if the mod is not present
- Fix `cutting_boards` tag failing to load, when any of the compatible mods is not present
- Fix decorated cutting boards not dropping the correct item, always dropping with a default banner and wood type
- Fix decorated cutting boards having a missing particle texture when breaking

# 1.0.0
### Additions
- Added cutting boards for vanilla `Minecraft` wood types
- Added compatibility with `Biomes O' Plenty` wood types
- Added compatibility with `Oh The Biomes We've Gone` wood types
- Added decorated cutting boards
  - Decorate a cutting board by combining it with a Banner

### Changes
- Farmer's Delight's `Cutting Board` is renamed to `Spruce Cutting Board`
- Farmer's Delight's `Cutting Board` is now crafted using `Spruce Planks`