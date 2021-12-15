package Extensions


import Tools.Pen
import Tools.Text
import Tools.Tools
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.widget.Toast
import my.Activities.Second_Activity
import srcImageIO.Image
import srcImageIO.Imageio
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
import java.io.*
import java.lang.Exception
import java.nio.ByteBuffer
import Extensions.notSupportedException as notSupportedException1

fun Convert_to_ByteArry(bitmap: Bitmap):ByteArray
{
    val width = bitmap.width
    val height = bitmap.height
    val size = bitmap.rowBytes * bitmap.height

    val byteBuffer = ByteBuffer.allocate(size)
    bitmap.copyPixelsToBuffer(byteBuffer)

    return byteBuffer.array()
}

fun open_color_picker(context: Context , prevcolor:Int)
{
val mListener=context as CanvasInvalidater

val colorPicker=AmbilWarnaDialog(context,prevcolor, object :OnAmbilWarnaListener{
    override fun onCancel(dialog: AmbilWarnaDialog?) {

    }

    override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
     Brush_Features.color=color
        Text.tPaint.color=color
        mListener.Invalidate()
        Pen.setpro()
    }

})
colorPicker.show()

}
fun Read_Image_From_Path(path:String,context:Context): Image?
{

    try {

         val img= Imageio.read(path)
        Tools.setimg(img)

        return img
    }catch (e:FileNotFoundException)
    {
      Toast.makeText(context,"Not Found",Toast.LENGTH_SHORT).show()
    }

    catch (e:IOException)
    {
        throw  Extensions.notSupportedException()

    }
    catch (e:IndexOutOfBoundsException)
    {
        Toast.makeText(context,"Out Of Bounds",Toast.LENGTH_SHORT).show()
    }
    catch (e:OutOfMemoryError)
    {
        Toast.makeText(context,"Out Of Memory",Toast.LENGTH_SHORT).show()
    }
       catch (e:Exception)
       {
           Toast.makeText(context,"Ex Type : $e",Toast.LENGTH_SHORT).show()
           throw  Extensions.notSupportedException()
       }
    return null
}
fun to_Back_request(activity: FragmentActivity,Tag:String)
{
  activity.supportFragmentManager.popBackStack(Tag,FragmentManager.POP_BACK_STACK_INCLUSIVE)

}
fun Save_Image_To_Buffer(Act:Second_Activity)
{
    Result_Image.mBuffer.position(0)
    Result_Image.mBitmap.copyPixelsToBuffer(Result_Image.mBuffer)
    Toast.makeText(Act.context,"Changes Saved",Toast.LENGTH_SHORT).show()

    Act.onBackPressed()
}
class notSupportedException: Exception()
{

}


