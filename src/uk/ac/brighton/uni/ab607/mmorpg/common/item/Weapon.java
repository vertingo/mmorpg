package uk.ac.brighton.uni.ab607.mmorpg.common.item;

import java.util.Arrays;

import uk.ac.brighton.uni.ab607.libs.parsing.PseudoHTML;
import uk.ac.brighton.uni.ab607.mmorpg.common.Player;
import uk.ac.brighton.uni.ab607.mmorpg.common.Stat;
import uk.ac.brighton.uni.ab607.mmorpg.common.combat.Element;

public class Weapon extends EquippableItem implements PseudoHTML {

    /**
     * 
     */
    private static final long serialVersionUID = 2639185454495196264L;

    // TODO: do weapons have aspd reduction? or if 2 weapons are equipped ?
    public enum WeaponType {
        ONE_H_SWORD, ONE_H_AXE, DAGGER, SPEAR, MACE, ROD, SHIELD,   // 1H, shield only left-hand
        TWO_H_SWORD, TWO_H_AXE, KATAR, BOW    // 2H
    }

    private static int uniqueWeaponID = 4000;

    public final WeaponType type;
    public final int pureDamage;

    /*package-private*/ Weapon(String name, String description, int ssX, int ssY, String author,
            ItemLevel level, WeaponType type, int pureDamage, Element element, int runesMax, Rune... defaultRunes) {
        super(""+uniqueWeaponID++, name, description, ssX, ssY, author, level, element, runesMax, defaultRunes);
        this.type = type;
        this.pureDamage = pureDamage;
    }

    /*package-private*/ Weapon(Weapon copy) {
        super(copy.id, copy.name, copy.description, copy.ssX, copy.ssY, copy.author, copy.level, copy.element, copy.runesMax, copy.defaultRunes);
        this.type = copy.type;
        this.pureDamage = copy.pureDamage;
    }

    /*package-private*/ Weapon(String name, String description, int ssX, int ssY, WeaponType type, int pureDamage) {
        super(""+uniqueWeaponID++, name, description, ssX, ssY);
        this.type = type;
        this.pureDamage = pureDamage;
    }

    @Override
    public void onEquip(Player ch) {
        super.onEquip(ch);
        ch.addBonusStat(Stat.ATK, getDamage());
    }

    @Override
    public void onUnEquip(Player ch) {
        super.onUnEquip(ch);
        ch.addBonusStat(Stat.ATK, -getDamage());
    }

    public int getDamage() {
        return pureDamage + refineLevel * (refineLevel > 2 ? level.bonus + 5 : level.bonus);
    }

    @Override
    public String toString() {
        return "ID: " + id + "\n"
                + (refineLevel == 0 ? "" : "+" + refineLevel + " ") + name + "(" + runesMax + ")" + "\n"
                + description + "\n"
                + "Author: " + author + "\n"
                + "Stats:" + "\n"
                + "Type: " + type + "\n"
                + "Damage: " + pureDamage + " + " + refineLevel * level.bonus + "\n"
                + "Element: " + element + "\n"
                + Arrays.toString(defaultRunes) + "\n"
                + "Runes installed: " + runes.toString() + "\n"
                + "Essence installed: " + (essence == null ? "NONE" : essence);
    }

    public String toStringShort() {
        return "ID: " + id + "\n"
                + (refineLevel == 0 ? "" : "+" + refineLevel + " ") + name + "(" + runesMax + ")" + "\n"
                //+ description + "\n"
                + "Author: " + author + "\n"
                + "Stats:" + "\n"
                + "Type: " + type + "\n"
                + "Damage: " + pureDamage + " + " + refineLevel * level.bonus + "\n"
                + "Element: " + element + "\n"
                + Arrays.toString(defaultRunes) + "\n"
                + runes.toString() + "\n"
                + (essence == null ? "" : essence.name);
    }

    @Override
    public String toPseudoHTML() {
        return HTML_START
                + "<center>" + BLUE + (refineLevel == 0 ? "" : "+" + refineLevel + " ") + name + FONT_END + " (" + runesMax + ")" + "</center>"
                + description + BR
                + "Author: " + RED + author + FONT_END + BR
                + "Type: " + BLUE + type + FONT_END + BR
                + "Damage: " + BLUE + pureDamage + " + " + refineLevel * (refineLevel > 2 ? level.bonus + 5 : level.bonus) + FONT_END + BR
                + "Element: " + BLUE + element + FONT_END + BR
                + GREEN + Arrays.toString(defaultRunes) + FONT_END + BR
                + "Runes: " + BR
                + GREEN + runes.toString() + FONT_END + BR
                + "Essence: " + BR
                + (essence == null ? "NONE" : essence.toStringHTML());
    }

    @Override
    public String toPseudoHTMLShort() {
        return HTML_START
                + "<center>" + BLUE + (refineLevel == 0 ? "" : "+" + refineLevel + " ") + name + FONT_END + " (" + runesMax + ")" + "</center>"
                + "Type: " + BLUE + type + FONT_END + BR
                + "Damage: " + BLUE + pureDamage + " + " + refineLevel * (refineLevel > 2 ? level.bonus + 5 : level.bonus) + FONT_END + BR
                + "Element: " + BLUE + element + FONT_END + BR
                + GREEN + Arrays.toString(defaultRunes) + FONT_END + BR
                + "Runes: " + BR
                + GREEN + runes.toString() + FONT_END + BR
                + "Essence: " + BR
                + (essence == null ? "NONE" : essence.toStringHTML());
    }
}