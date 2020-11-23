package jp.rssh.TestApplication

import android.graphics.Color.green
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    val viewList:MutableList<View> = ArrayList<View>()
    var listCount:Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.textView).text = listCount.toString()

        findViewById<Button>(R.id.increase_button).setOnClickListener{view->
            addListCount(10)
        }

        findViewById<Button>(R.id.decrease_button).setOnClickListener{view->
            addListCount(-10)
        }
    }

    fun addListCount(num:Int){
        listCount = listCount+num
        if(listCount <= 1){
            listCount = 1
        }
        findViewById<TextView>(R.id.textView).text = listCount.toString()

        adjustListView(findViewById<ConstraintLayout>(R.id.main_layout))
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val (posx, posy) = getViewScreenPosition(R.id.main_layout)
        var points:MutableList<Point> = ArrayList<Point>()
        if(event is MotionEvent){
            points = getEventScreenPositions(event)
        }

        if(points.size == 0){
            return super.onTouchEvent(event)
        }

        points.forEach {
            val view:View = createImageViewGreen()
            view.x = (it.x - posx).toFloat()
            view.y = (it.y - posy).toFloat()

            viewList.add(view)

            val layout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.main_layout)
            layout.addView(view)
            adjustListView(layout)
        }

        return super.onTouchEvent(event)
    }

    fun adjustListView(layout:ConstraintLayout){
        if(viewList.size < listCount){
            return
        }

        val deleteItems:List<View> = viewList.take(viewList.size - listCount)

        deleteItems.forEach{
            layout.removeView(it)
            viewList.remove(it)
        }
    }

    fun createImageViewGreen():ImageView{
        val view:ImageView = ImageView(this)
        view.setImageResource(R.drawable.green)
        return view
    }

    fun getViewScreenPosition(id:Int):Pair<Int, Int>{
        val view:View = findViewById<View>(id)
        val pos:IntArray = IntArray(2)
        view.getLocationOnScreen(pos)
        return return pos[0] to pos[1]
    }

    fun getEventScreenPositions(event:MotionEvent):MutableList<Point>{
        val ret:MutableList<Point> = ArrayList<Point>()

        for(i in 0..event.pointerCount-1){
            val pid:Int = event.getPointerId(i)
            var x:Float = event.getX(i)
            var y:Float = event.getY(i)

            ret.add(Point(x.toInt(), y.toInt()))
        }

        return ret
    }
}