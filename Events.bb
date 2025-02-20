; -------------------------
;  Internal : Add Gui Event
; -------------------------
Function Gui_AddEvent(EventType%, Target.GuiObject)
	
    Local ev.GuiEvent = New GuiEvent
    ev\EventType = EventType%
    ev\Target = Target
	
    ; Ajouter à la fin de la liste
    If Gui_CurrentEvent = Null Then
        Gui_CurrentEvent = ev
    Else
        Local Latest.GuiEvent = Gui_CurrentEvent
		
        While Latest\NextEvent <> Null
            Latest = Latest\NextEvent
        Wend
		
        Latest\NextEvent = ev
    EndIf
	
End Function

; -----------------------------
;  Internal : Refresh Gui Event
; -----------------------------
Function GetWidgetEvent(Target.GuiObject) 
	If Target<>Null And Gui_PreviousSelected = Target  Then
		Return Gui_PreviousEvent%
	EndIf
	
    Return False
End Function

; -----------------------------
;  Internal : Refresh Gui Event
; -----------------------------
Function Gui_RefreshEvents()
    Local ev.GuiEvent = Gui_CurrentEvent
	Gui_PreviousEvent% = 0
	
    While ev <> Null
		
        If ev\EventType = Gui_Event_MouseOn% Then
            ;DebugLog "Surlignage du bouton : " + ev\Target\Caption$
			Gui_PreviousEvent% = Gui_Event_MouseOn%
			Gui_PreviousSelected = ev\Target
		EndIf
		
		If ev\EventType = Gui_Event_Pressed% Then
            ;DebugLog "Press le bouton : " + ev\Target\Caption$
			Gui_PreviousEvent% = Gui_Event_Pressed% 
			Gui_PreviousSelected = ev\Target
        EndIf
		
		If ev\EventType = Gui_Event_Clicked% Then
            ;DebugLog "Clic sur le bouton : " + ev\Target\Caption$
			Gui_PreviousEvent% = Gui_Event_Clicked% 
			Gui_PreviousSelected = ev\Target
		EndIf
		
        ev = ev\NextEvent
    Wend
	
    ; Une fois traités, vider la liste
    Gui_ClearEvents()
	
End Function

; ---------------------------
;  Internal : Flush Gui Event
; ---------------------------
Function Gui_ClearEvents()
	
    While Gui_CurrentEvent <> Null
        ev.GuiEvent = Gui_CurrentEvent
        Gui_CurrentEvent = Gui_CurrentEvent\NextEvent
        Delete ev
    Wend
	
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D