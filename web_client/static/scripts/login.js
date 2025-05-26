$(document).ready(async function () {
  await window.initCommen();

  const host = window.host;

  if (Cookies.get(`access_token`)) {
    $.ajax({
      method: "GET",
      url: `${host}/api/users/me`,
      headers: { 'Authorization': 'Bearer ' + Cookies.get(`access_token`) },
      success: () => {
        window.location = `home.html`
      }
    });
  }

  const flushMessage = sessionStorage.getItem(`flush_message`);
  if (flushMessage) {
    $(`.flush-message`).text(flushMessage).show();
    sessionStorage.removeItem(`flush_message`);
  }

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
    if (!re.test(email.trim())) {
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

    $(`.flush-message`).hide();
    clearErrors();

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
      url: `${host}/api/auth/login`,
      data: JSON.stringify(userData),
      contentType: 'application/json',
      success: (data) => {

        Cookies.set('access_token', data.accessToken, { expires: 10, path: `/` });
        Cookies.set('refresh_token', data.refreshToken, { expires: 10, path: `/` });

        $.ajax({
          method: "GET",
          url: `${host}/api/users/me`,
          headers: { 'Authorization': 'Bearer ' + data.accessToken },
          success: (user) => {

            const localStorageUser = {
              publicId: user.publicId,
              profileImgUrl: user.profileImgUrl,
              firstName: user.firstName,
              lastName: user.lastName,
            }
            localStorage.setItem(`user`, JSON.stringify(localStorageUser));
          },
          error: function (response) {
            if (response.responseJSON) {
              console.error(response.responseJSON.message);
            } else {
              console.error(`An error occurred while sending the request, please try again later`)
            }
          }
        });

        window.location = `home.html`
      },

      error: (response) => {
        if (response.status === 403) {
          $(`.flush-message`).css(`background-color`, `#9E1B32`)
            .text(`Your email is not verified. Please check your inbox or resend the verification email.`)
            .show();
          return;
        }
        if (response.responseJSON) {
          console.error(response.responseJSON.message);
        } else {
          console.error(`An error occurred while sending the request, please try again later`)
        }
        showError();
      }
    });
  });
});