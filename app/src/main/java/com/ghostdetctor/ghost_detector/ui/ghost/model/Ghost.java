package com.ghostdetctor.ghost_detector.ui.ghost.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "ghost")
public class Ghost implements Serializable {
    @PrimaryKey
    private int id;

    private String name;
    private String alias;
    private String description;
    private String danger;
    private String die;
    private int ghostItemImage;
    private int ghostDetailImage;
    private boolean isHorror;
    private boolean isCaptured;
    private String imagePath;

    public Ghost() {
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Ghost(int id, String name, String alias, String description, String danger, String die,int ghostItemImage, int ghostDetailImage, boolean isHorror) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.description = description;
        this.danger = danger;
        this.die = die;
        this.ghostItemImage = ghostItemImage;
        this.ghostDetailImage = ghostDetailImage;
        this.isHorror = isHorror;
    }



    public Ghost(int id, String name, String description, String danger, String die,int ghostItemImage, int ghostDetailImage, boolean isHorror) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.danger = danger;
        this.die = die;
        this.ghostItemImage = ghostItemImage;
        this.ghostDetailImage = ghostDetailImage;
        this.isHorror = isHorror;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getGhostItemImage() {
        return ghostItemImage;
    }

    public void setGhostItemImage(int ghostItemImage) {
        this.ghostItemImage = ghostItemImage;
    }

    public int getGhostDetailImage() {
        return ghostDetailImage;
    }

    public void setGhostDetailImage(int ghostDetailImage) {
        this.ghostDetailImage = ghostDetailImage;
    }

    public boolean isCaptured() {
        return isCaptured;
    }

    public void setCaptured(boolean captured) {
        isCaptured = captured;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDanger() {
        return danger;
    }

    public void setDanger(String danger) {
        this.danger = danger;
    }

    public String getDie() {
        return die;
    }

    public void setDie(String die) {
        this.die = die;
    }

    public boolean isHorror() {
        return isHorror;
    }

    public void setHorror(boolean horror) {
        isHorror = horror;
    }

}
