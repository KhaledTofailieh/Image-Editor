package my.Activities

import Extensions.AnimDuration
import Extensions.FragmentListener
import Extensions.RotationButtons
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class Rotate_left_right_fragment : Fragment() {

lateinit var rotate_left:ImageView
    lateinit var rotate_right:ImageView
    lateinit var flip_H:ImageView
    lateinit var flip_V:ImageView
    lateinit var view1:View
    lateinit var mListener:FragmentListener
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       view1=inflater.inflate(R.layout.rotate_left_right_fragment, container, false)

        rotate_left=view1.findViewById(R.id.Rotate_left_button)
        rotate_right=view1.findViewById(R.id.Rotate_right_button)
        flip_H=view1.findViewById(R.id.flip_H)
        flip_V=view1.findViewById(R.id.flip_V)

        rotate_right.setOnClickListener {
            YoYo.with(Techniques.BounceIn).duration(AnimDuration.Click).playOn(rotate_right)
           mListener.onRotateButtonClicked(RotationButtons.Rotate_Right)
        }
        rotate_left.setOnClickListener {
            YoYo.with(Techniques.BounceIn).duration(AnimDuration.Click).playOn(rotate_left)
            mListener.onRotateButtonClicked(RotationButtons.Rotate_Left)
        }
        flip_V.setOnClickListener {
            YoYo.with(Techniques.BounceIn).duration(AnimDuration.Click).playOn(flip_V)
            mListener.onRotateButtonClicked(RotationButtons.Flip_V)
         }
        flip_H.setOnClickListener {
            YoYo.with(Techniques.BounceIn).duration(AnimDuration.Click).playOn(flip_H)
            mListener.onRotateButtonClicked(RotationButtons.Flip_H)
        }
        return view1
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener=context as FragmentListener
    }
}
