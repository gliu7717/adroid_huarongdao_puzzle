package com.huarongdao

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.widget.ImageView
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private var  blockers : Array<ImageView?> = arrayOfNulls(12)
    private var currentView: ImageView? = null
    private val topMargin = 139
    private val precision = 5

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        blockers[0] = findViewById<ImageView> (R.id.iv_zhangfei)
        blockers[1] = findViewById<ImageView> (R.id.iv_caocao)
        blockers[2] = findViewById<ImageView> (R.id.iv_zhaoyun)
        blockers[3] = findViewById<ImageView> (R.id.iv_machao)
        blockers[4] = findViewById<ImageView> (R.id.iv_guanyu)
        blockers[5] = findViewById<ImageView> (R.id.iv_huangzhong)
        blockers[6] = findViewById<ImageView> (R.id.iv_solider1)
        blockers[7] = findViewById<ImageView> (R.id.iv_solider2)
        blockers[8] = findViewById<ImageView> (R.id.iv_solider3)
        blockers[9] = findViewById<ImageView> (R.id.iv_blank1)
        blockers[10] = findViewById<ImageView> (R.id.iv_blank2)
        blockers[11] = findViewById<ImageView> (R.id.iv_solider4)

        val scale = resources.displayMetrics.density
        Log.i("GTAG", scale.toString())
        val vto:ViewTreeObserver? = blockers[11]?.getViewTreeObserver()
        if (vto != null) {
            vto.addOnGlobalLayoutListener(
                object: ViewTreeObserver.OnGlobalLayoutListener{
                    override fun onGlobalLayout() {
                    }
                }
            )
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }

    private var x = 0f
    private var y = 0f
    private var dx = 0f
    private var dy = 0f
    private var original_x = 0f
    private var original_y = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i("GTAG", event?.toString().toString())

        if(event?.action == MotionEvent.ACTION_DOWN)
        {
            x = event.x
            y = event.y
            currentView = getCurrentView(x,y )
            if(currentView!=null) {
                original_x = currentView?.x!!
                original_y = currentView?.y!!
            }
           // Log.i("GTAG", x.toString() + ":" + y.toString())
        }
        else if(event?.action == MotionEvent.ACTION_MOVE)
        {
            dx = event.x -x
            dy = event.y - y
            if(currentView!=null) {
                currentView?.x = currentView?.x?.plus(dx)!!
                currentView?.y = currentView?.y?.plus(dy)!!
            }

            Log.i("GTAG", x.toString() + ":" + y.toString())

            x = event.x
            y = event.y
        }
        else if(event?.action == MotionEvent.ACTION_UP)
        {
            if(currentView!=null) {
                dx = currentView?.x!! - original_x
                dy = currentView?.y!! - original_y
                Log.i("GTAG", "dx:" + dx.toString() + ":dy:" + dy.toString())
                currentView?.x = original_x
                currentView?.y = original_y
                var dir = getDirection (dx, dy)
                Log.i("GTAG", "dir:" + dir.toString())
                if(dir == 1)
                    moveLeft(currentView)
                if(dir == 2)
                    moveRight(currentView)
                if(dir == 3)
                    moveUp(currentView)
                if(dir == 4)
                    moveDown(currentView)

            }
        }
        return super.onTouchEvent(event)
    }

    private fun moveLeft(view:ImageView?):Unit
    {
        val neighbours: Array<ImageView?>? = getNeighbourBlankBlockViews(view,1)
        if(neighbours!= null && view != null)
        {
            Log.i("GTAG", "neibour and view are not null")
            val w = blockers[9]?.width
            if(neighbours[0]!=null)
            {
                Log.i("GTAG", neighbours[0].toString().toString() )
                val loc = IntArray(2)
                neighbours[0]?.getLocationOnScreen(loc)
                view!!.x = view.x.plus(-w!!)
                neighbours[0]?.x = neighbours[0]?.x?.plus(view!!.width)!!
            }
            if(neighbours[1]!=null)
            {
                neighbours[1]?.x = neighbours[1]?.x?.plus(view!!.width)!!
            }
        }
    }

    private fun moveRight(view:ImageView?):Unit
    {
        val neighbours: Array<ImageView?>? = getNeighbourBlankBlockViews(view,2)
        if(neighbours!= null && view != null)
        {
            Log.i("GTAG", "neibour and view are not null")
            val w = blockers[9]?.width
            if(neighbours[0]!=null)
            {
                Log.i("GTAG", neighbours[0].toString().toString() )
                val loc = IntArray(2)
                neighbours[0]?.getLocationOnScreen(loc)
                view!!.x = view.x.plus(w!!)
                neighbours[0]?.x = neighbours[0]?.x?.plus(-view!!.width)!!
            }
            if(neighbours[1]!=null)
            {
                neighbours[1]?.x = neighbours[1]?.x?.plus(-view!!.width)!!
            }
        }
    }

    private fun moveDown(view:ImageView?):Unit
    {
        val neighbours: Array<ImageView?>? = getNeighbourBlankBlockViews(view,4)
        if(neighbours!= null && view != null)
        {
            Log.i("GTAG", "neibour and view are not null")
            val h = blockers[9]?.height
            if(neighbours[0]!=null)
            {
                Log.i("GTAG", neighbours[0].toString().toString() )
                val loc = IntArray(2)
                neighbours[0]?.getLocationOnScreen(loc)
                view!!.y = view.y.plus(h!!)
                neighbours[0]?.y = neighbours[0]?.y?.plus(-view!!.height)!!
            }
            if(neighbours[1]!=null)
            {
                neighbours[1]?.y = neighbours[1]?.y?.plus(-view!!.height)!!
            }
        }
    }

    private fun moveUp(view:ImageView?):Unit
    {
        val neighbours: Array<ImageView?>? = getNeighbourBlankBlockViews(view,3)
        if(neighbours!= null && view != null)
        {
            Log.i("GTAG", "neibour and view are not null")
            val h = blockers[9]?.height
            if(neighbours[0]!=null)
            {
                Log.i("GTAG", neighbours[0].toString().toString() )
                val loc = IntArray(2)
                neighbours[0]?.getLocationOnScreen(loc)
                view!!.y = view.y.plus(-h!!)
                neighbours[0]?.y = neighbours[0]?.y?.plus(view!!.height)!!
            }
            if(neighbours[1]!=null)
            {
                neighbours[1]?.y = neighbours[1]?.y?.plus(view!!.height)!!
            }
        }
    }

    private fun getLeftViews(view:ImageView?):Array<ImageView?>?
    {
        var neighbourViews: Array<ImageView?> = arrayOfNulls(2)
        if(view==null)
            return neighbourViews

        val viewloc = IntArray(2)
        val blank1loc = IntArray(2)
        val blank2loc = IntArray(2)
        view?.getLocationOnScreen(viewloc)
        blockers[9]?.getLocationOnScreen(blank1loc)
        blockers[10]?.getLocationOnScreen(blank2loc)
        Log.i("GTAG", "view:" + viewloc[0].toString() + ":" + viewloc[1].toString())
        Log.i("GTAG", "loc1:" + blank1loc[0].toString() + ":" + blank1loc[1].toString())
        Log.i("GTAG", "loc2:" + blank2loc[0].toString() + ":" + blank2loc[1].toString())
        val w = blockers[9]?.width
        val h = blockers[9]?.height
        if( abs( view.height - h!!) <= precision)
        {
            Log.i("GTAG", "single block " + view.width.toString())
            if( abs(viewloc[0] -  blank1loc[0] - w!!)<= precision &&
                abs(viewloc[1] - blank1loc[1]) <= precision )
                neighbourViews[0] = blockers[9]
            if( abs(viewloc[0]  - blank2loc[0] - w )<= precision &&
                abs(viewloc[1] - blank2loc[1]) <= precision )
                neighbourViews[0] = blockers[10]
        }
        if (w != null && h!=null) {
            if( abs(view.height - h * 2) <= precision) {
                if ( abs(viewloc[0] - blank1loc[0] - w) <= precision  &&
                    abs(viewloc[0] - blank2loc[0] - w) <= precision ){
                    if(  abs( viewloc[1] - blank1loc[1] ) <= precision &&
                        abs( viewloc[1] + h  - blank2loc[1] ) <= precision)
                    {
                        neighbourViews[0] = blockers[9]
                        neighbourViews[1] = blockers[10]
                    }
                    if(  abs( viewloc[1] - blank2loc[1] ) <= precision &&
                        abs( viewloc[1] +h  - blank1loc[1] ) <= precision)
                    {
                        neighbourViews[0] = blockers[10]
                        neighbourViews[1] = blockers[9]
                    }
                }
            }
        }

        return neighbourViews
    }


    private fun getRightViews(view:ImageView?):Array<ImageView?>?
    {
        var neighbourViews: Array<ImageView?> = arrayOfNulls(2)
        if(view==null)
            return neighbourViews

        val viewloc = IntArray(2)
        val blank1loc = IntArray(2)
        val blank2loc = IntArray(2)
        view?.getLocationOnScreen(viewloc)
        blockers[9]?.getLocationOnScreen(blank1loc)
        blockers[10]?.getLocationOnScreen(blank2loc)
        Log.i("GTAG", "view:" + viewloc[0].toString() + ":" + viewloc[1].toString())
        Log.i("GTAG", "loc1:" + blank1loc[0].toString() + ":" + blank1loc[1].toString())
        Log.i("GTAG", "loc2:" + blank2loc[0].toString() + ":" + blank2loc[1].toString())
        val w = blockers[9]?.width
        val h = blockers[9]?.height
        if(abs(view.height - h!!) <= precision)
        {
            Log.i("GTAG", "single block " + view.width.toString())
            if( abs(blank1loc[0] - viewloc[0] - view.width   )<= precision &&
                abs(viewloc[1] - blank1loc[1]) <= precision )
                neighbourViews[0] = blockers[9]
            if( abs(blank2loc[0] - viewloc[0] - view.width   )<= precision &&
                abs(viewloc[1] - blank2loc[1]) <= precision )
                neighbourViews[0] = blockers[10]
        }
        if (w != null && h!=null) {
            if( abs(view.height - h * 2) <= precision) {
                if ( abs(blank1loc[0] - viewloc[0] - view.width) <= precision  &&
                    abs(blank2loc[0] - viewloc[0] - view.width) <= precision ){

                    if(  abs( viewloc[1] - blank1loc[1] ) <= precision &&
                        abs( viewloc[1] + h  - blank2loc[1] ) <= precision)
                    {
                        neighbourViews[0] = blockers[9]
                        neighbourViews[1] = blockers[10]
                    }
                    if(  abs( viewloc[1] - blank2loc[1] ) <= precision &&
                        abs( viewloc[1] +h  - blank1loc[1] ) <= precision)
                    {
                        neighbourViews[0] = blockers[10]
                        neighbourViews[1] = blockers[9]
                    }
                }
            }
        }
        return neighbourViews
    }

    private fun getDownViews(view:ImageView?):Array<ImageView?>?
    {
        var neighbourViews: Array<ImageView?> = arrayOfNulls(2)
        if(view==null)
            return neighbourViews

        val viewloc = IntArray(2)
        val blank1loc = IntArray(2)
        val blank2loc = IntArray(2)
        view?.getLocationOnScreen(viewloc)
        blockers[9]?.getLocationOnScreen(blank1loc)
        blockers[10]?.getLocationOnScreen(blank2loc)
        Log.i("GTAG", "view:" + viewloc[0].toString() + ":" + viewloc[1].toString())
        Log.i("GTAG", "loc1:" + blank1loc[0].toString() + ":" + blank1loc[1].toString())
        Log.i("GTAG", "loc2:" + blank2loc[0].toString() + ":" + blank2loc[1].toString())
        val w = blockers[9]?.width
        Log.i("GTAG", "view width block " + view.width.toString())
        Log.i("GTAG", "w " + w.toString())
        if(abs(view.width - w!!) <= precision)
        {
            Log.i("GTAG", "single block " + view.width.toString())
            if( abs(blank1loc[1] - viewloc[1] - view.height   )<= precision &&
                abs(viewloc[0] - blank1loc[0]) <= precision )
                neighbourViews[0] = blockers[9]
            if( abs(blank2loc[1] - viewloc[1] - view.height   )<= precision &&
                abs(viewloc[0] - blank2loc[0]) <= precision )
                neighbourViews[0] = blockers[10]
        }
        if (w != null) {
            if( abs(view.width - w * 2) <= precision) {
                if ( abs(blank1loc[1] - viewloc[1] - view.height) <= precision  &&
                     abs(blank2loc[1] - viewloc[1] - view.height) <= precision ){

                    if(  abs( viewloc[0] - blank1loc[0] ) <= precision &&
                         abs( viewloc[0] +w  - blank2loc[0] ) <= precision)
                    {
                        neighbourViews[0] = blockers[9]
                        neighbourViews[1] = blockers[10]
                    }
                    if(  abs( viewloc[0] - blank2loc[0] ) <= precision &&
                        abs( viewloc[0] +w  - blank1loc[0] ) <= precision)
                    {
                        neighbourViews[0] = blockers[10]
                        neighbourViews[1] = blockers[9]
                    }
                }
            }
        }
        return neighbourViews
    }

    private fun getUpViews(view:ImageView?):Array<ImageView?>?
    {
        var neighbourViews: Array<ImageView?> = arrayOfNulls(2)
        if(view==null)
            return neighbourViews

        val viewloc = IntArray(2)
        val blank1loc = IntArray(2)
        val blank2loc = IntArray(2)
        view?.getLocationOnScreen(viewloc)
        blockers[9]?.getLocationOnScreen(blank1loc)
        blockers[10]?.getLocationOnScreen(blank2loc)
        Log.i("GTAG", "view:" + viewloc[0].toString() + ":" + viewloc[1].toString())
        Log.i("GTAG", "loc1:" + blank1loc[0].toString() + ":" + blank1loc[1].toString())
        Log.i("GTAG", "loc2:" + blank2loc[0].toString() + ":" + blank2loc[1].toString())
        val h = blockers[9]?.height
        val w = blockers[9]?.width
        Log.i("GTAG", "view width block " + view.width.toString())
        Log.i("GTAG", "w " + h.toString())
        if(abs(view.width - w!!) <= precision)
        {
            Log.i("GTAG", "single block " + view.width.toString())
            if( abs(viewloc[1] - blank1loc[1] - h!!)<= precision &&
                abs(viewloc[0] - blank1loc[0]) <= precision )
                neighbourViews[0] = blockers[9]
            if( abs(viewloc[1] - blank2loc[1] - h   )<= precision &&
                abs(viewloc[0] - blank2loc[0]) <= precision )
                neighbourViews[0] = blockers[10]
        }
        if (w != null && h!=null) {
            if( abs(view.width - w * 2) <= precision) {
                if ( abs(viewloc[1] - blank1loc[1] - h) <= precision  &&
                    abs(viewloc[1] -blank2loc[1]  - h) <= precision ){

                    if(  abs( viewloc[0] - blank1loc[0] ) <= precision &&
                        abs( viewloc[0] +w  - blank2loc[0] ) <= precision)
                    {
                        neighbourViews[0] = blockers[9]
                        neighbourViews[1] = blockers[10]
                    }
                    if(  abs( viewloc[0] - blank2loc[0] ) <= precision &&
                        abs( viewloc[0] +w  - blank1loc[0] ) <= precision)
                    {
                        neighbourViews[0] = blockers[10]
                        neighbourViews[1] = blockers[9]
                    }
                }
            }
        }
        return neighbourViews
    }


    private fun getNeighbourBlankBlockViews(
                view:ImageView?, dir: Int):Array<ImageView?>?
    {
        var neighbourViews: Array<ImageView?> = arrayOfNulls(2)
        if(view == null)
            return neighbourViews
        if(dir == 1) // left
            return getLeftViews(view)
        if(dir == 2) // right
            return getRightViews(view)
        if(dir == 3) // up
            return getUpViews(view)
        if(dir == 4) // down
            return getDownViews(view)
        return neighbourViews
    }

    private fun getDirection(dx: Float, dy: Float):Int
    {
        var dir = 0  // 1: left
        // 2: right
        // 3: up
        // 4: down
        Log.i("GTAG", "dir check dx:" + dx.toString() + ":dy:" + dy.toString())
        if(dx == 0f && dy == 0f)
            return dir
        if(dx >= 0 && dy >= 0) {
            dir = if(dx > dy)
                2// move right
            else
                4 // move down
        }
        else if(dx <= 0 && dy <= 0){
            dir = if(dx < dy)
                1// move left
            else
                3 // move down
        }
        else if(dx >= 0 && dy <= 0){
            dir = if(dx < -dy)
                1// move right
            else
                3 // move up
        }
        else if(dx <= 0 && dy >= 0){
            dir = if(dy > -dx)
                4// move down
            else
                1 // move left
        }
        return dir
    }

    private fun getCurrentView(x:Float, y:Float):ImageView?
    {
        currentView = null
        for(blockView in blockers) {
            val location = IntArray(2)
            blockView?.getLocationOnScreen(location)
            Log.i("GTAG", "location:" + location[0].toString() + ":" + location[1].toString())
            val w = blockView?.width
            val h = blockView?.height
            Log.i("GTAG", "size:" + w.toString() + ":" + h.toString())
            val right = location[0] + w!!
            val bottom = location[1] + h!!
            Log.i("GTAG", "x:" + x.toString() + ": y " + y.toString())
            Log.i("GTAG", "right:" + right.toString() + ": bottom " + bottom.toString())

            if (x > location[0] && x < location[0] + w!! &&
                y > location[1] && y < location[1] + h!!)
            {
                currentView = blockView
                Log.i("GTAG", currentView.toString().toString())
                break
            }
        }
        return currentView
    }
}