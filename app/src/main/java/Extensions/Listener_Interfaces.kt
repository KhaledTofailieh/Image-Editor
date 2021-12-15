package Extensions

interface FragmentListener
{
    fun onMainButtonClicked(type:MainButtons)
    fun onEditingButtonClicked(type:EditingButtons)
    fun onAdjustingButtonClicked(type:AdjustingButtons)
    fun onChangeBrush(type:BrushFeatureButtons)
    fun onExpandRequest(type:ExpandButtons,Expanded:Boolean):Boolean
    fun onSecondaryButtonClicked(type:Secondary_buttons)
    fun onRotateButtonClicked(type:RotationButtons)
    fun onZoomButtonClicked(type:ZoomButtons)
    fun onTextSubmittingClicked(text:String?)
    fun onSaveSubmitted(name:String,type:String)
    fun onChangeTextProperties(type:TextPropertiesButtons , value:Int)
    fun onChangeShapeProperties(type:ShapePropertiesButtons , value:Int)
}
interface DrawerListener
{
   fun onDrawing(point:ArrayList<Point>,color:Int,size:Int,type:DrawingButtons)
}
interface ShapeSelectionListener
{
    fun onShapeSelected(id:Int)
}
interface AdjustingListenerInerface
{
    fun onHueSaturationControllerChanged(type:AdjustController, value:Int)
    fun onColorEditingControllerChanged(type: AdjustController,value:Int)
    fun onColorSliceChanged(slice:String)
    fun onBrightnessControllerChanged(value: Int)

}
interface CanvasInvalidater
{
    fun Invalidate()
}
interface  ImageGeneration
{
    fun onOkPressed(height:Int , width:Int , color: Int)
}


