package Spinners.Data

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import my.Activities.R

data class Size_item_info(val number:Int, val image:Int)

val Sizes_information_List= arrayOf(
    Size_item_info(5,R.drawable.ic_size_1), Size_item_info(10,R.drawable.ic_size_2)
    ,Size_item_info(15,R.drawable.ic_size_3),Size_item_info(20,R.drawable.ic_size_4)
    ,Size_item_info(25,R.drawable.ic_size_5))

