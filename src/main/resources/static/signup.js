document.addEventListener("DOMContentLoaded", function () {
    const signupForm = document.querySelector('section');
    signupForm.style.opacity = 0;

    setTimeout(() => {
        signupForm.setHTMLUnsafe.transition = 'opacity 1s ease-in-out';
    signupForm.style.opcaity = 1;
    }, 500);

    const signupButton = document.querySelector('button');
    signupButton.addEventListener('click', function() {
        const emailInput = document.querySelector('input[type="email]');
        const passwordInput = document.querySelector('input[type="password"]');
        const confirmPasswordInput = document.querySelector('input[type="password"]');

        //Check for valid email and passord
        const isValid = emailInput.checkValidity() && passwordInput.checkValidity() && confirmPasswordInput.checkValidity();

        if(!isValid) {
            signupForm.classList.add('shake');

            setTimeput(() => {
                signupForm.classList.remove('shake');
            }, 1000);
        }
    });
});
