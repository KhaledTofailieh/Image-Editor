package my.Activities

import Extensions.EditingButtons
import Extensions.FragmentListener
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class Edit_btns_fragment : Fragment() {

    lateinit var mListener:FragmentListener
    lateinit var Brush_button:ImageButton
    lateinit var Crop_button:ImageButton
    lateinit var Text_button:ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.edit_btns_fragment, container, false)

        Brush_button=view.findViewById(R.id.brush_button)
        Crop_button=view.findViewById(R.id.crop_button)
        Text_button=view.findViewById(R.id.text_button)

        Brush_button.setOnClickListener {
            mListener.onEditingButtonClicked(EditingButtons.Paint)

        }
        Crop_button.setOnClickListener {
            mListener.onEditingButtonClicked(EditingButtons.Crop)

        }
        Text_button.setOnClickListener {

            mListener.onEditingButtonClicked(EditingButtons.Text)
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mListener=context as FragmentListener
    }
}
