using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public abstract class HealthScript : MonoBehaviour
{
    public float maxHealth;
    public float health { get; set; }
    private void Start()
    {
        health = maxHealth;
    }
    public virtual void TakeDamage(float damage)
    {
        health -= damage;
        if(health <= 0f)
        {
            DieCondition();
        }
    }
    protected abstract void DieCondition();
}
