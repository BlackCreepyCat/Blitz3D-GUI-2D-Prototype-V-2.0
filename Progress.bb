; --------------------------------------
; Public : Function to create a Progress
; --------------------------------------
Function CreateProgress.GuiObject(Px%, Py%, Tx%, Ty%, Caption$, Min#, Max# , Value# , Parent.GuiObject )
	G.GuiObject = New GuiObject
	
	G\Family = Gui_ObjectTypeProgress%
	G\Parent = Parent
	G\firstChild = Null
	G\nextSibling = Null
	
	G\Px% = Px%
	G\Py% = Py%
	
	G\Tx% = Tx%
	G\Ty% = Ty%
	
	G\R% = Gui_Color_ProgressBackground_R%
	G\G% = Gui_Color_ProgressBackground_G%
	G\B% = Gui_Color_ProgressBackground_B%
	
	G\ValMin# = Min#
	G\ValMax# = Max#
	G\Value#  = Value#
	
	G\Visible = True
	G\Disable = False
	
	G\Caption$ = Caption$
	
	; Compute the gadget position from the parent
	If Parent\Family = Gui_ObjectTypeWindow Then
		; Compute relative position from parent (if its a window)
		G\Px = Parent\Px + Px + 2
		G\Py = Parent\Py + Py + (Parent\TitleHeight + 2)
	Else
		; Compute relative position from parent (for other gadgets, no offset needed)
		G\Px = Parent\Px + Px
		G\Py = Parent\Py + Py
	EndIf	
	
	G\initialRelativeX = Px  ; Storing the first position relative 
	G\initialRelativeY = Py
	
	Gui_AddChild(Parent, G)
	
	Return G
End Function

; ------------------------------
; Internal : Redraw the Progress
; ------------------------------
Function Gui_DrawProgress(Progress.GuiObject)
	
	If Progress\Family = Gui_ObjectTypeProgress% And Progress\Visible = True Then
		; Progress background
		Gui_Rect(Progress\Px,Progress\Py, Progress\Tx , Progress\Ty , 1 , Progress\R%/2 , Progress\G%/2 , Progress\B%/2 , 1)
		
		
		; -----------------------
		; Cursor size computation
		; -----------------------
		CursorSx = Int(Progress\Value# * (Progress\Tx - 4)) / Progress\ValMax#
		
		If CursorSx < 1 Then CursorSx = 1
		If CursorSx > Progress\Tx - 3 Then CursorSx = Progress\Tx - 3
		
		Gui_Rect(Progress\Px+2,Progress\Py+2, CursorSx , Progress\Ty - 4 , 1 , Progress\R% , Progress\G% , Progress\B% , 0)
		
		; Draw the text
		Caption$ = Str$( Int(100 * Progress\Value# / Progress\ValMax#) ) 	
		Gui_Text(Progress\Px + Progress\Tx/ 2 - (StringWidth (Progress\Caption$) /2 + StringWidth (Caption$)  / 2) , Progress\Py + (Progress\Ty / 2) - (StringHeight ("A") / 2), Progress\Caption$ + Caption$ + "%"  , 200,200,200)
	EndIf
	
End Function

; -------------------------------
; Internal : Refresh the Progress
; -------------------------------
Function Gui_RefreshProgress(Progress.GuiObject)
	
	If Gui_GetParentWindow(Progress) = Gui_CurrentWindow And Progress\Visible = True And Progress\Disable = False  Then
		Progress\MouseOn% = False
		
		; If the button is highlighted i create the events
		If Gui_TestZone(Progress\Px,Progress\Py,  Progress\Tx , Progress\Ty,False, False) And Gui_MousePressLeft% Then
			Progress\MouseOn% = True
			
			; If the button is pressed i create the events
			If GUI_MouseClickLeft Then
				Gui_AddEvent(Gui_Event_Pressed%, Progress)
				DebugLog "GUI MSG: Progress Event [Pressed] Created by ["+ Progress\Caption$ + "]"
				Gui_PreviousSelected = Progress
			EndIf
		EndIf
		
		; If the button is clicked i create the events
		If Gui_TestZone(Progress\Px,Progress\Py,  Progress\Tx , Progress\Ty) And Gui_MouseReleaseLeft% And (Gui_PreviousSelected = Progress) Then
			Progress\MouseOn% = True
			
			Gui_AddEvent(Gui_Event_Clicked%, Progress)
			DebugLog "GUI MSG: Progress Event [Clicked] Created by [" + Progress\Caption$ + "]"
		EndIf	
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D