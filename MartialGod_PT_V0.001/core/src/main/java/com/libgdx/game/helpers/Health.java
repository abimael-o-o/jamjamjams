package com.libgdx.game.helpers;

public class Health {
    private int MaxHealth = 0;
    private int Health;
    private boolean IsDead;

    public Health(int h){
        MaxHealth = h;
        Health = MaxHealth;
        IsDead = false;
    }

    public void TakeDamage(int damage){
        Health -= damage;
        if(Health <= 0 ){
            IsDead = true;
        }
    }

    public boolean DeathState(){
        return IsDead;
    }

    public int GetHealth(){
        return Health;
    }
}
