package de.intelligence.windowstoolbar;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.platform.win32.COM.Unknown;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;

import de.intelligence.windowstoolbar.exceptions.CoInitException;

final class WindowsTaskbarInternal extends Unknown implements IWindowsTaskbarInternal {

    private static final GUID CLSID_TaskbarList = new GUID("{56FDF344-FD6D-11d0-958A-006097C9A090}");
    private static final GUID IID_ITaskbarList3 = new GUID("{EA1AFB91-9E28-4B86-90E9-9E9F8A5EEFAF}");

    private static WindowsTaskbarInternal INSTANCE;

    public static IWindowsTaskbarInternal getInstance() {
        if(!COM.isCoinitialize()) {
            throw new CoInitException("Please initialize the COM library before using this class!");
        }
        if (WindowsTaskbarInternal.INSTANCE == null) {
            final PointerByReference pointerRef = new PointerByReference();
            COMUtils.checkRC(Ole32.INSTANCE.CoCreateInstance(WindowsTaskbarInternal.CLSID_TaskbarList,
                    null, WTypes.CLSCTX_SERVER, WindowsTaskbarInternal.IID_ITaskbarList3, pointerRef));
            WindowsTaskbarInternal.INSTANCE = new WindowsTaskbarInternal(pointerRef.getValue());
        }
        return INSTANCE;
    }

    private final Pointer pComInstance;

    public WindowsTaskbarInternal(Pointer pComInstance) {
        super(pComInstance);
        this.pComInstance = pComInstance;
        this.HrInit();
    }

    @Override
    public WinNT.HRESULT HrInit() {
        return this.validateAndReturn((WinNT.HRESULT) super._invokeNativeObject(VTable.HrInit,
                new Object[] {this.pComInstance}, WinNT.HRESULT.class));
    }

    @Override
    public WinNT.HRESULT SetProgressValue(WinDef.HWND hWnd, int ullCompleted, int ullTotal) {
        return this.validateAndReturn((WinNT.HRESULT) super._invokeNativeObject(VTable.SetProgressValue,
                new Object[] {this.pComInstance, hWnd, ullCompleted, ullTotal}, WinNT.HRESULT.class));
    }

    @Override
    public WinNT.HRESULT SetProgressState(WinDef.HWND hWnd, int tbpFlags) {
        return this.validateAndReturn((WinNT.HRESULT) super._invokeNativeObject(VTable.SetProgressState,
                new Object[] {this.pComInstance, hWnd, tbpFlags}, WinNT.HRESULT.class));
    }

    @Override
    public WinNT.HRESULT ThumbBarAddButtons(WinDef.HWND hWnd, int cButtons, THUMBBUTTON[] pButton) {
        return this.validateAndReturn((WinNT.HRESULT) super._invokeNativeObject(VTable.ThumbBarAddButtons,
                new Object[] {this.pComInstance, hWnd, cButtons, pButton}, WinNT.HRESULT.class));
    }

    @Override
    public WinNT.HRESULT ThumbBarUpdateButtons(WinDef.HWND hWnd, int cButtons, THUMBBUTTON[] pButton) {
        return this.validateAndReturn((WinNT.HRESULT) super._invokeNativeObject(VTable.ThumbBarUpdateButtons,
                new Object[] {this.pComInstance, hWnd, cButtons, pButton}, WinNT.HRESULT.class));
    }

    private WinNT.HRESULT validateAndReturn(WinNT.HRESULT hResult) {
        COMUtils.checkRC(hResult);
        return hResult;
    }

}
