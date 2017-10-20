# v0.1-beta1
- Add: @FXController annotation
- Add: @Decoration annotation
- Add: @I18n annotation
- Add: @Stage annotation
- Add: Create a View instance using a ViewFactory.create(Controller.class)
- Add: Show a View using showInStage() method

# v0.1-beta2
- Remove: View class
- Add: View interface
- Rename ViewFactory class to Views
- Add: StageView type that implements View interface
- Create a StageView using Views.create(Controller.class) or Views.create(Controller.class, Stage)
- StageView implements show() method to show in a Stage 

# v0.1-beta3
- Moving ControllerFactory from Views to new Controllers class.
- Adding support for localised view title, that can be defined in resource bundle of view 
- Adding support for show, hide and hide request listeners for the View

