document.getElementById('playBtn').addEventListener('click', function() {
    const status = document.getElementById('status');
    status.textContent = 'Плеер запущен!';
    this.disabled = true;
});
