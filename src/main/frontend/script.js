// Function to open the modal
function startManaging() {
    document.getElementById("authModal").style.display = "block"; // Show the modal
}

// Function to close the modal
function closeModal() {
    document.getElementById("authModal").style.display = "none"; // Hide the modal
}

// Function to toggle between login and registration forms
function toggleAuth() {
    const authForm = document.querySelector('.auth-form');
    const toggleText = document.getElementById('toggle-auth');

    if (toggleText.textContent.includes('Register')) {
        toggleText.innerHTML = `Already have an account? <span onclick="toggleAuth()">Login here</span>`;
        authForm.innerHTML = `
            <input type="text" placeholder="Username" required>
            <input type="email" placeholder="Email" required>
            <input type="password" placeholder="Password" required>
            <button class="btn btn-three" onclick="register()">Register</button>
        `;
    } else {
        toggleText.innerHTML = `Don't have an account? <span onclick="toggleAuth()">Register here</span>`;
        authForm.innerHTML = `
            <input type="email" placeholder="Email" required>
            <input type="password" placeholder="Password" required>
            <button class="btn btn-three" onclick="login()">Login</button>
        `;
    }
}

// Dummy function for login
function login() {
    const email = document.querySelector('.auth-form input[type="email"]').value;
    const password = document.querySelector('.auth-form input[type="password"]').value;

    // Perform your login logic here (e.g., AJAX request to the backend)
    console.log('Login with', email, password);

    // Close modal on successful login
    closeModal();
}

// Dummy function for registration
function register() {
    const username = document.querySelector('.auth-form input[type="text"]').value;
    const email = document.querySelector('.auth-form input[type="email"]').value;
    const password = document.querySelector('.auth-form input[type="password"]').value;

    // Perform your registration logic here (e.g., AJAX request to the backend)
    console.log('Register with', username, email, password);

    // Close modal on successful registration
    closeModal();
}

// Dummy function for Google login
function loginWithGoogle() {
    // Perform Google login logic here (e.g., redirect to Google OAuth)
    console.log('Login with Google');
}

// Dummy function for GitHub login
function loginWithGitHub() {
    // Perform GitHub login logic here (e.g., redirect to GitHub OAuth)
    console.log('Login with GitHub');
}

// Click event to close modal when clicking outside of it
window.onclick = function(event) {
    const modal = document.getElementById("authModal");
    if (event.target === modal) {
        closeModal();
    }
}
