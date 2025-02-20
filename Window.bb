; ---------------------------------------
; Public : Function to to create a window
; ---------------------------------------
Function Gui_CreateWindow.GuiObject(Px, Py, Tx, Ty, Caption$ , Close% = True , Maximize% = False )
	
	G.GuiObject = New GuiObject
	
	G\Family = Gui_ObjectTypeWindow%
	
	G\Parent = Null
	G\firstChild = Null
	G\nextSibling = Null
	
	G\Px = Px
	G\Py = Py
	
	G\Tx = Tx
	G\Ty = Ty
	
	G\OldPx = Px
	G\OldPy = Py
	
	G\OldTx = Tx
	G\OldTy = Ty
	
	G\Caption$ = Caption$
	G\TitleHeight = Gui_Size_WinTitleBar%
	G\Draggable = True
	
	G\R% = Gui_Color_WinBackground_R% 
	G\G% = Gui_Color_WinBackground_G%
	G\B% = Gui_Color_WinBackground_B%
	
	If Close% = True
		NewPx = (G\Tx - G\TitleHeight) 
		
		G\Gadget_A = CreateButton(NewPx - 3,-G\TitleHeight, G\TitleHeight-1, G\TitleHeight-1, "X", G , 2)
		G\Gadget_A\R% = 188
		G\Gadget_A\G% = 40
		G\Gadget_A\B% = 19
	EndIf
	
	
	If Maximize% = True
		If Close% = False Then
			NewPx = G\Tx - (G\TitleHeight + 3)
		Else
			NewPx = G\Tx - (G\TitleHeight * 2 + 2) 
		EndIf
		
		G\Gadget_B = CreateButton(NewPx,-G\TitleHeight, G\TitleHeight-1, G\TitleHeight-1, "¤", G , 2)
		G\Gadget_B\R% = 28
		G\Gadget_B\G% = 32
		G\Gadget_B\B% = 43
	EndIf
	
	Gui_CurrentWindow = G
	
	Return G
	
End Function

; ----------------------------------------------
; Internal : Function to bring a window to front
; ----------------------------------------------
Function Gui_BringToFront()
	G.GuiObject= Last GuiObject			
	
	While G <> Null 
		
		If G\Family = Gui_ObjectTypeWindow% Then
			
			; -------------------
			; Window button setup
			; -------------------		
			NewTx = G\Tx
			
			If G\Gadget_A <> Null Then
				NewTx = G\Tx - (G\TitleHeight + 2)
			EndIf
			
			If G\Gadget_B <> Null Then
				NewTx = NewTx - (G\TitleHeight + 2)
			EndIf
			
			; -----------------------
			; Refresh mouse on window
			; -----------------------
			If Gui_TestZone(G\Px,G\Py,  G\Tx , G\Ty)
				
				Insert G After Last GuiObject
				
				Gui_CurrentWindow = G
				G\DragMode=False
				
				; -----------------------------
				; Refresh dragging informations
				; -----------------------------
				If Gui_TestZone(G\Px,G\Py,  NewTx , G\TitleHeight) And G\Draggable = True  Then
					G\DragMode=True
					
					G\DragPx=GUI_MouseX-G\Px
					G\DragPy=GUI_MouseY-G\Py
				EndIf
				
				Gui_UpdateChildrenOrder(G)
				
				Return True
			EndIf
			
		EndIf
		
		; --------------------------------------
		; Loop the type from backward to forward
		; --------------------------------------	
		G = Before G
	Wend	
End Function

; ------------------------------------
; Internal : fonction to move a window
; ------------------------------------
Function Gui_MoveWindow()
	If Gui_CurrentWindow<>Null Then
		
		If  Gui_CurrentWindow\DragMode=True 		
			Gui_CurrentWindow\Px = GUI_MouseX - Gui_CurrentWindow\DragPx
			Gui_CurrentWindow\Py = GUI_MouseY - Gui_CurrentWindow\DragPy
			
			Gui_CurrentWindow\OldPx = Gui_CurrentWindow\Px
			Gui_CurrentWindow\OldPy = Gui_CurrentWindow\Py
		Else
			Gui_CurrentWindow\DragPx = GUI_MouseX - Gui_CurrentWindow\Px
			Gui_CurrentWindow\DragPy = GUI_MouseY - Gui_CurrentWindow\Py
		EndIf
		
		Gui_UpdateChildrenPosition(Gui_CurrentWindow)
	EndIf
End Function

