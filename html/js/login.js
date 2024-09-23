document
  .getElementById("togglePassword")
  .addEventListener("click", function () {
    const passwordInput = document.getElementById("password");
    const eyeIcon = document.getElementById("eyeIcon");

    // Toggle the type attribute
    const type =
      passwordInput.getAttribute("type") === "password" ? "text" : "password";
    passwordInput.setAttribute("type", type);

    // Toggle the eye icon
    eyeIcon.classList.toggle("fa-eye-slash");
    eyeIcon.classList.toggle("fa-eye");
  });
