document.addEventListener("DOMContentLoaded", function () {
    // REGISTER
    const submitButton = document.getElementById("submit-button");
    if (submitButton) {
        submitButton.addEventListener("click", function (event) {
            event.preventDefault();

            const username = document.getElementById("username").value;
            const password = document.getElementById("password").value;
            const confirmPassword = document.getElementById("confirm-password").value;
            const email = document.getElementById("email").value;

            if (password !== confirmPassword) {
                alert("Passwords do not match!");
                return;
            }

            const data = {
                username: username,
                password: password,
                email: email
            };

            fetch("/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            })
            .then(response => {
                if (response.ok) {
                    window.location.href = "/login.html";
                } else {
                    alert("Registration failed");
                }
            })
            .catch(err => console.error("Register error:", err));
        });
    }

    // LOGIN
    const loginForm = document.getElementById('contact-form');
    if (loginForm) {
        loginForm.addEventListener("submit", function (event) {
            event.preventDefault();

            const username = document.getElementById("username").value;
            const password = document.getElementById("password").value;

            const params = new URLSearchParams();
            params.append("username", username);
            params.append("password", password);

            fetch("/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: params,
                credentials: "same-origin"
            })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                } else if (response.status === 401 || response.status === 403) {
                    document.getElementById("errorMessage")?.classList.remove("hidden");
                } else {
                    window.location.href = "/upload.html";
                }
            })
            .catch(error => {
                console.error("Login error:", error);
                document.getElementById("errorMessage")?.classList.remove("hidden");
            });
        });
    }
});

// LOGOUT
function logout() {
    window.location.href = "/login.html";
}

// FILE UPLOAD
async function handleUpload() {
    const documentFile = document.getElementById('document')?.files[0];
    const photoFile = document.getElementById('photo')?.files[0];
    const videoFile = document.getElementById('video')?.files[0];

    if (!documentFile && !photoFile && !videoFile) {
        console.log("⚠️ No file selected");
        alert("Please select at least one file to upload.");
        return;
    }

    const formData = new FormData();
    if (documentFile) formData.append("document", documentFile);
    if (photoFile) formData.append("photo", photoFile);
    if (videoFile) formData.append("video", videoFile);

    for (const [key, value] of formData.entries()) {
        console.log(`Selected file : 📂 ${key}: ${value.name}`);
    }
    
    try {
        const response = await fetch("/upload", {
            method: "POST",
            body: formData
        });

        console.log("📡 Raw Response:", response);

        if (response.ok) {
            const text = await response.text();
            console.log("✅ Success:", text);
            alert("✅ Upload successful: " + text);
        } else {
            console.log("❌ Error:", response.status, response.statusText);
            alert("❌ Upload failed: " + response.statusText);
        }
    } catch (error) {
        console.error("🚨 Upload failed:", error);
    }
}
