document.addEventListener("DOMContentLoaded", function () {
    const contactForm = document.getElementById('contact-form');
    if (contactForm) {
      contactForm.addEventListener('submit', function (e) {
        e.preventDefault();
        alert('Thanks for reaching out! I will get back to you soon.');
      });
    }

    fetch("/authenticated", {
      method: "GET",
    })
    .then(response => response.json())
    .then(data => {
        if (data.authenticated) {
            console.log("User is authenticated", data.username);
            // Show user-specific data or make a UI change
        }
    })
    .catch(error => {
        console.error("Error:", error);
    });
  
    const loginForm = document.getElementById("loginForm");
    if (loginForm) {
      loginForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;
  
        fetch("/login", {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({ username: username, password: password }),
        })
        .then(response => {
          if (response.ok) {
            //if authentication is successful, redirect to the upload.html page
            window.location.href = "/upload.html";
          } else {
            //if authentication fails, show an error message
            document.getElementById("errorMessage").classList.remove("hidden");
          }
        })
        .catch(error => { 
          console.error("Error:", error);
        });
      });
    }
  });
  
  function logout() {
    fetch("/logout", {
        method: "POST",
    })
    .then(response => {
        if (response.ok) {
            window.location.href = "/index.html";
        }
    })
    .catch(error => {
        console.error("Logout error:", error);
    });
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