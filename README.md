# project_Blog
# sgdevcamp_blog

## 1. 기술 스택

JavaSQLite

## 2. 프로젝트에 대한 개요

글 쓰기/수정 & 글 목록/ 삭제 (**MainActivity.java, CustomAdapter.java / PostItem.java**)

- 제목과 내용을 작성하여 업로드할 수 있고, 수정 및 삭제가 가능합니다.
- 업로드한 글은 스크롤을 내리면서 확인 가능합니다. (리싸이클러뷰 )
- 해당 게시글에 대한 정보는 SQLite를 이용하여 내부 데이터베이스에 저장합니다. -> **DBHelper.java**
- 데이터베이스에는 (id, title, content, date) 가 저장됩니다.

댓글 (**DetailActivity.java, CustomAdapter_cm / CommentItem.java**)

- 게시글을 클릭하면 게시글에 대한 댓글 화면으로 넘어갑니다.(DetailActivity).
- 댓글을 입력 및 삭제 할 수 있습니다.
- 마찬가지로 SQLite를 이용하여 저장하였고, (id, postId, comment) 가 저장됩니다. -> **DBHelper_cm.java**
    - postId는 글 목록에서 글을 삭제할 때, 해당 postId에 대한 댓글을 함께 지우기 위해서 추가하였습니다.

관리자 도구

- 블라인드 모드로 구현하려고 하였으나, 미완성.
    - 관리자 모드 버튼을 클릭하면, 게시글의 삭제 및 수정 버튼을 가려지도록 구현할 계획입니다..

## 3. 코드 중 확인 받고 싶은 부분

- 에뮬레이터로 실행시켜서, 글을 하나 작성하고, 해당 글을 클릭해서 DetailActivity로 넘어갈때(CustomAdapter -> DetailActivity), 해당 글의 id, title, content 값을 같이 보내주었습니다. 그래서 title과 content 내용을 화면 위쪽에 띄우고, 그 밑에 댓글을 입력할 수 있게 구현하였습니다. 이때, 댓글을 입력하면, 데이터베이스에 저장되고, (id, postId, comment)에서 postId 값을 intent로 받은 해당 글의 id값을 postId로 저장하려하는데, 처음 글을 작성하고, 해당 글의 DetailActivity로 넘어갈 때마다 해당 id 값이 아닌 0이 들어갑니다.근데 또 에뮬레이터로 실행을 끄고, 다시 실행하면, 기존에 0으로 넘어갔던 id값이 해당 글의 id값으로 잘 넘어갑니다. 글을 새로 작성할 때마다 0으로 들어가는건데, , 어느부분을 수정해야 하는지 잘 모르겠습니다.. 이 부분 확인해 주세요!
- 관리자 모드를 클릭하면 리사이클러뷰의 각각의 뷰들에 있는 버튼들을 모두 가리고 싶었는데, 그 모든 뷰들의 버튼들에 적용하는 방법을 잘 모르겠습니다.
- 관리자 모드 버튼은 MainActivity에 있고, 각각 뷰들에 대한 명령은 CustomAdapter에서 할 수 있으니까, CustomAdapter각각의 뷰들에 있는 버튼들을 모두 가리는 함수(changeMode(int check))를 public으로 만들고, MainActivity에서 사용하려 했습니다.
- 근데 모든 뷰의 버튼들에 적용하는 방법을 모르겠습니다.

## 4. 개발 관련 과정에서 궁금했던 부분

- 관리자 도구 구현을 할 때, 삭제나 수정 권한이 관리자에게만 주어졌다고 가정하고 블라인드 모드로 구현하려 했던 것인데, 보통 어떤 식으로 구현하나요?
- 저는 댓글들을 (id, postId, comment) 형태로 저장하였는데, 같은 게시글에 댓글이 달릴 때마다 postId 값이 계속 중복해서 나타나는게 좋아보이진 않은데, 좀 더 효율적으로 바꿀 수 있을까요?
- 개발을 시작할 때, 보통 데이터베이스를 먼저 만드나요? UI를 먼저 만드나요?
