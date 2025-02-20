Type GuiObject
	
	Field Family%
	Field Parent.GuiObject
	
    Field firstChild.GuiObject 		; Premier enfant
    Field nextSibling.GuiObject 	; Prochain enfant du même parent
	
	Field initialRelativeX      	; Position relative initiale par rapport au parent
    Field initialRelativeY
	
    Field Gadget_A.GuiObject 		; For all Gadgets
    Field Gadget_B.GuiObject 		; For all Gadgets
    Field Gadget_C.GuiObject 		; For all Gadgets
	
	Field Px%
	Field Py%
	
	Field Tx%
	Field Ty%
	
	Field OldPx%
	Field OldPy%
	Field OldTx%
	Field OldTy%
	
	Field R%					; For all Gadgets
	Field G%					; For all Gadgets
	Field B%					; For all Gadgets
	
	Field Switch%				; For the Checkbox
	
	Field CenterX%				; For the Labels
	Field CenterY%				; For the Labels
	
	Field Value#				; For all Gadgets
	Field ValMax#				; For all Gadgets
	Field ValMin#				; For all Gadgets
	
	Field State%				; For all Gadgets	
	
	Field Visible%				; For all Gadgets
	Field Disable%				; For all Gadgets
	Field MouseOn%				; For all Gadgets
	Field Style%				; For all Gadgets
	Field TitleHeight			; For the Window
	Field Draggable% 			; For the Window
	Field DragMode%				; For the Window
	Field DragPx%				; For the Window
	Field DragPy%				; For the Window
	
	Field Caption$
End Type

Type GuiEvent
    Field EventType% 			; Type d'événement (ex: "Highlight", "Click")
    Field Target.GuiObject  	; L'élément concerné
    Field NextEvent.GuiEvent  	; Prochain événement dans la liste
End Type


Global Gui_ObjectTypeWindow% 	= 1100
Global Gui_ObjectTypeButton% 	= 1200
Global Gui_ObjectTypeLabel%  	= 1300
Global Gui_ObjectTypeFrame%  	= 1400
Global Gui_ObjectTypeCheckbox%	= 1500
Global Gui_ObjectTypeProgress%	= 1600


Global Gui_PreviousSelected.GuiObject 
Global Gui_PreviousEvent%

Global Gui_SystemFont% 
Global Gui_CurrentWindow.GuiObject
Global Gui_CurrentEvent.GuiEvent


Global Gui_Event_MouseOn% = 1130
Global Gui_Event_Pressed% = 1140
Global Gui_Event_Clicked% = 1150


Global Gui_Size_WinTitleBar% = 30

Global Gui_Color_WinBackground_R% = 33
Global Gui_Color_WinBackground_G% = 36
Global Gui_Color_WinBackground_B% = 51

Global Gui_Color_WinTitleBar_R% = 29
Global Gui_Color_WinTitleBar_G% = 32
Global Gui_Color_WinTitleBar_B% = 45

Global Gui_Color_WinCaption_R% = 240
Global Gui_Color_WinCaption_G% = 250
Global Gui_Color_WinCaption_B% = 255

Global Gui_Color_ButtonBackground_R% = 33
Global Gui_Color_ButtonBackground_G% = 36
Global Gui_Color_ButtonBackground_B% = 51

Global Gui_Color_ButtonSelected_R% = 188
Global Gui_Color_ButtonSelected_G% = 39
Global Gui_Color_ButtonSelected_B% = 19

Global Gui_Color_ButtonCaption_R% = 240
Global Gui_Color_ButtonCaption_G% = 250
Global Gui_Color_ButtonCaption_B% = 255

Global Gui_Color_CheckboxCaption_R% = 240
Global Gui_Color_CheckboxCaption_G% = 250
Global Gui_Color_CheckboxCaption_B% = 255

Global Gui_Color_FrameBorder_R% = 55
Global Gui_Color_FrameBorder_G% = 65
Global Gui_Color_FrameBorder_B% = 85

