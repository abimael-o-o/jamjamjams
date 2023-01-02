using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;
using UnityEngine.VFX;

public class LightningGun : AimShoot
{
    public Transform pos1;
    public Transform pos2;
    public Transform pos3;
    public Transform pos4;

    public VisualEffect lightningEffect;
    private void Awake()
    {
        PlayerInputActions inputActions = new PlayerInputActions();
        inputActions.Enable();
        inputActions.Player.Attack.started += InputAttack;
        inputActions.Player.Attack.performed += InputAttack;
        inputActions.Player.Attack.canceled += InputAttack;
    }

    public void InputAttack(InputAction.CallbackContext context)
    {
        if (context.performed)
        {
            lightningEffect.Play();
            lightningEffect.SetFloat("LifeTime", 3f);
            Shoot(muzzle.position, dir, "Enemy", 5);
        }
        if (context.canceled)
        {
            lightningEffect.SetFloat("LifeTime", 0.1f);
        }
    }
    public void Shoot(Vector2 origin, Vector2 direction, string target, float damage)
    {
        if (Physics2D.Raycast(origin, direction, defDistanceRay))
        {
            RaycastHit2D _hit = Physics2D.Raycast(origin, direction);
            Draw2DRay(origin, _hit.point);

            if (_hit.transform.gameObject.tag == target)
            {
                _hit.transform.gameObject.GetComponent<HealthScript>().TakeDamage(damage);
                if(_hit.transform.gameObject.name == "Battery") { Drain( 1f ); }
            }
        }
        else
        {
            Draw2DRay(origin, direction * defDistanceRay);
        }
    }
    public void Draw2DRay(Vector2 startPos, Vector2 endPos)
    {
        pos1.position = startPos;
        pos4.position = endPos;
        //pos2.position = startPos;
        //pos3.position = startPos;
    }
    public void Drain(float add)
    {
        player.GetComponent<EB_Health>().AddHealth(add);
    }
}
