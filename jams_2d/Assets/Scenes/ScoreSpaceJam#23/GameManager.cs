using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameManager : MonoBehaviour
{
    float DayCount = 0;
    int timer;
    bool canChangeDay = true;

    void FixedUpdate()
    {
        timer = (int)Time.time;
        Debug.Log(timer);
        if(timer == 10 && canChangeDay)
        {
            //Reset day count.
            canChangeDay = false;
            DayCount++;
        }
    }
}
