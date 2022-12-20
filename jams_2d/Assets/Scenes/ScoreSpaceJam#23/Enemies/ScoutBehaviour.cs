using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ScoutBehaviour : PI_EnemyController
{
    public GameObject bulletPrefab;
    public Transform bulletSpawn1;
    public Transform bulletSpawn2;
    public float bulletSpeed;
    protected override void Shoot()
    {
        GameObject bullet = Instantiate(bulletPrefab, bulletSpawn1.position, bulletSpawn1.rotation);
        bullet.GetComponent<Rigidbody2D>().velocity = bulletSpawn1.up * bulletSpeed;

        GameObject bullet2 = Instantiate(bulletPrefab, bulletSpawn2.position, bulletSpawn2.rotation);
        bullet2.GetComponent<Rigidbody2D>().velocity = bulletSpawn2.up * bulletSpeed;
    }
}
