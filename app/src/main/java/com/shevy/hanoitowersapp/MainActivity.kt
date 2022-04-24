package com.shevy.hanoitowersapp

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Point
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.Toast
import com.shevy.hanoitowersapp.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val ringDragMessage = "Ring Added"
    private val ringOn = "Bingo! Ring on base"
    private val ringOff: String = "Ring off"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        attachViewDragListener()
        binding.basesOfPyramids.setOnDragListener(ringDragListener)
    }

    // Creates a ring drag event listener
    private val ringDragListener = View.OnDragListener { view, dragEvent ->

        val draggableItem = dragEvent.localState as View

        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                // dims the view when the ring has entered the drop area
                binding.base1.alpha = 0.3f
                binding.mediumRing.alpha = 0.3f
                binding.largeRing.alpha = 0.3f
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                // reset opacity if the ring exits drop area without drop action
                binding.base1.alpha = 1.0f
                binding.mediumRing.alpha = 1.0f
                binding.largeRing.alpha = 1.0f
                //when ring exits drop-area without dropping set ring visibility to VISIBLE
                if (checkIfRingIsOnBase(dragEvent)) {
                draggableItem.visibility = View.VISIBLE
                view.invalidate()
                }
                checkIfRingIsOnBase(dragEvent)
                //true
            }
            DragEvent.ACTION_DROP -> {
                // reset opacity if the ring is dropped
                binding.base1.alpha = 1.0f
                binding.mediumRing.alpha = 1.0f
                binding.largeRing.alpha = 1.0f
                //on drop event in the target drop area, read the data and
                // re-position the ring in it's new location
                if (checkIfRingIsOnBase(dragEvent)) {
                    if (dragEvent.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        val draggedData = dragEvent.clipData.getItemAt(0).text
                        //println("draggedData $draggedData")
                    }

                    //re-position the ring in the updated x, y co-ordinates
                    // with ring center position pointing to new x,y co-ordinates
                    draggableItem.x = dragEvent.x - (draggableItem.width / 2)
                    draggableItem.y = dragEvent.y - (draggableItem.height / 2)

                    //on drop event remove the ring from parent viewGroup
                    val parent = draggableItem.parent as ConstraintLayout
                    parent.removeView(draggableItem)

                    //add the ring view to a new viewGroup where the ring was dropped
                    val dropArea = view as ConstraintLayout
                    dropArea.addView(draggableItem)
                }

                checkIfRingIsOnBase(dragEvent)
                //true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                draggableItem.visibility = View.VISIBLE
                if (checkIfRingIsOnBase(dragEvent)) {
                    view.invalidate()
                }
                //true
                checkIfRingIsOnBase(dragEvent)
            }
            else -> {
                false
            }
        }
    }

    /**
     * Method checks whether the mask is dropped on the face and
     * displays an appropriate Toast message
     *
     * @param dragEvent DragEvent
     */
    private fun checkIfRingIsOnBase(dragEvent: DragEvent): Boolean {
        //x,y co-ordinates left-top point
        val base2XStart = binding.base2.x
        val base2YStart = binding.base2.y

        //x,y co-ordinates bottom-end point
        val base2XEnd = base2XStart + binding.base2.width
        val base2YEnd = base2YStart + binding.base2.height

/*        val toastMsg =
            if (dragEvent.x in faceXStart..faceXEnd && dragEvent.y in faceYStart..faceYEnd) {
                ringOn
            } else {
                ringOff
            }*/
        //Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show()

/*        Log.d("testLog", "View1 = $view")
        Log.d("testLog", "dragEvent1 = $dragEvent")*/

        //x,y co-ordinates left-top point
        val ringSmallXStart = binding.smallRing.x
        val ringSmallYStart = binding.smallRing.y

        val ringMediumXStart = binding.mediumRing.x
        val ringMediumYStart = binding.mediumRing.y

        val ringLargeXStart = binding.largeRing.x
        val ringLargeYStart = binding.largeRing.y

        //x,y co-ordinates bottom-end point
        val ringSmallXEnd = ringSmallXStart + binding.smallRing.width
        val ringSmallYEnd = ringSmallYStart + binding.smallRing.height

        val ringMediumXEnd = ringMediumXStart + binding.mediumRing.width
        val ringMediumYEnd = ringMediumYStart + binding.mediumRing.height

        val ringLargeXEnd = ringLargeXStart + binding.largeRing.width
        val ringLargeYEnd = ringLargeYStart + binding.largeRing.height

/*
        Log.d(
            "testLog",
            "ringMediumYStart = $ringMediumYStart, ringMediumYEnd = $ringMediumYEnd, binding.mediumRing.width = ${binding.mediumRing.width} "
        )
        Log.d(
            "testLog",
            "ringSmallYStart = $ringSmallYStart, ringSmallYEnd = $ringSmallYEnd, binding.smallRing.width = ${binding.smallRing.width} "
        )
        Log.d(
            "testLog",
            "ringMediumXStart = $ringMediumXStart, ringMediumXEnd = $ringMediumXEnd, binding.mediumRing.height = ${binding.mediumRing.height} "
        )
        Log.d(
            "testLog",
            "ringSmallXStart = $ringSmallXStart, ringSmallXEnd = $ringSmallXEnd, binding.smallRing.height = ${binding.smallRing.height} "
        )
*/

        if (ringSmallXStart !in ringMediumXStart..ringMediumXEnd && ringSmallXStart !in ringLargeXStart..ringLargeXEnd) {
                Toast.makeText(this, "All right", Toast.LENGTH_LONG)
                    .show()
                return true
        } else if (ringSmallXStart in ringMediumXStart..ringMediumXEnd && ringSmallXStart in ringLargeXStart..ringLargeXEnd) {
            return if (ringSmallYStart < ringMediumYStart && ringSmallYStart < ringLargeYStart) {
                Toast.makeText(this, "All right", Toast.LENGTH_LONG)
                    .show()
                true
            } else {
                Toast.makeText(this, "You can't do it", Toast.LENGTH_LONG)
                    .show()
                return false
            }
        } else if (ringSmallXStart in ringLargeXStart..ringLargeXEnd) {
            return if (ringSmallYStart < ringLargeYStart) {
                Toast.makeText(this, "All right", Toast.LENGTH_LONG)
                    .show()
                true
            } else {
                Toast.makeText(this, "You can't do it", Toast.LENGTH_LONG)
                    .show()
                return false
            }
        } else if (ringSmallXStart in ringMediumXStart..ringMediumXEnd) {
            return if (ringSmallYStart < ringMediumYStart) {
                Toast.makeText(this, "All right", Toast.LENGTH_LONG)
                    .show()
                true
            } else {
                Toast.makeText(this, "You can't do it", Toast.LENGTH_LONG)
                    .show()
                return false
            }
        } else {
            Toast.makeText(this, "HZ", Toast.LENGTH_LONG)
                .show()
            return false
        }
    }


    /**
     * Method enables drag feature on the draggable view
     */
    private fun attachViewDragListener() {

        binding.smallRing.setOnLongClickListener { view: View ->

            // Create a new ClipData.Item with custom text data
            val item = ClipData.Item(ringDragMessage)

            // Create a new ClipData using a predefined label, the plain text MIME type, and
            // the already-created item. This will create a new ClipDescription object within the
            // ClipData, and set its MIME type entry to "text/plain"
            val dataToDrag = ClipData(
                ringDragMessage,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            // Instantiates the drag shadow builder.
            val maskShadow = RingDragShadowBuilder(view)

            // Starts the drag
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                //support pre-Nougat versions
                @Suppress("DEPRECATION")
                view.startDrag(dataToDrag, maskShadow, view, 0)
            } else {
                //supports Nougat and beyond
                view.startDragAndDrop(dataToDrag, maskShadow, view, 0)
            }

            view.visibility = View.INVISIBLE
            true
        }

        binding.mediumRing.setOnLongClickListener { view: View ->

            // Create a new ClipData.Item with custom text data
            val item = ClipData.Item(ringDragMessage)

            // Create a new ClipData using a predefined label, the plain text MIME type, and
            // the already-created item. This will create a new ClipDescription object within the
            // ClipData, and set its MIME type entry to "text/plain"
            val dataToDrag = ClipData(
                ringDragMessage,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            // Instantiates the drag shadow builder.
            val maskShadow = RingDragShadowBuilder(view)

            // Starts the drag
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                //support pre-Nougat versions
                @Suppress("DEPRECATION")
                view.startDrag(dataToDrag, maskShadow, view, 0)
            } else {
                //supports Nougat and beyond
                view.startDragAndDrop(dataToDrag, maskShadow, view, 0)
            }

            view.visibility = View.INVISIBLE
            true
        }

        binding.largeRing.setOnLongClickListener { view: View ->

            // Create a new ClipData.Item with custom text data
            val item = ClipData.Item(ringDragMessage)

            // Create a new ClipData using a predefined label, the plain text MIME type, and
            // the already-created item. This will create a new ClipDescription object within the
            // ClipData, and set its MIME type entry to "text/plain"
            val dataToDrag = ClipData(
                ringDragMessage,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            // Instantiates the drag shadow builder.
            val maskShadow = RingDragShadowBuilder(view)

            // Starts the drag
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                //support pre-Nougat versions
                @Suppress("DEPRECATION")
                view.startDrag(dataToDrag, maskShadow, view, 0)
            } else {
                //supports Nougat and beyond
                view.startDragAndDrop(dataToDrag, maskShadow, view, 0)
            }

            view.visibility = View.INVISIBLE
            true
        }

    }
}

