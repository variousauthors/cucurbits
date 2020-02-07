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
 *  - NOTE for companion planting:
 *    - the companion plants that occupy potential grow spots (on the + ) should
 *      speed up the stem growth so that the rate is the same no matter how many
 *      available spots there are to grow. Like, if 3 spots are covered, then it should
 *      grow 4 times as fast?
 *       - downside is that this makes it less of a trade-off to use those spots...
 *         like, the first 4 upgrades are free, but then if you want more upgrades
 *         you need to pay a cost?
 *       - OK but how about there is a companion that _does_ work this way, so you can
 *         plant it to "direct" the placement of the gourd.
 * 
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
 * 
 * [] Squash
 *    - it checks the drops of a block nearby
 *      - if the drops can be compressed, it breaks the block and stores the drops in the stem
 *        - checking whether there is a storage block for this item...
 *      - once the stem has 9 of the drop, it plants a gourd that contains the compressed block
 *      - it can only store 1 kind of item at a time
 *      - it only tries to break blocks and does not re-plant wheat
 *        - feels like that should be a different gourd?
 *    - need to change corecumbers so that the additional drops are reported by getDrops so that
 *      the squash can pick up on them...
 *    - if a block drops more than one kind of item
 *      - ideally we grab the first compressable one and drop the rest when we remove the target
 *      - failing that we ignore blocks with more than one drops... but then corecumber needs
 *        to be changed to not drop seeds :/
 *      
 *    
 * [] Breeding Gourd
 *    - it gathers "fertility points" from its companion crops
 *      - this harvests and replants the crop? Or maybe it just slowly works.
 *      - if it has appropriate fertility points, it puts nearby animals into love mode
 *        (eg if it has carrot points, it puts pigs into love mode, if it has wheat it puts cows into love mode)
 *      - it only works for animals that are put into love mode by crops
 *      
 * [] Slime Detecting Gourd
 * 
 * */
@Mod(Cucurbits.MODID)
public final class Cucurbits {

    public static final String MODID = "cucurbits";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public Cucurbits() {}
}