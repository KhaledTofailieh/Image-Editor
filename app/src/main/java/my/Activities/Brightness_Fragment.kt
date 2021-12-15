package my.Activities

import Extensions.AdjustingListenerInerface
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView

class Brightness_Fragment : Fragment() {
    lateinit var mListener:AdjustingListenerInerface
    lateinit var  mView:View
    lateinit var BController:SeekBar
    lateinit var Bvalue:TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       mView=inflater.inflate(R.layout.brightness_fragment, container, false)
         Bvalue=mView.findViewById(R.id.BrightnessValue_txt)
         BController=mView.findViewById(R.id.Brightness_Controller)
         BController.max=100
        BController.progress=50
        BController.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener
        {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Bvalue.text=p1.toString()+"%"
                mListener.onBrightnessControllerChanged(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        return mView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener=context as AdjustingListenerInerface
    }
}