/**
 * Drag shadow builder builds a shadow for the mask when drag is ongoing
 *
 * @param view View for which drag shadow has to be displayed
 */
private class RingDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {

    //set shadow to be the actual mask
    private val shadow = when (view) {
        binding.smallRing -> ResourcesCompat.getDrawable(
            view.context.resources,
            R.drawable.circle_small,
            view.context.theme
        )
        binding.mediumRing -> ResourcesCompat.getDrawable(
            view.context.resources,
            R.drawable.circle_medium,
            view.context.theme
        )
        else -> ResourcesCompat.getDrawable(
            view.context.resources,
            R.drawable.circle_big,
            view.context.theme
        )
    }

    // Defines a callback that sends the drag shadow dimensions and touch point back to the
    // system.
    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        // Sets the width of the shadow to full width of the original View
        val width: Int = view.width

        // Sets the height of the shadow to full height of the original View
        val height: Int = view.height

        // The drag shadow is a Drawable. This sets its dimensions to be the same as the
        // Canvas that the system will provide. As a result, the drag shadow will fill the
        // Canvas.
        shadow?.setBounds(0, 0, width, height)

        // Sets the size parameter's width and height values. These get back to the system
        // through the size parameter.
        size.set(width, height)

        // Sets the touch point's position to be in the middle of the drag shadow
        touch.set(width / 2, height / 2)
    }

    // Defines a callback that draws the drag shadow in a Canvas that the system constructs
    // from the dimensions passed in onProvideShadowMetrics().
    override fun onDrawShadow(canvas: Canvas) {
        // Draws the Drawable in the Canvas passed in from the system.
        shadow?.draw(canvas)
    }
}