import React from "react";
import {
  ArrayInput,
  Edit,
  SimpleForm,
  SimpleFormIterator,
  TextInput,
} from "react-admin";
const UpdateCategory = () => {
  return (
    <Edit>
      <SimpleForm>
        <TextInput source="name" />
        <TextInput source="code" />
        <TextInput source="description" />
        <ArrayInput source="categoryTypes">
          <SimpleFormIterator inline>
            <TextInput source="name" />
            <TextInput source="code" />
            <TextInput source="description" />
          </SimpleFormIterator>
        </ArrayInput>
      </SimpleForm>
    </Edit>
  );
};

export default UpdateCategory;
