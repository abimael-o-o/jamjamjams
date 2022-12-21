using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Bullet : MonoBehaviour
{
    public float life = 3;
    public float damage;
    private void Awake()
    {
        Destroy(gameObject, life);
    }
    private void OnTriggerEnter2D(Collider2D collision)
    {
        if(collision.gameObject.tag == "Player" || collision.gameObject.tag == "PlayerSystem")
        {
            collision.gameObject.GetComponent<HealthScript>().TakeDamage(damage);
        }
        Destroy(gameObject);
    }
}
