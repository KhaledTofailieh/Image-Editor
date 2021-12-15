package my.Activities

import pCanvas.canvass
import Extensions.*

import Spinners.Data.text_type_Array_Data
import Tools.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri

import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ContentFrameLayout
import android.view.MenuItem

import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout

import android.widget.Toast
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import pCanvas.Circle
import pCanvas.Rectangle
import srcImageIO.ImageType
import srcImageIO.Imageio
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import Extensions.Line as Line


@SuppressLint("Registered")
class Second_Activity :AppCompatActivity() ,FragmentListener,DrawerListener
    , ShapeSelectionListener , AdjustingListenerInerface , CanvasInvalidater{

    var hidden=false
    var Lock=false
    var context=this as Context
    var activity=this as Activity
    lateinit var main_fragment:main_fragment
    lateinit var default_fragment:default_fragment
    lateinit var blankFragment: BlankFragment
    lateinit var main_board: FrameLayout
    lateinit var second_board_layout:ContentFrameLayout
    lateinit var editing_board:Edit_btns_fragment
    lateinit var adjusting_board:Adjust_btns_fragment
    lateinit var second_board_fragment:Secondary_btns_fragment
    lateinit var layout_view: FrameLayout
    lateinit var brush_feature_fragmet: BrushFeatures
    lateinit var temp_fragment:Fragment

    lateinit var expand_up_layout:FrameLayout
    lateinit var expand_down_layout:FrameLayout

    lateinit var expand_down:ExpendDownFragment
    lateinit var expand_up:ExpendUpFragment
    lateinit var AppLevel:Fragment_Level

    lateinit var zoom_layout:FrameLayout
    lateinit var rotate_layout:FrameLayout
    lateinit var zoom_fragment:Zoom_in_out_fragment
    lateinit var rotate_fragment:Rotate_left_right_fragment
    lateinit var HS_Fragment:Hue_Saturation_Adjustment_Fragment
    lateinit var brightness_fragment: Brightness_Fragment
    lateinit var edit_colors_fragment:EditColors_Fragment
    lateinit var addText_fragment:AddingTextFragment
    lateinit var mCanvas:canvass
    lateinit var GListener:GestureListener
    lateinit var textPropertiesFragment:TextPropertiesFragment
    lateinit var Gdetector:GestureDetectorCompat
    lateinit var navigation_view:NavigationView
    lateinit var parent_view:DrawerLayout
    lateinit var  saving_fragment:SavingFragment
    lateinit var shape_fragment:ShapesAndStickers
    lateinit var mRectangle: Rectangle
    lateinit var mLine:pCanvas.Line
    lateinit var mCircle: Circle
    lateinit var ShapeProperties:Shape_Properties
    lateinit var Imm:InputMethodManager
    val time=Calendar.getInstance().time



    val PenPoints=ArrayList<Point>()

    var toolType=Tool_Type.Null
    @SuppressLint("ClickableViewAccessibility", "CommitTransaction")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)


        this.main_fragment = main_fragment()
        this.default_fragment = default_fragment()
        this.main_board = findViewById(R.id.main_board_layout)
        this.layout_view = findViewById(R.id.my_image_layout)
        this.second_board_layout=findViewById(R.id.secondary_board_layout)
        this.expand_down_layout=findViewById(R.id.expand_down_layout)
        this.expand_up_layout=findViewById(R.id.expand_Up_layout)

        this.zoom_layout=findViewById(R.id.Zoom_layout)
        this.rotate_layout=findViewById(R.id.Rotate_layout)

        this.second_board_fragment= Secondary_btns_fragment()
        this.adjusting_board = Adjust_btns_fragment(this)
        this.editing_board = Edit_btns_fragment()
        this.blankFragment= BlankFragment()
        this.brush_feature_fragmet=BrushFeatures(this)
        this.zoom_fragment= Zoom_in_out_fragment()
        this.rotate_fragment= Rotate_left_right_fragment()
        this.expand_down= ExpendDownFragment()
        this.expand_up= ExpendUpFragment()
        this.HS_Fragment=Hue_Saturation_Adjustment_Fragment(this)
        this.brightness_fragment=Brightness_Fragment()
        this.AppLevel=Fragment_Level.in_main_menu
        this.GListener= GestureListener(this)
        this.Gdetector= GestureDetectorCompat(this,GListener)
        this.edit_colors_fragment= EditColors_Fragment()
        this.addText_fragment= AddingTextFragment()
        this.navigation_view=findViewById(R.id.nav_view)
        this.textPropertiesFragment= TextPropertiesFragment()
        this.saving_fragment= SavingFragment()
        this.parent_view=findViewById(R.id.second_parent_layout)
        this.shape_fragment= ShapesAndStickers()
        this.ShapeProperties= Shape_Properties()
        Gdetector.setOnDoubleTapListener(GListener)

        try {

            mCanvas=canvass(this)
            mCanvas.CalculateOffsetLeft_Top(Result_Image.mImage!!.Width,Result_Image.mImage!!.Height)
            this.mRectangle= Rectangle(this , mCanvas.mtop,mCanvas.mleft)
            this.mLine=pCanvas.Line(this,mCanvas.mleft,mCanvas.mtop)
            this.mCircle=pCanvas.Circle(this,mCanvas.mleft,mCanvas.mtop)

            layout_view.addView(mCanvas)

        }
        catch (e:KotlinNullPointerException)
        {
            Toast.makeText(this,"Not Supported Image ",Toast.LENGTH_SHORT).show()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_board_layout,this.main_fragment).addToBackStack(App_Levels.main_menu).commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.Zoom_layout,this.zoom_fragment).commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.Rotate_layout,this.rotate_fragment).commit()

        YoYo.with(Techniques.FlipInX).delay(200).duration(1000).playOn(zoom_layout)
        YoYo.with(Techniques.FlipInX).delay(200).duration(1000).playOn(rotate_layout)

        YoYo.with(Techniques.FadeIn).duration(AnimDuration.Show).playOn(main_board)
        layout_view.setOnTouchListener { v, event ->
            val act=event.action
            mRectangle.onTouchEvent(event)
            mLine.onTouchEvent(event)
                mCanvas.onTouchEvent(event)
               // Gdetector.onTouchEvent(event)

                if(AppLevel==Fragment_Level.in_main_menu)
                {
                    if(act == MotionEvent.ACTION_UP )
                    {
                        hidden=
                            when(hidden)
                            {
                                true->{
                                    supportFragmentManager.beginTransaction()
                                        .replace(R.id.main_board_layout,this.main_fragment).addToBackStack(App_Levels.main_menu).commit()
                                    zoom_layout.visibility=View.VISIBLE
                                    rotate_layout.visibility=View.VISIBLE
                                    YoYo.with(Techniques.FadeIn).duration(AnimDuration.Show).playOn(zoom_layout)
                                    YoYo.with(Techniques.FadeIn).duration(AnimDuration.Show).playOn(rotate_layout)
                                    YoYo.with(Techniques.FadeIn).duration(AnimDuration.Show).playOn(main_board)
                                    false
                                }
                                false->{
                                    supportFragmentManager.popBackStack(App_Levels.main_menu,FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                    supportFragmentManager.beginTransaction()
                                        .replace(R.id.main_board_layout,default_fragment).commit()

                                    zoom_layout.visibility=View.INVISIBLE
                                    rotate_layout.visibility=View.INVISIBLE
                                    true
                                }
                            }
                    }
            }


            true
        }
        navigation_view.setNavigationItemSelectedListener(object:NavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(Item: MenuItem): Boolean {
                when(Item.itemId)
                {
                    R.id.Save_menu ->
                    {
                        if(AppLevel == Fragment_Level.in_tool)
                        {
                            Toast.makeText(context,"Please Close Tool and Try again",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            AppLevel=Fragment_Level.in_selector
                            supportFragmentManager.beginTransaction().replace(R.id.main_board_layout,saving_fragment).addToBackStack(App_Levels.selector_menu).commit()
                        }
                        parent_view.closeDrawers()
                    }
                    R.id.Settings_menu->
                    {

                    }
                    R.id.Share_menu->
                    {
                        parent_view.closeDrawers()
                        val url= MediaStore.Images.Media.insertImage(context.contentResolver, Result_Image.mBitmap, "myImage", "description")
                        val intent=Intent()
                        intent.action=Intent.ACTION_SEND
                        intent.putExtra(Intent.EXTRA_STREAM,Uri.parse(url))
                        intent.type="image/*"
                        startActivity(Intent.createChooser(intent,"Share with"))
                    }
                }
                return true
            }
        })

    }
    override fun onMainButtonClicked(type: MainButtons) {
        zoom_layout.visibility=View.INVISIBLE
        rotate_layout.visibility=View.INVISIBLE
        AppLevel=Fragment_Level.in_selector
                  when(type)
                  {
                      MainButtons.Adjust->
                      {
                         supportFragmentManager.beginTransaction()
                             .replace(R.id.main_board_layout,adjusting_board).addToBackStack(App_Levels.selector_menu).commit()
                            Filter.calculateHSL()
                      }
                      MainButtons.Edit->
                      {
                          supportFragmentManager.beginTransaction()
                          .replace(R.id.main_board_layout,editing_board).addToBackStack(App_Levels.selector_menu).commit()


                      }
                      MainButtons.Filters->
                      {
                          supportFragmentManager.beginTransaction().replace(R.id.main_board_layout,shape_fragment)
                              .addToBackStack(App_Levels.selector_menu).commit()

                      }
                  }
        YoYo.with(Techniques.SlideInRight).duration(AnimDuration.Translate).playOn(main_board)
    }

    @SuppressLint("CommitTransaction")
    override fun onEditingButtonClicked(type: EditingButtons) {
     Lock=true

        supportFragmentManager.beginTransaction().
            replace(R.id.secondary_board_layout,second_board_fragment).addToBackStack(App_Levels.tool_menu).commit()

        supportFragmentManager.beginTransaction().
            replace(R.id.expand_down_layout,expand_down).addToBackStack(App_Levels.tool_menu).commit()

        YoYo.with(Techniques.SlideInLeft)
            .duration(AnimDuration.Translate).playOn(second_board_layout)

        expand_down_layout.visibility=View.VISIBLE

        YoYo.with(Techniques.SlideInRight).duration(AnimDuration.Translate).playOn(expand_up_layout)
        YoYo.with(Techniques.SlideInLeft).duration(AnimDuration.Translate).playOn(expand_down_layout)


        AppLevel=Fragment_Level.in_tool

        when(type)
        {
            EditingButtons.Paint->
            {
                toolType=Tool_Type.Drawer
                temp_fragment=brush_feature_fragmet
                supportFragmentManager.beginTransaction().
                    replace(R.id.main_board_layout,temp_fragment).addToBackStack(App_Levels.tool_menu).commit()


                expand_up_layout.visibility=View.VISIBLE

                supportFragmentManager.beginTransaction().
                    replace(R.id.expand_Up_layout,expand_up).addToBackStack(App_Levels.tool_menu).commit()
                YoYo.with(Techniques.SlideInRight).duration(AnimDuration.Translate).playOn(main_board)

                Tools.setBitmap(Result_Image.mBitmap)
                mCanvas.tool=Tool_Type.Drawer
                mCanvas.Invalidate()

            }
            EditingButtons.Crop->
            {
                toolType=Tool_Type.Crop

                temp_fragment=default_fragment
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_board_layout,temp_fragment).addToBackStack(App_Levels.tool_menu).commit()
                mCanvas.tool=Tool_Type.Crop
                mCanvas.resetCropper()
                mCanvas.Invalidate()
            }
            EditingButtons.Text->
            {
                toolType=Tool_Type.Text
                temp_fragment=addText_fragment

                second_board_layout.visibility=View.INVISIBLE
                expand_down_layout.visibility=View.INVISIBLE
                expand_up_layout.visibility=View.INVISIBLE

                supportFragmentManager.beginTransaction().
                    replace(R.id.expand_Up_layout,expand_up).addToBackStack(App_Levels.tool_menu).commit()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_board_layout,addText_fragment).addToBackStack(App_Levels.tool_menu).commit()
                YoYo.with(Techniques.SlideInRight).duration(AnimDuration.Translate).playOn(main_board)

            }
        }


    }

    override fun onAdjustingButtonClicked(type: AdjustingButtons) {
    AppLevel=Fragment_Level.in_tool

        supportFragmentManager.beginTransaction().
            replace(R.id.secondary_board_layout,second_board_fragment).addToBackStack(App_Levels.tool_menu).commit()

        supportFragmentManager.beginTransaction().
            replace(R.id.expand_down_layout,expand_down).addToBackStack(App_Levels.tool_menu).commit()

        supportFragmentManager.beginTransaction().
            replace(R.id.expand_Up_layout,expand_up).addToBackStack(App_Levels.tool_menu).commit()


        expand_down_layout.visibility=View.VISIBLE
        expand_up_layout.visibility=View.VISIBLE

        YoYo.with(Techniques.SlideInLeft).duration(AnimDuration.Translate).playOn(second_board_layout)
        YoYo.with(Techniques.SlideInRight).duration(AnimDuration.Translate).playOn(expand_up_layout)
        YoYo.with(Techniques.SlideInLeft).duration(AnimDuration.Translate).playOn(expand_down_layout)

        when(type)
        {
            AdjustingButtons.EditColor->
            {
              temp_fragment=edit_colors_fragment
                toolType=Tool_Type.EditColors
            }
            AdjustingButtons.Brightness->
            {
               temp_fragment=brightness_fragment
                toolType=Tool_Type.Brightness
            }
            AdjustingButtons.HueSaturation->
            {
                temp_fragment=HS_Fragment
                toolType=Tool_Type.HueSaturation
            }
        }
        supportFragmentManager.beginTransaction().replace(R.id.main_board_layout,temp_fragment).addToBackStack(App_Levels.tool_menu).commit()
        YoYo.with(Techniques.SlideInRight).duration(AnimDuration.Translate).playOn(main_board)

    }

    override fun onChangeBrush(type: BrushFeatureButtons) {
        when(type)
        {
            BrushFeatureButtons.color ->
            {
                open_color_picker(this,Brush_Features.color)
            }
            BrushFeatureButtons.size ->
            {

            }
            else -> {}
        }
    }

    override fun onExpandRequest(type: ExpandButtons,Expanded:Boolean) :Boolean{

        when(type) {
            ExpandButtons.Expand_Up -> {

                if (Expanded) {

                    supportFragmentManager.beginTransaction().replace(R.id.main_board_layout, default_fragment)
                        .commit()
                    return  false
                } else {
                    supportFragmentManager.beginTransaction().replace(R.id.main_board_layout, temp_fragment)
                        .commit()
                    YoYo.with(Techniques.FadeIn).duration(AnimDuration.Show).playOn(main_board)
                    return true
                }
            }
            ExpandButtons.Expand_Down -> {

                if (Expanded) {
                    supportFragmentManager.beginTransaction().replace(R.id.secondary_board_layout, blankFragment)
                        .commit()
                    return false
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.secondary_board_layout, second_board_fragment).commit()
                    YoYo.with(Techniques.FlipInX).duration(AnimDuration.Show).playOn(second_board_layout)
                    return true
                }
            }
        }

    }

    override fun onSecondaryButtonClicked(type:Secondary_buttons) {
       when(type)
       {
           Secondary_buttons.close_button ->
           {

                  onBackPressed()
                  mCanvas.Invalidate()
           }
           Secondary_buttons.ok_button ->
           {
               when(toolType)
               {

                   Tool_Type.Crop ->
                   {
                       Crop.crop(mCanvas.getCropperLT(),mCanvas.getCropperRB())
                       Result_Image.mBitmap= Bitmap.createBitmap( Result_Image.mImage!!.Width,Result_Image.mImage!!.Height,Bitmap.Config.ARGB_8888)
                       Result_Image.mBuffer.position(0)
                       Result_Image.mBitmap.copyPixelsFromBuffer(Result_Image.mBuffer)
                       mCanvas.CalculateOffsetLeft_Top(Result_Image.mBitmap.width,Result_Image.mBitmap.height)

                       mCanvas.Invalidate()
                       onBackPressed()
                       System.gc()
                   }

                   Tool_Type.EditColors,Tool_Type.Drawer,Tool_Type.HueSaturation ->
                   {
                      Save_Image_To_Buffer(this)
                   }
                  Tool_Type.Text->
                  {
                     Text.SaveChanges(mCanvas,this)
                  }
                   Tool_Type.Rectangle->{
                        Pen.setpro(mRectangle.mRect.RectPaint.color,mRectangle.mRect.RectPaint.strokeWidth.toInt()/2)
                        val p=Point(mRectangle.mRect.TopLeft.X-mCanvas.mleft,mRectangle.mRect.TopLeft.Y-mCanvas.mtop)
                        val p1=Point(mRectangle.mRect.BottomRight.X-mCanvas.mleft,mRectangle.mRect.BottomRight.Y-mCanvas.mtop)
                        val r= DrawShape()
                       r.DrawSquare(p,p1)
                       Save_Image_To_Buffer(this)
                   }
                   Tool_Type.Line->{
                       Pen.setpro(mLine.mLine.Lpaint.color, mLine.mLine.Lpaint.strokeWidth.toInt()/2)
                       val p = Point(mLine.mLine.start.X-mCanvas.mleft, mLine.mLine.start.Y-mCanvas.mtop)
                       val p1 = Point(mLine.mLine.end.X-mCanvas.mleft, mLine.mLine.end.Y-mCanvas.mtop)
                       val r = DrawShape()
                       r.DrawLine(p, p1)
                       Save_Image_To_Buffer(this)

                   }
                   Tool_Type.Circle->{
                       Pen.setpro(mCircle.mCircle.CPaint.color,mCircle.mCircle.CPaint.strokeWidth.toInt()/2)
                       var p = Point(mCircle.mCircle.Center.X-mCanvas.mleft,mCircle.mCircle.Center.Y-mCanvas.mtop)

                       var r = DrawShape()
                       r.DrawCircle(100f, p)
                       Save_Image_To_Buffer(this)
                   }
               }
           }
       }
    }

    override fun onRotateButtonClicked(type: RotationButtons) {
        when(type)
        {

            RotationButtons.Rotate_Right -> {
                Tools.setimg(Result_Image.mImage!!)
                Rotation.rotate(RotateDirect.RightRotate)
                Result_Image.mBuffer=Result_Image.mImage!!.RGBA
                Result_Image.mBitmap = Bitmap.createBitmap(Result_Image.mImage!!.Width, Result_Image.mImage!!.Height, Bitmap.Config.ARGB_8888)
                Result_Image.mBuffer.position(0)
                Result_Image.mBitmap!!.copyPixelsFromBuffer(Result_Image.mBuffer)
               YoYo.with(Techniques.RotateInDownLeft).duration(AnimDuration.Click).playOn(mCanvas)
                System.gc()

            }

            RotationButtons.Rotate_Left ->
            {
                Tools.setimg(Result_Image.mImage!!)
                Rotation.rotate(RotateDirect.LeftRotate)
                Result_Image.mBuffer=Result_Image.mImage!!.RGBA
                Result_Image.mBitmap = Bitmap.createBitmap(Result_Image.mImage!!.Width, Result_Image.mImage!!.Height, Bitmap.Config.ARGB_8888)
                Result_Image.mBuffer.position(0)
                Result_Image.mBitmap!!.copyPixelsFromBuffer(Result_Image.mBuffer)
                YoYo.with(Techniques.RotateInDownRight).duration(AnimDuration.Click).playOn(mCanvas)
                System.gc()

            }
            RotationButtons.Flip_H ->{
                Tools.setimg(Result_Image.mImage!!)
                Flip.HorizionalFlip()
                Result_Image.mBuffer=Result_Image.mImage!!.RGBA
                Result_Image.mBitmap = Bitmap.createBitmap(Result_Image.mImage!!.Width, Result_Image.mImage!!.Height, Bitmap.Config.ARGB_8888)
                Result_Image.mBuffer.position(0)
                Result_Image.mBitmap!!.copyPixelsFromBuffer(Result_Image.mImage!!.RGBA)
                YoYo.with(Techniques.FlipInY).duration(AnimDuration.Click).playOn(mCanvas)
                System.gc()

            }
            RotationButtons.Flip_V ->{
                Tools.setimg(Result_Image.mImage!!)
                Flip.VerticalFlip()
                Result_Image.mBuffer=Result_Image.mImage!!.RGBA
                Result_Image.mBitmap = Bitmap.createBitmap(Result_Image.mImage!!.Width, Result_Image.mImage!!.Height, Bitmap.Config.ARGB_8888)
                Result_Image.mBuffer.position(0)
                Result_Image.mBitmap!!.copyPixelsFromBuffer(Result_Image.mBuffer)
                YoYo.with(Techniques.FlipInX).duration(AnimDuration.Click).playOn(mCanvas)
                System.gc()
            }
        }
        mCanvas.CalculateOffsetLeft_Top(Result_Image.mImage!!.Width,Result_Image.mImage!!.Height)
        mCanvas.Invalidate()
    }

    override fun onZoomButtonClicked(type: ZoomButtons) {
        try {
            when(type)
            {
                ZoomButtons.Zoom_Out ->
                {
                    if(Zooming_Rates.rate> 0 )
                    {
                        Result_Image.mBuffer= Zooming.Zoomout(Result_Image.mBuffer,Result_Image.mImage!!.Width,Result_Image.mImage!!.Height)
                        Zooming_Rates.rate--
                    }
                }
                ZoomButtons.Zoom_In->
                {

                    if(Zooming_Rates.rate < 2)
                    {
                        Result_Image.mBuffer= Zooming.Zoom(Result_Image.mBuffer,Result_Image.mImage!!.Width,Result_Image.mImage!!.Height)
                        YoYo.with(Techniques.ZoomIn).duration(AnimDuration.Click).playOn(mCanvas)
                        Zooming_Rates.rate++
                    }
                }

            }
            Result_Image.mBitmap=Bitmap.createBitmap(Zooming.newWidth, Zooming.newHeight,Bitmap.Config.ARGB_8888)
            Result_Image.mBitmap!!.copyPixelsFromBuffer(Result_Image.mBuffer)
            Result_Image.mImage!!.Width= Zooming.newWidth
            Result_Image.mImage!!.Height= Zooming.newHeight

            mCanvas.CalculateOffsetLeft_Top(Result_Image.mImage!!.Width,Result_Image.mImage!!.Height)
            mCanvas.Invalidate()

            System.gc()
        }catch (e:Exception)
        {

        }

    }

    override fun onDrawing(points: ArrayList<Point>, color: Int, size: Int, type:DrawingButtons) {
       when(type){
           DrawingButtons.Brush->{
               mCanvas.penType=DrawingButtons.Brush
               Pen.PenSquare(points)
               mCanvas.Invalidate()
           }
           DrawingButtons.Rubber->{
               mCanvas.penType=DrawingButtons.Rubber
               Pen.Rupper(points)
               mCanvas.Invalidate()
           }
           DrawingButtons.Spray-> {
               mCanvas.penType=DrawingButtons.Spray
               Pen.PenRadius(points)
               mCanvas.Invalidate()
           }
           DrawingButtons.Fill->
           {
               mCanvas.penType=DrawingButtons.Fill
               val point=points.get(0)
               Current_Color.color=Result_Image.mBitmap.getPixel(point.X.toInt(),point.Y.toInt())
               Fill.fill(point,color)
               mCanvas.Invalidate()
           }


       }


    }


    override fun onHueSaturationControllerChanged(type: AdjustController, value: Int) {

        Tools.setBitmap(Result_Image.mBitmap)

        when(type)
        {
            AdjustController.Hue ->
            {

                HueFilter.huenotcolorize(value.toFloat())

            }
            AdjustController.Saturation->
            {
                SaturationFilter.Saturationcolorize(value.toFloat()/100f)

            }

        }
        mCanvas.Invalidate()
    }

    override fun onColorSliceChanged(slice: String) {

    }

    override fun onBrightnessControllerChanged(value: Int) {

    }

    override fun onColorEditingControllerChanged(type: AdjustController, value: Int) {

        Tools.setBitmap(Result_Image.mBitmap)
        when(type)
        {
            AdjustController.Hue->
            {
               HueFilter.huecolorize(value.toFloat())
            }
            AdjustController.Saturation->
            {

            }
        }
        mCanvas.Invalidate()
    }

    override fun onTextSubmittingClicked(text: String?) {

       Text.SubmitText(text,mCanvas)
        temp_fragment=textPropertiesFragment
        toolType=Tool_Type.Text
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_board_layout,temp_fragment).addToBackStack(App_Levels.tool_menu).commit()


        second_board_layout.visibility=View.VISIBLE
        expand_down_layout.visibility=View.VISIBLE

        expand_up_layout.visibility=View.VISIBLE



        YoYo.with(Techniques.SlideInLeft)
            .duration(AnimDuration.Translate).playOn(second_board_layout)
        YoYo.with(Techniques.SlideInLeft)
            .duration(AnimDuration.Translate).playOn(expand_down_layout)
        YoYo.with(Techniques.SlideInRight)
            .duration(AnimDuration.Translate).playOn(main_board)
        YoYo.with(Techniques.SlideInRight)
            .duration(AnimDuration.Translate).playOn(expand_up_layout)

    }

    override fun onChangeTextProperties(type: TextPropertiesButtons, value: Int) {

        when(type)
        {
            TextPropertiesButtons.color->
            {
                Text.tPaint.setARGB(255, Color.red(value),Color.green(value),Color.blue(value))
                mCanvas.Invalidate()
            }
            TextPropertiesButtons.size->
            {
                 Text.tPaint.textSize=value.toFloat()
                mCanvas.Invalidate()
            }
            TextPropertiesButtons.type->
            {
                Text.tPaint.typeface= text_type_Array_Data.get(value).type
                mCanvas.Invalidate()
            }
        }
    }

    override fun Invalidate() {
        mCanvas.Invalidate()
    }

    override fun onBackPressed() {
        super.onBackPressed()
       when(AppLevel)
       {
           Fragment_Level.in_main_menu->
           {
               hidden=true
               zoom_layout.visibility=View.INVISIBLE
               rotate_layout.visibility=View.INVISIBLE
               to_Back_request(this,App_Levels.tool_menu)
           }
           Fragment_Level.in_selector ->
           {
               to_Back_request(this,App_Levels.selector_menu)
               YoYo.with(Techniques.SlideInLeft).duration(AnimDuration.Translate).playOn(main_board)
               zoom_layout.visibility=View.VISIBLE
               rotate_layout.visibility=View.VISIBLE
               YoYo.with(Techniques.SlideInLeft).duration(AnimDuration.Translate).playOn(zoom_layout)
               YoYo.with(Techniques.SlideInLeft).duration(AnimDuration.Translate).playOn(rotate_layout)
               AppLevel=Fragment_Level.in_main_menu

           }
           Fragment_Level.in_tool->
           {
               to_Back_request(this,App_Levels.tool_menu)
               YoYo.with(Techniques.SlideInLeft).duration(AnimDuration.Translate).playOn(main_board)
               if(supportFragmentManager.fragments.contains(temp_fragment))
               {
                   supportFragmentManager.beginTransaction().remove(temp_fragment).commit()
               }
               if(supportFragmentManager.fragments.contains(default_fragment))
               {
                   supportFragmentManager.beginTransaction().remove(default_fragment).commit()

               }
               if(supportFragmentManager.fragments.contains(blankFragment))
               {
                   supportFragmentManager.beginTransaction().remove(blankFragment).commit()
               }

               mCanvas.tool=Tool_Type.Null
               toolType=Tool_Type.Null
               try{
                   layout_view.removeView(mRectangle)
                   layout_view.removeView(mLine)
                   layout_view.removeView(mCircle)
                   layout_view.invalidate()
               }catch (e:java.lang.Exception)
               {
                   Toast.makeText(context,"Errrror",Toast.LENGTH_SHORT).show()
               }
               Result_Image.mBuffer.position(0)
               Result_Image.mBitmap.copyPixelsFromBuffer(Result_Image.mBuffer)


               PenPoints.clear()
               mCanvas.Invalidate()
               second_board_layout.visibility=View.VISIBLE
               expand_up_layout.visibility=View.INVISIBLE
               expand_down_layout.visibility=View.INVISIBLE
               expand_up.visible=true
               expand_down.visible=true
               AppLevel=Fragment_Level.in_selector
           }
       }
    }

    override fun onSaveSubmitted(name: String, type: String) {
        when(type)
        {
            "Bmp"->{
                Imageio.write(Result_Image.mImage!!,ImageType.Bmp,FilePath.path+"/$name")
                Toast.makeText(context,"Saved in ${FilePath.path}",Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
            "Jpg"->{
                Imageio.write(Result_Image.mImage!!,ImageType.Jpeg,FilePath.path+"/$name")
                Toast.makeText(context,"Saved in ${FilePath.path}",Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
            "Png" ->{
                try {
                    val f=File(FilePath.path+"/$name")
                    FileOutputStream(f).use { out ->
                        Result_Image.mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    }
                        Toast.makeText(context,"Saved in ${FilePath.path}",Toast.LENGTH_SHORT).show()
                        onBackPressed()

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onShapeSelected(id: Int) {
        try {
        when(id)
        {
                R.drawable.line->
                {
                    toolType = Tool_Type.Line
                    mLine.mleft = mCanvas.mleft
                    mLine.mtop = mCanvas.mtop
                    layout_view.addView(mLine)

                    temp_fragment = ShapeProperties
                    AppLevel = Fragment_Level.in_tool
                    supportFragmentManager.beginTransaction().replace(R.id.main_board_layout, temp_fragment)
                        .addToBackStack(App_Levels.tool_menu).commit()

                    YoYo.with(Techniques.SlideInRight).duration(AnimDuration.Translate).playOn(main_board)

                }
                R.drawable.circle->{


                    mCircle.mleft=mCanvas.mleft
                    mCircle.mtop=mCanvas.mtop
                    toolType=Tool_Type.Circle
                    layout_view.addView(mCircle)
                    AppLevel = Fragment_Level.in_tool

                    temp_fragment = ShapeProperties
                    supportFragmentManager.beginTransaction().replace(R.id.main_board_layout, temp_fragment)
                        .addToBackStack(App_Levels.tool_menu).commit()

                    YoYo.with(Techniques.SlideInRight).duration(AnimDuration.Translate).playOn(main_board)

                }
                R.drawable.rectangle->{

                    toolType = Tool_Type.Rectangle
                    mRectangle.mtop = mCanvas.mtop
                    mRectangle.mleft = mCanvas.mleft
                    layout_view.addView(mRectangle)
                    temp_fragment = ShapeProperties
                    AppLevel = Fragment_Level.in_tool
                    supportFragmentManager.beginTransaction().replace(R.id.main_board_layout, temp_fragment)
                        .addToBackStack(App_Levels.tool_menu).commit()

                    YoYo.with(Techniques.SlideInRight).duration(AnimDuration.Translate).playOn(main_board)


                }


        }
        }catch (e:java.lang.Exception){}
        temp_fragment=ShapeProperties
        AppLevel=Fragment_Level.in_tool
        supportFragmentManager.beginTransaction().
            replace(R.id.main_board_layout,temp_fragment).addToBackStack(App_Levels.tool_menu).commit()
        supportFragmentManager.beginTransaction().
            replace(R.id.secondary_board_layout,second_board_fragment).addToBackStack(App_Levels.tool_menu).commit()

        YoYo.with(Techniques.SlideInLeft).duration(AnimDuration.Translate).playOn(second_board_layout)

        YoYo.with(Techniques.SlideInRight).duration(AnimDuration.Translate).playOn(main_board)
    }

    override fun onChangeShapeProperties(type: ShapePropertiesButtons, value: Int) {
        when(type)
        {
            ShapePropertiesButtons.Size->{
                mRectangle.mRect.RectPaint.strokeWidth=value*4f
                mLine.mLine.Lpaint.strokeWidth=value*4f
                mCircle.mCircle.CPaint.strokeWidth=value*4f

            }
            ShapePropertiesButtons.sColor->{
                mRectangle.mRect.RectPaint.color=value
                mLine.mLine.Lpaint.color=value
                mCircle.mCircle.CPaint.color=value
            }
        }
        layout_view.invalidate()
        mRectangle.invalidate()
        mLine.invalidate()
        mCircle.invalidate()
    }
}