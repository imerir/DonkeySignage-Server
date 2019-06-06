# Create Widget edit menu

Each field need to have the `listen-edit` class.

For each custom input you need to add a class like this: `modify_custom_${id}` where `${id}` is the id of the widget config and, the attribute `data-name` with the name of the parameter.

For the id, i usually put this: `modify_custom_${id}_${name}` where `${id}` is the id of the widget config and `${name}` is the name.

## Media

### Del

Add the class `delMedia` and this attributes:

- `data-id` : name or id of media.
- `data-target` : #id of the imput field to edit on delete.
- `data-list` : #id of the `<ul>` node.

### Add

Add the class `modal-trigger-media` and this attributes:

- `data-list` : #id of the `<ul>` node.
- `data-input` : #id of the imput field to edit.