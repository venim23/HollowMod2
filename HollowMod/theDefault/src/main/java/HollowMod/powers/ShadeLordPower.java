package HollowMod.powers;

import HollowMod.DefaultMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThornsPower;

import static HollowMod.DefaultMod.makePowerPath;

//Add X to the level of Void Power, Void will add vulnerability and

public class ShadeLordPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = DefaultMod.makeID("ShadesPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("voidking_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("voidking_power32.png"));

    public ShadeLordPower(final AbstractCreature owner, final int amount) {
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
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, ShadeLordPower.POWER_ID));
        }
    }
    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if ((c.block > 0)){

            int blockreduced = Math.round(c.block/2);

            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new ThornsPower(owner, blockreduced), blockreduced));
            c.block = (c.block - blockreduced);
            //AbstractDungeon.actionManager.addToBottom(new LoseBlockAction(owner, owner, blockreduced));

        }

    }
    @Override
    public void atStartOfTurn() {
        if (this.owner.getPower(ThornsPower.POWER_ID).amount > 0) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, ThornsPower.POWER_ID, Math.round(this.owner.getPower(ThornsPower.POWER_ID).amount / 2)));
        }
    }

    public void atEndOfTurn(final boolean isPlayer){
        if ( this.amount > 0) {
            AbstractDungeon.actionManager.addToBottom(
                    new ReducePowerAction(owner, owner, ShadeLordPower.POWER_ID, 1));
        }


        }


    @Override
    public void updateDescription() {
        this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new ShadeLordPower(owner, amount);
    }
}
