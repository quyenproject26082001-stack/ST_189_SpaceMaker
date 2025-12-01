package com.oc.christmas.ocmaker.create.avatar.listener.listenerdraw

import android.view.MotionEvent
import com.oc.christmas.ocmaker.create.avatar.core.custom.drawview.DrawView

class ZoomEvent : DrawEvent {
    override fun onActionDown(drawView: DrawView?, event: MotionEvent?) {}
    override fun onActionMove(drawView: DrawView?, event: MotionEvent?) {
        drawView!!.rotateZoomCurrentDraw(event!!)
    }
    override fun onActionUp(drawView: DrawView?, event: MotionEvent?) {
        if (drawView!!.getOnDrawListener() != null) {
            drawView.getOnDrawListener()!!.onZoomFinishedDraw(drawView.getCurrentDraw()!!)
        }
    }
}
