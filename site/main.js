const { app, BrowserWindow, ipcMain, dialog } = require('electron');
const path = require('path');

console.log('Main process starting...');

let mainWindow;

function createWindow() {
  console.log('Creating window...');
  
  mainWindow = new BrowserWindow({
    width: 1280,
    height: 720,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      contextIsolation: true,
      nodeIntegration: false,
      enableRemoteModule: false
    }
  });

  // DevTools
  mainWindow.webContents.openDevTools();
  
  mainWindow.loadFile('index.html');
  
  mainWindow.webContents.on('did-finish-load', () => {
    console.log('Window finished loading index.html');
  });
}

// Обработчик PLAY
ipcMain.handle('show-message', async (event, message) => {
  console.log('IPC: show-message received:', message);
  
  const result = await dialog.showMessageBox(mainWindow, {
    type: 'info',
    message: message,
    buttons: ['OK']
  });
  
  console.log('Dialog closed with result:', result);
  return result;
});

app.whenReady().then(() => {
  console.log('App is ready');
  createWindow();
});

app.on('window-all-closed', () => {
  console.log('All windows closed');
  if (process.platform !== 'darwin') {
    app.quit();
  }
});

app.on('activate', () => {
  if (BrowserWindow.getAllWindows().length === 0) {
    createWindow();
  }
});

console.log('Main process setup complete');