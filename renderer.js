document.addEventListener('DOMContentLoaded', function() {
  const playBtn = document.getElementById('playBtn');
  
  playBtn.addEventListener('click', async function() {
    try {
      await window.electronAPI.showMessage('Кнопка Play нажата!');
    } catch (error) {
      console.error('Ошибка:', error);
      alert('Fallback: Кнопка Play нажата!');
    }
  });
});