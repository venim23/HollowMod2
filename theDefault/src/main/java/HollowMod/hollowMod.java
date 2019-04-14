package HollowMod;

import HollowMod.relics.*;
import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import HollowMod.cards.*;
import HollowMod.characters.TheBugKnight;
import HollowMod.potions.PlaceholderPotion;
import HollowMod.util.IDCheckDontTouchPls;
import HollowMod.util.TextureLoader;
import HollowMod.variables.DefaultCustomVariable;
import HollowMod.variables.DefaultSecondMagicNumber;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


//TODO: FIRST THINGS FIRST: RENAME YOUR PACKAGE AND ID NAMES FIRST-THING!!!
// Right click the package (Open the project pane on the left. Folder with black dot on it. The name's at the very top) -> Refactor -> Rename, and name it whatever you wanna call your mod.
// Scroll down in this file. Change the ID from "theDefault:" to "yourModName:" or whatever your heart desires (don't use spaces). Dw, you'll see it.
// In the JSON strings (resources>localization>eng>[all them files] make sure they all go "yourModName:" rather than "theDefault". You can ctrl+R to replace in 1 file, or ctrl+shift+r to mass replace in specific files/directories (Be careful.).
// Start with the DefaultCommon cards - they are the most commented cards since I don't feel it's necessary to put identical comments on every card.
// After you sorta get the hang of how to make cards, check out the card template which will make your life easier

/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 */

@SpireInitializer
public class hollowMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber {
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(hollowMod.class.getName());
    private static String modID;

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Hollow Mod";
    private static final String AUTHOR = "Venim"; // And pretty soon - You!
    private static final String DESCRIPTION = "A mod that attempts to add the Knight from Hollow Knight as a Playable Character, All credit to Hollow Knight goes to Team Cherry. Created using the DefaultMod Template - credit to Gremious";

    // =============== INPUT TEXTURE LOCATION =================

    // Colors (RGB)
    // Character Color
    public static final Color BUGKNIGHT_GRAY = CardHelper.getColor(26.0f, 30.0f, 35.0f);
    public static final Color BUGKNIGHT_PALE = CardHelper.getColor(213.0f, 212.0f, 212.0f);

