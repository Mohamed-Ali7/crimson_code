$(document).ready(function () {

  const host = `192.168.1.2:8080`;

  const customErrorMessage = `An error occurred while sending the request, please try again later or contact support`;


  async function loadCurrentUser() {
    let currentUser = localStorage.getItem(`user`);

    if (currentUser) {
      return Promise.resolve(JSON.parse(currentUser));
    }

    return await $.ajax({
      method: "GET",
      url: "http://localhost:8080/api/users/me",
      headers: {'Authorization': 'Bearer ' + data.accessToken}})
      .then((user) => {
        const localStorageUser = {
          publicId: user.publicId,
          profileImgUrl: user.profileImgUrl,
          firstName: user.firstName,
          lastName: user.lastName,
        }
        localStorage.setItem(`user`, JSON.stringify(localStorageUser));

        return localStorageUser;
      }).catch(function(response) {
        if (response.responseJSON) {
          console.error(response.responseJSON.message);
        } else {
          console.error(`An error occurred while sending the request, please try again later`)
        }
      });
  }

  async function loadTags() {
    let tags = sessionStorage.getItem(`tags`);
    if (tags) {
      return Promise.resolve(JSON.parse(tags));
    }

    return await $.ajax({
      method: `GET`,
      url: `http://${host}/api/tags`,
      contentType: `application/json`
    }).then(function (data) {
      sessionStorage.setItem(`tags`, JSON.stringify(data.content));
      return data.content;
    }).catch(function (response) {
      if (response.responseJSON) {
        console.error(response.responseJSON.message);
      } else {
        console.error(customErrorMessage);
      }
      return [];
    });
  }


  loadCurrentUser().then(user => {
    
    const profilePictureURL = $(`.profile-details .profile-pic img`);
    const userFullName = $(`.profile-details .user-name`);
    const downArrow = $(`<i class="fa fa-caret-down"></i>`);
    
    profilePictureURL.attr(`src`, `http://${host}` + user.profileImgUrl);

    userFullName.text(`${user.firstName} ${user.lastName}`);

    userFullName.append(downArrow);
  })

  loadTags().then(tags => {
    tags.forEach(tag => {
      const AlltagCheckBoxes = $(`.tag-checkboxes`);

      const tagLabel = $(`<label class="tag-dropdown-item"></label>`);
      tagLabel.attr('data-id', tag.id);

      const tagCheckBox = $(`<input type="checkbox">`);
      tagCheckBox.val(tag.name);

      tagLabel.append(tagCheckBox).append(tag.name);
      

      AlltagCheckBoxes.append(tagLabel);
    });
  });

  async function loadCategories() {
    let categories = sessionStorage.getItem(`categories`);
    if (categories) {
      return Promise.resolve(JSON.parse(categories));
    }

    return await $.ajax({
      method: `GET`,
      url: `http://${host}/api/categories`,
      contentType: `application/json`
    }).then(function (data) {
      sessionStorage.setItem(`categories`, JSON.stringify(data.content));
      return data.content;
    }).catch(function (response) {
      if (response.responseJSON) {
        console.error(response.responseJSON.message);
      } else {
        console.error(customErrorMessage);
      }
      return [];
    });
  }

  loadCategories().then(categories => {
    categories.forEach(category => {
      const categoryDropdownList = $(`.category-dropdown`);
      const categoryItem = $(`<span class="category-item">${category.name}</span>`);
      const categoryDropdownListItem = $(`<li></li>`);

      categoryItem.attr(`data-id`, category.id);
      categoryItem.text(category.name);

      categoryDropdownListItem.append(categoryItem);

      categoryDropdownList.append(categoryDropdownListItem);
    })
  })

  const accessToken = Cookies.get(`access_token`);
  if (!accessToken) {
    $('.user-profile').hide();
    $(`.mobile-view-logout-btn`).hide();
    $(`.login-signup-btn`).css(`display`, `flex`);
  } else {
    $('.user-profile').show();
  }

  const mainWrapper = $(`.main-wrapper`);
  const navButtons = $(`.navbar-buttons`);
  const leftNavButtons = $(`.nav-left-buttons`);
  const navbarSearch = $(`.search-container`);


  if (window.innerWidth <= 900) {
    mainWrapper.prepend(navbarSearch);
  }

  $(window).on(`resize`, function () {

    if (window.innerWidth > 900) {

      $(`.navbar-buttons`).css(`display`, `flex`);
      $(`.search-container`).css(`display`, `flex`);

      if (navButtons.has(navbarSearch).length === 0) {
        leftNavButtons.after(navbarSearch);
      }
    } else {
      if (mainWrapper.has(navbarSearch).length === 0) {
        mainWrapper.prepend(navbarSearch);
      }
    }
  });

  const tagSearch = $(`.tag-search`);

  $(`#tag-dropdown-btn`).on(`click`, function () {
    $(`.tag-dropdown-content`).toggleClass(`open`);
    $(`#tag-dropdown-btn`).toggleClass(`active`);
    tagSearch.val(``)
  });

  $(`.mobile-view-search-btn`).on(`click`, function () {
    $(`.search-container`).css(`display`, `flex`);
    $(`.navbar-buttons`).hide();
    $(`.menu-bar`).removeClass(`active`);
  });

  tagSearch.on(`keyup`, function () {

    const searchValue = $(this).val().trim().toLowerCase();

    const tagDropdownItems = document.querySelectorAll(`.tag-dropdown-item`);

    tagDropdownItems.forEach(item => {
      if (item.textContent.toLowerCase().startsWith(searchValue)) {
        item.classList.remove(`hide`);
      } else {
        item.classList.add(`hide`);
      }
    })
  });

  $(`.menu-bar`).on(`click`, function () {
    if (window.innerWidth <= 900) {
      $(this).toggleClass(`active`);
      if ($(this).hasClass(`active`)) {
        $(`.navbar-buttons`).css(`display`, `flex`);
      } else {
        $(`.navbar-buttons`).hide();
      }
    }
  });

  $(`.profile-details`).on(`click`, function () {
    if (window.innerWidth > 900) {
      $(this).toggleClass(`expand`);
      if ($(this).hasClass(`expand`)) {
        $(`.profile-dropdown`).show();
      } else {
        $(`.profile-dropdown`).hide();
      }
    }
  });

  $(`.category-details`).on(`click`, function () {

    $(this).toggleClass(`active`);
    if ($(this).hasClass(`active`)) {
      $(`.category-dropdown`).show();
    } else {
      $(`.category-dropdown`).hide();
    }
  });

  $(document).on('click', function (e) {
    if ($('.user-profile').has(e.target).length === 0) {
      $('.profile-dropdown').hide();
      $('.profile-details').removeClass('expand');
    }
    if ($('.categories-btn').has(e.target).length === 0) {
      $(`.category-dropdown`).hide();
      $('.category-details').removeClass('active');
    }

    const tagDropdown = document.querySelector(`.tag-dropdown`);
    const tagDropdownBtn = document.querySelector(`#tag-dropdown-btn`);
    const dropdownContent = document.querySelector(`.tag-dropdown-content`);
    if (!tagDropdown.contains(e.target)) {
      dropdownContent.classList.remove(`open`);
      tagDropdownBtn.classList.remove(`active`);
    }

    if (window.innerWidth <= 900) {
      const navbarButtons = document.querySelector(`.navbar-buttons`);
      const menuBar = document.querySelector(`.menu-bar`);
      if (!navbarButtons.contains(e.target) && e.target !== menuBar) {
        menuBar.classList.remove(`active`);
        navbarButtons.style.display = `none`;
      }

      const modileViewSearchBtn = document.querySelector(`.mobile-view-search-btn`);
      const searchContainer = document.querySelector(`.search-container`);
      if (!searchContainer.contains(e.target) && e.target !== modileViewSearchBtn) {
        searchContainer.style.display = `none`;
      }
    }
  });

  const mainSearchButton = $(`.search-container .fa-search`);
  mainSearchButton.on(`click`, function () {
    const searchQuery = $(`.main-search-input`).val().trim();

    const tags = [];

    const checkboxes = $(`.tag-dropdown-item input`).toArray();

    checkboxes.forEach(checkbox => {
      if (checkbox.checked) {
        tags.push(checkbox.value);
      }
    });
    
    // search.html, search.css, and search.js will be implemented soon
    window.location = `search.html?query=${searchQuery}&tags=${tags.join(`,`)}`;
    
  });

  $(`.search-container .main-search-input`).on(`keyup`, function (e) {
    e.preventDefault();
    if (e.key === `Enter`) {
      mainSearchButton.click();
    }
  })


  $(`.logout-btn`).on(`click`, logout);
  $(`.mobile-view-logout-btn`).on(`click`, logout);

  function logout () {
    const accessToken = Cookies.get(`access_token`);
    const refreshToken = Cookies.get(`refresh_token`);

    const reqData = {
      refreshToken: refreshToken,
    };

    $.ajax({
      method: `POST`,
      url: `http://${host}/api/auth/logout`,
      data: JSON.stringify(reqData),
      contentType: `application/json`,
      headers: {'Authorization': `Bearer ${accessToken}`},
      success: function (data) {
        console.log("SUCCESS")
        Cookies.remove(`access_token`);
        Cookies.remove(`refresh_token`);
        
        window.location = `login.html`;
      },
      error: function (response) {
        if (response.responseJSON) {
          console.error(response.responseJSON.message);
        } else {
          console.error(customErrorMessage);
        }
      }
    });
  }

  $(`.navbar-login-btn`).on(`click`, function () {
    window.location = `login.html`;
  });

  $(`.navbar-signup-btn`).on(`click`, function () {
    window.location = `sign_up.html`;
  });

  $(`.nav-left-buttons .home-btn`).on(`click`, function () {
    window.location = `home.html`;
  });

}); 