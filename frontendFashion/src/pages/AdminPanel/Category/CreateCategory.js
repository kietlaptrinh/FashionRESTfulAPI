import React from "react";
import { Create, required, SimpleForm, TextInput } from "react-admin";

const CreateCategory = () => {
  return (
    <Create>
      <SimpleForm>
        <TextInput source="id" validate={[required()]} />
        <TextInput source="name" validate={[required()]} />
        <TextInput source="code" validate={[required()]} />
        <TextInput source="description" validate={[required()]} />
      </SimpleForm>
    </Create>
  );
};

export default CreateCategory;
