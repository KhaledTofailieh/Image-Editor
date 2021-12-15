package pCanvas

import Extensions.*
import Extensions.Circle
import Extensions.Line
import android.content.Context
import android.graphics.Canvas

import android.view.MotionEvent
import android.view.View

@Suppress("UNREACHABLE_CODE")
class Line(context: Context , var mleft:Int,var mtop:Int) : View(context)
{
    val mLine=Line(Screen_Size.Width/2f,Screen_Size.Height/2f,(Screen_Size.Width/2f)+30, (Screen_Size.Height/2f)+30)
    val mCircle=Circle(50f, 50f, 16.0f)
    var TouchedPoint=0
    val eventPoint=Point(0f,0f)


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawLine(mLine.start.X,mLine.start.Y,mLine.end.X,mLine.end.Y,mLine.Lpaint)

        canvas.drawCircle(mLine.start.X,mLine.start.Y,mCircle.Raduis,mCircle.CPaint)
        canvas.drawCircle(mLine.end.X,mLine.end.Y,mCircle.Raduis,mCircle.CPaint)
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
       when(event.action){
           MotionEvent.ACTION_DOWN->{
               if(LegalPoint(eventPoint)) {
                   if (mLine.start.inPointRange(eventPoint.X, eventPoint.Y)) {
                       TouchedPoint = 1
                       mLine.start.X = eventPoint.X
                       mLine.start.Y = eventPoint.Y
                   }
                   else if (mLine.end.inPointRange(eventPoint.X, eventPoint.Y)) {
                       TouchedPoint = 2
                       mLine.end.X = eventPoint.X
                       mLine.end.Y = eventPoint.Y
                   } else {
                   }
               }
           }
           MotionEvent.ACTION_MOVE->{
               if(LegalPoint(eventPoint)){
               when(TouchedPoint){
                   1->{
                       mLine.start.X=eventPoint.X
                       mLine.start.Y=eventPoint.Y
                   }
                   2->{
                       mLine.end.X=eventPoint.X
                       mLine.end.Y=eventPoint.Y
                   }
               }
           }
           }
       }
        postInvalidate()
        return super.onTouchEvent(event)
    }

}