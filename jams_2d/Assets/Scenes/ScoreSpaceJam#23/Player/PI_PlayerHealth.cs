using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PI_PlayerHealth : HealthScript
{
    private void Update()
    {
        Debug.Log(health);
    }
    protected override void DieCondition()
    {
        Destroy(gameObject);
    }
}
