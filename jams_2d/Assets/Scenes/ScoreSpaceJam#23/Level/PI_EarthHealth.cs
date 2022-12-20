using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PI_EarthHealth : HealthScript
{
    protected override void DieCondition()
    {
        Destroy(gameObject);
    }
}
