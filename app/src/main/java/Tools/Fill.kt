package Tools

import Extensions.Brush_Features
import Extensions.Current_Color
import Extensions.Point
import Extensions.Result_Image
import android.graphics.Color
import java.lang.Exception
import java.util.ArrayList

object Fill
{
    private val X= arrayOf(-1,-1,-1,0,0,1,1,1)
    private val Y= arrayOf(-1,0,1,-1,1,1,0,-1)//1
    fun in_grid(point:Point):Boolean
    {
    return (point.X <Result_Image.mImage!!.Width && point.X>0 &&  point.Y < Result_Image.mImage!!.Height && point.Y>0)
    }
    fun not_visited(x:Int,y:Int,color:Int):Boolean
    {
        //println("---------> Image:${Result_Image.mBitmap.getPixel(x,y)}  , Color : $color")
        return (Result_Image.mBitmap.getPixel(x,y)) != color
    }

  fun fill_it(point:Point,mColor:Int )
  {
        val x=point.X.toInt()
        val y=point.Y.toInt()
     if(in_grid(point)) {
         if (not_visited(x, y, mColor)) {

             // println("----CCC-----> Image:${Result_Image.mBitmap.getPixel(x,y)}  , Color : $mColor")

             if (Result_Image.mBitmap.getPixel(x, y) == Current_Color.color)
             {
                 Result_Image.mBitmap.setPixel(x, y, mColor)
                 for (i in 0 until 5)
                 {
                     try
                     {
                         fill_it(Point(x + X[i].toFloat(), y + Y[i].toFloat()), mColor)
                     } catch (e: Exception) {

                     }
                 }
             }
         }
     }
  }
    fun fill(p: Point, color: Int) {
        var p = p
        var stack: ArrayList<Point>? = ArrayList()
        stack!!.add(p)
        var visit: BooleanArray? = BooleanArray(Tools.img.Width * Tools.img.Height)
        var i = 0
        while (i < stack.size) {
            p = stack[i++]
            visit!![p.Y.toInt() * Tools.img.Width + p.X.toInt()] = true
            val color1 = Tools.bitmap.getPixel(p.X.toInt(), p.Y.toInt())
            Tools.bitmap.setPixel(p.X.toInt(), p.Y.toInt(),color)
            if ((p.Y.toInt() - 1) >= 0 && Tools.bitmap.getPixel(p.X.toInt(), p.Y.toInt()-1) == color1 && !visit!![(p.Y.toInt() - 1) * Tools.img.Width + p.X.toInt()]) {
                val p1 = Point(i.toFloat(), i.toFloat())
                p1.X = p.X
                p1.Y = p.Y - 1
                stack.add(p1)
                visit[(p.Y.toInt() - 1) * Tools.img.Width + p.X.toInt()] = true
            }
            if ((p.Y.toInt() + 1) < Tools.img.Height  && Tools.bitmap.getPixel(p.X.toInt(), p.Y.toInt()+1) == color1 && !visit!![(p.Y.toInt() + 1) * Tools.img.Width + p.X.toInt()]) {
                val p1 = Point(i.toFloat(), i.toFloat())
                p1.X = p.X
                p1.Y = p.Y + 1
                stack.add(p1)
                visit[(p.Y.toInt() + 1) * Tools.img.Width + p.X.toInt()] = true
            }
            if ((p.X.toInt() - 1) >= 0  && Tools.bitmap.getPixel(p.X.toInt()-1, p.Y.toInt())
                 == color1 && !visit!![p.Y.toInt() * Tools.img.Width + p.X.toInt() - 1]
            ) {
                val p1 = Point(i.toFloat(), i.toFloat())
                p1.X = p.X - 1
                p1.Y = p.Y
                stack.add(p1)
                visit[p.Y.toInt() * Tools.img.Width + p.X.toInt() - 1] = true
            }
            if ((p.X.toInt() + 1) <  Tools.img.Width && Tools.bitmap.getPixel(p.X.toInt()+1, p.Y.toInt()) == color1 && !visit!![p.Y.toInt() * Tools.img.Width + p.X.toInt() + 1]) {
                val p1 = Point(i.toFloat(), i.toFloat())
                p1.X = p.X + 1
                p1.Y = p.Y
                stack.add(p1)
                visit[p.Y.toInt() * Tools.img.Width + p.X.toInt() + 1] = true
            }
        }
        visit = null
        stack = null
    }

}