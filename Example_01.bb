; -------------------------------------------------
; Parent/Child GUI prototype By Creepy Cat (C) 2024
; -------------------------------------------------
Include "Timer.bb"
Include "Label.bb"
Include "Mouse.bb"
Include "Kernel.bb"
Include "Window.bb"
Include "Button.bb"
Include "CheckBox.bb"
Include "Progress.bb"
Include "Frame.bb"
Include "Events.bb"

Graphics3D 1280,720,32,2


Gui_Init()


WinA.GuiObject = Gui_CreateWindow(50, 50, 550, 200, "Window 01",False,True)
WinA\Draggable = False

VX.GuiObject = CreateButton(0, 0, 100, 30, "DEL CHILD", WinA)
XX.GuiObject = CreateButton(0, 30, 100, 30, "NEWIN", VX)
YX.GuiObject = CreateButton(0, 30, 100, 30, "QUIT", XX)

CheckBox_A.GuiOBject = CreateCheckbox.GuiObject(150, 130 , "Mon Checkbox A", WinA )



WinB.GuiObject = Gui_CreateWindow(250,150, 700,500, "Window 02" , True , True)

V.GuiObject = CreateButton(0, 0, 100, 30, "Button A", WinB)
X.GuiObject = CreateButton(0, 30, 100, 30, "Button B", V , 2	)
Y.GuiObject = CreateButton(0, 30, 100, 30, "Button TEST", X)

Label_A.GuiObject = CreateLabel(130, 30, "CLICK ME", WinB,0,0)
Label_B.GuiObject = CreateLabel(0, 20, "BIG LABEL TEST A", Label_A,0,0)

Frame_A.GuiObject = CreateFrame(100, 100, 200, 120, "My Frame", WinB)
Frame_But.GuiObject = CreateButton(10, 20, 100, 30, "Frame Button", Frame_A)
CheckBox_B.GuiOBject = CreateCheckbox.GuiObject(10, 55 , "Mon Checkbox B", Frame_A )
CheckBox_C.GuiOBject = CreateCheckbox.GuiObject(10, 80 , "Mon Checkbox C", Frame_A )

Progress_A.GuiOBject = CreateProgress(50,320, 200, 25,"Mouse X : " , 0,GraphicsWidth(),0, WinB )
Progress_B.GuiOBject = CreateProgress(50,350, 200, 25,"" , 0,GraphicsHeight(),0, WinB )
Progress_B\R%=80
Progress_B\G%=120
Progress_B\B%=50

Global fps, frameCount, lastTime
lastTime = MilliSecs()


While Not KeyHit(1)
	
	Cls
	
	Progress_A\Value# = Gui_MouseX%
	Progress_B\Value# = Gui_MouseY%
	
	If MouseDown(2) Then 
		
		Gui_MoveWidget(Progress_A, Gui_MouseX% , Gui_MouseY%)
		
	EndIf
	
	frameCount = frameCount + 1
	
	If MilliSecs() - lastTime >= 1000 Then
		fps = frameCount
		frameCount = 0
		lastTime = MilliSecs()
	EndIf
	
	;Gui_PreviousSelected = Null
	Gui_Refresh()
	
	If GetWidgetEvent(yx) = Gui_Event_Clicked% Then 
		;Gui_DeleteChild(X)
		xx\Visible=1-xx\Visible
	EndIf
	
	
	If GetWidgetEvent(CheckBox_A) = Gui_Event_Clicked% Then 
		WinA\Draggable=1 - WinA\Draggable
		
	EndIf
	
	If GetWidgetEvent(vx) = Gui_Event_Clicked% Then 
		XX\Disable=1 - XX\Disable
	EndIf
	
	If GetWidgetEvent(Label_A) = Gui_Event_Clicked% Then 
		yx\Disable=1 - yx\Disable
		
		Label_A\R% = Rnd(255)
		Label_A\G% = Rnd(255,50)
		Label_A\B% = Rnd(255)
	EndIf
	
	If GetWidgetEvent(Label_B) = Gui_Event_Clicked% Then 
		xx\Disable=1 - xx\Disable
		
		Label_B\R% = Rnd(255)
		Label_B\G% = Rnd(255,50)
		Label_B\B% = Rnd(255)
	EndIf
	
	If GetWidgetEvent(xx) = Gui_Event_Clicked% Then 
		WinC.GuiObject = Gui_CreateWindow(Rnd(250,400),Rnd(250,400), 450, 250, "Window XX", True, True)
		
		DDV.GuiObject = CreateButton(10, 0, 100, 35, "Button A", WinC)
		DDX.GuiObject = CreateButton(10, 30, 100, 35, "Button B", DDV)
		DDY.GuiObject = CreateButton(10, 30, 100, 35, "Button C", DDX)
		
		DOV.GuiObject = CreateButton(140, 0, 100, 30, "Button A", WinC)
		DOX.GuiObject = CreateButton(140, 30, 100, 30, "Button B", WinC)
		DOY.GuiObject = CreateButton(140, 60, 100, 30, "Button C", WinC)
		
	EndIf
	
	RenderWorld	
	
	Text 10,10, "FPS: " + fps
	
	If Gui_PreviousSelected <> Null Then 
		Text 10,30, "Gui ID : " + Gui_PreviousSelected\Caption$
	Else
		Text 10,30, "Gui ID : " + "Null"
	EndIf
	
	
	Flip False
Wend








;~IDEal Editor Parameters:
;~C#Blitz3D