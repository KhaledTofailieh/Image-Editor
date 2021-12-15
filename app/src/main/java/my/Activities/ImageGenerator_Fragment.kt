package my.Activities

import Extensions.*
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.image_generator_fragment.*
import pCanvas.new_Image
import srcImageIO.Image
import yuku.ambilwarna.AmbilWarnaDialog

class ImageGenerator_Fragment : Fragment(){

    lateinit var mListener:ImageGeneration
    lateinit var mContext: Context
    lateinit var mLayout: FrameLayout
    lateinit var ok_button:ImageButton
    lateinit var choose_color:ImageButton
    lateinit var full_screen:ImageView
    lateinit var minimize_screen:ImageView
    lateinit var view1:View
    lateinit var blank_image:new_Image
    lateinit var board_layout:RelativeLayout
    lateinit var heightInput:EditText
    lateinit var widthInput:EditText
    lateinit var Imm:InputMethodManager

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view1 = inflater.inflate(R.layout.image_generator_fragment, container, false)
        view1.minimumHeight = Screen_Size.Height
        view1.minimumWidth = Screen_Size.Width

        mLayout = view1.findViewById(R.id.Generator_layout)
        board_layout = view1.findViewById(R.id.button_board)
        blank_image = new_Image(mContext)
        mLayout.addView(blank_image)
        ok_button = view1.findViewById(R.id.ok_create_button)
        choose_color = view1.findViewById(R.id.check_color_button)
        full_screen = view1.findViewById(R.id.set_full_screen)
        minimize_screen = view1.findViewById(R.id.set_minimize_btn)
        heightInput = view1.findViewById(R.id.height_input)
        widthInput = view1.findViewById(R.id.width_Input)

        mLayout.setOnTouchListener { view, motionEvent ->

            blank_image.onTouchEvent(motionEvent)
            val h=blank_image.getimageHeight().toString() ; val w=blank_image.getimageWidth().toString()

            heightInput.setText(h)
            heightInput.hint = h

            widthInput.setText(w)
            widthInput.hint = w
            true
        }


        ok_button.setOnClickListener {

            mListener.onOkPressed(blank_image.getimageHeight(), blank_image.getimageWidth(),  blank_image.mRect.RectPaint.color)

        }
        choose_color.setOnClickListener {

            val colorPicker = AmbilWarnaDialog(
                mContext,
                blank_image.mRect.RectPaint.color,
                object : AmbilWarnaDialog.OnAmbilWarnaListener {
                    override fun onCancel(dialog: AmbilWarnaDialog?) {

                    }

                    override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                        blank_image.mRect.RectPaint.color = color
                        blank_image.invalidate()
                    }

                })
            colorPicker.show()

        }
        full_screen.setOnClickListener {

            blank_image.setfullScreen()
            val h=blank_image.getimageHeight().toString() ; val w=blank_image.getimageWidth().toString()
            heightInput.setText(h)
            heightInput.hint = h

           widthInput.setText(w)
            widthInput.hint =w

            blank_image.invalidate()
        }
        minimize_screen.setOnClickListener {
            blank_image.setMinimize()

            val h=blank_image.getimageHeight().toString() ; val w=blank_image.getimageWidth().toString()

            heightInput.setText(h)
            heightInput.hint = h

            widthInput.setText(w)
            widthInput.hint = w

            blank_image.invalidate()
        }

        heightInput.setOnEditorActionListener { textView, i, keyEvent ->
            if(LegalHeight(heightInput.text.toString().toInt()))
            {
                when (i) {
                    EditorInfo.IME_ACTION_DONE -> {
                        heightInput.hint=heightInput.text
                        Imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        Imm.hideSoftInputFromWindow(heightInput.applicationWindowToken, 0)
                        blank_image.customHeight(heightInput.text.toString().toInt())
                        blank_image.invalidate()

                        true
                    }

                    else -> { false } }

            }

            else{
                Toast.makeText(mContext,"Height must between : 25 and ${Screen_Size.Height - board_layout.height}",Toast.LENGTH_SHORT).show()
                YoYo.with(Techniques.BounceIn).duration(AnimDuration.Click).playOn(heightInput)
                false }

        }
        widthInput.setOnEditorActionListener { textView, i, keyEvent ->
            if(LegalWidth(widthInput.text.toString().toInt()))
            {
                when (i) {
                    EditorInfo.IME_ACTION_DONE -> {

                        widthInput.hint=widthInput.text
                        Imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        Imm.hideSoftInputFromWindow(widthInput.applicationWindowToken, 0)
                        blank_image.customWidth(widthInput.text.toString().toInt())
                        blank_image.invalidate()
                        true
                    }

                    else -> {
                        Toast.makeText(mContext,"Width must between : 25 and ${Screen_Size.Width}",Toast.LENGTH_SHORT).show()
                        YoYo.with(Techniques.BounceIn).duration(AnimDuration.Click).playOn(widthInput)
                        false

                    }
                }
            }
           else {false}
        }

        return view1
    }

    fun LegalHeight(height:Int):Boolean
    {
       return ((height>=25) and (height <= Screen_Size.Height - board_layout.height) )
    }
    fun LegalWidth(width:Int):Boolean
    {
        return ((width>=25) and (width <= Screen_Size.Width))
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext=context!!
        mListener=mContext as ImageGeneration

    }

}

