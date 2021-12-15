package pCanvas
import Extensions.*
import Extensions.Point
import Tools.Text
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View


@Suppress("UNREACHABLE_CODE")
class canvass( context: Context) : View(context) {

     var mleft=0
    var mtop=0
    var penType=DrawingButtons.Brush
    private var mRect=Rectangle((Screen_Size.Width/2).toFloat(),(Screen_Size.Height/2).toFloat(),(Screen_Size.Width/2).toFloat()+100f,(Screen_Size.Height/2).toFloat()+100f)
    private var mCircle=Circle(50f,50f,16.0f)
    private val strokePaint:Paint
    private var touchedPoint=Point((Screen_Size.Width/2).toFloat(),(Screen_Size.Height/2).toFloat())

    private val mListener:DrawerListener
    val cpoint=Point(touchedPoint.X,touchedPoint.Y)
    val ppoint=Point(touchedPoint.X,touchedPoint.Y)
    var TranslatedPoint=0
    var tool=Tool_Type.Null
    var text:String?=null

      init {
          mListener=context as DrawerListener
          strokePaint= Paint()
          strokePaint.style=Paint.Style.STROKE
          strokePaint.strokeWidth=1.5f
          strokePaint.color=Color.GRAY
          CalculatePointIndex(touchedPoint)
      }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas!!.drawBitmap(Result_Image.mBitmap!!,mleft.toFloat(),mtop.toFloat(),null)
        when(tool)
        {
          Tool_Type.Crop->  {



                canvas.drawRect(mRect.TopLeft.X,mRect.TopLeft.Y,mRect.BottomRight.X,mRect.BottomRight.Y,mRect.RectPaint)
              canvas.drawRect(mRect.TopLeft.X,mRect.TopLeft.Y,mRect.BottomRight.X,mRect.BottomRight.Y,strokePaint)

              canvas.drawCircle(mRect.TopLeft.X,mRect.TopLeft.Y,mCircle.Raduis,mCircle.CPaint)
                canvas.drawCircle(mRect.BottomRight.X,mRect.BottomRight.Y,mCircle.Raduis,mCircle.CPaint)

                canvas.drawCircle(mRect.TopRight.X,mRect.TopRight.Y,mCircle.Raduis,mCircle.CPaint)
                canvas.drawCircle(mRect.BottomLeft.X,mRect.BottomLeft.Y,mCircle.Raduis,mCircle.CPaint)
            }
            Tool_Type.Drawer-> {

            }
            Tool_Type.Text->
            {
                canvas.drawText(text!!,touchedPoint.X,touchedPoint.Y,Text.tPaint)
            }
        }
    }
    fun CalculateOffsetLeft_Top(Width:Int,Height:Int)
    {
        mleft=(Screen_Size.Width-Width)/2
        mtop=(Screen_Size.Height-Height)/2
    }
    fun getCropperLT():Point
    {
        val Left_top=Point(0f,0f)
        Left_top.X=Math.abs(mRect.TopLeft.X-mleft)
        Left_top.Y=Math.abs(mRect.TopLeft.Y-mtop)
        return Left_top
    }
    fun getCropperRB():Point
    {
        val bottom_right=Point(0f,0f)
        bottom_right.X=Math.abs(mRect.BottomRight.X-mleft)
        bottom_right.Y=Math.abs(mRect.BottomRight.Y-mtop)
        return bottom_right
    }
    fun CalculatePointIndex(point: Point=touchedPoint):Point
    {
        val point_in_image=Point(0f,0f)
        point_in_image.X=point.X-mleft
        point_in_image.Y=point.Y-mtop
        return point_in_image
    }
    fun LegalPoint(point: Point):Boolean
    {
      if(point.X >= mleft&& point.X<(Result_Image.mImage!!.Width+mleft) && point.Y >=mtop && point.Y< (Result_Image.mImage!!.Height+mtop) )
      {
        return  true
      }
        return false
    }
    fun CalculatePoints(prevPoint: Point,currentPoint: Point):ArrayList<Point>
    {
        var distance=0.0
        var Xinterval=0.0
        var Yinterval=0.0
        var pointsNum=0.0
        val PointsList=ArrayList<Point>()
        var nX=0f
        var nY=0f
        var size=0f
        size=if((penType==DrawingButtons.Brush) or (penType==DrawingButtons.Rubber) )
        {
            Brush_Features.size+1f
        }else
        {
            0.1f
        }
         distance=Math.sqrt((currentPoint.X-prevPoint.X).toDouble()*(currentPoint.X-prevPoint.X)+
                (currentPoint.Y-prevPoint.Y)*(currentPoint.Y-prevPoint.Y))
         pointsNum=distance/size


         Xinterval=(currentPoint.X-prevPoint.X)/(pointsNum)
         Yinterval=(currentPoint.Y-prevPoint.Y)/(pointsNum)

       for(i in 0 until pointsNum.toInt())
        {
            nX=prevPoint.X+(i.toFloat()*Xinterval.toFloat())
            nY=prevPoint.Y+(i.toFloat()*Yinterval.toFloat())

            PointsList.add(Point(nX,nY))
        }
     return PointsList
    }
    fun LegalTranslate(center:Point):Boolean
    {
        if(center.X+mRect.RectRadiusX <= Result_Image.mBitmap.width+mleft && center.X-mRect.RectRadiusX>=mleft
            &&center.Y-mRect.RectRadiusY>=mtop &&center.Y+mRect.RectRadiusY<=Result_Image.mBitmap.height+mtop)
            return  true
        return false
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean
    {
         touchedPoint.X=event!!.x
         touchedPoint.Y=event.y


       when(tool)
        {
           Tool_Type.Crop ->
           {
               when(event.action)
               {
                   MotionEvent.ACTION_DOWN ->
                   {
                       if(LegalPoint(touchedPoint)) {
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
                       }else {TranslatedPoint =-1}



                   }
                   MotionEvent.ACTION_MOVE ->
                   {
                       if(LegalPoint(touchedPoint))
                       {
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
                               else -> { }
                           }
                       }

                       postInvalidate()
                   }
               }
           }
           Tool_Type.Drawer ->
           {
               when(event.action)
               {
                   MotionEvent.ACTION_DOWN->
                   {
                       if(LegalPoint(touchedPoint))
                       {

                         val p=  CalculatePointIndex(touchedPoint)

                           cpoint.X=touchedPoint.X
                           cpoint.Y=touchedPoint.Y

                           ppoint.X=touchedPoint.X
                           ppoint.Y=touchedPoint.Y

                           val points= arrayListOf(p)
                           mListener.onDrawing(points,Brush_Features.color,Brush_Features.size,Brush_Features.type)
                       }
                   }
                   MotionEvent.ACTION_MOVE->
                   {
                       if((LegalPoint(touchedPoint)) and (penType != DrawingButtons.Fill))
                       {

                           cpoint.X=touchedPoint.X
                           cpoint.Y=touchedPoint.Y

                           val c= CalculatePointIndex(cpoint)

                           if(LegalPoint(ppoint)){
                               val p=CalculatePointIndex(ppoint)
                               mListener.onDrawing(CalculatePoints(p,c),Brush_Features.color,Brush_Features.size,Brush_Features.type)
                               ppoint.X=touchedPoint.X
                               ppoint.Y=touchedPoint.Y
                           }else{

                               with(ppoint){
                                   X=touchedPoint.X
                                   Y=touchedPoint.Y
                               }
                               val p=CalculatePointIndex(ppoint)
                               val points= arrayListOf(p)
                               mListener.onDrawing(points,Brush_Features.color,Brush_Features.size,Brush_Features.type)


                           }
                       }
                       else {
                           with(cpoint){
                               X=touchedPoint.X
                               Y=touchedPoint.Y
                           }
                          with(ppoint){
                              X=touchedPoint.X
                              Y=touchedPoint.Y
                          }

                       }

                   }
               }
           }
           Tool_Type.Text->
           {
               touchedPoint.X=event.x
               touchedPoint.Y=event.y
               CalculatePointIndex(touchedPoint)
               invalidate()
           }
        }
        return super.onTouchEvent(event)
    }
    fun resetTochedPoint()
     {
      touchedPoint.X=(Screen_Size.Width/2 ).toFloat()
         touchedPoint.Y=(Screen_Size.Height/2).toFloat()
     }
    fun resetCropper()
    {
        with(mRect){
            TopLeft.X=(Screen_Size.Width/2).toFloat()
            TopLeft.Y=(Screen_Size.Height/2).toFloat()

            TopRight.X=(Screen_Size.Width/2).toFloat()+100f
            TopRight.Y=(Screen_Size.Height/2).toFloat()

            BottomLeft.X=(Screen_Size.Width/2).toFloat()
            BottomLeft.Y=(Screen_Size.Height/2).toFloat()+100f


            BottomRight.X=(Screen_Size.Width/2).toFloat()+100f
            BottomRight.Y=(Screen_Size.Height/2).toFloat()+100f
        }
        with(mCircle)
        {
            Center.X=50f
            Center.Y=50f
        }

    }

    fun Invalidate()
    {
        invalidate()
    }

}

