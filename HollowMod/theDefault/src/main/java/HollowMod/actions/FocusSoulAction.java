package HollowMod.actions;

import HollowMod.powers.CommonPower;
import HollowMod.powers.SoulPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FocusSoulAction extends AbstractGameAction {
    private int magicNumber;
    private AbstractPlayer p;
    private int soulCost;

    public FocusSoulAction(AbstractPlayer p, int cost)
    {
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.source = p;
        this.soulCost = cost;
    }

    public boolean canPayFocus()
    {
        boolean canUse = false;
        if (((this.source.hasPower("SoulPower")) && (this.source.getPower("SoulPower").amount >= this.soulCost))){
            canUse = true;
        }
        return canUse;
    }

    @Override
    public void update() {
        if (canPayFocus())
        {
            this.source.getPower("SoulPower").reducePower(this.soulCost);
            this.source.getPower("SoulPower").updateDescription();

        }
        isDone = true;
}



            //for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                //updateCard(c);
            }
            //for (AbstractCard c : AbstractDungeon.player.hand.group) {
               // updateCard(c);

            //for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                //updateCard(c);


