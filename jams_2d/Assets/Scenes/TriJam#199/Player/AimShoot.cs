using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AimShoot : MonoBehaviour
{
    public Transform gun;
    public Transform crosshair;
    private void Start()
    {
        Cursor.lockState = CursorLockMode.Confined;
        Cursor.visible = false;
    }
    private void Update()
    {
        Vector2 mousePos = Input.mousePosition;
        Vector3 worldPos = Camera.main.ScreenToWorldPoint(new Vector3(mousePos.x, 
            Mathf.Clamp(mousePos.y, 200, 400), Camera.main.nearClipPlane));
        crosshair.position = new Vector3(crosshair.position.x, worldPos.y, crosshair.position.z);

        Vector3 Look = gun.InverseTransformPoint(crosshair.position);
        float angle = Mathf.Atan2(Look.y, Look.x) * Mathf.Rad2Deg;
        gun.Rotate(0, 0, angle);

        if (Input.GetKeyDown(KeyCode.Mouse0))
        {
            Shoot();
        }
    }
    public void Shoot()
    {

    }
}
