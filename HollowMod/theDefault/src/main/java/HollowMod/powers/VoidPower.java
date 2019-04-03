package HollowMod.powers;

import HollowMod.DefaultMod;
import HollowMod.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static HollowMod.DefaultMod.makePowerPath;

//Add X to the level of Void Power, Void will add vulnerability and

public class VoidPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = DefaultMod.makeID("VoidPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int VOID_LEVEL = 0;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("VoidGod_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("VoidGod_power32.png"));

    public VoidPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = true;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void stackPower(int stackAmount)
    {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, VoidPower.POWER_ID));
        }
        if (this.amount > 0){
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, SoulPower.POWER_ID));
        }
    }
    @Override
    public void atStartOfTurn() {
        if (this.amount > 0) {
            VOID_LEVEL = this.amount;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new VulnerablePower(owner,1, false)));
        }

    }

    // Note: If you want to apply an effect when a power is being applied you have 3 options:
    //onInitialApplication is "When THIS power is first applied for the very first time only."
    //onApplyPower is "When the owner applies a power to something else (only used by Sadistic Nature)."
    //onReceivePowerPower from StSlib is "When any (including this) power is applied to the owner."


    // At the end of the turn, remove gained Dexterity.
    @Override
    public void atEndOfTurn(final boolean isPlayer) {


        if ( this.amount > 0) {
            flash(); // Makes the power icon flash.
                AbstractDungeon.actionManager.addToBottom(
                        new ReducePowerAction(owner, owner, VoidPower.POWER_ID, 1));
                // this should reduce the void power by 1 at the e3nd of each turn.


                // Reduce the power by 1 for each count - i.e. for each card played this turn.
                // DO NOT HARDCODE YOUR STRINGS ANYWHERE: i.e. don't write any Strings directly i.e. "Dexterity" for the power ID above.
                // Use the power/card/relic etc. and fetch it's ID like shown above. It's really bad practice to have "Strings" in your code:

                /*
                 * 1. It's bad for if somebody likes your mod enough (or if you decide) to translate it.
                 * Having only the JSON files for translation rather than 15 different instances of "Dexterity" in some random cards is A LOT easier.
                 *
                 * 2. You don't have a centralised file for all strings for easy proof-reading, and if you ever want to change a string
                 * you now have to go through all your files individually.
                 *
                 * 3. Without hardcoded strings, editing a string doesn't require a compile, saving you time (unless you clean+package).
                 *
                 */
        }

    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new VoidPower(owner, amount);
    }
}