Global Gui_Color_ProgressBackground_R% = 155
Global Gui_Color_ProgressBackground_G% = 80
Global Gui_Color_ProgressBackground_B% = 100

; ---------------------------------------------------------------
; Internal : move GuiObject childs positions during window moving
; ---------------------------------------------------------------
Function Gui_UpdateChildrenPosition(parent.GuiObject)
    Local child.GuiObject = parent\firstChild
	
    While child <> Null
		
		If parent\Family = Gui_ObjectTypeWindow Then
			; Compute relative position from parent (if its a window)
			child\Px = parent\Px + (2 + child\initialRelativeX)
			child\Py = parent\Py + (parent\TitleHeight + 2) + child\initialRelativeY
		Else
		    ; Compute relative position from parent (for other gadgets, no offset needed)
			child\Px = parent\Px + child\initialRelativeX
			child\Py = parent\Py + child\initialRelativeY
			
		EndIf
		
        ; Recursive function for child of the child
        Gui_UpdateChildrenPosition(child)
        
        child = child\nextSibling
    Wend
End Function

; -------------------------------------------------------
; Internal : allow to get the parentwin of a child struct
; -------------------------------------------------------
Function Gui_GetParentWindow.GuiObject(child.GuiObject)
    If child = Null Then Return Null
    
	If child\Family = Gui_ObjectTypeWindow% Then 
		Return child
	EndIf
	
    Return Gui_GetParentWindow(child\Parent)
End Function

; --------------------------------------------------
; Public : Allow you to move a widget and the childs
; --------------------------------------------------
Function Gui_MoveWidget.GuiObject(parent.GuiObject, Px% , Py%)
    If parent = Null Then Return Null	
	
	parent\Px% = Px%
	parent\Py% = Py%
	
	parent\initialRelativeX% = parent\Px% - parent\Parent\Px - 2  ; 2 = Border size
	parent\initialRelativeY% = parent\Py% - parent\Parent\Py - (parent\Parent\TitleHeight + 2) ; 2 = Border size
	
	Gui_UpdateChildrenPosition(parent)
End Function

; -----------------------------------------------------
; Internal : add a GuiObject child to another GuiOBject
; -----------------------------------------------------
Function Gui_AddChild(parent.GuiObject, child.GuiObject)
    If parent\firstChild = Null Then
        parent\firstChild = child
    Else
		
		Local LastChild.GuiObject = parent\firstChild
		
        While LastChild\nextSibling <> Null
            LastChild = LastChild\nextSibling
        Wend
		
        LastChild\nextSibling = child
	EndIf
End Function

; ------------------------------------------
; Internal : Remove all gadget widget childs
; ------------------------------------------
Function Gui_DeleteChild(parent.GuiObject)
	If parent<>Null Then
		
		Local child.GuiObject = parent\firstChild
		
		While child <> Null
			; Supprimer chaque enfant, en appelant récursivement pour les enfants des enfants
			Local nextChild.GuiObject = child\nextSibling
			
			; Appeler la fonction récursivement pour supprimer les enfants des enfants
			Gui_DeleteChild(child)
			
			Delete child
			child = nextChild
		Wend
		
		; Maintenant supprimer le widget lui même
		Delete parent
	
	EndIf
End Function

; ----------------------------------------------------------------------
; Internal : update GuiObject childs z order after window bring to front
; ----------------------------------------------------------------------
Function Gui_UpdateChildrenOrder(parent.GuiObject)
    Local child.GuiObject = parent\firstChild
	
    While child <> Null
        Insert child After Last GuiObject
        
        ; Fonction recursive pour updater l'ordre des enfants 
        Gui_UpdateChildrenOrder(child)
        
        child = child\nextSibling
    Wend
End Function

; --------------------------
; Internal : Init Gui System
; --------------------------
Function Gui_Init()
	Gui_SystemFont% = LoadFont("Arial",16,False)
End Function

