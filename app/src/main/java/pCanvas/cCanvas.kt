package pCanvas

import Extensions.*
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

@Suppress("UNREACHABLE_CODE")
class new_Image(context: Context): View(context) {
    private var imwidth = 0
    private var imheight = 0
    var TranslatedPoint=0
     val mRect = Rectangle(
        (Screen_Size.Width / 2).toFloat()-50f, (Screen_Size.Height / 2).toFloat()-50f, (Screen_Size.Width / 2).toFloat() + 50f,
        (Screen_Size.Height / 2).toFloat() + 50f)


    val sRect = Rectangle(
        (Screen_Size.Width / 2).toFloat()-50f, (Screen_Size.Height / 2).toFloat()-50f, (Screen_Size.Width / 2).toFloat() + 50f,
        (Screen_Size.Height / 2).toFloat() +50f)

    private var mCircle = Circle(50f, 50f, 16.0f)
    private var touchedPoint = Point((Screen_Size.Width / 2).toFloat(), (Screen_Size.Height / 2).toFloat())
    init{
        mRect.setPaintProperties(Paint.Style.FILL,Color.WHITE)
        sRect.RectPaint.style=Paint.Style.STROKE
        sRect.RectPaint.color=Color.GRAY
        sRect.RectPaint.strokeWidth=1.5f
        sRect.RectPaint.strokeCap=Paint.Cap.SQUARE
        sRect.RectPaint.strokeJoin=Paint.Join.BEVEL
        mRect.RectPaint.setARGB(255,255,255,255)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawRect(mRect.TopLeft.X,mRect.TopLeft.Y,mRect.BottomRight.X,mRect.BottomRight.Y,mRect.RectPaint)
        canvas!!.drawRect(mRect.TopLeft.X,mRect.TopLeft.Y,mRect.BottomRight.X,mRect.BottomRight.Y,sRect.RectPaint)


        canvas.drawCircle(mRect.TopLeft.X,mRect.TopLeft.Y,mCircle.Raduis,mCircle.CPaint)
        canvas.drawCircle(mRect.BottomRight.X,mRect.BottomRight.Y,mCircle.Raduis,mCircle.CPaint)

        canvas.drawCircle(mRect.TopRight.X,mRect.TopRight.Y,mCircle.Raduis,mCircle.CPaint)
        canvas.drawCircle(mRect.BottomLeft.X,mRect.BottomLeft.Y,mCircle.Raduis,mCircle.CPaint)
    }


    fun getimageWidth():Int {
      imwidth=Math.abs(mRect.TopRight.X-mRect.TopLeft.X).toInt()
        return imwidth
    }
    fun getimageHeight(): Int {
     imheight=Math.abs(mRect.TopLeft.Y - mRect.BottomLeft.Y).toInt()
        return imheight
    }
    fun setfullScreen() {
        with(mRect){
            TopLeft.X=0f
            TopLeft.Y=0f

            TopRight.X=Screen_Size.Width.toFloat()
            TopRight.Y=0f

            BottomLeft.X=0f
            BottomLeft.Y=Screen_Size.Height.toFloat()

            BottomRight.X=Screen_Size.Width.toFloat()
            BottomRight.Y=Screen_Size.Height.toFloat()

        }
        postInvalidate()
    }
    fun setMinimize()
    {
        with(mRect){
            TopLeft.X=(Screen_Size.Width.toFloat()/2)-50f
            TopLeft.Y=(Screen_Size.Height.toFloat()/2)-50

            TopRight.X=(Screen_Size.Width.toFloat()/2)+50f
            TopRight.Y=(Screen_Size.Height.toFloat()/2)-50

            BottomLeft.X=(Screen_Size.Width.toFloat()/2)-50
            BottomLeft.Y=(Screen_Size.Height.toFloat()/2)+50

            BottomRight.X=(Screen_Size.Width.toFloat()/2)+50
            BottomRight.Y=(Screen_Size.Height.toFloat()/2)+50

        }
        postInvalidate()
    }
    fun customHeight(height:Int)
    {
      val newHeight=height/2
        with(mRect){

            TopLeft.Y=(Screen_Size.Height.toFloat()/2)-newHeight

            TopRight.Y=(Screen_Size.Height.toFloat()/2)-newHeight

            BottomLeft.Y=(Screen_Size.Height.toFloat()/2)+newHeight

            BottomRight.Y=(Screen_Size.Height.toFloat()/2)+newHeight

        }
        postInvalidate()
    }
    fun customWidth(width:Int)
    {
        val newWidth=width/2
        with(mRect) {
            TopLeft.X = (Screen_Size.Width.toFloat() / 2) - newWidth
            TopRight.X = (Screen_Size.Width.toFloat() / 2) + newWidth
            BottomLeft.X = (Screen_Size.Width.toFloat() / 2) - newWidth
            BottomRight.X = (Screen_Size.Width.toFloat() / 2) + newWidth
        }

    }
    fun LegalPoint(point: Point):Boolean
    {
        return ((point.Y > 0) and (point.Y<Screen_Size.Height-50) )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        touchedPoint.X=event!!.x
        touchedPoint.Y=event.y
        when(event.action)
        {
            MotionEvent.ACTION_DOWN ->
            {
                    if(mRect.BottomRight.inPointRange(touchedPoint.X,touchedPoint.Y))
                    {
                        TranslatedPoint=1
                    }
                    else if(mRect.TopLeft.inPointRange(touchedPoint.X,touchedPoint.Y))
                    {
                        TranslatedPoint=2
                    }
                    else if (mRect.TopRight.inPointRange(touchedPoint.X,touchedPoint.Y))
                    {
                        TranslatedPoint=3
                    }
                    else if(mRect.BottomLeft.inPointRange(touchedPoint.X,touchedPoint.Y))
                    {
                        TranslatedPoint=4
                    }
                    else
                    {
                        TranslatedPoint=-1
                    }




            }
            MotionEvent.ACTION_MOVE ->
            {
                if(LegalPoint(touchedPoint))
                    when(TranslatedPoint)
                    {
                        1-> {
                            with(mRect)
                            {
                                BottomRight.TranslatePoint(touchedPoint.X,touchedPoint.Y)
                                BottomLeft.TranslatePoint(BottomLeft.X,touchedPoint.Y)
                                TopRight.TranslatePoint(touchedPoint.X,TopRight.Y)
                                CalculatetRectRC()
                            }
                        }
                        2-> {
                            with(mRect)
                            {
                                TopLeft.TranslatePoint(touchedPoint.X,touchedPoint.Y)
                                TopRight.TranslatePoint(TopRight.X,touchedPoint.Y)
                                BottomLeft.TranslatePoint(touchedPoint.X,BottomLeft.Y)
                                CalculatetRectRC()
                            }

                        }
                        3->{
                            with(mRect)
                            {
                                TopRight.TranslatePoint(touchedPoint.X,touchedPoint.Y)
                                TopLeft.TranslatePoint(TopLeft.X, touchedPoint.Y)
                                BottomRight.TranslatePoint(touchedPoint.X, BottomRight.Y)
                                CalculatetRectRC()
                            }
                        }
                        4->{
                            with(mRect)
                            {
                                BottomLeft.TranslatePoint(touchedPoint.X,touchedPoint.Y)
                                TopLeft.TranslatePoint(touchedPoint.X,TopLeft.Y)
                                BottomRight.TranslatePoint(BottomRight.X,touchedPoint.Y)
                                CalculatetRectRC()
                            }
                        }
                        else ->{}
                    }


                postInvalidate()
            }
        }
        return super.onTouchEvent(event)
    }

}