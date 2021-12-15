package pCanvas

import Extensions.*
import Extensions.Circle
import Extensions.Rectangle
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

@Suppress("UNREACHABLE_CODE")
class Rectangle(context: Context ,var mtop:Int , var mleft:Int) : View(context)
{
    val mRect = Rectangle(
        (Screen_Size.Width / 2).toFloat()-50f, (Screen_Size.Height / 2).toFloat()-50f, (Screen_Size.Width / 2).toFloat() + 50f,
        (Screen_Size.Height / 2).toFloat() + 50f)



    private var mCircle = Circle(50f, 50f, 16.0f)
    private var TranslatedPoint=-1
    private var touchedPoint = Point((Screen_Size.Width / 2).toFloat(), (Screen_Size.Height / 2).toFloat())
    init{
        mRect.RectPaint.style= Paint.Style.STROKE
        mRect.RectPaint.color= Color.GRAY
        mRect.RectPaint.strokeWidth=2f
        mRect.RectPaint.strokeCap= Paint.Cap.SQUARE
        mRect.RectPaint.strokeJoin= Paint.Join.BEVEL
        mRect.RectPaint.setARGB(255,255,255,255)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas!!.drawRect(mRect.TopLeft.X,mRect.TopLeft.Y,mRect.BottomRight.X,mRect.BottomRight.Y,mRect.RectPaint)


        canvas.drawCircle(mRect.TopLeft.X,mRect.TopLeft.Y,mCircle.Raduis,mCircle.CPaint)
        canvas.drawCircle(mRect.BottomRight.X,mRect.BottomRight.Y,mCircle.Raduis,mCircle.CPaint)

        canvas.drawCircle(mRect.TopRight.X,mRect.TopRight.Y,mCircle.Raduis,mCircle.CPaint)
        canvas.drawCircle(mRect.BottomLeft.X,mRect.BottomLeft.Y,mCircle.Raduis,mCircle.CPaint)
    }

    fun LegalPoint(point: Point):Boolean
    {
        return ((point.Y > 0) and (point.Y<Screen_Size.Height-50) )
    }
    fun LegalTranslate(center:Point):Boolean
    {
        if(center.X+mRect.RectRadiusX <= Result_Image.mBitmap.width+mleft && center.X-mRect.RectRadiusX>=mleft
            &&center.Y-mRect.RectRadiusY>=mtop &&center.Y+mRect.RectRadiusY<= Result_Image.mBitmap.height+mtop)
            return  true
        return false
    }
    fun getTopLeft():Point{
        return mRect.TopLeft
    }
    public fun getButtomRight():Point{
        return mRect.BottomRight
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
                else if(mRect.Center.inPointRange(touchedPoint.X,touchedPoint.Y))
                {
                    TranslatedPoint=5
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
                        5->{
                            if(LegalTranslate(touchedPoint))
                            {
                                mRect.TranslateRect(touchedPoint.X,touchedPoint.Y)
                                mRect.CalculatetRectRC()
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

