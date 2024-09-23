document
  .getElementById("togglePassword")
  .addEventListener("click", function () {
    const passwordInput = document.getElementById("password");
    const eyeIcon = document.getElementById("passEyeIcon");

    // Toggle the type attribute
    const type =
      passwordInput.getAttribute("type") === "password" ? "text" : "password";
    passwordInput.setAttribute("type", type);

    // Toggle the eye icon
    eyeIcon.classList.toggle("fa-eye-slash");
    eyeIcon.classList.toggle("fa-eye");
  });

document
  .getElementById("toggleNewPassword")
  .addEventListener("click", function () {
    const passwordInput = document.getElementById("newPassword");
    const eyeIcon = document.getElementById("newPassEyeIcon");

    // Toggle the type attribute
    const type =
      passwordInput.getAttribute("type") === "password" ? "text" : "password";
    passwordInput.setAttribute("type", type);

    // Toggle the eye icon
    eyeIcon.classList.toggle("fa-eye-slash");
    eyeIcon.classList.toggle("fa-eye");
  });

document
  .getElementById("toggleConfirmNewPassword")
  .addEventListener("click", function () {
    const confirmPasswordInput = document.getElementById("confirmNewPassword");
    const confirmEyeIcon = document.getElementById("confirmNewPassEyeIcon");

    // Toggle the type attribute
    const type =
      confirmPasswordInput.getAttribute("type") === "password"
        ? "text"
        : "password";
    confirmPasswordInput.setAttribute("type", type);

    // Toggle the eye icon
    confirmEyeIcon.classList.toggle("fa-eye-slash");
    confirmEyeIcon.classList.toggle("fa-eye");
  });
