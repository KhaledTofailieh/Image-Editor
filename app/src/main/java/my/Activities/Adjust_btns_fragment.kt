package my.Activities

import Extensions.AdjustingButtons
import Extensions.AnimDuration
import Extensions.FragmentListener
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

@SuppressLint("ValidFragment")
class Adjust_btns_fragment(val mContext:Context): Fragment() {
     lateinit var EditColor:ImageButton

     lateinit var HSFilter:ImageButton
    lateinit var mListener:FragmentListener
    lateinit var mView:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

      mView= inflater.inflate(R.layout.adjust_btns_fragment, container, false)
        HSFilter=mView.findViewById(R.id.HS_button)
        EditColor=mView.findViewById(R.id.Color_edit_button)


        HSFilter.setOnClickListener {
            YoYo.with(Techniques.Bounce).duration(AnimDuration.Click).playOn(HSFilter)
            mListener.onAdjustingButtonClicked(AdjustingButtons.HueSaturation)
        }

        EditColor.setOnClickListener {
            mListener.onAdjustingButtonClicked(AdjustingButtons.EditColor)
            YoYo.with(Techniques.Bounce).duration(AnimDuration.Click).playOn(EditColor)

        }
        return mView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as FragmentListener
    }
}
