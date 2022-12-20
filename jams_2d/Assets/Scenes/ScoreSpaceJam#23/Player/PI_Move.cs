using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public class PI_Move : PlayerMovement
{
    public float rotationSpeed;
    private void Awake()
    {
        PlayerInputActions playerActions = new PlayerInputActions();
        playerActions.Enable();
        playerActions.Player.Move.performed += INPUT_VEL;
    }
    public override void Update()
    {
        Player_Move();
    }
    public override void Player_Move()
    {
        Vector2 moveDir = new Vector2(velX, velY);
        float inputMagnitude = Mathf.Clamp01(moveDir.magnitude);
        moveDir.Normalize();

        transform.Translate(moveDir * runSpeed * inputMagnitude * Time.deltaTime, Space.World);

        if(moveDir != Vector2.zero)
        {
            Quaternion toRotation = Quaternion.LookRotation(Vector3.forward, moveDir);
            transform.rotation = Quaternion.RotateTowards(transform.rotation, toRotation, rotationSpeed);
        }
    }
}
