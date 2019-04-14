package HollowMod.powers;

import HollowMod.hollowMod;
import HollowMod.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static HollowMod.hollowMod.makePowerPath;

//Gain 1 dex for the turn for each card played.

public class SoulPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = hollowMod.makeID("SoulPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int SoulLimit = 6;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("Soulvial84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("Soulvial32.png"));

    public SoulPower(final AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        if (owner.hasPower(VoidPower.POWER_ID)){
            this.amount = 0;
        } else {
            this.amount = amount;
        }
        this.priority = 0;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }
    public void stackPower(int stackAmount)
    {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount > SoulLimit) {
            this.amount = SoulLimit;
        }
        if((this.amount == 0) || (AbstractDungeon.player.hasPower(VoidPower.POWER_ID))) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, SoulPower.POWER_ID));
        }
    }

    // On use card, apply (amount) of Dexterity. (Go to the actual power card for the amount.)

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        this.description = (DESCRIPTIONS[0] + SoulLimit + DESCRIPTIONS[1]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new SoulPower(owner, amount);
    }
}
