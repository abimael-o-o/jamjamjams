using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public class EB_Move : PlayerMovement
{
    private Animator animator;
    private void Awake()
    {
        animator = GetComponent<Animator>();
        PlayerInputActions playerActions = new PlayerInputActions();
        playerActions.Enable();
        playerActions.Player.Move.performed += INPUT_VEL;
        playerActions.Player.Jump.performed += Player_Jump;
    }
    public override void Update()
    {
        base.Update();

        //Set animation states.
        animator.SetFloat("Vel_X", Mathf.Abs(rb.velocity.x));
        animator.SetInteger("Vel_Y",((int)rb.velocity.y));
    }
}
