package com.suhotrub.conversations.ui.util.ui

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.View


class CustomDragShadow(val v: View) : View.DragShadowBuilder(v) {

    // The drag shadow image, defined as a drawable thing
    private val shadow = ColorDrawable(Color.LTGRAY);


    override fun onProvideShadowMetrics(outShadowSize: Point, outShadowTouchPoint: Point) {

        // Sets the width of the shadow to half the width of the original View
        val width = view.width

        // Sets the height of the shadow to half the height of the original View
        val height = view.height

        // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
        // Canvas that the system will provide. As a result, the drag shadow will fill the
        // Canvas.
        shadow.setBounds(0, 0, width, height);

        // Sets the size parameter's width and height values. These get back to the system
        // through the size parameter.
        outShadowSize.set(width, height);

        // Sets the touch point's position to be in the middle of the drag shadow
        outShadowTouchPoint.set(width / 2, height / 2);
    }

    // Defines a callback that draws the drag shadow in a Canvas that the system constructs
    // from the dimensions passed in onProvideShadowMetrics().
    override fun onDrawShadow(canvas: Canvas) {
        // Draws the ColorDrawable in the Canvas passed in from the system.
        shadow.draw(canvas)
    }
}