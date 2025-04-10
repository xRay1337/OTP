## Описание работы с сервисом

> После начала работы приложения вам будет предложено выбрать роль admin или user.
> Необходимо ввести роль, логин и пароль.
> Если такого пользователя в базе нет, он зарегистрируется.
> Если есть и хэш пароля совпал с хэшем в базе, то он авторизируется.
> Иначе в доступе будет отказано.

> Администратор может задавать длину кода и время его жизни.
> Для этого ему нужно ввести (length или timeout) и целое число.

> Пользователь может выбирать способ отправки.
> Доступно console, email, smpp и telegram.

> Приложение работает в бесконечном цикле до ввода слова exit.
> Все ключевые действия логируются в консоль(от WARN) и файл(от INFO) в директории "./logs".
> Так же в коде проставлено логирование уровня DEBUG.
> Логика статусов OTP-кода сосредоточена в базе. Все необходимые скрипты в директории "./sql".

## Для проверяющего

| **Критерий**                                                     | **Бал** |
|------------------------------------------------------------------|---------|
| *Структура приложения соответствует требованиям*                 | 5       |
| *Используется система сборки Maven*                              | 5       |
| *Реализован минимальный функционал основных операций приложения* | 17      |
| *Запросы к приложению имеют разграничение по ролям*              | 5       |
| *Было реализовано минимальное покрытие логами каждого запроса*   | 3       |
| *Реализован механизм рассылки OTP-кодов по почте*                | 5       |
| *Реализован механизм рассылки OTP-кодов через эмулятор SMPP*     | 5       |
| *Реализован механизм рассылки OTP-кодов через эмулятор Telegram* | 5       |
| *Реализовано подробное покрытие всех запросов к API логами*      | 3       |
| **Итого**                                                        | 53      |

Если баллов получится меньше ожидаемого, но достаточно для зачёта, то я на большее не претендую и прошу принять.
