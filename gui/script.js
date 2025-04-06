window.onload = function() {
    const path = window.location.pathname;

    if (path === '/' || path.endsWith('index.html')) {
        loadGallery();
    } else if (path.endsWith('viewer.html')) {
        loadViewer();
    }
};

async function loadGallery() {
    const res = await fetch('/list');
    const videoFilenames = await res.json();
    const container = document.getElementById('container');

    if (videoFilenames.length === 0) {
        const message = document.createElement('p');
        message.textContent = "Wow, such empty...";
        message.className = "empty-message";
        container.appendChild(message);
        
    } else {
        videoFilenames.forEach(videoFilename => {
            const preview = document.createElement('video');
            preview.className = "preview";
            preview.src = `/videos/${videoFilename}#t=0,10`;
            preview.muted = true;
            preview.autoplay = false;
            preview.loop = true;
    
            preview.addEventListener('mouseenter', function() {
                preview.play();
            });
    
            preview.addEventListener('mouseleave', function() {
                preview.pause();
                preview.currentTime = 0;
            });
    
            preview.addEventListener('click', function() {
                window.location.href = `/viewer.html?video=${encodeURIComponent(videoFilename)}`;
            });
    
            container.appendChild(preview);
        });
    }
}

function loadViewer() {
    const urlParams = new URLSearchParams(window.location.search);
    const videoFilename = urlParams.get('video');

    const videoPlayer = document.getElementById('video-player');
    if (videoPlayer && videoFilename) {
        videoPlayer.src = `/videos/${videoFilename}`;
    }

    const filenameText = document.getElementById('videoFilename');
    if (filenameText && videoFilename) {
        filenameText.textContent = videoFilename;
    }

    const backButton = document.querySelector('.back-button');
    if (backButton) {
        backButton.addEventListener('click', function() {
            window.location.href = '/';
        });
    }
}
