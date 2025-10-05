const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('electronAPI', {
  versions: {
    node: () => process.versions.node,
    chrome: () => process.versions.chrome,
    electron: () => process.versions.electron
  },
  showMessage: (message) => {
    return ipcRenderer.invoke('show-message', message);
  },
  openFile: () => ipcRenderer.invoke('dialog:openFile'),
  showMessage: (message) => ipcRenderer.invoke('show-message', message)
});