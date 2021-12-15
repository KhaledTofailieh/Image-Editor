package my.Activities

import Extensions.AnimDuration
import Extensions.FragmentListener
import Extensions.ZoomButtons
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo


class Zoom_in_out_fragment : Fragment() {
lateinit var zoom_in:ImageView
    lateinit var zoom_out:ImageView
    lateinit var view1:View
    lateinit var mListener: FragmentListener
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       view1=inflater.inflate(R.layout.zoom_in_out_fragment, container, false)

        zoom_in=view1.findViewById(R.id.Zoom_in_button)
        zoom_out=view1.findViewById(R.id.Zoom_out_button)

        zoom_in.setOnClickListener {
            YoYo.with(Techniques.BounceIn).duration(AnimDuration.Click).playOn(zoom_in)
            mListener.onZoomButtonClicked(ZoomButtons.Zoom_In)
        }
       zoom_out.setOnClickListener {
           YoYo.with(Techniques.Bounce).duration(AnimDuration.Click).playOn(zoom_out)
           mListener.onZoomButtonClicked(ZoomButtons.Zoom_Out)
       }

        return  view1
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mListener = context as FragmentListener
    }
}
