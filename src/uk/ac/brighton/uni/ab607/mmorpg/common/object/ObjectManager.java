package uk.ac.brighton.uni.ab607.mmorpg.common.object;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import uk.ac.brighton.uni.ab607.mmorpg.common.Attribute;
import uk.ac.brighton.uni.ab607.mmorpg.common.Attribute.AttributeInfo;
import uk.ac.brighton.uni.ab607.mmorpg.common.Effect;
import uk.ac.brighton.uni.ab607.mmorpg.common.GameCharacter;
import uk.ac.brighton.uni.ab607.mmorpg.common.GameCharacter.Experience;
import uk.ac.brighton.uni.ab607.mmorpg.common.GameMath;
import uk.ac.brighton.uni.ab607.mmorpg.common.Player;
import uk.ac.brighton.uni.ab607.mmorpg.common.Rune;
import uk.ac.brighton.uni.ab607.mmorpg.common.GameCharacter.Stat;
import uk.ac.brighton.uni.ab607.mmorpg.common.StatusEffect;
import uk.ac.brighton.uni.ab607.mmorpg.common.StatusEffect.Status;
import uk.ac.brighton.uni.ab607.mmorpg.common.item.DroppableItem;
import uk.ac.brighton.uni.ab607.mmorpg.common.item.EquippableItem.Element;
import uk.ac.brighton.uni.ab607.mmorpg.common.item.EquippableItem.ItemLevel;
import uk.ac.brighton.uni.ab607.mmorpg.common.item.GameItem;
import uk.ac.brighton.uni.ab607.mmorpg.common.object.Armor.ArmorType;
import uk.ac.brighton.uni.ab607.mmorpg.common.object.Enemy.EnemyType;
import uk.ac.brighton.uni.ab607.mmorpg.common.object.Weapon.WeaponType;
import uk.ac.brighton.uni.ab607.mmorpg.common.object.GameMap.SpawnInfo;
import uk.ac.brighton.uni.ab607.mmorpg.common.request.SkillUseResult;

public class ObjectManager {

    private static HashMap<String, Weapon> defaultWeapons = new HashMap<String, Weapon>();
    private static HashMap<String, Armor> defaultArmor = new HashMap<String, Armor>();
    private static HashMap<String, Skill> defaultSkills = new HashMap<String, Skill>();
    private static HashMap<String, Enemy> defaultEnemies = new HashMap<String, Enemy>();
    private static HashMap<String, Essence> defaultEssences = new HashMap<String, Essence>();
    private static HashMap<String, GameMap> defaultMaps = new HashMap<String, GameMap>();

    private ObjectManager() {}

