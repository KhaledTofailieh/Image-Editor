package Tools

import Extensions.*
import android.app.Activity
import android.graphics.Canvas
import android.graphics.Paint
import my.Activities.Second_Activity
import pCanvas.canvass
import pCanvas.mtextPaint

object  Text
{
      var tPaint=mtextPaint()
      var coords=Point((Screen_Size.Width/2).toFloat(),(Screen_Size.Height/2).toFloat())
      var text=""

    fun SubmitText(txt:String?,mCanvas: canvass)
    {
        text=txt!!
        mCanvas.text=txt
        mCanvas.tool= Tool_Type.Text
        mCanvas.resetTochedPoint()
        mCanvas.Invalidate()
    }
    fun SaveChanges(mCanvas: canvass,Act: Second_Activity)
    {
        val canvas= Canvas(Result_Image.mBitmap)
        val p=mCanvas.CalculatePointIndex()

        canvas.drawText(mCanvas.text!!,p.X,p.Y,tPaint)

        Save_Image_To_Buffer(Act)
    }

}
