using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AimShoot : MonoBehaviour
{
    public Transform gun;
    public Transform crosshair;
    public Transform muzzle;

    private float defDistanceRay = 100;
    public LineRenderer lineRenderer;

    public EB_Move player;
    bool enemy = true;

    private void Start()
    {
        Cursor.lockState = CursorLockMode.Confined;
        Cursor.visible = false;
    }
    private void Update()
    {
        Vector2 mousePos = Input.mousePosition;
        Vector3 worldPos = Camera.main.ScreenToWorldPoint(new Vector3(mousePos.x, 
            Mathf.Clamp(mousePos.y, 200, 700), Camera.main.nearClipPlane));
        crosshair.position = new Vector3(crosshair.position.x, worldPos.y, crosshair.position.z);

        Vector3 Look = gun.InverseTransformPoint(crosshair.position);
        float angle = Mathf.Atan2(Look.y, Look.x) * Mathf.Rad2Deg;
        gun.Rotate(0, 0, angle);

    }
    private void FixedUpdate()
    {
        Vector2 dir = (player.isFacingRight) ? gun.right : -gun.right;
        if (Input.GetKey(KeyCode.Mouse0))
        {
            Shoot(muzzle.position, dir, enemy, "Enemy", 5);
        }
        if (Input.GetKey(KeyCode.Mouse1))
        {
            Shoot(muzzle.position, dir, !enemy, "Battery", 1);
        }
    }
    public void Shoot(Vector2 origin, Vector2 direction, bool targetEnemy, string target, float damage)
    {
        if (Physics2D.Raycast(origin, direction, defDistanceRay))
        {
            RaycastHit2D _hit = Physics2D.Raycast(origin, direction);
            Draw2DRay(origin, _hit.point);

            if(_hit.transform.gameObject.tag == target)
            {
                _hit.transform.gameObject.GetComponent<HealthScript>().TakeDamage(damage);
                if (!targetEnemy) { Drain(damage); }
            }
        }
        else
        {
            Draw2DRay(origin, direction * defDistanceRay);
        }
    }
    public void Draw2DRay(Vector2 startPos, Vector2 endPos)
    {
        lineRenderer.SetPosition(0, startPos);
        lineRenderer.SetPosition(1, endPos);
    }
    public void Drain(float add)
    {
        player.GetComponent<EB_Health>().AddHealth(add);
    }
}
