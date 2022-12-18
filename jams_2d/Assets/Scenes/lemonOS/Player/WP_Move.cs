using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public class WP_Move : PlayerMovement
{
    private void Awake()
    {
        PlayerInputActions playerInputActions = new PlayerInputActions();
        playerInputActions.Player.Enable();
        playerInputActions.Player.Jump.performed += Player_Jump;
        playerInputActions.Player.Move.performed += INPUT_VEL;
    }
}
