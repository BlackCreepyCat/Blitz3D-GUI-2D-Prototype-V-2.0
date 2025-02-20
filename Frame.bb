; -----------------------------------
; Public : Function to create a Frame
; -----------------------------------
Function CreateFrame.GuiObject(Px%, Py%, Tx%, Ty%, Caption$, Parent.GuiObject )
	G.GuiObject = New GuiObject
	
	G\Family = Gui_ObjectTypeFrame%
	G\Parent = Parent
	G\firstChild = Null
	G\nextSibling = Null
	
	G\Px% = Px%
	G\Py% = Py%
	
	G\Tx% = Tx%
	G\Ty% = Ty%
	
	G\R% = Gui_Color_FrameBorder_R%
	G\G% = Gui_Color_FrameBorder_G%
	G\B% = Gui_Color_FrameBorder_B%
	
	G\Visible = True
	G\Disable = True
	
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

; ---------------------------
; Internal : Redraw the Frame
; ---------------------------
Function Gui_DrawFrame(Frame.GuiObject)
	
	If Frame\Family = Gui_ObjectTypeFrame% And Frame\Visible = True Then
		; Frame background
		Gui_Rect(Frame\Px,Frame\Py, Frame\Tx , Frame\Ty , 1 , Frame\R% , Frame\G% , Frame\B% , 2)
		Gui_Rect(Frame\Px + Frame\Tx/ 2 - (StringWidth (Frame\Caption$) / 2) -2 , Frame\Py - 1 , StringWidth (Frame\Caption$) + 4 , 4 , 1 , Gui_Color_WinBackground_R% , Gui_Color_WinBackground_G% , Gui_Color_WinBackground_B% , 0)
		
		; Draw the text
		Gui_Text(Frame\Px + Frame\Tx/ 2 - (StringWidth (Frame\Caption$) / 2) , Frame\Py - (StringHeight ("A") / 2), Frame\Caption$ , 200,200,200)
	EndIf
	
End Function

; ----------------------------
; Internal : Refresh the Frame
; ----------------------------
Function Gui_RefreshFrame(Frame.GuiObject)
	
	
	
	
	If Gui_GetParentWindow(Frame) = Gui_CurrentWindow And Frame\Visible = True And Frame\Disable = False  Then
		Frame\MouseOn% = False
		
		; If the button is highlighted i create the events
		If Gui_TestZone(Frame\Px,Frame\Py,  Frame\Tx , Frame\Ty,False, False) And Gui_MousePressLeft% Then
			Frame\MouseOn% = True
			
			; If the button is pressed i create the events
			If GUI_MouseClickLeft Then
				Gui_AddEvent(Gui_Event_Pressed%, Frame)
				DebugLog "GUI MSG: Frame Event [Pressed] Created by ["+ Frame\Caption$ + "]"
				Gui_PreviousSelected = Frame
			EndIf
		EndIf
		
		; If the button is clicked i create the events
		If Gui_TestZone(Frame\Px,Frame\Py,  Frame\Tx , Frame\Ty) And Gui_MouseReleaseLeft% And (Gui_PreviousSelected = Frame) Then
			Frame\MouseOn% = True
			
			Gui_AddEvent(Gui_Event_Clicked%, Frame)
			DebugLog "GUI MSG: Frame Event [Clicked] Created by [" + Frame\Caption$ + "]"
		EndIf	
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D