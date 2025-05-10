$(document).ready(function () {
  const host = `192.168.1.2:8080`;

  const urlParams = new URLSearchParams(window.location.search);

  const page = urlParams.get(`page`) ? urlParams.get(`page`) : 1;
  const size = urlParams.get(`size`) ? urlParams.get(`size`) : 9;
  const sortBy = urlParams.get(`sort_by`) ? urlParams.get(`sort_by`) : `createdAt`;
  const sortDir = urlParams.get(`sort_dir`) ? urlParams.get(`sort_dir`) : `desc`;

  $.ajax({
    method: `GET`,
    url: `http://${host}/api/posts?page=${page}&size=${size}&sort_by=${sortBy}&sort_dir=${sortDir}`,
    success: function (postsPage) {
      postsPage.content.forEach(post => {

        const postCard = $(`<div class="post-card"></div>`).data(`post-id`, post.id);

        const postImage = $(`<img class="post-image" alt="Post Thumbnail">`);

        if (post.imageUrl) {
          postImage.attr(`src`, `http://${host}${post.imageUrl}`);
        } else {
          postImage.attr(`src`, '../static/images/default_post_thumbnail.png');
        }

        postImage.on(`error`, function () {
          const defaultSrc = '../static/images/default_post_thumbnail.png';
          if ($(this).attr('src') !== defaultSrc) {
            $(this).attr('src', defaultSrc);
          }
        });

        const postContent = $(`<div class="post-content"></div>`);

        const postTitle = $(`<h2 class="post-title"></h2>`).text(post.title);
        const postExcerpt = $(`<p class="post-excerpt"></p>`).text(post.content);

        const postMeta = $(`<div class="post-meta"></div>`);
        const authorInfoContainer = $(`<div class="author-info-container"></div>`);

        const authorAvatar = $(`<img class="author-avatar">`);
        const authorInfo = $(`<div class="author-info"></div>`);
        const authorName = $(`<span class="author-name"></span>`)
        const postDate = $(`<span class="post-date"></span>`).text(formatDate(post.createdAt));

        const user = post.user;

        if (user.profileImgUrl) {
          authorAvatar.attr(`src`, `http://${host}${user.profileImgUrl}`);
        } else {
          authorAvatar.attr(`src`, '../static/images/default_profile_pic.png');
        }

        authorAvatar.on(`error`, function () {
          const defaultSrc = '../static/images/default_profile_pic.png';
          if ($(this).attr('src') !== defaultSrc) {
            $(this).attr('src', defaultSrc);
          }
        });

        authorName.text(`${user.firstName} ${user.lastName}`);

        const postCategory = $(`<span class="post-category"></span>`);

        const category = post.category;
        postCategory.data(`category-id`, category.id);
        postCategory.text(category.name);

        const postTags = $(`<div class="post-tags"></div>`);

        post.tags.forEach(tag => {
          const tagSpan = $(`<span class="tag">${tag.name}</span>`).data(`tag-id`, tag.id);
          postTags.append(tagSpan);
        })

        // post.html not implemented yet
        const readMoreLink = $(`<a href="post.html?id=${post.id}" class="read-more">Read More</a>`);

        postCard.append(postImage);
        postContent.append(postTitle, postExcerpt);
        authorInfo.append(authorName, postDate);

        authorInfoContainer.append(authorAvatar, authorInfo);
        postMeta.append(authorInfoContainer, postCategory);
        postContent.append(postMeta, postTags, readMoreLink);
        postCard.append(postContent);

        $(`.post-cards-wrapper`).append(postCard);
      });

      renderPagination(postsPage.pageNumber, postsPage.totalPages);
    },
    error: function (response) {
      if (response.responseJSON) {
        console.error(response.responseJSON.message);
      } else {
        console.error(`An error occurred while sending the request, please try again later`)
      }
    }
  });

  $(document).on('click', '.pagination-controls button', function () {
    window.location = `home.html?page=${$(this).text()}`;
  });

  function renderPagination(currentPage, totalPages) {

    const totalButtons = 5;
    const pages = [];

    const paginationControls = $(`.pagination-controls`);

    paginationControls.empty();

    if (totalPages <= totalButtons) {

      for (let i = 1; i <= totalPages; i++) {
        const button = $(`<button>${i}</button>`);
        if (i === currentPage) {
          button.addClass(`active`);
        }
        paginationControls.append(button);
      }
      return;
    }

    pages.push(1);

    const start = currentPage >= totalPages - 1 ? Math.max(2, totalPages - 3) : Math.max(2, currentPage - 1);
    const end = currentPage <= 2 ? Math.min(4, totalPages - 1) : Math.min(currentPage + 1, totalPages - 1);

    if (start > 2) {
      pages.push(`...`);
    }

    for (let i = start; i <= end; i++) {
      pages.push(i);
    }

    if (end < totalPages - 1) {
      pages.push(`...`);
    }

    for (let page of pages) {
      if (page === `...`) {
        paginationControls.append($(`<span>...</span>`));
      } else {
        const button = $(`<button>${page}</button>`);
        if (page === currentPage) {
          button.addClass(`active`);
        }
        paginationControls.append(button);
      }
    }

    const lastButton = $(`<button>${totalPages}</button>`);
    if (totalPages === currentPage) {
      lastButton.addClass(`active`);
    }
    paginationControls.append(lastButton);
  }

  function formatDate(isoDate) {
    const date = new Date(isoDate);

    return date.toLocaleDateString(`en-US`, {
      year: `numeric`,
      month: `long`,
      day: `numeric`,
    })
  }
});