; -----------------------------------
; Public : Function to create a label
; -----------------------------------
Function CreateLabel.GuiObject(Px%, Py%, Caption$, Parent.GuiObject, CenterX% = 0 , CenterY% = 0)
	G.GuiObject = New GuiObject
	
	G\Family = Gui_ObjectTypeLabel%
	G\Parent = Parent
	G\firstChild = Null
	G\nextSibling = Null
	
	G\Px% = Px%
	G\Py% = Py%
	
	SetFont Gui_SystemFont% 
	G\Tx = StringWidth (Caption$)
	G\Ty = StringHeight (Caption$)
	
	G\R% = 255
	G\G% = 255
	G\B% = 255
	
	G\Visible = True
	G\Disable = False
	
	G\Caption$ = Caption$
	G\CenterX% = CenterX%
	G\CenterY% = CenterY%
	
	
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

; ---------------------------
; Internal : Redraw the label
; ---------------------------
Function Gui_DrawLabel(label.GuiObject)
	
	If label\Family = Gui_ObjectTypeLabel% And label\Visible = True Then
		
		; Change the zone from the string justifying
		Select label\CenterX%
			Case 0
				NewTx = 0
			Case 1
				NewTx = StringWidth (label\Caption$) / 2	
			Case 2
				NewTx = StringWidth (label\Caption$) 
		End Select
		
		
		Select label\CenterY%
			Case 0
				NewTy = 0
			Case 1
				NewTy = StringHeight (label\Caption$) / 2	
			Case 2
				NewTy = StringHeight (label\Caption$) 
		End Select
		
		Gui_Text(label\Px - NewTx ,label\Py - NewTy , label\Caption$ , label\R%,label\G%,label\B%)
	EndIf
	
End Function

; ----------------------------
; Internal : Refresh the label
; ----------------------------
Function Gui_RefreshLabel(label.GuiObject)
	
	If Gui_GetParentWindow(label) = Gui_CurrentWindow And label\Visible = True And label\Disable = False  Then
		label\MouseOn% = False
		
		; Change the zone from the string justifying
		Select label\CenterX%
			Case 0
				NewTx = 0
			Case 1
				NewTx = StringWidth (label\Caption$) / 2	
			Case 2
				NewTx = StringWidth (label\Caption$) 
		End Select
		
		Select label\CenterY%
			Case 0
				NewTy = 0
			Case 1
				NewTy = StringHeight (label\Caption$) / 2	
			Case 2
				NewTy = StringHeight (label\Caption$) 
		End Select
		
		; If the label is highlighted i create the events
		If Gui_TestZone(label\Px - NewTx,label\Py - NewTy,  label\Tx , label\Ty,False, False) And Gui_MousePressLeft% Then
			label\MouseOn% = True
			
			; If the button is pressed i create the events
			If GUI_MouseClickLeft Then
				Gui_AddEvent(Gui_Event_Pressed%, label)
				DebugLog "GUI MSG: Label Event [Pressed] Created by [" + label\Caption$ + "]"
				Gui_PreviousSelected = label
			EndIf
			
		EndIf
		
		; If the button is clicked i create the events
		If Gui_TestZone(label\Px - NewTx,label\Py - NewTy,  label\Tx , label\Ty) And Gui_MouseReleaseLeft% And (Gui_PreviousSelected = label) Then
			label\MouseOn% = True
			
			Gui_AddEvent(Gui_Event_Clicked%, label)
			DebugLog "GUI MSG: Label Event [Clicked] Created by [" + label\Caption$ + "]"
			Gui_PreviousSelected = Null
		EndIf	
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D