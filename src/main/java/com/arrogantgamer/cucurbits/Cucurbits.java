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
 * [] Lava Melon
 *    - seeds, stem, attached stem, fruit
 *    - it searches below for lava and only grows fruit if it can find lava
 *      - how does it search? randomly. :. it gets slower and slower the less laval is below it
 *      - where does it search? for now I am stuck on this 3x3x5 area inspired by grass... but let's see
 *        this encourages planting a patch of them to harvest the magma from a pool, but a
 *        more dynamic shape, like a + or a ♢ encourages more dynamic planting patterns...
 *        (actually yes, lets take inspiration from Melon with its strange growth patterns
 *        this makes for more interesting gameplay)
 *    - it generates around lava lakes? (but in that case, it should be able to search an area
 *      that lets it pull laval from a lava pool even if it is on the edge of the pool...
 *      - in addition, what is it plantable in? Can it grow in sand??? Gravel???
 *        - PROBLEM <- sand and gravel get to decide what can be planted in them :D
 *        - SOLUTION <- I can override the methods in stem that care about canSustainPlant 
 *    - the fruit
 *      - it can be crafted with a bucket to make a lava bucket
 *      - it can be used instead of a lava bucket in recipes
 *        - like... many of the recipes that use lava_bucket return empty bucket
 *          so we'd have to figure that out
 *      - it stacks and has the same burntime as lava
 *      - it has Material.LAVA so maybe the game will treat it weirdly???
 *        - my goal here is to have thermalily treat the fruit as lava
 *      
 * 
 * */
@Mod(Cucurbits.MODID)
public final class Cucurbits {

    public static final String MODID = "cucurbits";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public Cucurbits() {}
}