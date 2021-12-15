package my.Activities

import Extensions.*
import Tools.Tools

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.FragmentManager
import android.util.DisplayMetrics
import android.widget.*
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import java.io.File
import java.nio.IntBuffer
import srcImageIO.Image as Image1

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() , ImageGeneration {


    lateinit var Gallery_btn:ImageButton
    lateinit var Camera_btn:ImageButton
    lateinit var Create_btn:ImageButton
    lateinit var Generator_fragment:ImageGenerator_Fragment
    lateinit var Generator_layout:FrameLayout
    lateinit var layout_view:RelativeLayout
    lateinit var camera_text:TextView
    lateinit var gallery_text:TextView
    lateinit var new_text:TextView
    var level=Fragment_Level.in_main_menu
    val CAMERA=2
    val GALLERY=1

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create a File object for the parent directory
        val AppDirectory = File(FilePath.path)
        AppDirectory.mkdirs()

        val metrics= DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        Screen_Size.Height=metrics.heightPixels
        Screen_Size.Width=metrics.widthPixels

        Gallery_btn=findViewById(R.id.gallery_btn)
        Camera_btn=findViewById(R.id.camera_btn)
        Create_btn=findViewById(R.id.create_new_image)
        layout_view=findViewById(R.id.first_view)
        Generator_layout=findViewById(R.id.Image_creator_layout)
        camera_text=findViewById(R.id.camera_txt)
        gallery_text=findViewById(R.id.gallery_txt)
        new_text=findViewById(R.id.new_txt)
        Generator_fragment= ImageGenerator_Fragment()

        YoYo.with(Techniques.StandUp).delay(100).duration(2000).playOn(Gallery_btn)
        YoYo.with(Techniques.StandUp).delay(100).duration(2000).playOn(Camera_btn)
        YoYo.with(Techniques.StandUp).delay(100).duration(2000).playOn(Create_btn)
        YoYo.with(Techniques.StandUp).delay(100).duration(2000).playOn(camera_text)
        YoYo.with(Techniques.StandUp).delay(100).duration(2000).playOn(gallery_text)
        YoYo.with(Techniques.StandUp).delay(100).duration(2000).playOn(new_text)

        Gallery_btn.setOnClickListener {
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImg.type="image/*"
            YoYo.with(Techniques.FadeIn).duration(AnimDuration.Click).playOn(Gallery_btn)
            startActivityForResult(pickImg,GALLERY)
        }
        Camera_btn.setOnClickListener {
            YoYo.with(Techniques.FadeIn).duration(AnimDuration.Click).playOn(Camera_btn)
            val capture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(capture,CAMERA)
        }
        Create_btn.setOnClickListener {
         supportFragmentManager.beginTransaction().
             replace(R.id.Image_creator_layout,Generator_fragment).addToBackStack("generator").commit()
            YoYo.with(Techniques.SlideInRight).duration(AnimDuration.Translate).playOn(Generator_layout)
           level=Fragment_Level.in_selector
        }

    }
