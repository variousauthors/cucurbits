package com.arrogantgamer.cucurbits;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

/*
 * NEXTSTEPS
 * =========
 * 
 * v1.0.0
 *  - configurable localization, so you can have either "lavamelon, corecumber, squash" or "lavamelon, corecumber, true squash"
 *    like "namespace collision friendly mode"
 * 
 * v0.1.0 
 * 
 * Prototype:
 *  - implement the stems and fruit, and basic functionality
 *  - just make them different colors
 *  - null pointer exception when you place a corecumber or squash,
 *    - it should get an empty item stack as its container item
 *  - stems that break blocks for fuel should skip tile entities (except other gourds)
 *    - needs a tag called "cucurbits"
 *  - stems need drops too
 *      
 * [] Lavamelon
 * [] Corecumber
 * [] Compression Gourd
 *    - it checks the drops of a block nearby
 *      - if the drops can be compressed, it breaks the block and stores the drops in the stem
 *        - checking whether there is a storage block for this item...
 *      - once the stem has 9 of the drop, it plants a gourd that contains the compressed block
 *      - it can only store 1 kind of item at a time
 *      - it only tries to break blocks and does not re-plant wheat
 *        - feels like that should be a different gourd?
 * [] Breeding Gourd
 *    - it gathers "fertility points" from its companion crops
 *      - this harvests and replants the crop? Or maybe it just slowly works.
 *      - if it has appropriate fertility points, it puts nearby animals into love mode
 *        (eg if it has carrot points, it puts pigs into love mode, if it has wheat it puts cows into love mode)
 *      - it only works for animals that are put into love mode by crops
 * [] Slime Detecting Gourd
 *    - it needs a use beyond just detecting slimes... 
 *      but it shouldn't be a source of slime
 *    - make it bouncy like a slime block, but not sticky
 *    - make it generate mana when something bounces off of it

 * 
 * */
@Mod(Cucurbits.MODID)
public final class Cucurbits {

    public static final String MODID = "cucurbits";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public Cucurbits() {}
}