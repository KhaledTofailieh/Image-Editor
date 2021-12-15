package pCanvas

import Extensions.*
import Extensions.Circle
import Extensions.Rectangle
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class Circle(context: Context ,var mleft:Int , var mtop:Int) : View(context)
{


    var mCircle = Circle(Screen_Size.Width/2f, Screen_Size.Height/2f, 100f)
    private var touchedPoint = 0
     var Raduis=100
    private val eventPoint=Point(0f,0f)
    init{
       with(mCircle.CPaint){
           style=Paint.Style.STROKE
           strokeWidth=4f
           color=255

       }
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
     canvas!!.drawCircle(mCircle.Center.X,mCircle.Center.Y,mCircle.Raduis, mCircle.CPaint)

    }
    fun LegalPoint(point: Point):Boolean
    {
        if(point.X >= mleft&& point.X<(Result_Image.mImage!!.Width+mleft) && point.Y >=mtop && point.Y< (Result_Image.mImage!!.Height+mtop) )
        {
            return  true
        }
        return false
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        eventPoint.X=event!!.x
        eventPoint.Y=event.y
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (LegalPoint(eventPoint)) {
                    if (mCircle.Center.inPointRange(eventPoint.X, eventPoint.Y)) {
                        touchedPoint = 1
                        mCircle.Center.X = eventPoint.X
                        mCircle.Center.Y = eventPoint.Y
                    } else {
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (LegalPoint(eventPoint)) {
                    when (touchedPoint) {
                        1 -> {
                            mCircle.Center.X = eventPoint.X
                            mCircle.Center.Y = eventPoint.Y
                        }
                        else ->{}

                    }
                }
            }
        }
        postInvalidate()
        return super.onTouchEvent(event)
    }


}