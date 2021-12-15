package my.Activities

import Extensions.AdjustController
import Extensions.AdjustingListenerInerface
import Extensions.FragmentListener
import android.content.Context
import android.drm.DrmStore
import android.inputmethodservice.Keyboard
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import java.security.CodeSigner
import java.security.Key
import java.util.*

class EditColors_Fragment : Fragment() {
    lateinit  var mView:View
    lateinit var HueAdjust: SeekBar
    lateinit var SaturationAdjust: SeekBar
    lateinit var mListener: AdjustingListenerInerface
    lateinit var HValue: EditText
    lateinit var SValue: EditText
    lateinit var mContext: Context
    lateinit var Imm:InputMethodManager
    var StrValue="0"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    mView=inflater.inflate(R.layout.edit_colors_fragment, container, false)
        HueAdjust=mView.findViewById(R.id.HueColor_Controller)
        SaturationAdjust=mView.findViewById(R.id.SaturationColor_Controller)
        SaturationAdjust.progress=100
        SaturationAdjust.max=240
        HValue=mView.findViewById(R.id.HueColorValue_txt)
        SValue=mView.findViewById(R.id.SaturationColorValue_txt)

        HueAdjust.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                HValue.setText(HueAdjust.progress.toString())
                HValue.hint=HueAdjust.progress.toString()

            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                mListener.onColorEditingControllerChanged(AdjustController.Hue,HueAdjust.progress)

            }

        })
        SaturationAdjust.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                SValue.setText(SaturationAdjust.progress.toString())
                SValue.hint=SaturationAdjust.progress.toString()

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                mListener.onColorEditingControllerChanged(AdjustController.Saturation,SaturationAdjust.progress)
            }
        })
      SValue.setOnEditorActionListener { textView, i, keyEvent ->
          StrValue=SValue.text.toString()
              when(i)
              {
                  EditorInfo.IME_ACTION_DONE ->
                  {
                      SValue.hint=SValue.text
                      SaturationAdjust.progress=StrValue.toInt()
                      Imm=mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                      Imm.hideSoftInputFromWindow(SValue.applicationWindowToken,0)
                      mListener.onColorEditingControllerChanged(AdjustController.Hue,StrValue.toInt())
                     true
                  }
                  else->{false}
              }
          }
     HValue.setOnEditorActionListener { textView, i, keyEvent ->
         StrValue=HValue.text.toString()
         when(i)
         {
             EditorInfo.IME_ACTION_DONE->{
                 HValue.hint=HValue.text
                 HueAdjust.progress=StrValue.toInt()
                 Imm=mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                 Imm.hideSoftInputFromWindow(HValue.applicationWindowToken,0)
                 mListener.onColorEditingControllerChanged(AdjustController.Hue,StrValue.toInt())
                 true
             }
             else->{false}
         }
     }

        return mView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mListener=context as AdjustingListenerInerface
        this.mContext=context
    }
}
