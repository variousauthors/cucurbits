package com.arrogantgamer.cucurbits;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

/*
 * NEXTSTEPS
 * =========
 * 
 * v0.1.0 
 * 
 * Prototype:
 *  - implement the stems and fruit, and basic functionality
 *  - just make them different colors
 *  
 * [] Lavamelon
 * [] Corecumber
 *    - it searches below it in a cross shape for ores, and makes fruit
 *    - it leaves "porous stone" when it pulls out ores (for now just do mossy cobblestone)
 *    - the fruit, when broken, drop the drops from the ore
 *    - how do we store the loot in the fruit!
 *      - you should not be able to extract/insert items in any way
 * 
 * */
@Mod(Cucurbits.MODID)
public final class Cucurbits {

    public static final String MODID = "cucurbits";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public Cucurbits() {}
}