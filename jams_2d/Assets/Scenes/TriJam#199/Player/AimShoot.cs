using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.VFX;

public class AimShoot : MonoBehaviour
{
    public Transform gun;
    public Transform muzzle;

    public float defDistanceRay = 20;

    public EB_Move player;
    public float mouseSens = 5f;
    float yRotation = 0f;
    public Vector2 dir;
    private void Start()
    {
        Cursor.lockState = CursorLockMode.Confined;
        Cursor.visible = false;
    }
    private void Update()
    {
        //Rotation parameters for gun aim.
        float mouseX = Input.GetAxisRaw("Mouse Y") * mouseSens;
        yRotation += mouseX;
        yRotation = Mathf.Clamp(yRotation, -90f, 90f);
        gun.localRotation = Quaternion.Euler(0, 0, yRotation);
    }
    private void FixedUpdate()
    {
        //dir is needed for the raycast direction.
        dir = (player.isFacingRight) ? gun.right : -gun.right;
    }
}
