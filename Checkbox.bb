; --------------------------------------
; Public : Function to create a Checkbox
; --------------------------------------
Function CreateCheckbox.GuiObject(Px%, Py% , Caption$, Parent.GuiObject, State% = True)
	G.GuiObject = New GuiObject
	
	G\Family = Gui_ObjectTypeCheckbox%
	G\Parent = Parent
	G\firstChild = Null
	G\nextSibling = Null
	
	G\Px% = Px%
	G\Py% = Py%
	
	G\Tx% = StringWidth(Caption$) + 15
	G\Ty% = 20
	
	G\R% = 97
	G\G% = 205
	G\B% = 254
	
	G\Visible = True
	G\Disable = False
	
	G\State% = State%
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
; Internal : Redraw the Checkbox
; ------------------------------
Function Gui_DrawCheckbox(Checkbox.GuiObject)
	
	If Checkbox\Family = Gui_ObjectTypeCheckbox% And Checkbox\Visible = True Then
		; Frame background
		Gui_Rect(Checkbox\Px,Checkbox\Py , Checkbox\Ty , Checkbox\Ty , 1 , Checkbox\R% , Checkbox\G% , Checkbox\B% , 1)
		
		; Draw the text
		Gui_Text(Checkbox\Px + Checkbox\Ty + 6 , Checkbox\Py + Checkbox\Ty - (StringHeight ("A") + 3 ) , Checkbox\Caption$ , Gui_Color_CheckboxCaption_R% , Gui_Color_CheckboxCaption_G% , Gui_Color_CheckboxCaption_B%)
		
		; Caption change from the state 
		If Checkbox\State% = 1 Then 
			Gui_Text(Checkbox\Px + 7 , Checkbox\Py + 2 , "X" , 0 , 0, 0 )
		EndIf
		
	EndIf
	
End Function

; -------------------------------
; Internal : Refresh the Checkbox
; -------------------------------
Function Gui_RefreshCheckbox(Checkbox.GuiObject)
	
	If Gui_GetParentWindow(Checkbox) = Gui_CurrentWindow And Checkbox\Visible = True And Checkbox\Disable = False  Then
		Checkbox\MouseOn% = False
		
		; If the button is highlighted i create the events
		If Gui_TestZone(Checkbox\Px , Checkbox\Py , Checkbox\Tx , Checkbox\Ty , False , False) And Gui_MousePressLeft% Then
			Checkbox\MouseOn% = True
			
			; If the button is pressed i create the events
			If GUI_MouseClickLeft Then
				Checkbox\State% = 1 - Checkbox\State%
				
				Gui_AddEvent(Gui_Event_Pressed%, Checkbox)
				DebugLog "GUI MSG: Checkbox Event [Pressed] Created by ["+ Checkbox\Caption$ + "] / State "+ Checkbox\State%
				Gui_PreviousSelected = Checkbox
			EndIf
		EndIf
		
		; If the button is clicked i create the events
		If Gui_TestZone(Checkbox\Px,Checkbox\Py, Checkbox\Tx , Checkbox\Ty) And Gui_MouseReleaseLeft% And (Gui_PreviousSelected = Checkbox) Then
			Checkbox\MouseOn% = True
			
			Gui_AddEvent(Gui_Event_Clicked%, Checkbox)
			DebugLog "GUI MSG: Checkbox Event [Clicked] Created by [" + Checkbox\Caption$ + "] / State "+ Checkbox\State%
		EndIf	
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D