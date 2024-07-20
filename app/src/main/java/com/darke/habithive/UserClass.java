package com.darke.habithive;

import com.darke.habithive.HabitClass;

import java.util.List;

public class UserClass {
    private String userId;
    private String name;
    private String email;
    private String avatar;
    private int level;
    private int xp;
    private int healthPoints;
    private int coins;
    private int mindAttribute;
    private int bodyAttribute;
    private int wealthAttribute;
    private int creativityAttribute;
    private int spiritualityAttribute;
    private int relationsAttribute;
    private List<HabitClass> habits;
//    private List<Achievement> achievements;
//    private List<Quest> quests;

    // Required empty constructor
    public UserClass() {}

    public UserClass(String userId, String name, String email, String avatar, int level, int xp,
                int healthPoints, int coins, int mindAttribute, int bodyAttribute,
                int wealthAttribute, int creativityAttribute, int spiritualityAttribute,
                int relationsAttribute, List<HabitClass> habits) {

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.level = level;
        this.xp = xp;
        this.healthPoints = healthPoints;
        this.coins = coins;
        this.mindAttribute = mindAttribute;
        this.bodyAttribute = bodyAttribute;
        this.wealthAttribute = wealthAttribute;
        this.creativityAttribute = creativityAttribute;
        this.spiritualityAttribute = spiritualityAttribute;
        this.relationsAttribute = relationsAttribute;
        this.habits = habits;
//        this.achievements = achievements;
//        this.quests = quests;
    }

    // Getters and Setters
    // User attributes getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getMindAttribute() {
        return mindAttribute;
    }

    public void setMindAttribute(int mindAttribute) {
        this.mindAttribute = mindAttribute;
    }

    public int getBodyAttribute() {
        return bodyAttribute;
    }

    public void setBodyAttribute(int bodyAttribute) {
        this.bodyAttribute = bodyAttribute;
    }

    public int getWealthAttribute() {
        return wealthAttribute;
    }

    public void setWealthAttribute(int wealthAttribute) {
        this.wealthAttribute = wealthAttribute;
    }

    public int getCreativityAttribute() {
        return creativityAttribute;
    }

    public void setCreativityAttribute(int creativityAttribute) {
        this.creativityAttribute = creativityAttribute;
    }

    public int getSpiritualityAttribute() {
        return spiritualityAttribute;
    }

    public void setSpiritualityAttribute(int spiritualityAttribute) {
        this.spiritualityAttribute = spiritualityAttribute;
    }

    public int getRelationsAttribute() {
        return relationsAttribute;
    }

    public void setRelationsAttribute(int relationsAttribute) {
        this.relationsAttribute = relationsAttribute;
    }

    // Habits getters and setters

    public List<HabitClass> getHabits() {
        return habits;
    }

    public void setHabits(List<HabitClass> habits) {
        this.habits = habits;
    }

    public HabitClass getHabitById(String habitId) {
        for (HabitClass habit : habits) {
            if (habit.getHabitId().equals(habitId)) {
                return habit;
            }
        }
        return null;
    }

//    public List<Achievement> getAchievements() {
//        return achievements;
//    }
//
//    public void setAchievements(List<Achievement> achievements) {
//        this.achievements = achievements;
//    }
//
//    public List<Quest> getQuests() {
//        return quests;
//    }
//
//    public void setQuests(List<Quest> quests) {
//        this.quests = quests;
//    }
}
