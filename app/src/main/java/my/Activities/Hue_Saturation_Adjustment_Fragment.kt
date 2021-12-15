package my.Activities

import Extensions.AdjustController
import Extensions.AdjustingListenerInerface
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*


@SuppressLint("ValidFragment")
class Hue_Saturation_Adjustment_Fragment(val mContext: Context) : Fragment() {
    lateinit  var mView:View
    lateinit var HueAdjust:SeekBar
    lateinit var SaturationAdjust:SeekBar
    lateinit var mListener:AdjustingListenerInerface
    lateinit var HValue:EditText
    lateinit var SValue:EditText
   var StrValue="0"
    lateinit var Imm:InputMethodManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       mView=  inflater.inflate(R.layout.hue__saturation__adjustment_fragment, container, false)

        HueAdjust=mView.findViewById(R.id.Hue_Controller)
        SaturationAdjust=mView.findViewById(R.id.Saturation_Controller)
        HValue=mView.findViewById(R.id.HueValue_txt)
        SValue=mView.findViewById(R.id.SaturationValue_txt)

        HueAdjust.max=359
        SaturationAdjust.max=240
        SaturationAdjust.progress=100

        HueAdjust.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                HValue.setText(HueAdjust.progress.toString())
                HValue.hint=HueAdjust.progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                mListener.onHueSaturationControllerChanged(AdjustController.Hue,HueAdjust.progress)
            }

        })

        SaturationAdjust.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                SValue.hint=SaturationAdjust.progress.toString()
                SValue.setText(SaturationAdjust.progress.toString())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                mListener.onHueSaturationControllerChanged(AdjustController.Saturation,SaturationAdjust.progress)
            }
        })
        HValue.setOnEditorActionListener { textView, i, keyEvent ->
            StrValue=HValue.text.toString()
            when(i)
            {
                EditorInfo.IME_ACTION_DONE->
                {
                    HValue.hint=HValue.text
                    HueAdjust.progress=StrValue.toInt()
                    mListener.onHueSaturationControllerChanged(AdjustController.Hue,StrValue.toInt())
                    Imm=mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    Imm.hideSoftInputFromWindow(HValue.applicationWindowToken,0)
                    true
                }
                else->{ false}
            }
        }
        SValue.setOnEditorActionListener { textView, i, keyEvent ->
            StrValue=SValue.text.toString()
            when(i) {
                EditorInfo.IME_ACTION_DONE ->
                {
                    SValue.hint=SValue.text
                    SaturationAdjust.progress=StrValue.toInt()
                    Imm=mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    Imm.hideSoftInputFromWindow(SValue.applicationWindowToken,0)
                    mListener.onHueSaturationControllerChanged(AdjustController.Saturation,StrValue.toInt())
                    true
                }
                else->{false}
            }
        }
        return mView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mListener=mContext as AdjustingListenerInerface
    }

}
