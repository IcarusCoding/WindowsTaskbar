package de.intelligence.windowstoolbar;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

sealed interface IWindowsTaskbarInternal permits WindowsTaskbarInternal {

    int MAX_BUTTONS = 7;

    WinNT.HRESULT HrInit();

    WinNT.HRESULT SetProgressValue(WinDef.HWND hWnd, int ullCompleted, int ullTotal);

    WinNT.HRESULT SetProgressState(WinDef.HWND hWnd, int tbpFlags);

    WinNT.HRESULT ThumbBarAddButtons(WinDef.HWND hWnd, int cButtons, THUMBBUTTON[] pButton);

    WinNT.HRESULT ThumbBarUpdateButtons(WinDef.HWND hWnd, int cButtons, THUMBBUTTON[] pButton);

    interface VTable {

        int HrInit = 0x03;
        int AddTab = 0x0B;
        int SetProgressValue = 0x09;
        int SetProgressState = 0x0A;
        int ThumbBarAddButtons = 0x0F;
        int ThumbBarUpdateButtons = 0x10;

        int SetTabOrder = 0x0D;

    }

    interface TBPFLAG {

        int TBPF_NOPROGRESS = 0x00;
        int TBPF_INDETERMINATE  = 0x01;
        int TBPF_NORMAL  = 0x02;
        int TBPF_ERROR  = 0x04;
        int TBPF_PAUSED  = 0x08;

    }

    @Structure.FieldOrder({"dwMask", "iId", "iBitmap", "hIcon", "szTip", "dwFlags"})
    class THUMBBUTTON extends Structure {

        public static final int MAX_TOOLTIP_LENGTH = 260;

        public int dwMask;
        public int iId;
        public int iBitmap;
        public WinDef.HICON hIcon;
        public char[] szTip = new char[MAX_TOOLTIP_LENGTH];
        public int dwFlags;

        interface THUMBBUTTONMASK {

            int THB_BITMAP = 0x01;
            int THB_ICON = 0x02;
            int THB_TOOLTIP = 0x04;
            int THB_FLAGS = 0x08;
            int ALL = THB_BITMAP | THB_ICON | THB_TOOLTIP | THB_FLAGS;

        }

        interface THUMBBUTTONFLAGS {

            int THBF_ENABLED = 0x00;
            int THBF_DISABLED = 0x01;
            int THBF_DISMISSONCLICK = 0x02;
            int THBF_NOBACKGROUND = 0x04;
            int THBF_HIDDEN = 0x08;
            int THBF_NONINTERACTIVE = 0x10;

        }

    }

}
