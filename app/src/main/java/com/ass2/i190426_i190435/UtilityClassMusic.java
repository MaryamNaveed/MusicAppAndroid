package com.ass2.i190426_i190435;

import java.util.ArrayList;
import java.util.List;

public class UtilityClassMusic {

    private static UtilityClassMusic instance;

    private List<Music> list=new ArrayList<>();

    public List<Music> getList() {
        return list;
    }

    public void setList(List<Music> list) {
        this.list = list;
    }

    private UtilityClassMusic(){}

    public static UtilityClassMusic getInstance(){
        if(instance == null){
            instance = new UtilityClassMusic();
        }
        return instance;
    }
}