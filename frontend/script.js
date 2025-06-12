window.addEventListener('DOMContentLoaded', () => {

    // --- DOM Elements ---
    const scoreEl = document.getElementById('score'), timeEl = document.getElementById('time'), comboEl = document.getElementById('combo');
    const comboDisplay = document.getElementById('combo-display'), reactionDisplay = document.getElementById('reaction-display'), reactionTimeEl = document.getElementById('reaction-time');
    const monsterEl = document.getElementById('monster'), bonusMonsterEl = document.getElementById('bonus-monster'), monsterFaceEl = document.getElementById('monster-face');
    const bombItemEl = document.getElementById('bomb-item'), clockItemEl = document.getElementById('clock-item');
    const gameArea = document.getElementById('game-area');
    const mainMenu = document.getElementById('main-menu'), gameScreen = document.getElementById('game-screen');
    const authButtons = document.getElementById('auth-buttons'), playerMenu = document.getElementById('player-menu'), guestPlayBtnContainer = document.getElementById('guest-play-button-container');
    const playerUsernameEl = document.getElementById('player-username');
    const muteBtn = document.getElementById('mute-btn');
    const themeToggleBtn = document.getElementById('theme-toggle-btn');
    const body = document.body;

    // --- Session & State ---
    let session = { token: null, playerId: null, username: null, isGuest: false };
    let gameMode, difficulty, initialDuration, score, timeLeft, hitStreak, combo, maxCombo, redTimestamp;
    let isGameActive, isRed, isMuted = false;
    let gameTimer, monsterTimer, bonusItemTimer;

    const difficulties = {
        'Mudah': { redDuration: [1000, 800], penaltyMultiplier: 0.5, itemChance: 0.5 },
        'Normal': { redDuration: [700, 500], penaltyMultiplier: 1, itemChance: 0.35 },
        'Sulit': { redDuration: [500, 400], penaltyMultiplier: 1.5, itemChance: 0.2 }
    };

    const API_BASE_URL = 'http://localhost:8080/api';
    const audio = { 
        hit: () => { if(!isMuted) console.log('SFX: Hit'); },
        miss: () => { if(!isMuted) console.log('SFX: Miss'); },
        powerUp: () => { if(!isMuted) console.log('SFX: Power Up'); },
    };

    // =================================================================================
    // --- MANAJEMEN TEMA & UI ---
    // =================================================================================
    function applyTheme(theme) {
        if (theme === 'light') {
            body.classList.add('light-mode');
            if (themeToggleBtn) themeToggleBtn.innerHTML = `<i class="fas fa-moon"></i> Ganti ke Dark`;
        } else {
            body.classList.remove('light-mode');
            if (themeToggleBtn) themeToggleBtn.innerHTML = `<i class="fas fa-sun"></i> Ganti ke Light`;
        }
    }

    function toggleTheme() {
        const newTheme = body.classList.contains('light-mode') ? 'dark' : 'light';
        localStorage.setItem('monster_panic_theme', newTheme);
        applyTheme(newTheme);
    }

    function checkTheme() {
        const savedTheme = localStorage.getItem('monster_panic_theme') || 'dark';
        applyTheme(savedTheme);
    }

    // =================================================================================
    // --- MANAJEMEN SESI & OTENTIKASI ---
    // =================================================================================
    function updateLoginStateUI() {
        if (!authButtons || !guestPlayBtnContainer || !playerMenu || !playerUsernameEl) {
            return;
        }
        if (session.token && !session.isGuest) {
            authButtons.classList.add('hidden');
            guestPlayBtnContainer.classList.add('hidden');
            playerMenu.classList.remove('hidden');
            playerMenu.classList.add('flex');
            playerUsernameEl.textContent = session.username;
        } else {
            authButtons.classList.remove('hidden');
            guestPlayBtnContainer.classList.remove('hidden');
            playerMenu.classList.add('hidden');
            playerMenu.classList.remove('flex');
        }
    }

    window.handleLogout = function() {
        localStorage.removeItem('monster_panic_session');
        session = { token: null, playerId: null, username: null, isGuest: false };
        updateLoginStateUI();
    }

    function checkSession() {
        checkTheme();
        const savedSession = localStorage.getItem('monster_panic_session');
        if (savedSession) {
            try {
                session = JSON.parse(savedSession);
            } catch (e) {
                localStorage.removeItem('monster_panic_session');
            }
        }
        updateLoginStateUI();
    }

    async function handleApiRequest(endpoint, method = 'GET', body = null) {
        const headers = { 'Content-Type': 'application/json' };
        if (session.token) {
            headers['Authorization'] = `Bearer ${session.token}`;
        }
        const config = { method, headers };
        if (body) {
            config.body = JSON.stringify(body);
        }
        
        const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
        const responseBody = await response.text();
        const data = responseBody ? JSON.parse(responseBody) : {};

        if (!response.ok) {
            throw new Error(data.message || 'Terjadi kesalahan pada server');
        }
        return data;
    }

    window.handleLogin = async function(event) {
        event.preventDefault();
        const username = document.getElementById('login-username').value;
        const password = document.getElementById('login-password').value;
        const errorEl = document.getElementById('login-error');
        try {
            const data = await handleApiRequest('/auth/login', 'POST', { username, password });
            session = { token: data.token, playerId: data.playerId, username: data.username, isGuest: false };
            localStorage.setItem('monster_panic_session', JSON.stringify(session));
            updateLoginStateUI();
            closeModal('login-modal');
        } catch (error) {
            errorEl.textContent = error.message;
        }
    }

    window.handleRegister = async function(event) {
        event.preventDefault();
        const username = document.getElementById('register-username').value;
        const password = document.getElementById('register-password').value;
        const errorEl = document.getElementById('register-error');
        try {
            await handleApiRequest('/auth/register', 'POST', { username, password });
            closeModal('register-modal');
            openModal('login-modal');
        } catch (error) {
            errorEl.textContent = error.message;
        }
    }

    // =================================================================================
    // --- MANAJEMEN PROFIL ---
    // =================================================================================
    window.handleChangeUsername = async function(event) {
        event.preventDefault();
        const newUsername = document.getElementById('new-username').value;
        const errorEl = document.getElementById('change-username-error');
        try {
            const data = await handleApiRequest('/players/me/username', 'PUT', { newUsername });
            alert(data.message);
            handleLogout();
            closeSubModal('change-username-modal');
            openModal('login-modal');
        } catch (error) {
            errorEl.textContent = error.message;
        }
    }

    window.handleChangePassword = async function(event) {
        event.preventDefault();
        const currentPassword = document.getElementById('current-password').value;
        const newPassword = document.getElementById('new-password').value;
        const errorEl = document.getElementById('change-password-error');
        try {
            const data = await handleApiRequest('/players/me/password', 'PUT', { currentPassword, newPassword });
            alert(data.message);
            closeSubModal('change-password-modal');
        } catch (error) {
            errorEl.textContent = error.message;
        }
    }

    window.showProfile = async function() {
        if (!session.token) return;
        openModal('profile-modal');
        const statsContainer = document.getElementById('player-stats-container');
        const achievementsList = document.getElementById('achievements-list');
        statsContainer.innerHTML = '<p>Memuat data profil...</p>';
        achievementsList.innerHTML = '';
        try {
            const profileData = await handleApiRequest('/players/me/profile');
            statsContainer.innerHTML = `
                <div class="grid grid-cols-2 gap-2 text-center">
                    <div><p class="text-sm text-color-secondary">Total Main</p><p class="text-2xl font-bold text-accent">${profileData.totalGames}</p></div>
                    <div><p class="text-sm text-color-secondary">Skor Tertinggi</p><p class="text-2xl font-bold text-accent">${profileData.highestScore}</p></div>
                </div>`;
            if (profileData.achievements.length === 0) {
                achievementsList.innerHTML = '<p class="text-center text-color-secondary">Belum ada pencapaian.</p>';
            } else {
                achievementsList.innerHTML = profileData.achievements.map(ach => `
                    <div class="flex items-center p-3 bg-primary rounded-lg">
                        <i class="${ach.iconClass} text-3xl text-yellow-400 w-12 text-center"></i>
                        <div class="ml-4"><h4 class="font-bold">${ach.name}</h4><p class="text-sm text-color-secondary">${ach.description}</p></div>
                    </div>`).join('');
            }
        } catch (error) {
            statsContainer.innerHTML = `<p class="text-danger">${error.message}</p>`;
        }
    }

    // =================================================================================
    // --- LOGIKA GAME & LAIN-LAIN ---
    // =================================================================================
    window.showScreen = function(screenId) {
        ['main-menu', 'game-screen'].forEach(id => document.getElementById(id)?.classList.add('hidden-screen'));
        document.getElementById(screenId)?.classList.remove('hidden-screen');
    }

    window.showMainMenu = function() {
        ['login-modal', 'register-modal', 'leaderboard-modal', 'profile-modal', 'settings-modal', 'guest-difficulty-modal', 'guest-game-over-modal', 'change-username-modal', 'change-password-modal'].forEach(closeModal);
        showScreen('main-menu');
        if(isGameActive) {
            isGameActive = false;
            clearInterval(gameTimer);
            clearTimeout(monsterTimer);
            clearTimeout(bonusItemTimer);
        }
        session.isGuest = false;
    }

    window.openModal = function(modalId) { document.getElementById(modalId)?.classList.remove('hidden-screen'); }
    window.closeModal = function(modalId) { document.getElementById(modalId)?.classList.add('hidden-screen'); }
    window.openSubModal = function(modalId) {
        closeModal('settings-modal');
        openModal(modalId);
    }
    window.closeSubModal = function(modalId) {
        closeModal(modalId);
        openModal('settings-modal');
    }

    window.showLeaderboard = async function() {
        openModal('leaderboard-modal');
        const listEl = document.getElementById('leaderboard-list');
        listEl.innerHTML = '<p>Memuat data...</p>';
        try {
            const scores = await handleApiRequest('/records/leaderboard');
            if (scores.length === 0) {
                listEl.innerHTML = '<p>Belum ada skor. Jadilah yang pertama!</p>';
                return;
            }
            listEl.innerHTML = scores.map((entry, index) => `
                <div class="flex justify-between items-center p-2 rounded ${index === 0 ? 'bg-yellow-600/50' : 'bg-surface'}">
                    <span>#${index + 1} ${entry.name}</span>
                    <span class="font-bold text-success-color">${entry.score}</span>
                    <span class="text-xs text-color-secondary">${entry.mode}</span>
                </div>
            `).join('');
        } catch (error) {
            listEl.innerHTML = `<p class="text-danger">${error.message}</p>`;
        }
    }

    async function submitScore() {
        if (!session.token || session.isGuest) { return; }
        try {
            const payload = { score, game_mode: `${gameMode} - ${Object.keys(difficulties).find(key => difficulties[key] === difficulty)}`, max_combo: maxCombo };
            await handleApiRequest('/records', 'POST', payload);
            await checkAchievements(payload);
        } catch (error) {
            console.error('Gagal mengirim skor:', error.message);
        }
    }
    
    async function checkAchievements(gameResult) {
        if (!session.token || session.isGuest) return;
        try {
            await handleApiRequest('/achievements/unlock', 'POST', gameResult);
        } catch (error) {
            console.error("Gagal memeriksa pencapaian:", error.message);
        }
    }

    window.showDifficultyForGuest = function() { openModal('guest-difficulty-modal'); }
    window.startGuestGame = function(level) { closeModal('guest-difficulty-modal'); session.isGuest = true; startGame(level); }
    window.openRegisterFromGuest = function() { closeModal('guest-game-over-modal'); openModal('register-modal'); }

    window.startGame = function(level) {
        difficulty = difficulties[level];
        showScreen('game-screen');
        setTimeout(initGame, 50);
    }

    function initGame() {
        score = 0; hitStreak = 0; combo = 0; maxCombo = 0; isGameActive = true; isRed = false;
        setupGameMode();
        updateUI();
        reactionTimeEl.textContent = '--ms';
        monsterEl.classList.add('hidden');
        [bonusMonsterEl, bombItemEl, clockItemEl].forEach(el => el?.classList.add('hidden'));
        gameTimer = setInterval(updateTimer, 1000);
        
        scheduleNextMonster();
        if (gameMode === 'AIM_TRAINER') scheduleBonusItem();
    }

    async function endGame() {
        isGameActive = false;
        clearInterval(gameTimer);
        clearTimeout(monsterTimer);
        clearTimeout(bonusItemTimer);
        if (session.token && !session.isGuest) {
            await submitScore();
            showMainMenu();
        } else {
            const guestScoreEl = document.getElementById('guest-final-score-display');
            if (guestScoreEl) guestScoreEl.textContent = score;
            openModal('guest-game-over-modal');
        }
    }

    function updateTimer() {
        timeLeft--;
        timeEl.textContent = timeLeft;
        if (timeLeft <= 0) endGame();
    }

    function setupGameMode() {
        if (window.innerWidth < 768) {
            gameMode = 'REACTION_TEST'; 
            initialDuration = 30;
            document.getElementById('game-title').textContent = 'Reaction Test';
            if (comboDisplay) comboDisplay.classList.add('hidden'); 
            if (reactionDisplay) reactionDisplay.classList.remove('hidden');
        } else {
            gameMode = 'AIM_TRAINER'; 
            initialDuration = 30;
            document.getElementById('game-title').textContent = 'Aim Trainer';
            if (comboDisplay) comboDisplay.classList.remove('hidden'); 
            if (reactionDisplay) reactionDisplay.classList.remove('hidden');
        }
        timeLeft = initialDuration; 
        if (timeEl) timeEl.textContent = timeLeft;
        centerMonster(monsterEl);
    }
    
    function scheduleNextMonster() {
        if (!isGameActive) return;
        const spawnDelay = gameMode === 'AIM_TRAINER' ? 300 + Math.random() * 400 : 500 + Math.random() * 500;
        monsterTimer = setTimeout(spawnRedMonster, spawnDelay);
    }

    function spawnRedMonster() {
        if (!isGameActive) return;
        if (gameMode === 'AIM_TRAINER') moveMonsterRandomly(monsterEl);
        else centerMonster(monsterEl);
        
        isRed = true;
        redTimestamp = Date.now();
        monsterEl.classList.remove('blue');
        monsterEl.classList.add('red');
        monsterFaceEl.textContent = '😡';
        monsterEl.classList.remove('hidden');
        
        const redDuration = Math.random() * difficulty.redDuration[1] + difficulty.redDuration[0];
        monsterTimer = setTimeout(handleMissedMonster, redDuration);
    }

    function handleMissedMonster() {
        if (!isGameActive || !isRed) return;
        applyPenalty('Terlambat!');
        isRed = false;
        monsterEl.classList.add('hidden');
        scheduleNextMonster();
    }

    function handleMonsterClick(event) {
        event.preventDefault();
        event.stopPropagation();
        
        if (!isGameActive) return;

        if (isRed) {
            const reactionTime = Date.now() - redTimestamp;
            reactionTimeEl.textContent = `${reactionTime}ms`;
            
            if (gameMode === 'AIM_TRAINER') handleAimTrainerHit(reactionTime); 
            else handleReactionTestHit(reactionTime);
            
            isRed = false;
            clearTimeout(monsterTimer);
            monsterEl.classList.add('hidden');
            scheduleNextMonster();
        }
    }

    function handleGameAreaClick(event) {
        if (gameMode !== 'AIM_TRAINER' || !isGameActive || event.target !== gameArea) return;
        applyPenalty('Meleset!');
    }

    function handleAimTrainerHit(reactionTime) {
        hitStreak++;
        const reactionPoints = Math.max(15, 120 - Math.floor(reactionTime / 10));
        let pointsWon = reactionPoints;

        if (hitStreak >= 3) {
            combo = hitStreak;
            const comboBonus = (combo - 2) * 10;
            pointsWon += comboBonus;
        }
        if (combo > maxCombo) maxCombo = combo;
        score += pointsWon;
        
        showFeedback(`+${pointsWon}${combo >= 3 ? ` (x${combo})` : ''}`, '#22C55E');
        updateUI();
    }

    function handleReactionTestHit(reactionTime) {
        const pointsWon = Math.max(10, 100 - Math.floor(reactionTime / 20));
        score += pointsWon;
        updateUI();
    }

    function applyPenalty(reason) {
        const timeElapsed = initialDuration - timeLeft;
        const pointsLost = 5 + Math.floor(20 * (timeElapsed / initialDuration));
        score = Math.max(0, score - Math.round(pointsLost * difficulty.penaltyMultiplier));
        
        showFeedback(`-${Math.round(pointsLost * difficulty.penaltyMultiplier)} (${reason})`, '#EF4444');
        if(gameMode === 'AIM_TRAINER') resetStreakAndCombo();
        updateUI();
    }

    function resetStreakAndCombo() {
        if (hitStreak > 0) console.log("SFX: Combo Break");
        hitStreak = 0;
        combo = 0;
        updateUI();
    }

    function scheduleBonusItem() {
        if (!isGameActive) return;
        const delay = Math.random() * 3000 + 2500;
        bonusItemTimer = setTimeout(showBonusItem, delay);
    }
    
    function showBonusItem() {
        if (!isGameActive || Math.random() > difficulty.itemChance) {
            scheduleBonusItem(); return;
        }
        const items = [bonusMonsterEl, bombItemEl, clockItemEl];
        const chosenItem = items[Math.floor(Math.random() * items.length)];
        
        moveMonsterRandomly(chosenItem);
        chosenItem.classList.remove('hidden');
        
        setTimeout(() => {
            chosenItem.classList.add('hidden');
            scheduleBonusItem();
        }, 2000);
    }

    function handleItemClick(event, itemType) {
        event.preventDefault();
        event.stopPropagation();
        if (!isGameActive) return;
        const targetEl = event.currentTarget;
        if (targetEl.classList.contains('hidden')) return;

        switch(itemType) {
            case 'BONUS': score += 50; showFeedback('+50', '#FBBF24'); break;
            case 'BOMB': score = Math.max(0, score - 75); showFeedback('-75!', '#DC2626'); break;
            case 'CLOCK': timeLeft += 5; timeEl.textContent = timeLeft; showFeedback('+5s', '#22D3EE'); break;
        }
        targetEl.classList.add('hidden');
        updateUI();
    }

    function updateUI() { 
        scoreEl.textContent = score; 
        if (gameMode === 'AIM_TRAINER') {
            comboEl.textContent = `x${combo}`; 
            comboEl.classList.toggle('combo-active', combo >= 3);
        }
    }
    
    function moveMonsterRandomly(el) {
        if (!gameArea || !el) return;
        const rect = gameArea.getBoundingClientRect();
        const size = el.offsetWidth;
        if (rect.width === 0 || rect.height === 0) {
            centerMonster(el); return;
        }
        el.style.left = `${Math.random() * (rect.width - size)}px`;
        el.style.top = `${Math.random() * (rect.height - size)}px`;
        el.style.transform = 'none';
    }

    function centerMonster(el) { 
        if (!el) return;
        el.style.left = '50%'; 
        el.style.top = '50%'; 
        el.style.transform = 'translate(-50%, -50%)'; 
    }

    function showFeedback(text, color) { 
        if (!gameArea) return;
        const el = document.createElement('div'); 
        el.textContent = text; 
        el.className = `absolute text-2xl md:text-3xl font-bold transition-opacity duration-1000 opacity-100`; 
        el.style.left = '50%'; 
        el.style.top = '50%'; 
        el.style.transform = 'translate(-50%, -50%)'; 
        el.style.color = color; 
        gameArea.appendChild(el); 
        setTimeout(() => { 
            el.style.opacity = '0'; 
            setTimeout(() => el.remove(), 1000); 
        }, 500); 
    }
    
    function toggleMute() { isMuted = !isMuted; muteBtn.innerHTML = `<i class="fas fa-volume-${isMuted ? 'mute' : 'up'}"></i>`; }

    // --- Event Listeners ---
    const clickEvent = 'ontouchstart' in window ? 'touchstart' : 'click';

    if (themeToggleBtn) themeToggleBtn.addEventListener('click', toggleTheme);
    if (muteBtn) muteBtn.addEventListener('click', toggleMute);
    
    if (monsterEl) monsterEl.addEventListener(clickEvent, handleMonsterClick);
    if (bonusMonsterEl) bonusMonsterEl.addEventListener(clickEvent, (e) => handleItemClick(e, 'BONUS'));
    if (bombItemEl) bombItemEl.addEventListener(clickEvent, (e) => handleItemClick(e, 'BOMB'));
    if (clockItemEl) clockItemEl.addEventListener(clickEvent, (e) => handleItemClick(e, 'CLOCK'));
    if (gameArea) gameArea.addEventListener('click', handleGameAreaClick);

    checkSession();
});