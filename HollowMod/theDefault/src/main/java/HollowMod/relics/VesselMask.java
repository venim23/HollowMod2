package HollowMod.relics;

import HollowMod.DefaultMod;
import HollowMod.patches.CardTagEnum;
import HollowMod.powers.SoulPower;
import HollowMod.powers.VoidPower;
import HollowMod.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.cards.DamageInfo;


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
    public static int nailGain = 1;


    public VesselMask() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
    }


    // Gain 1 Strength on on equip.
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {

        if ((damageAmount > 0) && (!AbstractDungeon.player.hasPower(VoidPower.POWER_ID)) && (info.type.equals(DamageInfo.DamageType.NORMAL))) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SoulPower(AbstractDungeon.player, nailGain), nailGain));
        }
        //this should generate 1 soul on every attack that deals damage to an opponent, as long as the player doesn't have the Void Power.
    }
    // Description
    @Override
    public String getUpdatedDescription() {

        return DESCRIPTIONS[0];
    }

}
