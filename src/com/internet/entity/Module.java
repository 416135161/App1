package com.internet.entity;

import com.internet.intrface.ModuleOnSelectedListener;


public class Module {
    private String name;
    private int iconId;
    private ModuleOnSelectedListener listener;
    
    public Module() {
        // TODO Auto-generated constructor stub
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIconId() {
        return iconId;
    }
    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
    public ModuleOnSelectedListener getListener() {
        return listener;
    }
    public void setListener(ModuleOnSelectedListener listener) {
        this.listener = listener;
    }
    
}
