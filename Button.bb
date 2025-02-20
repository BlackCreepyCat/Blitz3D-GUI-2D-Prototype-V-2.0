; ---------------------------------------
; Public : Function to to create a button
; ---------------------------------------
Function CreateButton.GuiObject(Px, Py, Tx, Ty, Caption$, Parent.GuiObject, Style% = 1)
	
	G.GuiObject = New GuiObject
	
	G\Family = Gui_ObjectTypeButton%
	G\Parent = Parent
	G\firstChild = Null
	G\nextSibling = Null
	
	G\Px = Px
	G\Py = Py
	G\Tx = Tx
	G\Ty = Ty
	
	G\R% = Gui_Color_ButtonBackground_R%
	G\G% = Gui_Color_ButtonBackground_G%
	G\B% = Gui_Color_ButtonBackground_B%
	
	G\Visible = True
	G\Disable = False
	G\Style% = Style% 
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

; ----------------------------
; Internal : Redraw the button
; ----------------------------
Function Gui_DrawButton(button.GuiObject)
	If button <> Null Then
		
		If button\Family = Gui_ObjectTypeButton% And button\Visible = True Then
			
			Select button\Style%
					
				Case 1
					; Button black background
					Gui_Rect(button\Px,button\Py, button\Tx ,  button\Ty, 0 , 20,20,20 , 1)
					
					; Button background
					If button\Disable = False Then
						Gui_Rect(button\Px + 1,button\Py + 1,  button\Tx -2 ,  button\Ty - 2, 1  , button\R%,button\G%,button\B% , 0 )
					Else
						Gui_Rect(button\Px + 1,button\Py + 1,  button\Tx -2 ,  button\Ty - 2, 1  , button\R%*2,button\G%*2,button\B%*2 , 0 )
					EndIf
					
					; Normal mode border	
					If button\MouseOn%=False Then
						Gui_Rect(button\Px + 1,button\Py + 1,  button\Tx -2 ,  button\Ty - 2, 0  , 140,150,160 , 0)
					Else
						Gui_Rect(button\Px + 1,button\Py + 1,  button\Tx -2 ,  button\Ty - 2, 0  , Gui_Color_ButtonSelected_R% ,Gui_Color_ButtonSelected_G% , Gui_Color_ButtonSelected_B% , 0)
					EndIf
					
					; Draw the text
					Gui_Text(button\Px + button\Tx/ 2 - (StringWidth (button\Caption$) / 2) , button\Py + (button\Ty / 2) - (StringHeight ("A") / 2), button\Caption$ , Gui_Color_ButtonCaption_R% , Gui_Color_ButtonCaption_G% , Gui_Color_ButtonCaption_B%)
					
				Case 2
						; Button background
						If button\Disable = False Then
							Gui_Rect(button\Px ,button\Py ,  button\Tx ,  button\Ty , 1  , button\R%,button\G%,button\B% , 0 )
						Else
							Gui_Rect(button\Px ,button\Py ,  button\Tx ,  button\Ty , 1  , button\R%*2,button\G%*2,button\B%*2 , 0 )
						EndIf
						
						; Normal mode border	
						If button\MouseOn%=True Then
							Gui_Rect(button\Px , button\Py,  button\Tx , button\Ty , 1  , 110,120,130 , 0)
						EndIf
						
						; Draw the text
						Gui_Text(button\Px + button\Tx/ 2 - (StringWidth (button\Caption$) / 2) , button\Py + (button\Ty / 2) - (StringHeight ("A") / 2), button\Caption$ , 200,200,200)
						
						
						
			End Select
			
		EndIf
		
	EndIf
End Function

; -----------------------------
; Internal : Refresh the button
; -----------------------------
Function Gui_RefreshButton(button.GuiObject)
	If button <> Null Then
		
		If Gui_GetParentWindow(button) = Gui_CurrentWindow And button\Visible = True And button\Disable = False  Then
			button\MouseOn% = False
			
			; If the button is highlighted i create the events
			If Gui_TestZone(button\Px,button\Py,  button\Tx , button\Ty,False, False) And Gui_MousePressLeft% Then
				button\MouseOn% = True
				
				; If the button is pressed i create the events
				If GUI_MouseClickLeft Then
					Gui_AddEvent(Gui_Event_Pressed%, button)
					DebugLog "GUI MSG: Button Event [Pressed] Created by ["+ button\Caption$ + "]"
					Gui_PreviousSelected = button
				EndIf
				
			EndIf
			
			; If the button is clicked i create the events
			If Gui_TestZone(button\Px,button\Py,  button\Tx , button\Ty , False , False ) And Gui_MouseReleaseLeft% And (Gui_PreviousSelected = button) Then
				Gui_AddEvent(Gui_Event_Clicked%, button)					
				DebugLog "GUI MSG: Button Event [Clicked] Created by [" + button\Caption$ + "]"
			EndIf	
			
		EndIf
		
	EndIf
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D