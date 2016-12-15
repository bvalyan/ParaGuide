package com.optimalotaku.paraguide;

/**
 * Created by Jerek on 12/14/2016.
 */

public class HeroData {

    private String paragonVersion;
    private String name;
    private String attackType;
    private String traits;
    private String scale;
    private String affinity1;
    private String affinity2;
    private Integer difficulty;
    private Integer mobility;
    private Integer basicAttack;
    private Integer durability;
    private Integer abilityAttack;
    private String imageIconURL;
    private HeroSkill primarySkill;
    private HeroSkill secondarySkillOne;
    private HeroSkill secondarySkillTwo;
    private HeroSkill secondarySkillThree;
    private HeroSkill ultimateSkill;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttackType() {
        return attackType;
    }

    public void setAttackType(String attackType) {
        this.attackType = attackType;
    }

    public String getTraits() {
        return traits;
    }

    public void setTraits(String traits) {
        this.traits = traits;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getAffinity1() {
        return affinity1;
    }

    public void setAffinity1(String affinity1) {
        this.affinity1 = affinity1;
    }

    public String getAffinity2() {
        return affinity2;
    }

    public void setAffinity2(String affinity2) {
        this.affinity2 = affinity2;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getMobility() {
        return mobility;
    }

    public void setMobility(Integer mobility) {
        this.mobility = mobility;
    }

    public Integer getBasicAttack() {
        return basicAttack;
    }

    public void setBasicAttack(Integer basicAttack) {
        this.basicAttack = basicAttack;
    }

    public Integer getDurability() {
        return durability;
    }

    public void setDurability(Integer durability) {
        this.durability = durability;
    }

    public Integer getAbilityAttack() {
        return abilityAttack;
    }

    public void setAbilityAttack(Integer abilityAttack) {
        this.abilityAttack = abilityAttack;
    }

    public String getImageIconURL() {
        return imageIconURL;
    }

    public void setImageIconURL(String imageIconURL) {
        this.imageIconURL = imageIconURL;
    }

    public HeroSkill getPrimarySkill() {
        return primarySkill;
    }

    public void setPrimarySkill(HeroSkill primarySkill) {
        this.primarySkill = primarySkill;
    }

    public HeroSkill getSecondarySkillOne() {
        return secondarySkillOne;
    }

    public void setSecondarySkillOne(HeroSkill secondarySkillOne) {
        this.secondarySkillOne = secondarySkillOne;
    }

    public HeroSkill getSecondarySkillTwo() {
        return secondarySkillTwo;
    }

    public void setSecondarySkillTwo(HeroSkill secondarySkillTwo) {
        this.secondarySkillTwo = secondarySkillTwo;
    }

    public HeroSkill getSecondarySkillThree() {
        return secondarySkillThree;
    }

    public void setSecondarySkillThree(HeroSkill secondarySkillThree) {
        this.secondarySkillThree = secondarySkillThree;
    }

    public HeroSkill getUltimateSkill() {
        return ultimateSkill;
    }

    public void setUltimateSkill(HeroSkill ultimateSkill) {
        this.ultimateSkill = ultimateSkill;
    }

    public String getParagonVersion() {
        return paragonVersion;
    }

    public void setParagonVersion(String paragonVersion) {
        this.paragonVersion = paragonVersion;
    }
}
