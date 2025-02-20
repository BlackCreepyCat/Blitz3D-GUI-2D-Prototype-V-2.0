; -----------
; Timer class
; -----------
Type Gui_Timer
	Field Start%
	Field TimeOut%
End Type


; -----------------------------
; Usefull to create timed event
; -----------------------------
Function Gui_SetTimer.Gui_Timer(TimeOut)
	This.Gui_Timer = New Gui_Timer
	This\Start   = MilliSecs() 
	This\TimeOut = This\Start + TimeOut
	
	Return This
End Function

Function Gui_DeleteTimer(Id.Gui_Timer)
	If Id <> Null
		Delete Id
		Return True
	EndIf
End Function

Function Gui_TimeOut(Id.Gui_Timer)
	If Id <> Null
		If Id\TimeOut < MilliSecs()
			Delete Id
			Return True
		Else
			Return False
		EndIf
	Else
		Return True
	EndIf
End Function