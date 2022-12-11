using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public abstract class PlayerMovement : MonoBehaviour
{
    public float velX { get; set; }
    public float velY { get; set; } //Use if needed.
    public Rigidbody2D rb { get; set; }
    public bool isFacingRight { get; set; } = true;

    public Transform groundCheck;
    public float jumpForce = 10f;
    public float runSpeed = 5f;
    public LayerMask groundLayer;
    public virtual void Start()
    {
        rb = GetComponent<Rigidbody2D>();
    }
    public virtual void Update()
    {
        Player_Move();
        Flip();
    }
    public virtual void Player_Move()
    {
        rb.velocity = new Vector2(velX * runSpeed, rb.velocity.y);
    }
    public virtual void Player_Jump(InputAction.CallbackContext context)
    {
        if (IsGrounded())
        {
            Debug.Log("Jump");
            rb.velocity = new Vector2(0, jumpForce);
        }
    }

    //These functions cannot be overritten >>
    //
    //Only read input that is coming through x & y direction.
    public void INPUT_VEL(InputAction.CallbackContext context)
    {
        velX = context.ReadValue<Vector2>().x;
        velY = context.ReadValue<Vector2>().y;
    }
    //Check the state of player ground.
    public bool IsGrounded()
    {
        return Physics2D.OverlapCircle(groundCheck.position, 0.2f, groundLayer);
    }
    //Turn the player left or right orientation that it's moving.
    public void Flip()
    {
        if (isFacingRight && velX < 0f || !isFacingRight && velX > 0f)
        {
            isFacingRight = !isFacingRight;
            Vector3 localScale = transform.localScale;
            localScale.x *= -1f;
            transform.localScale = localScale;
        }
    }
}
