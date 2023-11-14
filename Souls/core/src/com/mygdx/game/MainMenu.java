package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;

public class MainMenu extends ScreenAdapter{
    
    public MainMenu(){
        System.out.println("Main Menu");
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyJustPressed(Keys.E)){
            System.out.println("Set the game screen.");
        }
    }
}
