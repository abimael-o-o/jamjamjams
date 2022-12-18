using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EB_Health : HealthScript
{
    protected override void DieCondition()
    {
        Debug.Log("Die or something.");
    }
}
