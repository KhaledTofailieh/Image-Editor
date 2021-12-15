package my.Activities

import Extensions.ExpandButtons
import Extensions.FragmentListener
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class ExpendDownFragment : Fragment() {
    lateinit var expand_btn: ImageView
    lateinit var mListener:FragmentListener
     var visible=true
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view1=inflater.inflate(R.layout.expend_down_fragment, container, false)

        expand_btn=view1.findViewById(R.id.expend_down_button)
        expand_btn.setOnClickListener {
            visible=
            if(visible)
            {
                expand_btn.setImageResource(R.drawable.ic_expand_down)
                mListener.onExpandRequest(ExpandButtons.Expand_Down,visible)
            }else
            {
                expand_btn.setImageResource(R.drawable.ic_expand_up)
                mListener.onExpandRequest(ExpandButtons.Expand_Down,visible)
            }
        }

        return view1
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener=context as FragmentListener
    }





}
