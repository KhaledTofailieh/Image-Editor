package my.Activities

import Extensions.ShapeSelectionListener
import Recycler_Views.Adapters.Shape_Recycle_Adapter
import Recycler_Views.Data.shapes_list_data
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ShapesAndStickers : Fragment() , ShapeSelectionListener {
    lateinit var mView: View
    lateinit var ShapesList: RecyclerView
    lateinit var mListener: ShapeSelectionListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      mView=inflater.inflate(R.layout.shapes_and_stickers_fragment, container, false)

        val linearmanager= LinearLayoutManager(this.context)

        ShapesList=mView.findViewById(R.id.shapes_list_view)
        linearmanager.orientation= LinearLayoutManager.HORIZONTAL
        val mAdapter= Shape_Recycle_Adapter(this.context!!, shapes_list_data)
        ShapesList.layoutManager=linearmanager
        ShapesList.adapter=mAdapter

        return mView
    }

    override fun onShapeSelected(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener=context as ShapeSelectionListener

    }
}