@SuppressLint("Recycle")
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (data != null) {

        if (requestCode == GALLERY) {

            val uri = data.data

            val pro = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(uri!!, pro, null, null, null)
            val index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()

            val path = cursor.getString(index)
            try {
                 Result_Image.mImage=Read_Image_From_Path(path,this)

                Result_Image.mBuffer=Result_Image.mImage!!.RGBA

                Result_Image.mBitmap= Bitmap.createBitmap(Result_Image.mImage!!.Width,Result_Image.mImage!!.Height,Bitmap.Config.ARGB_8888)
                Result_Image.mBitmap.copyPixelsFromBuffer(Result_Image.mBuffer)

                Tools.setBitmap(Result_Image.mBitmap)
                Tools.setimg(Result_Image.mImage)
                val i = Intent(this, Second_Activity::class.java)
                startActivity(i)

            }catch(e:notSupportedException)
            {
                val bmp = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
               // val bmp1=Bitmap.createBitmap(bmp)
                val img= srcImageIO.Image( bmp.width,bmp.height)
                img.RGBA= IntBuffer.allocate(bmp.width*bmp.height)
                img.Height=bmp.height
                img.Width=bmp.width
                Result_Image.mImage=img
                Result_Image.mBuffer=Result_Image.mImage!!.RGBA
                Result_Image.mBuffer.position(0)
                bmp.copyPixelsToBuffer(Result_Image.mBuffer)
                Result_Image.mBitmap=Bitmap.createBitmap(bmp.width,bmp.height,Bitmap.Config.ARGB_8888)

                Result_Image.mBuffer.position(0)
                Result_Image.mBitmap.copyPixelsFromBuffer(Result_Image.mBuffer)

                Tools.setBitmap(Result_Image.mBitmap)
                Tools.setimg(Result_Image.mImage)

                val intent = Intent(this, Second_Activity::class.java)
                startActivity(intent)
            }
            catch (e:Exception)
            {
                Toast.makeText(this,"Not Supported Image",Toast.LENGTH_SHORT).show()
            }

        }
        else if (requestCode == CAMERA) {
            val thimbu  = data.extras.get("data") as Bitmap

            val img= srcImageIO.Image( Screen_Size.Width,Screen_Size.Height)
            img.RGBA= IntBuffer.allocate(Screen_Size.Width*Screen_Size.Height)
            img.Height=Screen_Size.Height
            img.Width=Screen_Size.Width
            Result_Image.mImage=img
            Result_Image.mBuffer=Result_Image.mImage!!.RGBA
            Result_Image.mBitmap=Bitmap.createScaledBitmap(thimbu,Screen_Size.Width,Screen_Size.Height,true)
            Result_Image.mBuffer.position(0)
            Result_Image.mBitmap.copyPixelsToBuffer(Result_Image.mBuffer)

            Tools.setimg(Result_Image.mImage)
            Tools.setBitmap(Result_Image.mBitmap)
            val intent = Intent(this, Second_Activity::class.java)
            startActivity(intent)

        }
        else
        {

        }

    }
}

    override fun onOkPressed(height: Int, width: Int, color: Int) {
        val img= srcImageIO.Image( Screen_Size.Width,Screen_Size.Height)
        img.RGBA= IntBuffer.allocate(width*height)
        img.Height=height
        img.Width=width
        Result_Image.mImage=img
        Result_Image.mBuffer=Result_Image.mImage!!.RGBA

        Result_Image.mBitmap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        val c=Canvas(Result_Image.mBitmap)
        c.drawARGB(255,Color.red(color),Color.green(color),Color.blue(color))

        Result_Image.mBuffer.position(0)
        Result_Image.mBitmap.copyPixelsToBuffer(Result_Image.mBuffer)

        Tools.setimg(Result_Image.mImage)
        Tools.setBitmap(Result_Image.mBitmap)

        val intent = Intent(this, Second_Activity::class.java)
        startActivity(intent)
        onBackPressed()

    }



    override fun onBackPressed() {
        super.onBackPressed()
        if(level == Fragment_Level.in_selector)
        {
            if(supportFragmentManager.fragments.contains(Generator_fragment))
            {
                supportFragmentManager.beginTransaction().remove(Generator_fragment).commit()
                supportFragmentManager.popBackStack("generator", FragmentManager.POP_BACK_STACK_INCLUSIVE)

            }
            YoYo.with(Techniques.SlideInLeft).duration(AnimDuration.Show).playOn(Camera_btn)
            YoYo.with(Techniques.SlideInLeft).duration(AnimDuration.Show).playOn(Gallery_btn)
            YoYo.with(Techniques.SlideInLeft).duration(AnimDuration.Show).playOn(Create_btn)

            YoYo.with(Techniques.SlideInLeft).duration(AnimDuration.Show).playOn(camera_text)
            YoYo.with(Techniques.SlideInLeft).duration(AnimDuration.Show).playOn(new_text)
            YoYo.with(Techniques.SlideInLeft).duration(AnimDuration.Show).playOn(gallery_text)

            level=Fragment_Level.in_main_menu
        }
    }
}
