using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class WP_Health : HealthScript
{
    protected override void DieCondition()
    {
        Debug.Log("Die or something.");
    }
}
