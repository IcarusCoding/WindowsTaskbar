package de.intelligence.windowstoolbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

import de.intelligence.windowstoolbar.exceptions.ConditionNotMetException;

public final class NativeIconHandler implements INativeIconHandler {

    private final Map<Icon, WinDef.HICON> icons;

    public NativeIconHandler() {
        this.icons = new HashMap<>();
    }

    @Override
    public boolean isRegistered(String id) {
        return this.findIcon(id).isPresent();
    }

    @Override
    public boolean isRegistered(Icon icon) {
        return this.icons.containsKey(icon);
    }

    @Override
    public void createNativeFor(Icon icon) {
        if (this.icons.containsKey(icon)) {
            return;
        }
        final IconPath iconPath = icon.getPath();
        final WinDef.HICON hIcon = new WinDef.HICON();
        if (iconPath.getType() == IconPath.IconPathType.CLASSPATH) {
            try (InputStream inputStream = Utils.WALKER.getCallerClass().getClassLoader().getResourceAsStream(iconPath.getPath())) {
                Conditions.checkState(inputStream != null, "Icon not found in classpath");
                final byte[] byteBuf = inputStream.readAllBytes();
                final Pointer byteBufPtr = new Memory(byteBuf.length);
                byteBufPtr.write(0, byteBuf, 0, byteBuf.length);
                final int directoryOffset = User32.INSTANCE.LookupIconIdFromDirectoryEx(byteBufPtr, true, 0, 0, 0);
                final WinDef.HICON hIconTemp = User32.INSTANCE.CreateIconFromResourceEx(byteBufPtr.share(directoryOffset),
                        new WinDef.DWORD(byteBuf.length - directoryOffset), true, new WinDef.DWORD(0x30000), 0, 0, 0);
                Conditions.checkState(hIconTemp != null, "Failed to create native icon for id \"" + icon.getId() + "\"");
                hIcon.setPointer(hIconTemp.getPointer());
            } catch (IOException ex) {
                throw new ConditionNotMetException(ex);
            }
        } else {
            final WinNT.HANDLE handle = User32.INSTANCE.LoadImageA(null, iconPath.getPath(), User32.IMAGE_ICON,
                    0, 0, User32.LR_LOADFROMFILE | User32.LR_DEFAULTSIZE);
            Conditions.checkState(!handle.equals(WinBase.INVALID_HANDLE_VALUE), "Failed to create native icon for id \"" + icon.getId() + "\"");
            hIcon.setPointer(handle.getPointer());
        }
        this.icons.put(icon, hIcon);
    }

    @Override
    public boolean destroyNativeFor(String id) {
        final Optional<Icon> iconOpt = this.findIcon(id);
        if (iconOpt.isEmpty()) {
            return false;
        }
        return this.destroyNativeFor(iconOpt.get());
    }

    @Override
    public boolean destroyNativeFor(Icon icon) {
        if (!this.icons.containsKey(icon)) {
            return false;
        }
        final boolean result = User32.INSTANCE.DestroyIcon(this.icons.get(icon)).booleanValue();
        if (result) {
            this.icons.remove(icon);
        }
        return result;
    }

    @Override
    public Optional<WinDef.HICON> getNativeFor(String id) {
        return this.findIcon(id).flatMap(this::getNativeFor);
    }

    @Override
    public Optional<WinDef.HICON> getNativeFor(Icon icon) {
        return Optional.of(this.icons.get(icon));
    }

    private Optional<Icon> findIcon(String id) {
        return this.icons.keySet().stream().filter(icon -> icon.getId().equals(id)).findAny();
    }

}
