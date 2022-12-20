using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EB_Enemies_Health : HealthScript
{
    protected override void DieCondition()
    {
        Destroy(gameObject);
    }
}
