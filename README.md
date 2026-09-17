
![MASTERmaxisVanilla+](src/main/resources/assets/mastermaxisvanillaplus/title.png "Title")
A vanilla-friendly expansion mod for Minecraft Fabric 1.21.4 that revamps the Savanna biome with brand-new ecosystems, majestic wildlife, unique flora, and rich building blocks, alongside a complete overhaul of ranged combat featuring modular bows, heavy arrows, crossbow tiers, bolts, optic attachments, and an interactive Fletching Table station.

---

## 📖 Table of Contents
1. [Biomes](#-biomes)
   - [Savanna & Savanna Plateau (Overhauled)](#savanna--savanna-plateau-overhauled)
   - [Rusty Savanna](#rusty-savanna)
   - [Scorched Savanna](#scorched-savanna)
2. [Fauna & Creatures](#-fauna--creatures)
   - [The Ostrich](#the-ostrich)
   - [Termites & Colonies](#termites--colonies)
   - [Muddy Pigs, Happiness & Truffles](#muddy-pigs-happiness--truffles)
3. [Flora & Trees](#-flora--trees)
   - [Baobab Tree, Leaf Stages & Harvesting](#baobab-tree-leaf-stages--harvesting)
   - [Dried Acacia](#dried-acacia)
   - [Redthorn](#redthorn)
4. [Baobab Fruit & Savanna Cuisine](#-baobab-fruit--savanna-cuisine)
   - [Baobab Fruit](#baobab-fruit)
   - [Gui Juice](#gui-juice)
   - [Monkey Bread](#monkey-bread)
   - [Baobab Stew](#baobab-stew)
   - [Truffle Porkchop](#truffle-porkchop)
5. [Fluids & Mud Mechanics](#-fluids--mud-mechanics)
   - [Mud Pools & Mud Water](#mud-pools--mud-water)
   - [Mud Balls & Mud Blocks](#mud-balls--mud-blocks)
6. [Blocks, Stones & Ores](#-blocks-stones--ores)
   - [Mudstone Collection](#mudstone-collection)
   - [Red Soil Family](#red-soil-family)
   - [Laterite Family](#laterite-family)
   - [Feather Block](#feather-block)
   - [Silted Gravel](#silted-gravel)
7. [Ranged Combat & Archery Overhaul](#-ranged-combat--archery-overhaul)
   - [Heavy Bow & Heavy Hardened Bow](#heavy-bow--heavy-hardened-bow)
   - [Heavy Arrows & Tipped Variants](#heavy-arrows--tipped-variants)
   - [Crossbow Overhaul & Bolt Ammunition](#crossbow-overhaul--bolt-ammunition)
   - [Iron & Nether Crossbows](#iron--nether-crossbows)
8. [Viewfinder & Optics System](#-viewfinder--optics-system)
   - [The Viewfinder](#the-viewfinder)
   - [Crossbow Scope Attachment & Aiming](#crossbow-scope-attachment--aiming)
   - [Dynamic Camera Sensitivity Damping](#dynamic-camera-sensitivity-damping)
9. [Fletching Table Overhaul](#-fletching-table-overhaul)
   - [Interactive GUI & Persistent Block Storage](#interactive-gui--persistent-block-storage)
   - [Weapon Upgrading & Attachment Station](#weapon-upgrading--attachment-station)
   - [Projectile Fletching Station](#projectile-fletching-station)
10. [Schematics, Curses & World Loot](#-schematics-curses--world-loot)
    - [Bow & Crossbow Schematics](#bow--crossbow-schematics)
    - [Curse of Welding](#curse-of-welding)
    - [Dungeon & Structure Loot Tables](#dungeon--structure-loot-tables)
    - [Fishing Treasures](#fishing-treasures)
11. [Crafting & Stonecutting Recipes](#-crafting--stonecutting-recipes)
12. [Creative Inventory Tabs](#-creative-inventory-tabs)

---

## 🌍 Biomes

### Savanna & Savanna Plateau (Overhauled)
The classic vanilla Savanna and its plateaus have received a major atmospheric and visual overhaul:
- **Natural Tree Density:** Vanilla acacia trees have been spaced out to create true open grasslands and savannah plains.
- **Mud Pools:** Natural wetland pools filled with murky mud water and soft mud shores.
- **Mudstone Rock Formations:** Distinct rocky pillars containing mudstone and veins of coal, iron, copper, and flint.
- **Termite Mounds:** Organic mounds erected by termite colonies across the terrain.
- **Baobab Trees:** Mighty ancient baobab trees tower over the horizon.
- **Wildlife:** Herds of Ostriches graze alongside vanilla horses and donkeys.

---

### Rusty Savanna
A rich, warm savanna biome dominated by iron-tinted soils and vibrant savannah vegetation:
- **Surface Composition:** Covered with **Grassy Red Soil** interlaced with patches of regular grass and coarse dirt.
- **Subsurface & Geology:** Underneath lies pure **Red Soil** descending into **Mudstone** layers and cliffs.
- **Flora:** Dotted with sparse acacia trees, majestic baobabs, patches of sweet watermelons, wild cacti, dead bushes, and thick clusters of hazardous **Redthorn**.
- **Atmosphere:** Features custom warm golden-rusty foliage and grass colors with clear, vibrant skies.
- **Fauna:** Home to abundant herds of Ostriches that feed on local termite mounds.

---

### Scorched Savanna
A barren, sun-baked wasteland pocket nestled within the savanna climate:
- **Generation:** Generates strictly as localized sub-biome pockets inside or directly bordering other savanna biomes (never spawning standalone in isolated oceans or deserts).
- **Surface & Geology:** Surface composed of hard, weathered **Laterite**, with 2–3 layers of **Mudstone** directly below before reaching solid bedrock stone.
- **Atmosphere & Flora:** Parched, desolate landscape populated by **Dried Acacia** tree skeletons (leafless trunks and weathered stumps), dead bushes, and sporadic redthorn.
- **Extreme Heat & Hazards:** Natural water does not generate here. Instead, small bubbling surface **Lava Pools** and lava springs emerge from the cracked ground.
- **Iron-Rich Mudstone Formations:** Smaller mudstone rock formations generate with **substantially increased Mudstone Iron Ore veins (~15%)**. Beware: while termites are rare inside (~7%), approximately **18% of these formations house a sealed molten lava chamber** at their core!
- **No Animal Spawns:** Passive wildlife cannot survive the scorching heat—only hostile night monsters and bats roam here.

---

## 🦤 Fauna & Creatures

### The Ostrich
Ostriches are tall, swift, flightless birds native to Savannas and Rusty Savannas.

#### Behaviors & Ecology
- **Diet:** Ostriches are natural predators of termites! When near an infected termite mound, they peck and eat termites, controlling infestations.
- **Feathers & Eggs:** Ostriches periodically lay **Ostrich Eggs** (which can be thrown or hatched) and drop **Ostrich Feathers**.
- **Territorial Defense:** If attacked, adult ostriches will retaliate with powerful kicks and peck attacks.

#### Taming, Breeding & Healing
- **Healing:** Right-click an injured adult or baby ostrich with a standard **Melon Slice** to restore 1 HP.
- **Taming:** Feed an untamed adult ostrich a **Glistering Melon Slice** (1/3 chance of success, displays heart particles when tamed).
- **Breeding:** Once tamed, feeding two ostriches **Glistering Melon Slices** will cause them to enter love mode and breed a baby chick.
- **Growth:** Baby ostriches can be fed **Melon Slices** or **Glistering Melon Slices** to accelerate their growth.

#### Riding & Water Running
- **Saddling:** Right-click a tamed ostrich with a **Saddle** to ride it.
- **Riding Controls:** Controllable using standard player movement keys. Speed scales with the ostrich's health.
- **Sprint Mode:** Press the **Jump Key** while mounted to activate **Sprint**. The ostrich runs **25% faster**!
- **Water Running:** While sprinting, ostriches can **run directly on the surface of water**! (Note: Sprinting does not allow walking on dense mud water).
- **Gliding / Slow Fall:** When falling from high cliffs, ostriches flap their wings, slowing down downward velocity and preventing fall damage.

#### Mysterious Rumors
- **A Well-Kept Secret:** Savanna folklore whispers of a legendary, nostalgic plumage hidden away... Will you be able to uncover its secret?

---

### Termites & Colonies
- **Termite Mound:** Natural gravel-like dirt mounds formed by colonies. Can be quickly harvested with Shears (0.25s) and used as furnace fuel (smelts 1.2 items per block).
- **Infected Termite Mound:** The beating heart of a living termite colony. Ticks over time and can spread termites to nearby wood blocks. Also harvests quickly with Shears and can be burned in furnaces.
- **Infected Wood:** Wood blocks infested by termites.
- **Termite Trail:** Soil surface paths created by foraging worker termites.
- **Control:** Introduce Ostriches to hunt the termites, or break the infected mounds to eliminate colonies.

---

### Muddy Pigs, Happiness & Truffles
Pigs have received an engaging behavioral expansion centered on happiness, mud wallowing, and foraging for rare subterranean truffles:

#### Happiness System & Behaviors
Pigs now track an internal happiness level (0% to 100%) and will actively prioritize activities that boost their well-being:
- **Grazing:** Pigs naturally graze on grass, short grass, and grassy red soil like sheep (restores +15% happiness, plays chewing sounds and green/heart particles). Honors `mobGriefing` gamerule.
- **Hand Feeding:** Players can feed pigs carrots, golden carrots, potatoes, or beetroots (+20% happiness).
- **Mud Wallowing:** Pigs are naturally attracted to savanna mud pools and mud blocks. They overcome their natural fear of fluids to bathe in mud water and roll in mud blocks (+30% happiness upon wallowing).

#### Visual Mud Variants
Bathing in mud coats the pig in thick mud, selecting a 50/50 independent visual variant:
1. **Mud Bottom:** The pig's lower torso, legs, and belly are caked in thick, dried mud (`pig_in_mud_bottom.png`).
2. **Mud Top:** Mud covers the pig's back, head, and snout, featuring a custom 3D model with a delicate blooming flower perched on top (`pig_in_mud_top.png` via `PigMuddedTopEntityModel`).
- **Natural Spawning:** Generated mud pools in savanna biomes spawn with 1–2 naturally wallowing muddy pigs.
- **Mud Drying & Washing:**
  - **Natural Drying:** After ~5 minutes away from mud, the dried mud cracks and naturally flakes off (accompanied by dirt/gravel break sounds and mud particles). Stepping back into mud refreshes the timer.
  - **Water & Rain:** Submerging in clean water or standing under rainfall rapidly washes the mud off, cleanly restoring the pig to its standard pink skin.

#### Truffle Hunting & Digging
- **Searching:** When an adult pig is both muddy and sufficiently content (happiness >= 75%), it begins roaming and sniffing the ground in search of truffles (piglets do not hunt truffles).
- **Digging Behavior:** Upon finding a suitable dirt or grassy patch, the pig lowers its snout and vigorously roots through the soil (digging animation, kicking up grass/dirt particles and grunting).
- **Yields:** Successfully foraging digs up subterranean delicacies:
  - **Truffle (`ItemInit.TRUFFLE`):** **60% chance**
  - **Brown Mushroom (`minecraft:brown_mushroom`):** **25% chance**
  - **Red Mushroom (`minecraft:red_mushroom`):** **15% chance**
  Rooting consumes 25% happiness and places truffle hunting on cooldown.
- **Truffle Item:** A rare culinary delicacy. Restores **2 Hunger** and **2.5 Saturation**. Can be eaten directly or cooked into luxurious **Truffle Porkchop**.

---

## 🌳 Flora & Trees

### Baobab Tree, Leaf Stages & Harvesting
- **Description:** Massive, ancient trees with thick composite trunks (2x2 up to 4x4 bases) and lush canopy layers.
- **Sapling:** Can be grown from a **Baobab Sapling** (grows naturally on grass, dirt, and red soil).
- **Full Wood Set:**
  - Baobab Log & Wood (with Stripped variants)
  - Baobab Planks
  - Baobab Stairs, Slabs, Fences, and Fence Gates
  - Baobab Doors, Trapdoors, Buttons, and Pressure Plates
- **Dynamic Leaf Growth Cycle:**
  - **Stage 0 (Baobab Leaves):** Normal lush leaves. Over time, random ticks trigger budding into flowers (1/20 chance per tick).
  - **Stage 1 (Flowering Baobab Leaves):** Leaves adorned with delicate white baobab blossoms. With time, flowers develop into ripe hanging fruits (1/40 chance per tick).
  - **Stage 2 (Fruiting Baobab Leaves):** Mature leaves bearing heavy, ripe baobab fruits ready for picking.
  - **Bone Meal Acceleration:** Applying Bone Meal to leaves in Stage 0 or Stage 1 instantly accelerates them to the next development stage.
  - **Harvesting:** Right-click (PPM) any **Fruiting Baobab Leaves** to harvest a fresh **Baobab Fruit**. Harvesting plays a crisp picking sound and smoothly resets the block back to standard leaves without breaking or decaying the tree canopy.
  - **Dual-Layer Rendering:** Leaf foliage retains full dynamic biome color tinting while the blossoms and fruits keep their authentic, untinted creamy-white and woody-brown tones.

### Dried Acacia
- **Description:** Leafless, weathered acacia skeletons and broken tree stumps unique to the Scorched Savanna.
- **Composition:** Pure acacia logs angled with directional axes, providing wood harvest opportunities even in the most desolate terrain.

### Redthorn
- **Description:** A hardy, spiky red plant growing in irregular clusters on grassy red soil, red soil, laterite, and coarse dirt.
- **Hazard:** Walking through Redthorn slows player and mob movement and deals continuous prick damage (similar to sweet berry bushes). Bees are immune.
- **Placement Rule:** Redthorn cannot grow submerged in water or fluids.

---

## 🍈 Baobab Fruit & Savanna Cuisine

The fruits harvested from baobab canopies can be enjoyed raw or used as the key ingredient in nutritious savanna recipes:

### Baobab Fruit
- **Source:** Harvested by right-clicking mature **Fruiting Baobab Leaves** or breaking fruiting leaf blocks.
- **Nutrition:** Restores **1 Hunger** (half shank) and **0.4 Saturation**. Can be eaten directly or cooked into three nutritious dishes.

### Gui Juice
- **Recipe:** 1x Water Bottle (`minecraft:potion`) + 1x **Baobab Fruit** + 1x **Sugar** (Shapeless).
- **Effects & Nutrition:** Restores **4 Hunger** and **4 Saturation**, granting **Speed I for 15 seconds**.
- **Properties:** Refreshing drinkable beverage. Drinking it returns an empty **Glass Bottle**. Stacks up to 16.

### Monkey Bread
- **Recipe:** 2x **Wheat** + 1x **Baobab Fruit** (Shapeless).
- **Nutrition & Healing:** Restores **5 Hunger** and **7 Saturation**, and immediately **heals 2 HP (1 full heart)** upon consumption!
- **Properties:** A wholesome, fibrous baked loaf celebrated across savanna cultures.

### Baobab Stew
- **Recipe:** 1x **Bowl** + 1x **Baobab Fruit** + 1x **Cooked Beef** (or **Cooked Porkchop**) + 1x **Brown Mushroom** (Shapeless).
- **Nutrition:** Restores **8 Hunger** (4 full shanks) and **9 Saturation**.
- **Properties:** Hearty, savory stew serving as high-tier savanna fuel. Eating it returns an empty **Bowl**. Stacks up to 1 (standard bowl food).

### Truffle Porkchop
- **Recipe:** 1x **Cooked Porkchop** + 1x **Truffle** (Shapeless).
- **Nutrition & Buffs:** Restores **10 Hunger** (5 full shanks) and **14 Saturation**, granting **Haste I for 40 seconds**!
- **Properties:** A gourmet delicacy infused with earthy truffle essence, providing tremendous energy and unmatched mining speed. Can be consumed even when full.

---

## 💧 Fluids & Mud Mechanics

### Mud Pools & Mud Water
- **Mud Water:** A dense, murky brown fluid found in natural savanna mud pools.
- **Mud Water Bucket:** Can be scooped up using an empty bucket.
- **Interaction:** Swimmers move slower through mud water than standard water.

### Mud Balls & Mud Blocks
- **Mud Ball:** Harvested from mud deposits or mud water.
  - Throwable item: can be thrown at entities to deal light damage, knockback, and apply **Slowness for 3 seconds**!
- **Mud Block:** A soft, squishy building block crafted from **4 Mud Balls** in a 2x2 crafting grid.

---

## 🧱 Blocks, Stones & Ores

### Mudstone Collection
A durable, warm sedimentary rock forming the bedrock and rock towers of savannas:
- **Base Variants:** Mudstone, Polished Mudstone, Mudstone Bricks.
- **Architectural Blocks:** Stairs, Slabs, Walls, Doors, Buttons, and Pressure Plates for all styles.
- **Mudstone Ores:**
  - **Mudstone Coal Ore** (yields Coal + XP)
  - **Mudstone Iron Ore** (smeltable into Iron Ingots)
  - **Mudstone Copper Ore** (smeltable into Copper Ingots)
  - **Mudstone Flint Ore** (yields Flint + XP)

### Red Soil Family
- **Red Soil:** Vibrant, iron-rich dirt with custom stairs and slabs. Features a uniform texture on all sides with randomized 90-degree rotations for natural terrain variation.
- **Grassy Red Soil:** Red soil covered with lush savanna turf. Built with dual-layer texturing (un-tinted red soil base with foliage-tinted grass overlay), randomized 90-degree rotations, and horizontal mirroring on sides to prevent repeating grid patterns. Naturally spreads to adjacent red soil blocks under sunlight.

### Laterite Family
- **Laterite:** Hard, porous ferruginous stone that covers the surface of Scorched Savannas. Features a unique top texture (`laterite_top`) with randomized 90-degree rotations, and dedicated side and bottom textures (`laterite_side_bot`).
- **Building Options:** Crafted into Laterite Stairs and Laterite Slabs via crafting table or stonecutter.

### Feather Block
- **Crafting:** Crafted with **9 Feathers** or **Ostrich Feathers** in a 3x3 crafting grid.
- **Fall Damage Negation:** Soft and bouncy—substantially cushions fall damage when landed upon!

### Silted Gravel
- A reddish-brown gravel variety affected by gravity. Drops flint with identical Fortune scaling as vanilla gravel (10% base up to 100% with Fortune III), or drops itself when mined with Silk Touch.

---

## 🏹 Ranged Combat & Archery Overhaul

Ranged combat has been expanded with distinct heavy archery gear, dedicated ammunition classifications, projectile damage adjustments, and precision ballistics.

### Heavy Bow & Heavy Hardened Bow
Engineered for archers demanding high projectile velocity and extended effective range:
- **Heavy Bow:**
  - **Pull Duration:** 25 ticks (1.25s, +0.25s compared to a standard 20-tick bow).
  - **Arrow Velocity:** **1.15x velocity multiplier** (~+4 blocks of projectile range).
  - **Durability:** 250 uses.
  - Features custom dynamic pulling textures and standby states.
- **Heavy Hardened Bow:**
  - **Pull Duration:** 27 ticks (1.35s).
  - **Arrow Velocity:** **1.25x velocity multiplier** (~+6 blocks of projectile range).
  - **Durability:** 320 uses.
  - Reinforced construction built with iron and heat treatment.

### Heavy Arrows & Tipped Variants
- **Heavy Arrow (`ItemInit.HEAVY_ARROW`):** Crafted with Iron Nuggets and Ostrich Feathers.
  - **Damage:** Deals **+1 base damage** over standard arrows (base 3.0 vs vanilla 2.0).
  - Compatible with all standard bows and heavy bows.
- **Tipped Heavy Arrows:** All vanilla potion effects can be applied to Heavy Arrows in the crafting table or Fletching Table.

### Crossbow Overhaul & Bolt Ammunition
Crossbows have been completely rebalanced: **all crossbows now shoot Bolts instead of Arrows**!
- **Bolt (`ItemInit.BOLT`):** The standard ammunition for crossbows. Crafted with flint, sticks, and feathers (or iron nuggets and sticks in the Fletching Table).
- **Tipped Bolts:** All potion effects can be applied to bolts using potions and lingering potions. Features dynamic dual-layer rendering with monochromatic potion fluid overlays (`tipped_bolt.png`).

### Iron & Nether Crossbows
Advanced tiers of crossbow weaponry offering improved stopping power, higher durability, and pinpoint accuracy:
- **Iron Crossbow:**
  - **Damage:** **+2 projectile damage bonus** over the vanilla crossbow.
  - **Loading Duration:** +0.2s (+4 ticks).
  - **Accuracy:** Reduced bolt spread (lower divergence).
  - **Durability:** 600 uses.
- **Nether Crossbow:**
  - **Damage:** **+4 projectile damage bonus** over the vanilla crossbow.
  - **Loading Duration:** +0.4s (+8 ticks).
  - **Accuracy:** Superior flight stabilization and tight clustering.
  - **Durability:** 800 uses.

---

## 🔭 Viewfinder & Optics System

### The Viewfinder
The **Viewfinder (`ItemInit.VIEWFINDER`)** is a precision optic device crafted from an **Amethyst Shard** mounted over a **Gold Ingot** in the center column.
- **Handheld Spyglass:** Can be used directly from the hand via Right Mouse Button (RMB) like a compact spyglass.
- **Magnification:** Provides an optimal **0.35x FOV multiplier** (~3x zoom) paired with an authentic purple-tinted circular scope vignette (`viewfinder_scope.png`).

### Crossbow Scope Attachment & Aiming
All crossbow models—Vanilla Crossbow, Iron Crossbow, and Nether Crossbow—can be upgraded with a Viewfinder attachment:
- **Mounting / Detaching:** Equip or remove the Viewfinder attachment freely inside the [Fletching Table](#-fletching-table-overhaul) (unless bound by the Curse of Welding).
- **Aiming Controls:**
  - When holding a **charged** crossbow equipped with a Viewfinder, press and hold the **Left Mouse Button (Attack Key)** or hold **Sneak (Shift)** to look through the scope!
  - Crossbow scopes project the custom scope overlay and apply 0.35x zoom while keeping the bolt ready to fire.
  - Press RMB while aiming to loose the projectile, or release LMB/Sneak to lower the optic.

### Dynamic Camera Sensitivity Damping
- Looking through a high-magnification optic at full mouse speed leads to overshooting and jittery crosshairs.
- While aiming through a Viewfinder (handheld or crossbow-mounted), camera rotation sensitivity is smoothly scaled down by **0.35x** (`ViewfinderItem.FOV_MULTIPLIER`). This maintains 1:1 focal length angular scaling, making precision aiming feel natural and controlled.

---

## 🎯 Fletching Table Overhaul

The vanilla Fletching Table has been transformed into a fully functional crafting and weapon upgrading station with an interactive GUI and persistent internal inventory:

### Interactive GUI & Persistent Block Storage
- Right-click any Fletching Table to open its dedicated workstation interface.
- **Persistent Storage:** Powered by `FletchingTableBlockEntity`. Items placed into the table remain securely inside even when closing the screen—no accidental inventory spills!

### Weapon Upgrading & Attachment Station
The upper portion of the Fletching Table interface is dedicated to bow and crossbow modifications:
- **Center Slot:** Weapon slot (Bow or Crossbow).
- **Left Slot:** Attachment slot (Viewfinder). Inserting or removing a Viewfinder mounts or unmounts the scope on the weapon.
  - **Item Preservation:** All existing enchantments, durability percentage, and custom anvil names are fully preserved!
- **Right Slot:** Schematic slot. Insert an **Iron Crossbow Scheme**, **Nether Crossbow Scheme**, **Heavy Bow Scheme**, or **Heavy Hardened Bow Scheme** alongside required alloy materials in the lower slots to forge upgraded weapons:
  - **Heavy Bow Upgrade:** Bow + Heavy Bow Scheme + 2x Leather + 2x Slimeball + 2x String.
  - **Heavy Hardened Bow Upgrade:** Heavy Bow + Heavy Hardened Bow Scheme + 2x Iron Ingot + 1x Flint and Steel.
  - **Iron Crossbow Upgrade:** Crossbow + Iron Crossbow Scheme + 3x Iron Ingot + 1x Slimeball + 2x String.
  - **Nether Crossbow Upgrade:** Iron Crossbow + Nether Crossbow Scheme + 3x Gold Ingot + 5x Copper Ingot + 1x Netherite Ingot.
  - Schemes and upgrade materials are consumed upon crafting the weapon.
  - The GUI renders custom grayed-out ghost items guiding the player on required materials.

### Projectile Fletching Station
The lower slots provide quick, batch fletching for ammunition (`Slot A + Slot B + Slot C = Result`):
- **4x Arrows:** Feather + Stick + Flint.
- **4x Heavy Arrows:** Ostrich Feather + Stick + Iron Nugget.
- **4x Bolts:** (Empty) + Stick + Iron Nugget.
- **Tipped Projectiles:** Up to 8 Arrows, Heavy Arrows, or Bolts + any Potion, Splash Potion, or Lingering Potion.

---

## 📜 Schematics, Curses & World Loot

### Bow & Crossbow Schematics
Ancient blueprints required to forge military-grade ranged weapons in the Fletching Table (cannot be crafted, only found in world loot):
- **Heavy Bow Scheme (`ItemInit.HEAVY_BOW_SCHEME`)**
- **Heavy Hardened Bow Scheme (`ItemInit.HEAVY_HARDENED_BOW_SCHEME`)**
- **Iron Crossbow Scheme (`ItemInit.IRON_CROSSBOW_SCHEME`)**
- **Nether Crossbow Scheme (`ItemInit.NETHER_CROSSBOW_SCHEME`)**

### Curse of Welding
- **Curse of Welding (`curse_of_welding`):** A rare weapon curse applicable to crossbows.
- **Mechanics:** Permanently welds any optic attachment to the weapon. While this curse is present, the Viewfinder cannot be removed from the crossbow in the Fletching Table!

### Dungeon & Structure Loot Tables
Explore the world to discover weapon schemes, pre-assembled weapons, and cursed gear:
- **Heavy Bow Scheme:**
  - Village Fletcher Houses: **5% chance**
  - Ruined Portals: **6% chance**
  - Shipwreck Chests: **4.5% chance**
- **Heavy Hardened Bow Scheme:**
  - Woodland Mansions: **8% chance**
  - Village Fletcher Houses: **1.5% chance**
  - Bastion Remnant Chests: **3% chance**
- **Iron Crossbow Scheme:**
  - Stronghold Chests: **5% chance**
  - Abandoned Mineshafts: **2.5% chance**
  - Village Fletcher Houses: **3% chance**
  - Shipwreck Chests: **2% chance**
- **Nether Crossbow Scheme:**
  - Nether Fortress (Bridge) Chests: **5% chance**
  - Bastion Remnant Chests (all types: Treasure, Bridge, Hoglin Stable, Other): **6% chance**
- **Found Crossbow Weapons:**
  - **Shipwrecks & Ruined Portals:**
    - Standard Crossbow: **5% chance** (up to 75% durability; 30% chance for Viewfinder; if scoped, 85% chance of Curse of Welding).
    - Iron Crossbow: **4% chance** (up to 75% durability; 20% chance for Viewfinder; if scoped, 65% chance of Curse of Welding).
  - **Bastion Remnants & Nether Fortresses:**
    - Nether Crossbow: **7% chance** (up to 60% durability; 25% chance for Viewfinder; if scoped, 50% chance of Curse of Welding).

### Fishing Treasures
- **Curse of Welding Enchanted Books** can be reeled in as rare gameplay treasure through fishing across all biomes.

---

## 🔨 Crafting & Stonecutting Recipes

### Notable Crafting Recipes

| Result | Ingredients | Pattern / Layout |
| :--- | :--- | :--- |
| **Viewfinder** | 1x Amethyst Shard + 1x Gold Ingot | Shaped: Shard above Ingot in middle column |
| **Heavy Arrow** (x4) | 1x Iron Nugget + 1x Stick + 1x Ostrich Feather | Shaped: Vertical column (Nugget, Stick, Feather) |
| **Bolt** (x4) | 1x Flint + 1x Stick + 1x Feather / Ostrich Feather | Shaped: Vertical column (Flint, Stick, Feather) |
| **Iron Crossbow** | 4x Iron Ingot + 2x String + 1x Slimeball | Shaped: Iron, String, Slime |
| **Nether Crossbow** | 1x Iron Crossbow + 4x Copper Ingot + 4x Gold Ingot | Shaped: Copper, Gold, Iron Crossbow |
| **Crossbow w/ Viewfinder** | 1x Crossbow + 1x Viewfinder | Shapeless (or via Fletching Table) |
| **Iron Crossbow w/ Viewfinder** | 1x Iron Crossbow + 1x Viewfinder | Shapeless (or via Fletching Table) |
| **Nether Crossbow w/ Viewfinder** | 1x Nether Crossbow + 1x Viewfinder | Shapeless (or via Fletching Table) |
| **Mud Block** | 4x Mud Ball | 2x2 square |
| **Feather Block** | 9x Feather / Ostrich Feather | 3x3 square |
| **Mudstone Bricks** (x4) | 4x Mudstone | 2x2 square |
| **Polished Mudstone** (x4) | 4x Mudstone | 2x2 square |
| **Red Soil Stairs** (x4) | 6x Red Soil | Standard stair shape |
| **Red Soil Slab** (x6) | 3x Red Soil | 3 horizontal line |
| **Laterite Stairs** (x4) | 6x Laterite | Standard stair shape |
| **Laterite Slab** (x6) | 3x Laterite | 3 horizontal line |
| **Baobab Planks** (x4) | 1x Baobab Log | Shapeless (1 log) |
| **Gui Juice** | 1x Water Bottle + 1x Baobab Fruit + 1x Sugar | Shapeless |
| **Monkey Bread** | 2x Wheat + 1x Baobab Fruit | Shapeless |
| **Baobab Stew** | 1x Bowl + 1x Baobab Fruit + 1x Cooked Beef / Cooked Porkchop + 1x Brown Mushroom | Shapeless |
| **Truffle Porkchop** | 1x Cooked Porkchop + 1x Truffle | Shapeless |

### Stonecutter Support
The Stonecutter can be used directly with **Mudstone**, **Mudstone Bricks**, **Polished Mudstone**, and **Laterite** to craft stairs, slabs, and walls with 1:1 ingredient efficiency!

---

## 🗂️ Creative Inventory Tabs

All items and blocks from the mod have been cleanly removed from the default Minecraft tabs and organized into two dedicated mod creative tabs:

- **Vanilla+ (`itemGroup.mod_item_group`):**
  - **Representative Icon:** Mudstone (`Blocks.MUDSTONE_BLOCK`)
  - **Contents:** All blocks, soils, rocks, baobab trees and wood set, leaves, fluids, foods, creature drops, and spawn eggs.
- **Vanilla+ Combat (`itemGroup.mod_combat_group`):**
  - **Representative Icon:** Iron Crossbow (`ItemInit.IRON_CROSSBOW`)
  - **Contents:**
    - **Bows:** Heavy Bow, Heavy Hardened Bow.
    - **Crossbows & Optics:** Viewfinder, Iron Crossbow, Nether Crossbow, and Viewfinder-mounted variants.
    - **Schematics:** Heavy Bow Scheme, Heavy Hardened Bow Scheme, Iron Crossbow Scheme, Nether Crossbow Scheme.
    - **Ammunition:** Heavy Arrows, Crossbow Bolts, and tipped projectile variants.

