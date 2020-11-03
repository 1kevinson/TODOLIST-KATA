Le but est de réaliser la version back du kata [TODO MVC](http://todomvc.com/). 

Pour l'instant le projet contient le strict nécessaire pour démarrer. **Reportez-vous à la branche 'solution'** pour une implémentation complète.

L'API à implémenter est la suivante (que vous pouvez aussi retrouver [en ligne](https://github.com/tastejs/todomvc-api/blob/master/todos.apib)).

Il est recommandé de faire des tests unitaires (et pourquoi pas du TDD), et de valider l'implémentation via les tests proposés par [todobackend.com](https://www.todobackend.com/specs/index.html?http://localhost:8080/todos).

--------


# Todos API

Todos API is a todoItemData storage backend for [TodoMVC](//todomvc.com).

# Group Todos

# Todos Collection [/todos]

## Create a Todo [POST]

+ Request (application/json)

            {
                "title": "dredd"
            }

+ Response 201 (application/json; charset=utf-8)

            {
                "id": 42,
                "title": "dredd",
                "completed": false
            }

## List all Todos [GET]

+ Response 200 (application/json; charset=utf-8)

            [{
                "id": 42,
                "title": "dredd",
                "completed": false
            }]

## Delete all Todos [DELETE]

+ Response 204

# Todo [/todos/{id}]

+ Parameters
  + id (required, number, `42`)

## Get a Todo [GET]

+ Response 200 (application/json; charset=utf-8)

            {
                "id": 42,
                "title": "dredd",
                "completed": false
            }

## Update a Todo [PUT]

+ Request (application/json)

            {
                "title": "dredd",
                "completed": true
            }

+ Response 200 (application/json; charset=utf-8)

            {
                "id": 42,
                "title": "dredd",
                "completed": true
            }

## Delete a Todo [DELETE]

+ Response 204


