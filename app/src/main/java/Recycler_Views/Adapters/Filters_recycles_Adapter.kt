package Recycler_Views.Adapters

import Extensions.ShapeSelectionListener
import Recycler_Views.Data.shape_item_data

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.android.synthetic.main.filter_item.view.*
import my.Activities.R

class Shape_Recycle_Adapter(val context: Context, val Shapes:Array<shape_item_data> ) :RecyclerView.Adapter<Shape_Recycle_Adapter.mViewHolder>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): mViewHolder {
         val mView=LayoutInflater.from(context).inflate(R.layout.filter_item,p0,false)

      return mViewHolder(mView)
    }

    override fun getItemCount(): Int {
        return Shapes.size
    }

    override fun onBindViewHolder(p0: mViewHolder, p1: Int) {
        p0.HolderId=Shapes[p1].id
        p0.setData(Shapes[p1],p1)
    }

    inner class mViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)
    {

        val mListener:ShapeSelectionListener
        var HolderId:Int=0
        init{
            mListener=context as ShapeSelectionListener
            itemView.setOnClickListener {
             mListener.onShapeSelected(HolderId)
                YoYo.with(Techniques.Bounce).duration(250).playOn(itemView)
            }
        }
       fun setData(shape:shape_item_data , pos:Int)
       {
           itemView.Shape_icon.setImageResource(shape.id)
       }

    }
}