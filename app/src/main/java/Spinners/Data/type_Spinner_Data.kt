package Spinners.Data

import my.Activities.R

data class  typeData(val name:String,val image:Int)

val types_info_list = arrayOf(typeData("Brush",R.drawable.brush),
                                            typeData("Spray",R.drawable.nspray),
                                            typeData("Rubber",R.drawable.nrubber),
                                            typeData("Fill",R.drawable.fill_icon)
)