    public static void load() {
        // ARMOR

        addArmor(new Armor(ID.Armor.HAT, "Hat", Desc.Armor.HAT, 10, 13, ArmorType.HELM, 0, 0));    // 5000, matches type enum for easy equip
        addArmor(new Armor(ID.Armor.CLOTHES, "Clothes", Desc.Armor.CLOTHES, 0, 13, ArmorType.BODY, 0, 0));  // 5001
        addArmor(new Armor(ID.Armor.SHOES, "Shoes", Desc.Armor.SHOES, 0, 14, ArmorType.SHOES, 0, 0));    // 5002
        addArmor(new Armor(ID.Armor.CHAINMAL, "Chainmail", Desc.Armor.CHAINMAL, 4, 13, ArmorType.BODY, 15, 5));

        addArmor(new Armor(ID.Armor.SOUL_BARRIER, "Soul Barrier", Desc.Armor.SOUL_BARRIER, 3, 13,
                "Stefos", ItemLevel.UNIQUE, ArmorType.BODY, 10, 50, Element.NEUTRAL, 3, new Rune(Attribute.WILLPOWER, 3)));

        addArmor(new Armor(ID.Armor.DOMOVOI, "Domovoi", Desc.Armor.DOMOVOI, 2, 13,
                "Joe", ItemLevel.UNIQUE, ArmorType.BODY, 15, 30, Element.NEUTRAL, 4, new Rune(Attribute.WILLPOWER, 3)));

        addArmor(new Armor(ID.Armor.SAPPHIRE_LEGION_PLATE_MAIL, "Sapphire Legion Plate Mail", Desc.Armor.SAPPHIRE_LEGION_PLATE_MAIL, 5, 13,
                "Oliver", ItemLevel.UNIQUE, ArmorType.BODY, 30, 10, Element.NEUTRAL, 4, new Rune(Attribute.VITALITY, 4)));

        addArmor(new Armor(ID.Armor.THANATOS_BODY_ARMOR, "Thanatos Body Armor", Desc.Armor.THANATOS_BODY_ARMOR, 6, 13,
                "Almas", ItemLevel.EPIC, ArmorType.BODY, 50, 25, Element.EARTH, 4, new Rune(Attribute.VITALITY, 5), new Rune(Attribute.PERCEPTION, 4)));

        // WEAPON

        addWeapon(new Weapon(ID.Weapon.HANDS, "Hands", Desc.Weapon.HANDS, 0, 7, WeaponType.MACE, 0));
        addWeapon(new Weapon(ID.Weapon.IRON_SWORD, "Iron Sword", Desc.Weapon.IRON_SWORD, 0, 5, WeaponType.ONE_H_SWORD, 15));
        addWeapon(new Weapon(ID.Weapon.KNIFE, "Knife", Desc.Weapon.KNIFE, 0, 6, WeaponType.DAGGER, 5));
        addWeapon(new Weapon(ID.Weapon.CLAYMORE, "Claymore", Desc.Weapon.CLAYMORE, 10, 5, WeaponType.TWO_H_SWORD, 35));
        addWeapon(new Weapon(ID.Weapon.BROADSWORD, "Broadsword", Desc.Weapon.BROADSWORD, 11, 5, WeaponType.TWO_H_SWORD, 28));

        addWeapon(new Weapon(ID.Weapon.BATTLESWORD,
                "Battlesword", Desc.Weapon.BATTLESWORD, 12, 5,
                "Almas", ItemLevel.NORMAL, WeaponType.TWO_H_SWORD, 44, Element.NEUTRAL, 2,
                new Rune(Attribute.STRENGTH, 2)));

        addWeapon(new Weapon(ID.Weapon.LONGSWORD,
                "Longsword", Desc.Weapon.LONGSWORD, 9, 5,
                "Almas", ItemLevel.NORMAL, WeaponType.TWO_H_SWORD, 33, Element.NEUTRAL, 2,
                new Rune(Attribute.DEXTERITY, 2), new Rune(Attribute.AGILITY, 1)));

        addWeapon(new Weapon(ID.Weapon.GETSUGA_TENSHO,
                "Getsuga Tensho", Desc.Weapon.GETSUGA_TENSHO, 4, 6,
                "Matthew", ItemLevel.EPIC, WeaponType.ONE_H_SWORD, 150, Element.NEUTRAL, 4,
                new Rune(Attribute.STRENGTH, 5), new Rune(Attribute.AGILITY, 4), new Rune(Attribute.DEXTERITY, 4), new Rune(Attribute.LUCK, 1)));

        addWeapon(new Weapon(ID.Weapon.SOUL_REAPER,
                "Soul Reaper", Desc.Weapon.SOUL_REAPER, 10, 10,
                "Sam Bowen", ItemLevel.EPIC, WeaponType.TWO_H_AXE, 170, Element.NEUTRAL, 4,
                new Rune(Attribute.STRENGTH, 7), new Rune(Attribute.VITALITY, 4), new Rune(Attribute.DEXTERITY, 2)));

        addWeapon(new Weapon(ID.Weapon.GUT_RIPPER,
                "The Gut Ripper", Desc.Weapon.GUT_RIPPER, 2, 6,
                "Tim Snow", ItemLevel.EPIC, WeaponType.DAGGER, 100, Element.NEUTRAL, 4,
                new Rune(Attribute.AGILITY, 4), new Rune(Attribute.DEXTERITY, 4), new Rune(Attribute.LUCK, 1)));

        addWeapon(new Weapon(ID.Weapon.DRAGON_CLAW,
                "Dragon's Claw", Desc.Weapon.DRAGON_CLAW, 12, 11,
                "Atheryos", ItemLevel.EPIC, WeaponType.BOW, 130, Element.FIRE, 4,
                new Rune(Attribute.VITALITY, 3), new Rune(Attribute.WISDOM, 5), new Rune(Attribute.AGILITY, 3)));

        addWeapon(new Weapon(ID.Weapon.FROSTMOURN, "Frostmourn", Desc.Weapon.FROSTMOURN, 8, 25,
                "Stefos", ItemLevel.EPIC, WeaponType.TWO_H_SWORD, 130, Element.WATER, 4,
                new Rune(Attribute.DEXTERITY, 5), new Rune(Attribute.STRENGTH, 3)));

        // SKILLS

        addSkill(new Skill(ID.Skill.Gladiator.BLOODLUST, "Bloodlust", Desc.Skill.Gladiator.BLOODLUST, false, 0.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 5844145407908548491L;

            private int value = 0;

            @Override
            public int getManaCost() {
                return 0;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                caster.addBonusStat(Stat.ATK, -value);
                // div 0 shouldn't occur
                value = (int) (10*level * caster.getTotalStat(Stat.MAX_HP) / (caster.getHP() + 1));
                caster.addBonusStat(Stat.ATK, value);
            }
        });

        addSkill(new Skill(ID.Skill.Gladiator.BASH, "Bash", Desc.Skill.Gladiator.BASH, true, 15.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 2177640389884854474L;

            @Override
            public int getManaCost() {
                return 5 + level * 3;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float dmg = (1 + (15 + 5*level) / 100.0f) * caster.getTotalStat(Stat.ATK);
                int d = caster.dealPhysicalDamage(target, dmg);
                target.addStatusEffect(new StatusEffect(Status.STUNNED, 5.0f));

                useResult = new SkillUseResult(GameMath.normalizeDamage(d) + ",STUNNED");
            }
        });

        addSkill(new Skill(ID.Skill.Warrior.MIGHTY_SWING, "Mighty Swing", Desc.Skill.Warrior.MIGHTY_SWING, true, 15.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 8019137126608309704L;

            @Override
            public int getManaCost() {
                return 5 + level * 4;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float diff = caster.getTotalAttribute(Attribute.STRENGTH) - target.getTotalAttribute(Attribute.STRENGTH);
                float dmg = (Math.max(diff, 0) + 10*level) * 5;
                int d = caster.dealPhysicalDamage(target, dmg);
                useResult = new SkillUseResult(GameMath.normalizeDamage(d));
            }
        });

        addSkill(new Skill(ID.Skill.Gladiator.DOUBLE_EDGE, "Double Edge", Desc.Skill.Gladiator.DOUBLE_EDGE, true, 0.0f) {
            /**
             *
             */
            private static final long serialVersionUID = -5670132035647752285L;

            @Override
            public int getManaCost() {
                return 0;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float dmg = (0.1f + 0.02f * level) * caster.getHP();
                caster.setHP(Math.round(caster.getHP() - dmg));
                caster.dealPureDamage(target, 2*dmg);

                useResult = new SkillUseResult(2*dmg + ",PURE");
            }
        });

        addSkill(new Skill(ID.Skill.Warrior.ROAR, "Roar", Desc.Skill.Warrior.ROAR, true, 5.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 5098091102433780519L;

            @Override
            public int getManaCost() {
                return 2 + level*2;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                caster.addEffect(new Effect((5.0f), ID.Skill.Warrior.ROAR,
                        new Rune[] {
                        new Rune(Attribute.STRENGTH, level*2),
                        new Rune(Attribute.VITALITY, level*2)
                },
                new Essence[] {}
                        ));

                useResult = new SkillUseResult("STR +" + level*2 + " VIT +" + level*2);
            }

            @Override
            public boolean isSelfTarget() {
                return true;
            }
        });

        addSkill(new Skill(ID.Skill.Crusader.LAST_STAND, "Last Stand", Desc.Skill.Crusader.LAST_STAND, true, 60.0f) {
            /**
             *
             */
            private static final long serialVersionUID = -8176078084748576113L;

            @Override
            public int getManaCost() {
                return 2 + level*5;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                caster.addEffect(new Effect((20.0f), ID.Skill.Crusader.LAST_STAND,
                        new Rune[] {
                }, new Essence[] {
                        new Essence(Stat.ATK, Math.round(caster.getBaseStat(Stat.ATK)))
                }
                        ));

                useResult = new SkillUseResult("ATK UP!");
            }

            @Override
            public boolean isSelfTarget() {
                return true;
            }
        });

        addSkill(new Skill(ID.Skill.Gladiator.SHATTER_ARMOR, "Shatter Armor", Desc.Skill.Gladiator.SHATTER_ARMOR, true, 30.0f) {
            /**
             *
             */
            private static final long serialVersionUID = -4834599835655165707L;

            @Override
            public int getManaCost() {
                return 2 + level*5;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                target.addEffect(new Effect((20.0f), ID.Skill.Gladiator.SHATTER_ARMOR,
                        new Rune[] {
                }, new Essence[] {
                        new Essence(Stat.ARM, -2*level)
                }
                        ));

                useResult = new SkillUseResult("ARM -" + 2*level);
            }
        });

        addSkill(new Skill(ID.Skill.Warrior.ARMOR_MASTERY, "Armor Mastery", Desc.Skill.Warrior.ARMOR_MASTERY, false, 0.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 8019137126608309704L;

            private int value = 0;

            @Override
            public int getManaCost() {
                return 0;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                caster.addBonusStat(Stat.ARM, -value);
                float factor = 2.0f;
                for (Skill skill : caster.getSkills()) {
                    if (skill.id.equals(ID.Skill.Crusader.DIVINE_ARMOR)) {
                        factor += 0.15f * skill.getLevel();
                        break;
                    }
                }

                value = (int)(factor * level);
                caster.addBonusStat(Stat.ARM, value);
            }
        });

        addSkill(new Skill(ID.Skill.Warrior.WARRIOR_HEART, "Heart of a Warrior", Desc.Skill.Warrior.WARRIOR_HEART, false, 0.0f) {
            /**
             *
             */
            private static final long serialVersionUID = -9161209014480342120L;

            private int value = 0;

            @Override
            public int getManaCost() {
                return 0;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                caster.addBonusStat(Stat.MAX_HP, -value);
                float factor = 0.025f;
                for (Skill skill : caster.getSkills()) {
                    if (skill.id.equals(ID.Skill.Crusader.FAITH)) {
                        factor += 0.01f * skill.getLevel();
                        break;
                    }
                }


                value = Math.round(factor * level * caster.getBaseStat(Stat.MAX_HP));
                caster.addBonusStat(Stat.MAX_HP, value);
            }
        });

        // MAGE SKILL SET

        addSkill(new Skill(ID.Skill.Mage.AIR_SPEAR, "Air Spear", Desc.Skill.Mage.AIR_SPEAR, true, 9.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 6306777256266732648L;

            @Override
            public int getManaCost() {
                return 5 + level * 5;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float dmg = caster.getTotalStat(Stat.MATK) + level *20;
                int d = caster.dealMagicalDamage(target, dmg, Element.AIR);

                useResult = new SkillUseResult(d);
            }
        });

        addSkill(new Skill(ID.Skill.Wizard.AMPLIFY_MAGIC, "Amplify Magic", Desc.Skill.Wizard.AMPLIFY_MAGIC, true, 30.0f) {
            /**
             *
             */
            private static final long serialVersionUID = -6423702278665617928L;

            @Override
            public int getManaCost() {
                return 5 + level * 5;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                caster.addEffect(new Effect((15.0f), ID.Skill.Wizard.AMPLIFY_MAGIC,
                        new Rune[] {},
                        new Essence[] {
                        new Essence(Stat.MATK, 10*level)
                }
                        ));

                useResult = new SkillUseResult("MATK +" + 10*level);
            }

            @Override
            public boolean isSelfTarget() {
                return true;
            }
        });

        addSkill(new Skill(ID.Skill.Enchanter.ASTRAL_PROTECTION, "Astral Protection", Desc.Skill.Enchanter.ASTRAL_PROTECTION, false, 0.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 8691650266711866295L;

            private int value = 0;

            @Override
            public int getManaCost() {
                return 0;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                caster.addBonusStat(Stat.MDEF, -value);
                value = level * 2;
                caster.addBonusStat(Stat.MDEF, value);
            }
        });

        addSkill(new Skill(ID.Skill.Mage.EARTH_BOULDER, "Earth Boulder", Desc.Skill.Mage.EARTH_BOULDER, true, 15.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 1871962939560471153L;

            @Override
            public int getManaCost() {
                return 5 + level * 5;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float dmg = caster.getTotalStat(Stat.MATK) + level *25;
                int d = caster.dealMagicalDamage(target, dmg, Element.EARTH);

                useResult = new SkillUseResult(d);
            }
        });

        addSkill(new Skill(ID.Skill.Mage.FIREBALL, "Fireball", Desc.Skill.Mage.FIREBALL, true, 9.0f) {
            /**
             *
             */
            private static final long serialVersionUID = -1839096679550971399L;

            @Override
            public int getManaCost() {
                return 5 + level * 5;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float dmg = caster.getTotalStat(Stat.MATK) + level *20;
                int d = caster.dealMagicalDamage(target, dmg, Element.FIRE);

                useResult = new SkillUseResult(d);
            }
        });

        addSkill(new Skill(ID.Skill.Mage.ICE_SHARD, "Ice Shard", Desc.Skill.Mage.ICE_SHARD, true, 9.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 5561489415884518543L;

            @Override
            public int getManaCost() {
                return 5 + level * 5;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float dmg = caster.getTotalStat(Stat.MATK) + level *20;
                int d = caster.dealMagicalDamage(target, dmg, Element.WATER);

                useResult = new SkillUseResult(d);
            }
        });

        addSkill(new Skill(ID.Skill.Wizard.MAGIC_MASTERY, "Magic Mastery", Desc.Skill.Wizard.MAGIC_MASTERY, false, 0.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 9020149732268399438L;

            private int value = 0;

            @Override
            public int getManaCost() {
                return 0;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                caster.addBonusAttribute(Attribute.INTELLECT, -value);
                caster.addBonusAttribute(Attribute.WILLPOWER, -value);
                value = level * 2;
                caster.addBonusAttribute(Attribute.INTELLECT, value);
                caster.addBonusAttribute(Attribute.WILLPOWER, value);
            }
        });

        addSkill(new Skill(ID.Skill.Enchanter.MAGIC_SHIELD, "Magic Shield", Desc.Skill.Enchanter.MAGIC_SHIELD, true, 60.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 7104420977798092420L;

            @Override
            public int getManaCost() {
                return 5 + level * 5;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                caster.addEffect(new Effect((25.0f), ID.Skill.Enchanter.MAGIC_SHIELD,
                        new Rune[] {},
                        new Essence[] {
                        new Essence(Stat.ARM, 5*level)
                }
                        ));

                useResult = new SkillUseResult("ARM +" + 5*level);
            }

            @Override
            public boolean isSelfTarget() {
                return true;
            }
        });

        addSkill(new Skill(ID.Skill.Wizard.MENTAL_STRIKE, "Mental Strike", Desc.Skill.Wizard.MENTAL_STRIKE, true, 20.0f) {

            /**
             *
             */
            private static final long serialVersionUID = -55046003688618764L;

            @Override
            public int getManaCost() {
                return 5 + level * 5;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float dmg = caster.getTotalStat(Stat.MATK) * (1 + level*0.1f);
                caster.dealPureDamage(target, dmg);

                useResult = new SkillUseResult(dmg + ",PURE");
            }
        });

        // SCOUT SKILL SET

        addSkill(new Skill(ID.Skill.Rogue.CRITICAL_STRIKE, "Critical Strike", Desc.Skill.Rogue.CRITICAL_STRIKE, true, 20.0f) {
            /**
             *
             */
            private static final long serialVersionUID = -7584376145233708322L;

            @Override
            public int getManaCost() {
                return 3 + level * 2;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float dmg = caster.getTotalStat(Stat.ATK) + 15 + 5 * level;
                caster.addBonusStat(Stat.CRIT_CHANCE, 50 + level * 3);
                int d = caster.dealPhysicalDamage(target, dmg);
                caster.addBonusStat(Stat.CRIT_CHANCE, -(50 + level * 3));

                useResult = new SkillUseResult(GameMath.normalizeDamage(d));
            }
        });

        addSkill(new Skill(ID.Skill.Ranger.PINPOINT_WEAKNESS, "Pinpoint Weakness", Desc.Skill.Ranger.PINPOINT_WEAKNESS, true, 15.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 2458408699758838323L;

            @Override
            public int getManaCost() {
                return 3 + level * 2;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                target.addEffect(new Effect((10.0f), ID.Skill.Ranger.PINPOINT_WEAKNESS,
                        new Rune[] {},
                        new Essence[] {
                        new Essence(Stat.ARM, -2*level)
                }
                        ));

                useResult = new SkillUseResult("ARM -" + 2*level);
            }
        });

        addSkill(new Skill(ID.Skill.Rogue.DOUBLE_STRIKE, "Double Strike", Desc.Skill.Rogue.DOUBLE_STRIKE, true, 8.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 5685022402103377679L;

            @Override
            public int getManaCost() {
                return 3 + level * 2;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                int dmg1 = caster.attack(target);
                int dmg2 = caster.attack(target);
                boolean stun = false;
                if (GameMath.checkChance(level*5)) {
                    target.addStatusEffect(new StatusEffect(Status.STUNNED, 2.5f));
                    stun = true;
                }

                useResult = new SkillUseResult(GameMath.normalizeDamage(dmg1) + "," + GameMath.normalizeDamage(dmg2)
                        + (stun ? ",STUNNED" : ",X2"));
            }
        });

        addSkill(new Skill(ID.Skill.Scout.EXPERIENCED_FIGHTER, "Experienced Fighter", Desc.Skill.Scout.EXPERIENCED_FIGHTER, false, 0.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 2024648263069876L;

            private int value = 0;

            @Override
            public int getManaCost() {
                return 0;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                caster.addBonusAttribute(Attribute.AGILITY, -value);
                caster.addBonusAttribute(Attribute.DEXTERITY, -value);
                value = level * 2;
                caster.addBonusAttribute(Attribute.AGILITY, value);
                caster.addBonusAttribute(Attribute.DEXTERITY, value);
            }
        });

        addSkill(new Skill(ID.Skill.Rogue.SHAMELESS, "Shameless", Desc.Skill.Rogue.SHAMELESS, true, 0.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 2306928037030551618L;

            @Override
            public int getManaCost() {
                return 10;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float dmg = caster.getTotalStat(Stat.ATK);
                float casterHPFactor = caster.getHP() / caster.getTotalStat(Stat.MAX_HP);
                float targetHPFactor = target.getHP() / target.getTotalStat(Stat.MAX_HP);
                if (casterHPFactor > targetHPFactor) {
                    dmg += level * 0.1f * (casterHPFactor - targetHPFactor) * dmg;
                }

                int d = caster.dealPhysicalDamage(target, dmg);
                useResult = new SkillUseResult(GameMath.normalizeDamage(d));
            }
        });

        addSkill(new Skill(ID.Skill.Scout.WEAPON_MASTERY, "Weapon Mastery", Desc.Skill.Scout.WEAPON_MASTERY, false, 0.0f) {
            /**
             *
             */
            private static final long serialVersionUID = -6762381875332894326L;

            private int value = 0;

            @Override
            public int getManaCost() {
                return 0;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                caster.addBonusStat(Stat.ATK, -value);
                value = level * 7;
                caster.addBonusStat(Stat.ATK, value);
            }
        });

        addSkill(new Skill(ID.Skill.Scout.POISON_ATTACK, "Poison Attack", Desc.Skill.Scout.POISON_ATTACK, true, 30.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 6791451275759000638L;

            @Override
            public int getManaCost() {
                return 3 + level*2;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                int dmg = caster.attack(target);
                boolean poison = false;
                if (GameMath.checkChance(level*7)) {
                    target.addStatusEffect(new StatusEffect(Status.POISONED, 10.0f));
                    poison = true;
                }

                useResult = new SkillUseResult(GameMath.normalizeDamage(dmg) + (poison ? ",POISONED" : ""));
            }
        });

        addSkill(new Skill(ID.Skill.Scout.TRICK_ATTACK, "Throw Dagger", Desc.Skill.Scout.TRICK_ATTACK, true, 20.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 1031700846462374399L;

            @Override
            public int getManaCost() {
                return 5 + level*2;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float dmg = caster.getTotalStat(Stat.ATK) + level * 2 * GameMath.random(5);
                int d = GameMath.normalizeDamage(caster.dealPhysicalDamage(target, dmg));
                boolean money = false;
                if (caster instanceof Player) {
                    ((Player)caster).incMoney(d);
                    money = true;
                }

                useResult = new SkillUseResult(d + (money ? ",MONEY" : ""));
            }
        });

        addSkill(new Skill(ID.Skill.Rogue.TRIPLE_STRIKE, "Triple Strike", Desc.Skill.Rogue.TRIPLE_STRIKE, true, 40.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 8295208480454374043L;

            @Override
            public int getManaCost() {
                return 3 + level*4;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float dmg = caster.getTotalStat(Stat.ATK);
                if (target.hasStatusEffect(Status.STUNNED)) {
                    dmg += level * 15;
                }

                int dmg1 = GameMath.normalizeDamage(caster.dealPhysicalDamage(target, dmg));
                int dmg2 = GameMath.normalizeDamage(caster.dealPhysicalDamage(target, dmg));
                int dmg3 = GameMath.normalizeDamage(caster.dealPhysicalDamage(target, dmg));

                useResult = new SkillUseResult(dmg1 + "," + dmg2 + "," + dmg3 + ",X3");
            }
        });

        addSkill(new Skill(ID.Skill.Crusader.HOLY_LIGHT, "Holy Light", Desc.Skill.Crusader.HOLY_LIGHT, true, 20.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 5685022402103377679L;

            @Override
            public int getManaCost() {
                return 3 + level*4;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                target.setHP(Math.min(target.getHP() + 30 + level*10, (int)target.getTotalStat(Stat.MAX_HP)));
                target.addEffect(new Effect(20.0f, ID.Skill.Crusader.HOLY_LIGHT, new Rune[] {
                        new Rune(Attribute.VITALITY, level*2)
                },
                new Essence[] {}));

                useResult = new SkillUseResult("VIT +" + level*2);
            }
        });

        addSkill(new Skill(ID.Skill.Crusader.PRECISION_STRIKE, "Precision Strike", Desc.Skill.Crusader.PRECISION_STRIKE, true, 20.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 2024648263069876L;

            @Override
            public int getManaCost() {
                return 3 + level*4;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float dmg = caster.getTotalStat(Stat.ATK) + (caster.getTotalAttribute(Attribute.STRENGTH) / 10) * level;
                caster.dealPureDamage(target, dmg);

                useResult = new SkillUseResult((int)dmg);
            }
        });

        addSkill(new Skill(ID.Skill.Crusader.DIVINE_ARMOR, "Divine Armor", Desc.Skill.Crusader.DIVINE_ARMOR, false, 0.0f) {
            /**
             *
             */
            private static final long serialVersionUID = -7936080589333242098L;

            @Override
            public int getManaCost() {
                return 0;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                // impl is in ARMOR_MASTERY
            }
        });

        addSkill(new Skill(ID.Skill.Crusader.FAITH, "Faith", Desc.Skill.Crusader.FAITH, false, 0.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 4325213967370795918L;

            @Override
            public int getManaCost() {
                return 0;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                // impl is in WARRIOR_HEART
            }
        });

        addSkill(new Skill(ID.Skill.Gladiator.ENDURANCE, "Endurance", Desc.Skill.Gladiator.ENDURANCE, true, 40.0f) {
            /**
             *
             */
            private static final long serialVersionUID = -7936080589333242098L;

            @Override
            public int getManaCost() {
                return 3 + level*4;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                caster.addEffect(new Effect(15.0f, ID.Skill.Gladiator.ENDURANCE, new Rune[] {},
                        new Essence[] {
                        new Essence(Stat.DEF, 2*level),
                        new Essence(Stat.HP_REGEN, 2*level)
                }));

                useResult = new SkillUseResult("DEF +" + level*2 + ", HP REGEN +" + 2*level);
            }

            @Override
            public boolean isSelfTarget() {
                return true;
            }
        });

        addSkill(new Skill(ID.Skill.Wizard.THUNDERBOLT_FIRESTORM, "Thunderbolt Firestorm", Desc.Skill.Wizard.THUNDERBOLT_FIRESTORM, true, 40.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 4325213967370795918L;

            @Override
            public int getManaCost() {
                return 3 + level*4;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float dmg = caster.getTotalStat(Stat.MATK) * (1.0f + level*0.15f);
                int dmg1 = caster.dealMagicalDamage(target, dmg, Element.FIRE);
                int dmg2 = caster.dealMagicalDamage(target, dmg, Element.AIR);

                useResult = new SkillUseResult(dmg1 + "," + dmg2);
            }
        });

        addSkill(new Skill(ID.Skill.Wizard.ICICLE_AVALANCHE, "Icicle Avalanche", Desc.Skill.Wizard.ICICLE_AVALANCHE, true, 40.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 6791451275759000638L;

            @Override
            public int getManaCost() {
                return 3 + level*4;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float dmg = caster.getTotalStat(Stat.MATK) * (1.0f + level*0.15f);
                int dmg1 = caster.dealMagicalDamage(target, dmg, Element.WATER);
                int dmg2 = caster.dealMagicalDamage(target, dmg, Element.EARTH);

                useResult = new SkillUseResult(dmg1 + "," + dmg2);
            }
        });

        addSkill(new Skill(ID.Skill.Enchanter.MANA_BURN, "Mana Burn", Desc.Skill.Enchanter.MANA_BURN, true, 20.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 1031700846462374399L;

            @Override
            public int getManaCost() {
                return 3 + level*4;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                int oldSP = target.getSP();
                target.setSP(Math.max(oldSP - 50 * level, 0));
                int dmg = caster.dealMagicalDamage(target, oldSP-target.getSP(), Element.NEUTRAL);

                useResult = new SkillUseResult(dmg);
            }
        });

        addSkill(new Skill(ID.Skill.Enchanter.CURSE_OF_WITCHCRAFT, "Curse of Witchcraft", Desc.Skill.Enchanter.CURSE_OF_WITCHCRAFT, true, 20.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 8295208480454374043L;

            @Override
            public int getManaCost() {
                return 3 + level*4;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                target.addStatusEffect(new StatusEffect(Status.SILENCED, level*3));

                useResult = new SkillUseResult("SILENCED");
            }
        });

        addSkill(new Skill(ID.Skill.Enchanter.MIND_BLAST, "Mind Blast", Desc.Skill.Enchanter.MIND_BLAST, true, 20.0f) {
            /**
             *
             */
            private static final long serialVersionUID = -3587620067204007562L;

            @Override
            public int getManaCost() {
                return 3 + level*4;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                // TODO: impl
            }
        });

        addSkill(new Skill(ID.Skill.Rogue.FIVE_FINGER_DEATH_PUNCH, "Five Finger Death Punch", Desc.Skill.Rogue.FIVE_FINGER_DEATH_PUNCH, true, 35.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 9128637084476710269L;

            @Override
            public int getManaCost() {
                return 3 + level*4;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float dmg = 20 + level*30 - target.getTotalStat(Stat.ARM);
                int d = caster.dealPhysicalDamage(target, dmg);

                useResult = new SkillUseResult(GameMath.normalizeDamage(d));
            }
        });

        addSkill(new Skill(ID.Skill.Ranger.BULLSEYE, "Bullseye", Desc.Skill.Ranger.BULLSEYE, true, 60.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 3498114139079315L;

            @Override
            public int getManaCost() {
                return 5 + level * 10;
            }

            @Override
            public void useImpl(GameCharacter caster, GameCharacter target) {
                float dmg = 100 + 0.2f*level * caster.getTotalAttribute(Attribute.DEXTERITY) - target.getTotalStat(GameCharacter.DEF);
                caster.dealPureDamage(target, dmg);

                useResult = new SkillUseResult(dmg + ",PURE");
            }
        });

        addSkill(new Skill(ID.Skill.Ranger.EAGLE_EYE, "Eagle Eye", Desc.Skill.Ranger.EAGLE_EYE, false, 0.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 7005439875094828368L;

            private int value = 0;

            @Override
            public int getManaCost() {
                return 0;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                caster.addBonusStat(Stat.ATK, -value);
                value = (int)(caster.getTotalAttribute(Attribute.DEXTERITY) * level * 0.1f);
                caster.addBonusStat(Stat.ATK, value);
            }
        });

        addSkill(new Skill(ID.Skill.Ranger.ENCHANTED_ARROW, "Enchanted Arrows", Desc.Skill.Ranger.ENCHANTED_ARROW, true, 35.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 5416340917264724397L;

            @Override
            public int getManaCost() {
                return 3 + level*4;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                float duration = target.getTotalStat(Stat.ARM) * 0.1f;
                target.addStatusEffect(new StatusEffect(Status.STUNNED, duration));

                useResult = new SkillUseResult("STUNNED");
            }
        });

        addSkill(new Skill(ID.Skill.Ranger.FAST_REFLEXES, "Fast Reflexes", Desc.Skill.Ranger.FAST_REFLEXES, true, 35.0f) {
            /**
             *
             */
            private static final long serialVersionUID = 5766544471206156505L;

            @Override
            public int getManaCost() {
                return 3 + level*4;
            }

            @Override
            protected void useImpl(GameCharacter caster, GameCharacter target) {
                caster.addEffect(new Effect(10.0f, ID.Skill.Ranger.FAST_REFLEXES, new Rune[] {},
                        new Essence[] {
                        new Essence(Stat.ASPD, level*2)
                }));

                useResult = new SkillUseResult("ASPD +" + level*2);
            }

            @Override
            public boolean isSelfTarget() {
                return true;
            }
        });


        // ENEMIES

        addEnemy(new Enemy(ID.Enemy.MINOR_FIRE_SPIRIT, "Minor Fire Spirit", Desc.Enemy.MINOR_FIRE_SPIRIT,
                EnemyType.NORMAL, Element.FIRE, 1, new AttributeInfo(),
                new Experience(100, 100, 100), 0, new DroppableItem(ID.Weapon.KNIFE, 50), new DroppableItem(ID.Armor.THANATOS_BODY_ARMOR, 10)));

        addEnemy(new Enemy(ID.Enemy.MINOR_EARTH_SPIRIT, "Minor Earth Spirit", Desc.Enemy.MINOR_EARTH_SPIRIT,
                EnemyType.NORMAL, Element.EARTH, 1, new AttributeInfo(),
                new Experience(100, 100, 100), 0, new DroppableItem(ID.Weapon.IRON_SWORD, 15), new DroppableItem(ID.Armor.SAPPHIRE_LEGION_PLATE_MAIL, 5)));

        addEnemy(new Enemy(ID.Enemy.MINOR_WATER_SPIRIT, "Minor Water Spirit", Desc.Enemy.MINOR_WATER_SPIRIT,
                EnemyType.NORMAL, Element.WATER, 1, new AttributeInfo(),
                new Experience(100, 100, 100), 0, new DroppableItem(ID.Armor.CHAINMAL, 25), new DroppableItem(ID.Weapon.SOUL_REAPER, 5)));


        // ESSENCES

        addEssence(new Essence("Minor Fire Spirit Essence", Stat.ATK, 5));

        // MAPS

        addMap(new GameMap("map1.txt", 0,
                new SpawnInfo(ID.Enemy.MINOR_EARTH_SPIRIT, 5),
                new SpawnInfo(ID.Enemy.MINOR_FIRE_SPIRIT, 5),
                new SpawnInfo(ID.Enemy.MINOR_WATER_SPIRIT, 5)));
    }

