package my.Activities

import Extensions.AnimDuration
import Extensions.FragmentListener
import Extensions.MainButtons
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


class main_fragment : Fragment() {

  lateinit var mListener:FragmentListener
   lateinit var Edit_button:ImageButton
    lateinit var Adjust_button:ImageButton
     lateinit var Filters_button:ImageButton
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.main_btns_fragment, container, false)

        this.Edit_button =view.findViewById(R.id.Edit_button)
        this.Adjust_button =view.findViewById(R.id.Adjust_button)
        this.Filters_button =view.findViewById(R.id.Filter_button)

        this.Adjust_button.setOnClickListener {

            YoYo.with(Techniques.BounceIn).duration(AnimDuration.Click).playOn(Adjust_button)
            mListener.onMainButtonClicked(MainButtons.Adjust)
        }
        this.Edit_button.setOnClickListener {

            YoYo.with(Techniques.BounceIn).duration(AnimDuration.Click).playOn(Edit_button)
            mListener.onMainButtonClicked(MainButtons.Edit)
        }
        this.Filters_button.setOnClickListener {

            YoYo.with(Techniques.BounceIn).duration(AnimDuration.Click).playOn(Filters_button)
            mListener.onMainButtonClicked(MainButtons.Filters)
        }


        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener=context as FragmentListener
    }
}
