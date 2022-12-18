using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class TerminalActions : MonoBehaviour
{
    public RawImage image;
    public Vector3 offset;
    private bool windowOn;
    private void OnTriggerEnter2D(Collider2D collision)
    {
        windowOn = true;
    }
    private void OnTriggerExit2D(Collider2D collision)
    {
        windowOn = false;
    }
    private void Update()
    {
        image.transform.position = Camera.main.WorldToScreenPoint(transform.position + offset);

        if (windowOn && Input.GetKeyDown(KeyCode.E))
        {
            image.gameObject.SetActive(true);
        }
    }
    public void SetWindowOff()
    {
        image.gameObject.SetActive(false);
    }
}
