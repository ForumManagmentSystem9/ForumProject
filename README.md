# ForumProject

# Проект: Социална Мрежа

Този проект представлява социална мрежа, където потребителите могат да създават публикации ,
коментари и да взаимодействат помежду си чрез харесвания и тагове.
За тази цел сме използвали Spring Boot, Hibernate и SQL Server база данни, хоствана в Azure.

## Настройки за Оторизация

За достъп до базата данни в Azure е необходима следната информация за оторизация:

- **Server Name:** Въведете името на вашия сървър.
- **Database Name:** Въведете името на вашата база данни.
- **User ID:** Въведете потребителското име за достъп.
- **Password:** Въведете паролата за достъп.
- **Connection String:** Тази информация е необходима за свързване към
базата данни чрез различни инструменти и приложения.
- ** И е необходимо да се свържете за допълнително удобрение чрез вашия IP address.

## Конфигурация на Hibernate

Класът `HibernateConfig` конфигурира Hibernate с помощта на Spring.
Той създава `SessionFactory` и дефинира източника на данни,
както и свойствата на Hibernate, използвайки настройки от `application.properties`.

## Конфигурация на Сигурността

Класът `SecurityConfig` настройва сигурността на приложението,
използвайки Spring Security. Той конфигурира филтъра за автентикация с JWT,
настройва управлението на сесиите и дефинира правилата за достъп до различните URL адреси.
Освен това, задава `BCryptPasswordEncoder` за криптиране на пароли и осигурява `AuthenticationManager` за управление на автентикацията.

## JWTAuthenticationFilter

Класът `JWTAuthenticationFilter` е Spring компонент,
който разширява `OncePerRequestFilter` за обработка на JWT автентикация.
Той извлича и проверява JWT от заглавката на HTTP заявките,
валидира токена и, ако е валиден, създава `UsernamePasswordAuthenticationToken`,
който се задава в контекста на сигурността. Това осигурява, че потребителят е автентикиран за всички последващи заявки.

## AuthenticationController

Класът `AuthenticationController` е Spring REST контролер,
който обработва аутентикация и регистрация на потребители.

## Models

* User
Моделът User представлява потребител в системата и съдържа следните полета:

- id: Идентификатор на потребителя (първичен ключ).
- firstName: Име на потребителя.
- lastName: Фамилно име на потребителя.
- email: Имейл адрес на потребителя (уникален).
- username: Потребителско име на потребителя.
- password: Хаширана парола на потребителя.
- role: Роля на потребителя (връзка с таблицата roles).
- phoneNumber: Телефонен номер на потребителя.
- isBlocked: Флаг, указващ дали потребителят е блокиран.

* Role
Моделът Role описва ролите в системата и съдържа следните полета:

- id: Идентификатор на ролята (първичен ключ).
- name: Името на ролята, което може да бъде 'USER', 'MODERATOR' или 'ADMIN'.

* Post
Моделът Post представлява пост в системата и съдържа следните полета:

- id: Идентификатор на поста (първичен ключ).
- title: Заглавие на поста.
- content: Съдържание на поста.
- likes: Брой харесвания на поста.
- user: Потребителят, който е създал поста (връзка с таблицата users).
- tags: Тагове, свързани с поста (връзка с таблицата tags чрез таблицата post_tag).

* Comment
- Моделът Comment представлява коментар в системата и съдържа следните полета:

- id: Идентификатор на коментара (първичен ключ).
- content: Съдържание на коментара.
- likes: Брой харесвания на коментара.
- post: Постът, към който е добавен коментарът (връзка с таблицата posts).
- user: Потребителят, който е създал коментара (връзка с таблицата users).

* Tag
Моделът Tag представлява таг и съдържа следните полета:

- id: Идентификатор на тага (първичен ключ).
- tag_name: Името на тага.

* PostDTO
Моделът PostDTO се използва за създаване на нови постове и съдържа следните полета:

- title: Заглавие на поста (задължително, между 16 и 64 символа).
- content: Съдържание на поста (задължително, между 32 и 8192 символа).
- likes: Брой харесвания (не е задължителен).
- creator_id: Идентификатор на създателя на поста (положителен).

* UserDto
Моделът UserDto се използва за създаване на нови потребители и съдържа следните полета:

- username: Потребителско име на потребителя (задължително, между 4 и 32 символа).
- password: Парола на потребителя (задължителна, между 4 и 32 символа).
- firstName: Име на потребителя (задължително, между 4 и 32 символа).
- lastName: Фамилно име на потребителя (задължително, между 4 и 32 символа).
- email: Имейл адрес на потребителя (задължителен, уникален).
- phone: Телефонен номер на потребителя (по избор).

## Repositories
* CommentRepositoryImpl
Този клас имплементира CommentRepository и предоставя CRUD операции за управление на коментари в базата данни чрез Hibernate.
Осигурява методи за извличане, създаване, обновяване и изтриване на коментари, както и за увеличаване на броя на харесванията.

* PostsRepositoryImpl
Тази имплементация на PostsRepository предоставя методи за управление на публикации,
използвайки Hibernate. Включва операции за съхраняване, обновяване, намиране и изтриване на публикации,
както и търсене на публикации по заглавие или потребителски ID.

* UserRepositoryImpl
UserRepositoryImpl реализира UserRepository и предлага операции за управление на потребителски данни чрез Hibernate.
Класът осигурява методи за създаване, намиране, обновяване и търсене на потребители по различни атрибути.


## Services

* AuthenticationServiceImpl
Този клас предоставя услуги за регистрация и автентикация на потребители.
Извършва проверка за съществуването на потребител по имейл, 
създава нов потребител, генерира JWT токен при регистрация и автентикация на потребител.

* CommentServiceImpl
Този клас осигурява операции за управление на коментари, включително създаване,
обновяване и изтриване на коментари, както и възможност за харесване на коментари.
Проверява разрешенията на потребителите за извършване на тези операции и осигурява достъп до всички коментари и коментари по ID.

* JWTService
Този клас управлява JWT (JSON Web Tokens) за автентикация. Осигурява генериране на токени,
валидиране на токени и извличане на информация от токени, като имейл и дата на изтичане.

* PostServiceImpl
Този клас предоставя услуги за управление на публикации, включително съхраняване,
обновяване, изтриване и извличане на публикации. Осигурява филтриране на публикации по потребител ID и заглавие,
както и проверка на разрешенията за модификация на публикации.

* UserServiceImpl
Този клас предоставя услуги за управление на потребителски данни,
включително извличане на потребител по ID или имейл, обновяване на потребителски данни и управление на потребителски пароли.
Осигурява и имплементация на UserDetailsService за интеграция със Spring Security.
