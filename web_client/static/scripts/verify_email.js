$(document).ready(function () {
  const urlParams = new URLSearchParams(window.location.search);

  const token = urlParams.get(`token`);

  $(`.send-verification-mail-btn`).on(`click`, function () {
    window.location = `verification_request.html`;
  });

  $.ajax({
    method: `GET`,
    url: `http://localhost:8080/api/auth/email-verification?token=${token}`,
    success: function (data) {
      sessionStorage.setItem(`flush_message`, `Your email has been verified successfully`);
      window.location = `login.html`;
    },
    error: function(respose) {
      $(`.verify-failure`).show();
      $(`.send-verification-mail-btn`).show();
    }
  });
}); 