    // Potion Colors in RGB
    public static final Color PLACEHOLDER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f); // Orange-ish Red
    public static final Color PLACEHOLDER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f); // Near White
    public static final Color PLACEHOLDER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f); // Super Dark Red/Brown

    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_BUGKNIGHT_GRAY = "HollowModResources/images/512/bg_attack_BK_s.png";
    private static final String SKILL_BUGKNIGHT_GRAY = "HollowModResources/images/512/bg_skill_BK_s.png";
    private static final String POWER_BUGKNIGHT_GRAY = "HollowModResources/images/512/bg_power_BK_s.png";

    private static final String ENERGY_ORB_BUGKNIGHT_GRAY = "HollowModResources/images/512/card_orb.png";
    private static final String CARD_ENERGY_ORB = "HollowModResources/images/512/card_energy_orb.png";

    private static final String ATTACK_BUGKNIGHT_GRAY_PORTRAIT = "HollowModResources/images/1024/bg_attack_BK.png";
    private static final String SKILL_BUGKNIGHT_GRAY_PORTRAIT = "HollowModResources/images/1024/bg_skill_BK.png";
    private static final String POWER_BUGKNIGHT_GRAY_PORTRAIT = "HollowModResources/images/1024/bg_power_BK.png";
    private static final String ENERGY_ORB_BUGKNIGHT_GRAY_PORTRAIT = "HollowModResources/images/1024/card_orb.png";

    // Character assets
    private static final String THE_BUGKNIGHT_BUTTON = "HollowModResources/images/charSelect/BugKnightButton.png";
    private static final String THE_BUGKNIGHT_PORTRAIT = "HollowModResources/images/charSelect/BugKnightPortrait.png";
    public static final String THE_BUGKNIGHT_SHOULDER_1 = "HollowModResources/images/char/BugKnight/shoulder.png";
    public static final String THE_BUGKNIGHT_SHOULDER_2 = "HollowModResources/images/char/BugKnight/shoulder2.png";
    public static final String THE_BUGKNIGHT_CORPSE = "HollowModResources/images/char/BugKnight/corpse.png";

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "HollowModResources/images/BadgeBug.png";

    // Atlas and JSON files for the Animations
    public static final String THE_BUGKNIGHT_SKELETON_ATLAS = "HollowModResources/images/char/defaultCharacter/skeleton.atlas";
    public static final String THE_BUGKNIGHT_SKELETON_JSON = "HollowModResources/images/char/defaultCharacter/skeleton.json";

    // =============== MAKE IMAGE PATHS =================

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/images/orbs/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    // =============== /MAKE IMAGE PATHS/ =================

    // =============== /INPUT TEXTURE LOCATION/ =================


    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================

    public hollowMod() {
        logger.info("Subscribe to BaseMod hooks");

        BaseMod.subscribe(this);

        // CHANGE YOUR MOD ID HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // CHANGE YOUR MOD ID HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // CHANGE YOUR MOD ID HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // CHANGE YOUR MOD ID HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // CHANGE YOUR MOD ID HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // CHANGE YOUR MOD ID HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // CHANGE YOUR MOD ID HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        setModID("HollowMod");
        // Now go to your resources folder in the project panel, and refactor> rename theDefaultResources to
        // yourModIDResources.
        // Also click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project)
        // replace all instances of theDefault with yourModID.
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod id. Not mine.
        // FINALLY and most importnatly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than theDefault. They get loaded before getID is a thing.
        logger.info("Done subscribing");

        logger.info("Creating the color " + TheBugKnight.Enums.HOLLOW_COLOR.toString());

        BaseMod.addColor(TheBugKnight.Enums.HOLLOW_COLOR, BUGKNIGHT_GRAY, BUGKNIGHT_GRAY, BUGKNIGHT_GRAY,
                BUGKNIGHT_GRAY, BUGKNIGHT_GRAY, BUGKNIGHT_GRAY, BUGKNIGHT_GRAY,
                ATTACK_BUGKNIGHT_GRAY, SKILL_BUGKNIGHT_GRAY, POWER_BUGKNIGHT_GRAY, ENERGY_ORB_BUGKNIGHT_GRAY,
                ATTACK_BUGKNIGHT_GRAY_PORTRAIT, SKILL_BUGKNIGHT_GRAY_PORTRAIT, POWER_BUGKNIGHT_GRAY_PORTRAIT,
                ENERGY_ORB_BUGKNIGHT_GRAY_PORTRAIT, CARD_ENERGY_ORB);

        logger.info("Done creating the color");
    }

    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP

    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStrings.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = hollowMod.class.getResourceAsStream("/IDCheckStrings.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT

        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
    } // NO

    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH

    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NNOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStrings.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = hollowMod.class.getResourceAsStream("/IDCheckStrings.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT

        String packageName = hollowMod.class.getPackage().getName(); // STILL NOT EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO
    // ====== YOU CAN EDIT AGAIN ======


    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("========================= Initializing Default Mod. Hi. =========================");
        hollowMod defaultmod = new hollowMod();
        logger.info("========================= /Default Mod Initialized. Hello World./ =========================");
    }

    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================


    // =============== LOAD THE CHARACTER =================

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + TheBugKnight.Enums.THE_BUGKNIGHT.toString());

        BaseMod.addCharacter(new TheBugKnight("the Bug Knight", TheBugKnight.Enums.THE_BUGKNIGHT),
                THE_BUGKNIGHT_BUTTON, THE_BUGKNIGHT_PORTRAIT, TheBugKnight.Enums.THE_BUGKNIGHT);

        receiveEditPotions();
        logger.info("Added " + TheBugKnight.Enums.THE_BUGKNIGHT.toString());
    }

    // =============== /LOAD THE CHARACTER/ =================


    // =============== POST-INITIALIZE =================


    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        settingsPanel.addUIElement(new ModLabel("DefaultMod doesn't have any settings! An example of those may come later.", 400.0f, 700.0f,
                settingsPanel, (me) -> {
        }));
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        // =============== EVENTS =================

        // This event will be exclusive to the City (act 2). If you want an event that's present at any
        // part of the game, simply don't include the dungeon ID
        // If you want to have a character-specific event, look at slimebound (CityRemoveEventPatch).
        // Essentially, you need to patch the game and say "if a player is not playing my character class, remove the event from the pool"

        // BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);

        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");

    }

    // =============== / POST-INITIALIZE/ =================


    // ================ ADD POTIONS ===================


    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.THE_DEFAULT".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
        BaseMod.addPotion(PlaceholderPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, PlaceholderPotion.POTION_ID, TheBugKnight.Enums.THE_BUGKNIGHT);

        logger.info("Done editing potions");
    }

    // ================ /ADD POTIONS/ ===================


    // ================ ADD RELICS ===================

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new PlaceholderRelic(), TheBugKnight.Enums.HOLLOW_COLOR);
        //BaseMod.addRelicToCustomPool(new BottledPlaceholderRelic(), TheDefault.Enums.HOLLOW_COLOR);
        //BaseMod.addRelicToCustomPool(new DefaultClickableRelic(), TheDefault.Enums.HOLLOW_COLOR);
        BaseMod.addRelicToCustomPool(new VesselMask(), TheBugKnight.Enums.HOLLOW_COLOR);


        // This adds a relic to the Shared pool. Every character can find this relic.
        BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);

        // Mark relics as seen (the others are all starters so they're marked as seen in the character file
        UnlockTracker.markRelicAsSeen(BottledPlaceholderRelic.ID);
        logger.info("Done adding relics!");
    }

    // ================ /ADD RELICS/ ===================


    // ================ ADD CARDS ===================

    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variabls");
        // Add the Custom Dynamic variabls
        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());

        logger.info("Adding cards");
        // Add the cards
        // Don't comment out/delete these cards (yet). You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.


        //Starters
        BaseMod.addCard(new skillQuickDash_s());
        BaseMod.addCard(new attackNailStrike_s());
        BaseMod.addCard(new skillFocusHeal_s());



        //attacks
        BaseMod.addCard(new attackCycloneSlash());
        BaseMod.addCard(new attackCoiledNail());
        BaseMod.addCard(new attackDashSlash());
        BaseMod.addCard(new attackDescendingDark());
        BaseMod.addCard(new attackHornetsHelp());
        BaseMod.addCard(new attackInfectedAttack());
        BaseMod.addCard(new attackQuirrelsAssistance());
        BaseMod.addCard(new attackSoulStrike());
        BaseMod.addCard(new skillSoulTotem());
        BaseMod.addCard(new attackWraithStrike());
        BaseMod.addCard(new attackVengefulVoid());
        BaseMod.addCard(new attackZotesMagnificence());
        BaseMod.addCard(new attackRecoilStrike());
        BaseMod.addCard(new attackFastStrike());
        BaseMod.addCard(new attackInfectedCysts());
        BaseMod.addCard(new attackPogoStrike());
        BaseMod.addCard(new attackAbyssShriek());
        BaseMod.addCard(new attackInfectionAssault());
        BaseMod.addCard(new attackSharpenedNail());
        BaseMod.addCard(new attackGreatSlash());
        BaseMod.addCard(new attackFuryoftheFallen());



        //Skills
        BaseMod.addCard(new skillCloakDash());
        BaseMod.addCard(new skillConfessorsAdvice());
        //BaseMod.addCard(new skillCornifersMap());
        BaseMod.addCard(new skillDoubleDash());
        BaseMod.addCard(new skillMantisMark());
        BaseMod.addCard(new skillMawleksShell());
        BaseMod.addCard(new skillSoulSplash());
        BaseMod.addCard(new skillTheNailsmith());
        BaseMod.addCard(new skillLifebloodCocoon());
        BaseMod.addCard(new skillStalwartShell());
        BaseMod.addCard(new skillDungDefenderAura());
        BaseMod.addCard(new skillGrimmsGift());
        BaseMod.addCard(new skillRadiancesLament());
        BaseMod.addCard(new skillCrystalDash());
        BaseMod.addCard(new skillHuntersJournal());
        BaseMod.addCard(new skillVoidDash());
        BaseMod.addCard(new skillLifebloodCore());
        BaseMod.addCard(new skillHiveblood());
        BaseMod.addCard(new skillLifebloodHeart());
        BaseMod.addCard(new skillPerfectDash());
        BaseMod.addCard(new skillSiblingsSouls());
        BaseMod.addCard(new skillMonarchWings());

        //Powers
        BaseMod.addCard(new powerElderbugsWisdom());
        BaseMod.addCard(new powerSharpenedShadows());
        BaseMod.addCard(new powerSlysStrikes());
        BaseMod.addCard(new powerWeaversong());
        BaseMod.addCard(new powerBrokenVessel());
        BaseMod.addCard(new powerMothsMistake());
        BaseMod.addCard(new powerGrubsong());
        BaseMod.addCard(new powerThornsofAgony());
        BaseMod.addCard(new powerShapeofUnn());
        //BaseMod.addCard(new powerLordofShades());


        //Deprecated
        //BaseMod.addCard(new OrbSkill());





        logger.info("Making sure the cards are unlocked.");
        // Unlock the cards
        // This is so that they are all "seen" in the library, for people who like to look at the card list
        // before playing your mod.

        //starter
        UnlockTracker.unlockCard(attackNailStrike_s.ID);
        UnlockTracker.unlockCard(skillQuickDash_s.ID);
        UnlockTracker.unlockCard(skillFocusHeal_s.ID);

        //Attacks
        UnlockTracker.unlockCard(attackCoiledNail.ID);
        UnlockTracker.unlockCard(attackCycloneSlash.ID);
        UnlockTracker.unlockCard(attackDashSlash.ID);
        UnlockTracker.unlockCard(attackDescendingDark.ID);
        UnlockTracker.unlockCard(attackHornetsHelp.ID);
        UnlockTracker.unlockCard(attackInfectedAttack.ID);
        UnlockTracker.unlockCard(attackQuirrelsAssistance.ID);
        UnlockTracker.unlockCard(attackSoulStrike.ID);
        UnlockTracker.unlockCard(attackVengefulVoid.ID);
        UnlockTracker.unlockCard(attackWraithStrike.ID);
        UnlockTracker.unlockCard(attackZotesMagnificence.ID);
        UnlockTracker.unlockCard(attackRecoilStrike.ID);
        UnlockTracker.unlockCard(attackFastStrike.ID);
        UnlockTracker.unlockCard(attackInfectedCysts.ID);
        UnlockTracker.unlockCard(attackPogoStrike.ID);
        UnlockTracker.unlockCard(attackAbyssShriek.ID);
        UnlockTracker.unlockCard(attackInfectionAssault.ID);
        UnlockTracker.unlockCard(attackSharpenedNail.ID);
        UnlockTracker.unlockCard(attackGreatSlash.ID);
        UnlockTracker.unlockCard(attackFuryoftheFallen.ID);

        //Skills
        UnlockTracker.unlockCard(skillCloakDash.ID);
        UnlockTracker.unlockCard(skillConfessorsAdvice.ID);
        UnlockTracker.unlockCard(skillDoubleDash.ID);
        //UnlockTracker.unlockCard(skillCornifersMap.ID);
        UnlockTracker.unlockCard(skillMantisMark.ID);
        UnlockTracker.unlockCard(skillMawleksShell.ID);
        UnlockTracker.unlockCard(skillSoulSplash.ID);
        UnlockTracker.unlockCard(skillSoulTotem.ID);
        UnlockTracker.unlockCard(skillTheNailsmith.ID);
        UnlockTracker.unlockCard(skillLifebloodCocoon.ID);
        UnlockTracker.unlockCard(skillStalwartShell.ID);
        UnlockTracker.unlockCard(skillDungDefenderAura.ID);
        UnlockTracker.unlockCard(skillGrimmsGift.ID);
        UnlockTracker.unlockCard(skillRadiancesLament.ID);
        UnlockTracker.unlockCard(skillCrystalDash.ID);
        UnlockTracker.unlockCard(skillHuntersJournal.ID);
        UnlockTracker.unlockCard(skillVoidDash.ID);
        UnlockTracker.unlockCard(skillLifebloodCore.ID);
        UnlockTracker.unlockCard(skillHiveblood.ID);
        UnlockTracker.unlockCard(skillPerfectDash.ID);
        UnlockTracker.unlockCard(skillLifebloodHeart.ID);
        UnlockTracker.unlockCard(skillMonarchWings.ID);
        UnlockTracker.unlockCard(skillSiblingsSouls.ID);
        // Powers

        UnlockTracker.unlockCard(powerSharpenedShadows.ID);
        //UnlockTracker.unlockCard(powerLordofShades.ID);
        UnlockTracker.unlockCard(powerElderbugsWisdom.ID);
        UnlockTracker.unlockCard(powerSlysStrikes.ID);
        UnlockTracker.unlockCard(powerWeaversong.ID);
        UnlockTracker.unlockCard(powerBrokenVessel.ID);
        UnlockTracker.unlockCard(powerMothsMistake.ID);
        UnlockTracker.unlockCard(powerGrubsong.ID);
        UnlockTracker.unlockCard(powerThornsofAgony.ID);
        UnlockTracker.unlockCard(powerShapeofUnn.ID);
        //Deprecated

        //UnlockTracker.unlockCard(OrbSkill.ID);










        logger.info("Done adding cards!");
    }

    // There are better ways to do this than listing every single individual card, but I do not want to complicate things
    // in a "tutorial" mod. This will do and it's completely ok to use. If you ever want to clean up and
    // shorten all the imports, go look take a look at other mods, such as Hubris.

    // ================ /ADD CARDS/ ===================


    // ================ LOAD THE TEXT ===================

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings");

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/HollowMod-Card-Strings.json");

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/HollowMod-Power-Strings.json");

        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/HollowMod-Relic-Strings.json");

        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/eng/HollowMod-Event-Strings.json");

        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/eng/HollowMod-Potion-Strings.json");

        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/eng/HollowMod-Character-Strings.json");

        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/eng/HollowMod-Orb-Strings.json");

        logger.info("Done edittting strings");
    }

    // ================ /LOAD THE TEXT/ ===================

    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword

        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/HollowMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }

    // ================ /LOAD THE KEYWORDS/ ===================    

    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

}
