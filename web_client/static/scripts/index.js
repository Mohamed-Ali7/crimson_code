$(document).ready(async function () {

  const host = `192.168.1.2:8080`;

  await window.initCommen();

  const postIds = [36, 48];

  for (let postId of postIds) {

    $.ajax({
      method: `GET`,
      url: `http://${host}/api/posts/${postId}`,
      success: function (post) {

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

        const postTitle = $(`<h3 class="post-title"></h3>`).text(post.title);
        const postExcerpt = $(`<p class="post-excerpt"></p>`).text(post.content);

        // post.html not implemented yet
        const readMoreLink = $(`<a href="post.html?id=${post.id}" class="read-more">Read More</a>`);

        postContent.append(postTitle, postExcerpt, readMoreLink)
        postCard.append(postImage, postContent);

        $(`.post-grid`).append(postCard);

      }
    });
  }
});