; -------------------
;  Refresh Gui System
; -------------------
Function Gui_Refresh()
	
	Gui_RefreshEvents()
	
	; Check the user mouse button to activate the different window states
	If GUI_MouseClickLeft Then
		Gui_BringToFront()
		
		Gui_PreviousSelected = Null ; On reset le dernier gadget selectionné si on clique sur le body de la fenetre
	EndIf

	If GUI_MousePressLeft  Then
		Gui_MoveWindow()
	EndIf	
	
	If GUI_MouseReleaseLeft And Gui_CurrentWindow <> Null Then
		Gui_CurrentWindow\DragMode=False
	EndIf	
	
	
	Gui_Redraw(Null)	
	Gui_RefreshMouse()	
	
End Function

; ----------------------------
; Internal : Redraw Gui System
; ----------------------------
Function Gui_Redraw(x.GuiObject)
	
		; Refresh des gadgets
		For g.GuiObject = Each GuiObject
			
			If g\Family = Gui_ObjectTypeFrame% Then
				Gui_RefreshFrame(G)
				Gui_DrawFrame(G)
			EndIf
			
			If g\Family = Gui_ObjectTypeButton% Then
				Gui_RefreshButton(G)
				Gui_DrawButton(G)
			EndIf
			
			If g\Family = Gui_ObjectTypeLabel% Then
				Gui_RefreshLabel(G)
				Gui_DrawLabel(G)
			EndIf
			
			If g\Family = Gui_ObjectTypeProgress% Then
				Gui_RefreshProgress(G)
				Gui_DrawProgress(G)
			EndIf
			
			If g\Family = Gui_ObjectTypeCheckbox% Then
				Gui_RefreshCheckbox(G)
				Gui_DrawCheckbox(G)
			EndIf
			
			; Windows en dernier
			If g\Family = Gui_ObjectTypeWindow% Then
				; Viewport lock
				Viewport g\Px,g\Py, g\Tx ,  g\Ty
				
				Gui_RefreshWindow(G)
				Gui_DrawWindow(G)
			EndIf
			
			; Dessiner les enfants
			If x<>Null Then
				child.GuiObject = x\firstChild
				
				While child <> Null
					Gui_Redraw(x)
					child = child\nextSibling
				Wend
			EndIf
			
		Next	
		
		; Viewport Reset
		Viewport 0,0, GraphicsWidth() , GraphicsHeight()
		
End Function





; -----------------------------------------
; Internal : Can be modified with FastImage
; -----------------------------------------
Function Gui_Rect(Px , Py , Tx , Ty , Fill% , R% , G% , B%  , Style% = 0)
	Select Style%
			
		; Simple
		Case 0
			Color R%,G%,B% : Rect(Px,Py,Tx,Ty,Fill%)
			
		; Flat Border
		Case 1
			Color 5,10,15 : Rect(Px,Py,Tx,Ty,Fill%)
			Color R%,G%,B% : Rect(Px + 1 , Py + 1 , Tx - 2 , Ty - 2 , 1)
			Color 40,50,60 : Rect(Px + 1 , Py + 1 , Tx - 2 , Ty - 2 , 0)
			
		; 3D Frame
		Case 2
			Color R%/2,G%/2,B%/2 : Rect(Px + 1 , Py + 1 , Tx  , Ty  , 0)
			Color R%,G%,B% : Rect(Px , Py , Tx , Ty  , 0)
			
	End Select
End Function

Function Gui_Text(Px,Py,Caption$ , R% = 255 ,G% = 255 ,B% = 255)
	SetFont Gui_SystemFont%
	Color R%,G%,B% : Text(Px,Py,Caption$)
End Function






; -------------------------------
; Internal : Interpolate function
; -------------------------------
Function InterpolateValue#(ValueA#,ValueB#,Smooth#=0.5)
	Return ValueA#+Smooth#*(ValueB#-ValueA#)
End Function

; -----------------------
; Internal : Max function
; -----------------------
Function Max#(a#, b#)
    If a# > b# Then 
		Return a# 
	Else 
		Return b#
	EndIf
End Function


;~IDEal Editor Parameters:
;~F#B1#C3#DC#EC#163#170#177
;~C#Blitz3D