    private static void addArmor(Armor armor) {
        defaultArmor.put(armor.id, armor);
    }

    private static void addWeapon(Weapon weapon) {
        defaultWeapons.put(weapon.id, weapon);
    }

    private static void addSkill(Skill skill) {
        defaultSkills.put(skill.id, skill);
    }

    private static void addEnemy(Enemy enemy) {
        defaultEnemies.put(enemy.id, enemy);
    }

    private static void addEssence(Essence e) {
        defaultEssences.put(e.id, e);
    }

    private static void addMap(GameMap m) {
        defaultMaps.put(m.name, m);
    }

    public static Skill getSkillByID(String id) {
        if (defaultSkills.containsKey(id)) {
            Skill sk = defaultSkills.get(id);
            Constructor<? extends Skill> c;
            try {
                c = sk.getClass().getDeclaredConstructor(String.class, String.class, String.class, Boolean.class, Float.class);
                return c.newInstance(sk.id, sk.name, sk.description, sk.active, sk.skillCooldown);
            }
            catch (NoSuchMethodException | SecurityException | InstantiationException
                    | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static Armor getArmorByID(String id) {
        return defaultArmor.containsKey(id) ? new Armor(defaultArmor.get(id)) : null;
    }

    public static Weapon getWeaponByID(String id) {
        return defaultWeapons.containsKey(id) ? new Weapon(defaultWeapons.get(id)) : null;
    }

    public static Enemy getEnemyByID(String id) {
        return defaultEnemies.containsKey(id) ? new Enemy(defaultEnemies.get(id)) : null;
    }

    public static GameItem getItemByID(String id) {
        if (id.startsWith("5"))
            return getArmorByID(id);
        if (id.startsWith("4"))
            return getWeaponByID(id);

        return null;
    }

    public static Essence getEssenceByID(String id) {
        return defaultEssences.containsKey(id) ? new Essence(defaultEssences.get(id)) : null;
    }

    /**
     *
     * @param name
     * @return
     *          COPY of a map
     */
    public static GameMap getMapByName(String name) {
        return defaultMaps.containsKey(name) ? new GameMap(defaultMaps.get(name)) : null;
    }
}
