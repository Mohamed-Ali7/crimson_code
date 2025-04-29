$(document).ready(function () {


  function showError() {
    $(`.form-input`).addClass('error');
    $('.error-message').css(`visibility`, `visible`);
  }

  function clearErrors() {
    $(`.form-input`).removeClass('error');
    $('.error-message').css(`visibility`, `hidden`);
  }

  function isEmailValid(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!email.trim().length === 0) {
      return false;
    }
    return true;
  }

  function isPasswordValid(password) {
    if (password.trim().length === 0) {
      return false;
    }
    return true;
  }

  $(`.form-input`).on(`blur`, function () {
    if ($(this).val().trim().length > 0) {
      $(this).removeClass(`error`);
    }
  });

  $('.bi').on('click', function () {
    const passwordInput = $('.password-input');
    if (passwordInput.attr('type') === 'password') {
      passwordInput.attr('type', 'text');
    } else {
      passwordInput.attr('type', 'password');
    }
    $(this).toggleClass('bi-eye');
  });

  $(`.form-input`).on(`blur`, () => {
    if ($(this).val().trim().length > 0) {
      clearErrors();
    }
  });

  $('.login-form').on('submit', (e) => {
    e.preventDefault();

    const email = $('.email-input').val().trim();
    const password = $('.password-input').val().trim();

    if (!isEmailValid(email) || !isPasswordValid(password)) {
      showError();
      return;
    }
    const userData = {
      email: email,
      password: password,
    };

    $.ajax({
      method: "POST",
      url: "http://localhost:8080/api/auth/login",
      data: JSON.stringify(userData),
      contentType: 'application/json',
      success: (data) => {
        localStorage.setItem('access_token', data.accessToken);
        localStorage.setItem('refresh_token', data.refreshToken);
        clearErrors();
        window.location = `#`
      },

      error: (response) => {
        showError();
      }
    });
  });
});