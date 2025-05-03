document.addEventListener("DOMContentLoaded", function () {
    const contactForm = document.getElementById('contact-form');
    if (contactForm) {
      contactForm.addEventListener('submit', function (e) {
        e.preventDefault();
        alert('Thanks for reaching out! I will get back to you soon.');
      });
    }
  
    const loginForm = document.getElementById("loginForm");
    if (loginForm) {
      loginForm.addEventListener("submit", function (event) {
        event.preventDefault();
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;
  
        if (username === "admin" && password === "password123") {
          document.getElementById("loadingAnimation")?.classList.remove("hidden");
          setTimeout(() => {
            window.location.href = "upload.html";
          }, 3000);
        } else {
          document.getElementById("errorMessage")?.classList.remove("hidden");
        }
      });
    }
  });
  
  function logout() {
    window.location.href = "index.html";
  }

  function handleUpload() {
    const documentFile = document.getElementById('document').files[0];
    const photoFile = document.getElementById('photo').files[0];
    const videoFile = document.getElementById('video').files[0];

    if (!documentFile && !photoFile && !videoFile) {
        alert("Please select at least one file to upload.");
        return;
    }

    alert("Files uploaded successfully!");
    // Here you can add your file upload logic (e.g., sending the files to a server)
}