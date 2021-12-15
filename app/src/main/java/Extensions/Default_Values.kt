package Extensions

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.os.Environment
import srcImageIO.Image

import java.nio.IntBuffer

object FilePath
{

    val path= Environment.getExternalStorageDirectory().absolutePath+"/myApp"
}
 object Brush_Features
{
    var color:Int=0
    var size =0
    var type=DrawingButtons.Brush
}
object App_Levels
{
    val main_menu="1"
    val selector_menu="2"
    val tool_menu="3"
}
object Result_Image
{
    var mImage: Image?=null
    lateinit  var mBuffer:IntBuffer
    lateinit var mBitmap:Bitmap

}
object Screen_Size
{
     var Height:Int=0
    var Width=0
}
object Zooming_Rates
{
    val rates= arrayOf(4,3,2,1,2,3,4)
    var rate=0
}
object Current_Color
{
    var color=0
}
enum class Fragment_Level
{
    in_main_menu, in_selector , in_tool
}
enum class Tool_Type
{
    Drawer,Crop,Text,HueSaturation,Brightness,EditColors,Null
    ,Rectangle,Circle,Line
}
enum class Filter_Types
{
    Hue,Saturation,None,All
}
object AnimDuration
{
    var Translate=400L
    var Click=250L
    var Show=500L
}
class Point(x: Float, y: Float)
{
    var X= x
    var Y= y
    fun inPointRange(x:Float,y:Float):Boolean
    {
        if(x>X-25f && x < X +25 && y > Y-25 && y< Y+25)
        {
            return true
        }
        return false
    }

    fun TranslatePoint(x:Float,y:Float)
    {
        X=x
        Y=y
    }

}
class Rectangle(tx:Float,ty:Float,bx:Float,by:Float)
{
     val Center:Point
     val TopLeft:Point
     val TopRight:Point
     val BottomRight:Point
     val BottomLeft:Point
     val RectPaint: Paint
    var RectRadiusX=0f
    var RectRadiusY=0f

    init {
        TopLeft= Point(tx,ty)
        TopRight= Point(bx,ty)
        BottomLeft= Point(tx,by)
        BottomRight= Point(bx,by)
        Center= Point(0f,0f)
        CalculatetRectRC()

        RectPaint= Paint()
        RectPaint.style= Paint.Style.STROKE
        RectPaint.strokeWidth=3.5f
        RectPaint.color= Color.WHITE
        RectPaint.isAntiAlias=true
        RectPaint.isDither=true

    }
    fun TranslateRect(x:Float,y:Float)
    {
        Center.X=x
        Center.Y=y

        TopLeft.X=Center.X-RectRadiusX
        TopLeft.Y=Center.Y-RectRadiusY

        BottomRight.X=Center.X+RectRadiusX
        BottomRight.Y=Center.Y+RectRadiusY

        TopRight.X=BottomRight.X
        TopRight.Y=TopLeft.Y

        BottomLeft.X=TopLeft.X
        BottomLeft.Y=BottomRight.Y
    }
    fun CalculatetRectRC()
    {

        RectRadiusX=Math.abs(BottomRight.X-TopLeft.X)/2
        RectRadiusY=Math.abs(BottomRight.Y-TopLeft.Y)/2
        Center.X=RectRadiusX+TopLeft.X
        Center.Y=RectRadiusY+TopLeft.Y
    }
    fun setPaintProperties(style:Paint.Style,color: Int)
    {
        RectPaint.style= style
        RectPaint.color= color
    }

}
class Circle(cx:Float, cy:Float,raduis:Float)
{
    val Center:Point
    val Raduis:Float
    val CPaint:Paint
    init {
            Raduis=raduis
            Center=Point(cx,cy)
            CPaint=Paint()
            CPaint.style=Paint.Style.FILL
            CPaint.isAntiAlias=true
            CPaint.color=Color.argb(200,3,115,160)
    }

    fun UpdateCoordinate(x:Float,y:Float)
    {
       Center.X=x
        Center.Y=y
    }
}
class Line(stx:Float, sty:Float , enx:Float , eny:Float){
    val start:Point
    val end:Point
    val Lpaint:Paint
    init{
        start=Point(stx,sty)
        end=Point(enx,eny)
        Lpaint=Paint()
        Lpaint.strokeWidth=2f
        Lpaint.style=Paint.Style.STROKE
        Lpaint.color=0

    }
}
