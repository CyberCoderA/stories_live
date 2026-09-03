# stories_live

This project is inspired by QuestionableQuesting and aims to recreate its core forum-based functionality, with a primary focus on publishing and organizing user-generated stories and threads. The platform is designed to provide users with a space to upload, manage, and discuss written content while supporting community interaction through forum-style discussions and comments.

The frontend is built with React, while the backend is developed using Spring Boot. The application follows a polyglot persistence approach using both PostgreSQL and MongoDB. PostgreSQL manages relational data such as user accounts, authentication, authorization, roles, and other structured entities requiring transactional consistency. MongoDB handles document-oriented content such as stories, threads, chapters, posts, and comments, providing flexibility for managing dynamic and deeply structured content.
