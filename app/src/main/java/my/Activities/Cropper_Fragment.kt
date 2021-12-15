package my.Activities

import Extensions.FragmentListener
import Extensions.Screen_Size
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout


class Cropper_Fragment : Fragment() {
    lateinit var view1:View

    lateinit var mListener: FragmentListener
    lateinit var mContext: Context
    lateinit var mLayout: FrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      view1=inflater.inflate(R.layout.cropper_fragment, container, false)
        view1.minimumHeight= Screen_Size.Height
        view1.minimumWidth= Screen_Size.Width
        mLayout=view1.findViewById(R.id.CropperLayout)

        return  view1
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext=context!!
        mListener=mContext as FragmentListener

    }
}
