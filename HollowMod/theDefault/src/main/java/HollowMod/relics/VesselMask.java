package HollowMod.relics;

import HollowMod.DefaultMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.SoulPower;
import HollowMod.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static HollowMod.DefaultMod.makeRelicOutlinePath;
import static HollowMod.DefaultMod.makeRelicPath;

public class VesselMask extends CustomRelic {
    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * At the start of each combat, gain 1 Strength (i.e. Vajra)
     */

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("VesselMask");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("mask.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("mask.png"));

    public VesselMask() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
    }


    // Gain 1 Strength on on equip.
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        int nailgain =1;
        if(card.hasTag(CardTagEnum.NAIL))  {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SoulPower(AbstractDungeon.player, nailgain) ));

        }
    }
    // Description
    @Override
    public String getUpdatedDescription() {

        return DESCRIPTIONS[0];
    }

}
