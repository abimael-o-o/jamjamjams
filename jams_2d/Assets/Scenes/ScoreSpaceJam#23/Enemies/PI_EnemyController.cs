using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public abstract class PI_EnemyController : MonoBehaviour
{
    public float rotationSpeed;
    public float maxSpeed;
    public float speed;
    public Transform player;
    public Transform Earth;
    public float distanceToFollowPlayer;

    bool isPlayerClose = false;

    public float fireRate;
    private float nextFire = 0f;
    private void Start()
    {
        speed = maxSpeed;
    }
    //Attack Pattern.
    void Update()
    {
        CheckPlayerDistance();
        Vector3 targetPosition = isPlayerClose ? player.position : Earth.position;

        //Change speed based on distacne of target.
        if (Vector3.Distance(targetPosition, transform.position) <= 4) 
        {
            speed = 0;
        }
        else
        {
            speed = maxSpeed;
        }

        Vector2 moveDir = targetPosition - transform.position;
        float inputMagnitude = Mathf.Clamp01(moveDir.magnitude);
        moveDir.Normalize();

        transform.Translate(moveDir * speed * inputMagnitude * Time.deltaTime, Space.World);

        Quaternion toRotation = Quaternion.LookRotation(Vector3.forward, moveDir);
        transform.rotation = Quaternion.RotateTowards(transform.rotation, toRotation, rotationSpeed);

        //Shoot in move direction.
        if(Vector3.Distance(targetPosition, transform.position) <= 15 && Time.time > nextFire)
        {
            nextFire = Time.time + fireRate;
            Shoot();
        }
    }
    public void CheckPlayerDistance()
    {
        if(Vector3.Distance(player.position,transform.position) <= distanceToFollowPlayer)
        {
            isPlayerClose = true;
        }
        else
        {
            isPlayerClose = false;
        }
    }

    protected abstract void Shoot();
}
