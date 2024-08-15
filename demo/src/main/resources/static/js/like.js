function toggleLike() {
    const heartIcon = document.getElementById('heartIcon');
    heartIcon.classList.toggle('liked');

    const likeCountElement = document.getElementById('likeCount');
    let currentCount = parseInt(likeCountElement.textContent.replace('Likes ', ''), 10);

    if (heartIcon.classList.contains('liked')) {
        heartIcon.style.transitionDuration = '0.3s';
        heartIcon.style.color = 'red';
        currentCount++;
    } else {
        heartIcon.style.transitionDuration = '0.3s';
        heartIcon.style.color = '';
        currentCount--;
    }

    likeCountElement.textContent = `Likes ${currentCount}`;
}
