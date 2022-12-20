using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EnemyHealth : HealthScript
{
    protected override void DieCondition()
    {
        Debug.Log("Kill Enemy");
        Destroy(gameObject);
    }
}
