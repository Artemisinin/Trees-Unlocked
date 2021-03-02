This mod is for Minecraft snapshot 21w08b (1.17 snapshot) and requires Fabric loader and Fabric API 0.31.1+1.17 in the mod directory.  Fabric can be downloaded at https://fabricmc.net/.

This is a simple demo mod that introduces a modified version of the tree feature that takes the normal tree configuration, but can be placed on almost every type of block. Allowable blocks are listed in a block tag, valid_ground_blocks.json, which can be edited to control what blocks the modified feature is placed on.

To demonstrate the modified tree feature, this mod includes an overworld with sponge-like "trees" added to the seafloor, a swamp biome bush that can grow in 1-2 block deep water, and a cave dimension with cave trees added, which can be accessed using the teleport command. The cave tree configured features are defined in json so that those interested in using datapacks can see how the modified tree feature is referenced.

The maxWaterDepth variable in the tree configuration is now used to control the depth of water that the modified tree feature can generate in. Vanilla trees are not affected.  To allow trees to grow on the sea floor, set this to -1.  Setting it to 0 will require placement on dry ground, and setting it to a value 1 or greater will place the tree only if the water over the placement block does not exceed that depth.

If you use this tree feature in your mod or datapack, an acknowledgment and link back to this mod would be appreciated.