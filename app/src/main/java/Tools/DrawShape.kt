package Tools

import Extensions.Point
import Extensions.Result_Image
import java.util.ArrayList
import android.view.View.Y
import android.view.View.X



class DrawShape
{
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
        size= 1f

        distance=Math.sqrt((currentPoint.X-prevPoint.X).toDouble()*(currentPoint.X-prevPoint.X)+
                (currentPoint.Y-prevPoint.Y)*(currentPoint.Y-prevPoint.Y))
        pointsNum=distance/size


        Xinterval=(currentPoint.X-prevPoint.X)/(pointsNum)
        Yinterval=(currentPoint.Y-prevPoint.Y)/(pointsNum)

        for(i in 0 until pointsNum.toInt()+1)
        {
            nX=prevPoint.X+(i.toFloat()*Xinterval.toFloat())
            nY=prevPoint.Y+(i.toFloat()*Yinterval.toFloat())

            PointsList.add(Point(nX,nY))
        }

        return PointsList
    }
     fun  DrawLine(p1: Point, p2: Point) {
        Pen.PenSquare(CalculatePoints(p1, p2))
    }

    fun DrawSquare(p1: Point, p4: Point) {

        var p2= Point(0f,0f)
        var p3= Point(0f,0f)
        p3.X=p1.X
        p3.Y=p4.Y
        p2.X=p4.X
        p2.Y=p1.Y
        DrawLine(p1, p2)
        DrawLine(p1, p3)
        DrawLine(p2, p4)
        DrawLine(p3, p4)
        Result_Image.mBitmap.setPixel(p4.X.toInt(), p4.Y.toInt(),Pen.pro.color)
    }

    fun DrawCircle(radius: Float, center: Point) {
        var deg_in_Rad: Float
        val index = 0
        val points = ArrayList<Point>()
        var point: Point
        for (j in 0..359) {
            point = Point(0f,0f)
            deg_in_Rad = j * 3.14f / 180.0f
            point.X = (Math.cos(deg_in_Rad.toDouble()) * radius).toFloat() + center.X //X
            point.Y = (Math.sin(deg_in_Rad.toDouble()) * radius).toFloat() + center.Y //Y

            points.add(point)
        }

        Pen.PenSquare(points)
    }
}