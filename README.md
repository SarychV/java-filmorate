# Проект Filmorate
Репозиторий java-filmorate содержит код для сервиса, который будет работать с фильмами 
и оценками пользователей, а также возвращать топ-5 фильмов, рекомендованных к просмотру.
Пользователи сервиса имеют возможность отметить понравившиеся фильмы, познакомиться
с популярными у других пользователей фильмами, завести себе друзей.

Каркас сервиса реализован с использованием Spring Boot.

### Описание схемы базы данных
Хранение данных выполняется в базе данных, имеющей следующую схему:

![Схема базы данных приложения Filmorate.](FilmorateERDiagram.jpg)

На диаграмме таблицы user, film, genre и rating представляют множества сущностей 
для пользователей, фильмов, жанров и рейтингов соответственно. 

Таблица user имеет следующие атрибуты:
* первичный ключ user_id - идентификатор пользователя сервиса;
* name - имя пользователя;
* login - логин пользователя;
* email - адрес электронной почты пользователя (должен иметь соответствующий формат);
* birthdate - дата рождения пользователя.

Атрибуты таблицы film следующие:
* первичный ключ film_id - идентификатор фильма;
* name - название фильма;
* description - описание фильма;
* release_date - дата выпуска;
* duration - длительность фильма в минутах.

Атрибуты таблицы genre:
* первичный ключ genre_id - идентификатор жанра;
* name - название жанра.

Таблица rating имеет следующие атрибуты:
* первичный ключ id;
* name - рейтинг MPA (Ассоциации кинокомпаний).

Для реализации запросов с предложением дружбы от одного пользователя к другому
используется таблица make_friend, содержащая вторичные ключи who(кто) и whom(кому), которые 
являются идентификаторами пользователей. Автоматический первичный ключ id используется 
во время создания таблицы.

Таблица friends реализует связь между пользователями, являющимися друзьями (вторичные ключи
user_id1 и user_id2 представляют идентификаторы соответствующих пользователей).
Автоматический первичный ключ id используется во время создания таблицы.

Таблица like реализует связь, указывающую на проставление пользователем лайка понравившемуся
фильму. Для связи используются идентификаторы пользователя(user_id) и фильма(film_id), 
являющиеся вторичными ключами. Автоматический первичный ключ id используется во время 
создания таблицы.

Для реализации связи между фильмами и жанрами используется таблица film_genre. Атрибуты 
с идентификаторами фильма(film_id) и жанра(genre_id) являются вторичными ключами.
Автоматический первичный ключ id используется во время создания таблицы.

### Примеры SQL запросов для обращения к базе данных
Для получения информации о пользователе по идентификатору может использоваться 
следующий запрос:
```
SELECT *
FROM user
WHERE user_id=id
```

Чтобы получить список всех пользователей:
```
SELECT *
FROM user
```
Аналогичные запросы выполняются для получения сведений о фильмах.