; ----------------------------
; Internal : Redraw the window
; ----------------------------
Function Gui_DrawWindow(win.GuiObject)
	If win<>Null Then
		If win\Family = Gui_ObjectTypeWindow% Then
			
			
			
			; Draw background and title
			Gui_Rect(win\Px,win\Py, win\Tx ,  win\Ty, 1 , win\R%,win\G%,win\B%  , 1) ; Background
			Gui_Rect(win\Px + 2,win\Py + 2,  win\Tx -4 , win\TitleHeight - 1 , 1 , Gui_Color_WinTitleBar_R% ,Gui_Color_WinTitleBar_G% , Gui_Color_WinTitleBar_B% , 0) ; Title
			
			; If it's not the selected window, i colorize it differently
			If Gui_CurrentWindow = win Then
				Gui_Text(win\Px + 7 , win\Py + (win\TitleHeight / 2) - (StringHeight ("A") / 2) + 2, win\Caption$ , Gui_Color_WinCaption_R%,Gui_Color_WinCaption_G%,Gui_Color_WinCaption_B%)
				
				If win\Gadget_A<>Null Then
					win\Gadget_A\R% = 188
					win\Gadget_A\G% = 40
					win\Gadget_A\B% = 19
				EndIf
			Else
				Gui_Text(win\Px + 7 , win\Py + (win\TitleHeight / 2) - (StringHeight ("A") / 2) + 2, win\Caption$ , Gui_Color_WinCaption_R% / 2 , Gui_Color_WinCaption_G%/2 , Gui_Color_WinCaption_B%/2)	
				
				If win\Gadget_A<>Null Then
					win\Gadget_A\R% = Gui_Color_WinTitleBar_R%
					win\Gadget_A\G% = Gui_Color_WinTitleBar_G%
					win\Gadget_A\B% = Gui_Color_WinTitleBar_B%
				EndIf
			EndIf
		
		EndIf
	EndIf
End Function

; -----------------------------
; Internal : Refresh the window
; -----------------------------
Function Gui_RefreshWindow(win.GuiObject)
	If win<>Null Then
		
		; Window close button setup
		If GetWidgetEvent(win\Gadget_A) = Gui_Event_Clicked% Then 
			Gui_DeleteWindow(win)
			Return True
		EndIf
		
		; Window maximize button resize setup
		If GetWidgetEvent(win\Gadget_B) = Gui_Event_Clicked% Then 
			
			win\switch = 1 - win\switch
			
			If win\switch = 1 Then
				win\Px = 0
				win\Py = 0
				
				win\Tx = GraphicsWidth()
				win\Ty = GraphicsHeight()
			Else
				win\Px = win\OldPx
				win\Py = win\OldPy
				
				win\Tx = win\OldTx
				win\Ty = win\OldTy
			EndIf
			
			Gui_ResizeWindow(win, win\Px, win\Py , win\Tx , win\Ty )
			
			
			Return True
		EndIf
		
	EndIf
End Function

; --------------------------
; Public : Delete the window
; --------------------------
Function Gui_DeleteWindow(win.guiObject)
	If win<>Null Then
		
		; Supprimer tous les enfants de la fenêtre de manière récursive
		Local child.guiObject = win\firstChild
		
		While child <> Null
			; Supprimer chaque enfant, en appelant récursivement pour les enfants des enfants
			Local nextChild.guiObject = child\nextSibling
			
			; Appeler la fonction récursivement pour supprimer les enfants des enfants
			Gui_DeleteWindow(child)
			
			Delete child
			child = nextChild
		Wend
		
		; Maintenant supprimer la fenêtre elle-même
		Delete win
		
	EndIf
End Function

; ---------------------------------------------
; Public : Fonction to position/resize a window
; ---------------------------------------------
Function Gui_ResizeWindow(win.GuiObject, newPx, newPy, newTx, newTy)
    If win = Null Then Return
    
    ; Mise à jour des dimensions de la fenêtre
    win\Px = newPx
    win\Py = newPy
    win\Tx = newTx
    win\Ty = newTy
	
	; Et refresh des positions X,Y des boutons de la fenetre, close gadget en premier
	If win\Gadget_A <> Null Then
		win\Gadget_A\Px = (win\Tx - win\TitleHeight) - 3
		win\Gadget_A\Py = win\Py
		
		win\Gadget_A\initialRelativeX = win\Gadget_A\Px
		win\Gadget_A\initialRelativeY = -win\TitleHeight
	EndIf
	
	; Maximize gadget
	If win\Gadget_B <> Null Then	
		
		If win\Gadget_A = Null Then
			newPx = win\Tx - (win\TitleHeight + 3)
		Else
			newPx = win\Tx - (win\TitleHeight * 2 + 2)
		EndIf
		
		
		win\Gadget_B\Px = newPx 
		win\Gadget_B\Py = win\Py 	
		
		win\Gadget_B\initialRelativeX = win\Gadget_B\Px
		win\Gadget_B\initialRelativeY = - win\TitleHeight
	EndIf	
	

    ; Repositionner les autres enfants en fonction de la nouvelle position de la fenêtre
    Gui_UpdateChildrenPosition(win)
    
    ; Recalculer l'ordre des enfants si nécessaire
    Gui_UpdateChildrenOrder(win)
End Function
;~IDEal Editor Parameters:
;~F#DB
;~C#Blitz3D