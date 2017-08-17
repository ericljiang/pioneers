# How to add new actions
* Create a subclass of `Action`. Must declare a no-args constructor for proper serialization.

    ```java
    @NoArgsConstructor
    public class ExampleAction extends Action {
        @Getter
        private String data;
        ...
    }
    ```

* Register subtype with `RuntimeTypeAdaptorFactory` in `Action`

    ```java
    RuntimeTypeAdapterFactory<Action> actionAdapter = RuntimeTypeAdapterFactory.of(Action.class)
            .registerSubtype(ExampleAction.class);
    ```

* Create a method `handleExampleAction(ExampleAction action)` in `Game`

    ```java
    public void handleExampleAction(ExampleAction action) {
        String data = action.getData();
        ...
    }
    ```

* Implement `ExampleAction#apply()` by calling the new action handler

    ```java
    @Override
    public void apply(Game game) {
        game.handleExampleAction(this);
    }
    ```

* Subclass the `Action` class in actions.js. The fields need to match those in the java subclass exactly.

    ```js
    class ExampleAction extends Action {
        constructor(data) {
            this.data = data;
            ...
        }
    }
    ```

If the server sends ExampleActions to clients, the client must have a function to handle them.

* Create a function `handleExampleAction` in game.js

    ```js
    function handleExampleAction(action) {
        var data = action.data;
        ...
    }
    ```
